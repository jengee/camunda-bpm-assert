package org.camunda.bpm.engine.test.assertions;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.test.util.CamundaBpmApi;

import java.util.Map;

/**
 * Class meant to statically access all
 * camunda BPM Process Engine Assertions.
 * <p/>
 * In your code use import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.*;
 *
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessEngineAssertions extends Assertions {

  static ThreadLocal<ProcessEngine> processEngine = new ThreadLocal<ProcessEngine>();

  protected ProcessEngineAssertions() {
  }

  /*
   * *Asserts* that process engine supports the requested API version. Use method
   * at the beginning of static helper method implementations which require 
   * Camunda BPM API versions higher than Camunda BPM "7.0".
   * 
   * @param   api Camunda BPM API version e.g. '7.1', '7.2' etc.
   * @throws  AssertionError if process engine does not support the requested API version
   */
  protected static void assertApi(String api) {
    AbstractProcessAssert.assertApi(api);
  }

  protected static boolean supportsApi(String api) {
    return CamundaBpmApi.supports(api);
  }

  /**
   * Retrieve the processEngine bound to the current testing thread
   * via calling init(ProcessEngine processEngine). In case no such
   * processEngine is bound yet, init(processEngine) is called with
   * a default process engine.
   *
   * @return  processEngine bound to the current testing thread
   * @throws  IllegalStateException in case a processEngine has not
   *          been initialised yet and cannot be initialised with a 
   *          default engine.
   */
  public static ProcessEngine processEngine() {
    ProcessEngine processEngine = ProcessEngineAssertions.processEngine.get();
    if (processEngine != null)
      return processEngine;
    Map<String, ProcessEngine> processEngines = ProcessEngines.getProcessEngines();
    if (processEngines.size() == 1) {
      processEngine = processEngines.values().iterator().next();
      init(processEngine);
      return processEngine;
    }
    String message = processEngines.size() == 0 ? "No ProcessEngine found to be " +
      "registered with " + ProcessEngines.class.getSimpleName() + "!" 
      : String.format(processEngines.size() + " ProcessEngines initialized. Call %s.init" +
      "(ProcessEngine processEngine) first!", ProcessEngineAssertions.class.getSimpleName());
    throw new IllegalStateException(message);
  }

  /**
   * Bind an instance of ProcessEngine to the current testing calls done
   * in your test method.
   *
   * @param   processEngine ProcessEngine which should be bound to the
   *          current testing thread.
   */
  public static void init(final ProcessEngine processEngine) {
    ProcessEngineAssertions.processEngine.set(processEngine);
    AbstractProcessAssert.resetLastAsserts();
  }

  /**
   * Resets operations done via calling init(ProcessEngine processEngine)
   * to its clean state - just as before calling init() for the first time.
   */
  public static void reset() {
    ProcessEngineAssertions.processEngine.remove();
    AbstractProcessAssert.resetLastAsserts();
  }

  /**
   * Assert that... the given ProcessDefinition meets your expecations.
   *
   * @param   actual ProcessDefinition under test
   * @return  Assert object offering ProcessDefinition specific assertions.
   */
  public static ProcessDefinitionAssert assertThat(final ProcessDefinition actual) {
    return ProcessDefinitionAssert.assertThat(processEngine(), actual);
  }

  /**
   * Assert that... the given ProcessInstance meets your expecations.
   *
   * @param   actual ProcessInstance under test
   * @return  Assert object offering ProcessInstance specific assertions.
   */
  public static ProcessInstanceAssert assertThat(final ProcessInstance actual) {
    return ProcessInstanceAssert.assertThat(processEngine(), actual);
  }

  /**
   * Assert that... the given Task meets your expecations.
   *
   * @param   actual Task under test
   * @return  Assert object offering Task specific assertions.
   */
  public static TaskAssert assertThat(final Task actual) {
    return TaskAssert.assertThat(processEngine(), actual);
  }

  /**
   * Assert that... the given Job meets your expecations.
   *
   * @param   actual Job under test
   * @return  Assert object offering Job specific assertions.
   */
  public static JobAssert assertThat(final Job actual) {
    return JobAssert.assertThat(processEngine(), actual);
  }

}
