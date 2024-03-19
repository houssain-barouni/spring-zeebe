package io.camunda.zeebe.spring.example;

import io.camunda.operate.CamundaOperateClient;
import io.camunda.operate.exception.OperateException;
import io.camunda.operate.model.ProcessDefinition;
import io.camunda.operate.search.SearchQuery;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PeriodicProcessStarter {

  private static Logger log = LoggerFactory.getLogger(ExampleApplication.class);

  private final ZeebeClient client;
  private final CamundaOperateClient operateClient;

  @Autowired
  public PeriodicProcessStarter(ZeebeClient client, CamundaOperateClient operateClient) {
    this.client = client;
    this.operateClient = operateClient;
  }

  @Scheduled(fixedRate = 5000L)
  public void startProcesses() {
    final ProcessInstanceEvent event =
        client
            .newCreateInstanceCommand()
            .bpmnProcessId("demoProcess")
            .latestVersion()
            .variables(
                "{\"a\": \""
                    + UUID.randomUUID().toString()
                    + "\",\"b\": \""
                    + new Date().toString()
                    + "\"}")
            .send()
            .join();

    log.info(
        "started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
        event.getProcessDefinitionKey(),
        event.getBpmnProcessId(),
        event.getVersion(),
        event.getProcessInstanceKey());
  }

  @Scheduled(fixedRate = 5000L)
  public void listProcesses() throws OperateException {
    SearchQuery query = new SearchQuery();
    List<ProcessDefinition> processDefinitions = operateClient.searchProcessDefinitions(query);
    processDefinitions.forEach(
        processDefinition ->
            log.info(
                "Process Definition: Key: {}, BPMN process Id: {}, Name: {}, Tenant: {}",
                processDefinition.getKey(),
                processDefinition.getBpmnProcessId(),
                processDefinition.getName(),
                processDefinition.getTenantId()));
  }
}
