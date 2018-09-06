/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * Modify at your own risk.
 */

package com.google.api.services.testing.model;

/**
 * A group of one or more TestExecutions, built by taking a product of values over a pre-defined set
 * of axes.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the Cloud Testing API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class TestMatrix extends com.google.api.client.json.GenericJson {

  /**
   * Information about the client which invoked the test. Optional
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private ClientInfo clientInfo;

  /**
   * How the host machine(s) are configured. Required
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private EnvironmentMatrix environmentMatrix;

  /**
   * Describes why the matrix is considered invalid. Only useful for matrices in the INVALID state.
   * @OutputOnly
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String invalidMatrixDetails;

  /**
   * The cloud project that owns the test matrix. @OutputOnly
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String projectId;

  /**
   * Where the results for the matrix are written. Required
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private ResultStorage resultStorage;

  /**
   * Indicates the current progress of the test matrix (e.g., FINISHED) @OutputOnly
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String state;

  /**
   * The list of test executions that the service creates for this matrix. @OutputOnly
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<TestExecution> testExecutions;

  static {
    // hack to force ProGuard to consider TestExecution used, since otherwise it would be stripped out
    // see https://github.com/google/google-api-java-client/issues/543
    com.google.api.client.util.Data.nullOf(TestExecution.class);
  }

  /**
   * Unique id set by the service. @OutputOnly
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String testMatrixId;

  /**
   * How to run the test. Required
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private TestSpecification testSpecification;

  /**
   * The time this test matrix was initially created. @OutputOnly
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String timestamp;

  /**
   * Information about the client which invoked the test. Optional
   * @return value or {@code null} for none
   */
  public ClientInfo getClientInfo() {
    return clientInfo;
  }

  /**
   * Information about the client which invoked the test. Optional
   * @param clientInfo clientInfo or {@code null} for none
   */
  public TestMatrix setClientInfo(ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
    return this;
  }

  /**
   * How the host machine(s) are configured. Required
   * @return value or {@code null} for none
   */
  public EnvironmentMatrix getEnvironmentMatrix() {
    return environmentMatrix;
  }

  /**
   * How the host machine(s) are configured. Required
   * @param environmentMatrix environmentMatrix or {@code null} for none
   */
  public TestMatrix setEnvironmentMatrix(EnvironmentMatrix environmentMatrix) {
    this.environmentMatrix = environmentMatrix;
    return this;
  }

  /**
   * Describes why the matrix is considered invalid. Only useful for matrices in the INVALID state.
   * @OutputOnly
   * @return value or {@code null} for none
   */
  public java.lang.String getInvalidMatrixDetails() {
    return invalidMatrixDetails;
  }

  /**
   * Describes why the matrix is considered invalid. Only useful for matrices in the INVALID state.
   * @OutputOnly
   * @param invalidMatrixDetails invalidMatrixDetails or {@code null} for none
   */
  public TestMatrix setInvalidMatrixDetails(java.lang.String invalidMatrixDetails) {
    this.invalidMatrixDetails = invalidMatrixDetails;
    return this;
  }

  /**
   * The cloud project that owns the test matrix. @OutputOnly
   * @return value or {@code null} for none
   */
  public java.lang.String getProjectId() {
    return projectId;
  }

  /**
   * The cloud project that owns the test matrix. @OutputOnly
   * @param projectId projectId or {@code null} for none
   */
  public TestMatrix setProjectId(java.lang.String projectId) {
    this.projectId = projectId;
    return this;
  }

  /**
   * Where the results for the matrix are written. Required
   * @return value or {@code null} for none
   */
  public ResultStorage getResultStorage() {
    return resultStorage;
  }

  /**
   * Where the results for the matrix are written. Required
   * @param resultStorage resultStorage or {@code null} for none
   */
  public TestMatrix setResultStorage(ResultStorage resultStorage) {
    this.resultStorage = resultStorage;
    return this;
  }

  /**
   * Indicates the current progress of the test matrix (e.g., FINISHED) @OutputOnly
   * @return value or {@code null} for none
   */
  public java.lang.String getState() {
    return state;
  }

  /**
   * Indicates the current progress of the test matrix (e.g., FINISHED) @OutputOnly
   * @param state state or {@code null} for none
   */
  public TestMatrix setState(java.lang.String state) {
    this.state = state;
    return this;
  }

  /**
   * The list of test executions that the service creates for this matrix. @OutputOnly
   * @return value or {@code null} for none
   */
  public java.util.List<TestExecution> getTestExecutions() {
    return testExecutions;
  }

  /**
   * The list of test executions that the service creates for this matrix. @OutputOnly
   * @param testExecutions testExecutions or {@code null} for none
   */
  public TestMatrix setTestExecutions(java.util.List<TestExecution> testExecutions) {
    this.testExecutions = testExecutions;
    return this;
  }

  /**
   * Unique id set by the service. @OutputOnly
   * @return value or {@code null} for none
   */
  public java.lang.String getTestMatrixId() {
    return testMatrixId;
  }

  /**
   * Unique id set by the service. @OutputOnly
   * @param testMatrixId testMatrixId or {@code null} for none
   */
  public TestMatrix setTestMatrixId(java.lang.String testMatrixId) {
    this.testMatrixId = testMatrixId;
    return this;
  }

  /**
   * How to run the test. Required
   * @return value or {@code null} for none
   */
  public TestSpecification getTestSpecification() {
    return testSpecification;
  }

  /**
   * How to run the test. Required
   * @param testSpecification testSpecification or {@code null} for none
   */
  public TestMatrix setTestSpecification(TestSpecification testSpecification) {
    this.testSpecification = testSpecification;
    return this;
  }

  /**
   * The time this test matrix was initially created. @OutputOnly
   * @return value or {@code null} for none
   */
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * The time this test matrix was initially created. @OutputOnly
   * @param timestamp timestamp or {@code null} for none
   */
  public TestMatrix setTimestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  @Override
  public TestMatrix set(String fieldName, Object value) {
    return (TestMatrix) super.set(fieldName, value);
  }

  @Override
  public TestMatrix clone() {
    return (TestMatrix) super.clone();
  }

}