package org.camunda.bpm.engine.test.assertions;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.helpers.Failure;
import org.camunda.bpm.engine.test.assertions.helpers.ProcessAssertTestCase;
import org.junit.Rule;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessInstanceAssertIsNotWaitingForTest extends ProcessAssertTestCase {

  @Rule
  public ProcessEngineRule processEngineRule = new ProcessEngineRule();

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isNotWaitingFor.bpmn"
  })
  public void testIsNotWaitingFor_One_Message_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isNotWaitingFor"
    );
    // When
    runtimeService().correlateMessage("myMessage");
    // Then
    assertThat(processInstance).isNotWaitingFor("myMessage");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isNotWaitingFor-2.bpmn"
  })
  public void testIsNotWaitingFor_Two_Messages_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isNotWaitingFor-2"
    );
    // When
    runtimeService().correlateMessage("myMessage");
    // And
    runtimeService().correlateMessage("yourMessage");
    // Then
    assertThat(processInstance).isNotWaitingFor("myMessage", "yourMessage");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isNotWaitingFor-2.bpmn"
  })
  public void testIsNotWaitingFor_One_Of_Two_Messages_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isNotWaitingFor-2"
    );
    // When
    runtimeService().correlateMessage("myMessage");
    // Then
    assertThat(processInstance).isNotWaitingFor("myMessage");
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isNotWaitingFor("yourMessage");
      }
    });
  }
  
  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isNotWaitingFor.bpmn"
  })
  public void testIsNotWaitingFor_One_Message_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isNotWaitingFor"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isNotWaitingFor("myMessage");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isNotWaitingFor.bpmn"
  })
  public void testIsNotWaitingFor_Not_Waiting_For_One_Of_One_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isNotWaitingFor"
    );
    // Then
    assertThat(processInstance).isNotWaitingFor("yourMessage");
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isNotWaitingFor.bpmn"
  })
  public void testIsNotWaitingFor_Not_Waiting_For_One_Of_Two_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isNotWaitingFor"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isNotWaitingFor("myMessage", "yourMessage");
      }
    });
  }

  @Test
  @Deployment(resources = {
    "ProcessInstanceAssert-isNotWaitingFor.bpmn"
  })
  public void testIsNotWaitingFor_Null_Error() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessInstanceAssert-isNotWaitingFor"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isNotWaitingFor();
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isNotWaitingFor(null);
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(processInstance).isNotWaitingFor("myMessage", null);
      }
    });
  }

}
