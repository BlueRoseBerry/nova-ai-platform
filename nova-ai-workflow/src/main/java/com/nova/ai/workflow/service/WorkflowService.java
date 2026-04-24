package com.nova.ai.workflow.service;

import com.nova.ai.workflow.executor.WorkflowExecutor;
import com.nova.ai.workflow.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkflowService {

    private final Map<String, WorkflowDefinition> definitions = new ConcurrentHashMap<>();
    private final Map<String, WorkflowInstance> instances = new ConcurrentHashMap<>();
    private final WorkflowExecutor executor = new WorkflowExecutor();

    public WorkflowDefinition createDefinition(WorkflowDefinition definition) {
        definitions.put(definition.workflowId(), definition);
        return definition;
    }

    public WorkflowInstance executeWorkflow(String workflowId, Map<String, Object> context) {
        WorkflowDefinition definition = definitions.get(workflowId);
        if (definition == null) {
            throw new IllegalArgumentException("Workflow not found: " + workflowId);
        }
        WorkflowInstance instance = executor.execute(definition, context);
        instances.put(instance.instanceId(), instance);
        return instance;
    }

    public WorkflowDefinition getDefinition(String workflowId) {
        return definitions.get(workflowId);
    }

    public List<WorkflowDefinition> listDefinitions() {
        return List.copyOf(definitions.values());
    }

    public WorkflowInstance getInstance(String instanceId) {
        return instances.get(instanceId);
    }
}
