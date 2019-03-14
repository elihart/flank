package ftl.util

import ftl.args.IArgs

object Artifacts {

    // TODO Filter out rerun shards here
    val testResultRgx = Regex(".*test_result_\\d+\\.xml$")

    fun regexList(args: IArgs): List<Regex> {
        return listOf(testResultRgx) + args.filesToDownload.map { Regex(it) }
    }
}
