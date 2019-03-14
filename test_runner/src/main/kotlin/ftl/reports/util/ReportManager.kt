package ftl.reports.util

import ftl.args.IArgs
import ftl.args.IosArgs
import ftl.gc.GcStorage
import ftl.json.MatrixMap
import ftl.reports.CostReport
import ftl.reports.HtmlErrorReport
import ftl.reports.JUnitReport
import ftl.reports.MatrixResultsReport
import ftl.reports.xml.model.JUnitTestResult
import ftl.reports.xml.parseAllSuitesXml
import ftl.reports.xml.parseOneSuiteXml
import ftl.shard.Shard
import ftl.util.Artifacts
import ftl.util.resolveLocalRunPath
import java.io.File
import java.nio.file.Paths
import kotlin.math.roundToInt

object ReportManager {

    private fun findXmlFiles(matrices: MatrixMap): List<File> {
        val xmlFiles = mutableListOf<File>()
        val rootFolder = File(resolveLocalRunPath(matrices))

        rootFolder.walk().forEach {
            if (it.name.matches(Artifacts.testResultRgx)) {
                xmlFiles.add(it)
            }
        }

        return xmlFiles
    }

    private fun getWebLink(matrices: MatrixMap, xmlFile: File): String {
        val matrixFolder = xmlFile.parentFile.parentFile.name
        val matrixPath = Paths.get(matrices.runPath).fileName.resolve(matrixFolder).toString()
        var webLink = ""
        val savedMatrix = matrices.map.values.firstOrNull { it.gcsPath.endsWith(matrixPath) }
        if (savedMatrix != null) {
            webLink = savedMatrix.webLink
        } else {
            println("WARNING: Matrix path not found in JSON. $matrixPath")
        }
        return webLink
    }

    private fun processXml(
        matrices: MatrixMap,
        process: (file: File) -> JUnitTestResult,
        mergeFlakyTestReruns: Boolean
    ): JUnitTestResult? {
        var mergedXml: JUnitTestResult? = null

        // TODO mergeFlakyTestReruns (choose to keep this separate from the pass below because this functionality
        // is temporary while FTL ships this feature natively.
        if (mergeFlakyTestReruns) {
            val rootFolder = File(resolveLocalRunPath(matrices))
            println("Merging flaky test results in $rootFolder")

            // group duplicate xml files together
            val rerunGroups = HashMap<String, MutableList<File>>()
            rootFolder.walk().forEach {
                if (it.canonicalPath.endsWith(".xml")) {
                    val regex = if (it.canonicalPath.contains("rerun"))
                        "\\/(shard.*)-rerun".toRegex()
                    else
                        "\\/(shard.*)\\/".toRegex()

                    val matcher = regex.find(it.canonicalPath)
                    if (matcher != null) {
                        val k = matcher.groupValues[1]
                        if (k in rerunGroups) {
                            rerunGroups[k]?.add(it)
                        } else {
                            rerunGroups[k] = mutableListOf(it)
                        }
                    }
                }
            }
            println(rerunGroups)

            // first merge the results for each test case to a map of test_case to list of results
            // then, we can use some kotlin collections stuff to determine whether each test passed at least once
            // and whether the test was flaky or not
            rerunGroups.forEach {
                val testsToResults = HashMap<String, MutableList<Boolean>>()
                it.value.forEach { xmlFile ->
                    process(xmlFile).testsuites?.forEach { testSuite ->
                        testSuite.testcases?.forEach { testCase ->
                            if (testCase.name!! in testsToResults) {
                                testsToResults[testCase.name]!!.add(testCase.successful())
                            } else {
                                // set the initial result
                                testsToResults[testCase.name] = mutableListOf(testCase.successful())
                            }
                        }
                    }
                }


                val mergedResults = testsToResults.mapValues { it.value.any{testPassed -> testPassed} }
                println(mergedResults)
                println("flaky? " + testsToResults.mapValues {it.value.distinct().size > 1})
            }
        }

        findXmlFiles(matrices).forEach { xmlFile ->
            val parsedXml = process(xmlFile)
            val webLink = getWebLink(matrices, xmlFile)

            parsedXml.testsuites?.forEach { testSuite ->
                testSuite.testcases?.forEach { testCase ->
                    testCase.webLink = webLink
                }
            }

            mergedXml = parsedXml.merge(mergedXml)
        }

        return mergedXml
    }

    private fun parseTestSuite(matrices: MatrixMap, args: IArgs): JUnitTestResult? {
        val iosXml = args is IosArgs
        return if (iosXml) {
            processXml(matrices, ::parseAllSuitesXml, args.flakyTestAttempts > 0)
        } else {
            processXml(matrices, ::parseOneSuiteXml, args.flakyTestAttempts > 0)
        }
    }

    /** Returns true if there were no test failures */
    fun generate(matrices: MatrixMap, args: IArgs): Int {
        val testSuite = parseTestSuite(matrices, args)
        val testSuccessful = matrices.allSuccessful()

        listOf(
            CostReport,
            MatrixResultsReport
        ).map {
            it.run(matrices, testSuite, printToStdout = true)
        }

        if (!testSuccessful) {
            listOf(
                HtmlErrorReport
            ).map { it.run(matrices, testSuite) }
        }

        JUnitReport.run(matrices, testSuite)
        processJunitXml(testSuite, args)

        return matrices.exitCode()
    }

    data class ShardEfficiency(
        val shard: String,
        val expectedTime: Double,
        val finalTime: Double,
        val timeDiff: Double
    )

    fun createShardEfficiencyList(oldResult: JUnitTestResult, newResult: JUnitTestResult, args: IArgs):
            List<ShardEfficiency> {
        val oldJunitMap = Shard.createJunitMap(oldResult, args)
        val newJunitMap = Shard.createJunitMap(newResult, args)

        val timeList = mutableListOf<ShardEfficiency>()
        args.testShardChunks.forEachIndexed { index, testSuite ->

            var expectedTime = 0.0
            var finalTime = 0.0
            testSuite.forEach { testCase ->
                expectedTime += oldJunitMap[testCase] ?: 0.0
                finalTime += newJunitMap[testCase] ?: 0.0
            }

            val timeDiff = (finalTime - expectedTime)
            timeList.add(ShardEfficiency("Shard $index", expectedTime, finalTime, timeDiff))
        }

        return timeList
    }

    private fun printActual(oldResult: JUnitTestResult, newResult: JUnitTestResult, args: IArgs) {
        val list = createShardEfficiencyList(oldResult, newResult, args)

        println("Actual shard times:\n" + list.joinToString("\n") {
            "  ${it.shard}: Expected: ${it.expectedTime.roundToInt()}s, Actual: ${it.finalTime.roundToInt()}s, Diff: ${it.timeDiff.roundToInt()}s"
        } + "\n")
    }

    private fun processJunitXml(newTestResult: JUnitTestResult?, args: IArgs) {
        if (newTestResult == null) return

        val oldTestResult = GcStorage.downloadJunitXml(args)

        newTestResult.mergeTestTimes(oldTestResult)

        if (oldTestResult != null) {
            printActual(oldTestResult, newTestResult, args)
        }

        GcStorage.uploadJunitXml(newTestResult, args)
    }
}
