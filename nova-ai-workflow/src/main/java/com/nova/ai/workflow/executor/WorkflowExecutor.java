package com.nova.ai.workflow.executor;

import com.nova.ai.workflow.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;

/**
 * JDK 25 Virtual Threads + Pattern Matching Showcase.
 * Workflow executor uses virtual threads for concurrent node execution
 * and switch pattern matching for node type dispatching.
 */
public class WorkflowExecutor {

    private static final Logger log = LoggerFactory.getLogger(WorkflowExecutor.class);
    private final java.util.concurrent.Executor virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public WorkflowInstance execute(WorkflowDefinition definition, Map<String, Object> initialContext) {
        log.info("Starting workflow: {} with {} nodes", definition.workflowId(), definition.nodes().size());

        WorkflowInstance instance = new WorkflowInstance(
            generateId(), definition.workflowId(), WorkflowStatus.RUNNING,
            definition.nodes().getFirst().id(), initialContext,
            java.time.Instant.now(), null
        );

        executeNode(definition, instance);
        return instance;
    }

    private void executeNode(WorkflowDefinition definition, WorkflowInstance instance) {
        NodeDefinition node = findNode(definition, instance.currentNodeId());
        if (node == null) {
            log.info("Workflow completed: {}", instance.instanceId());
            return;
        }

        log.info("Executing node: {} [{}]", node.name(), node.type());

        Object result = switch (node.type()) {
            case LLM_CALL -> executeLlmNode(node, instance.context());
            case API_CALL -> executeApiNode(node, instance.context());
            case CONDITION -> executeConditionNode(node, instance.context());
            case SKILL_EXECUTION -> executeSkillNode(node, instance.context());
            case PARALLEL -> executeParallelNode(definition, node, instance.context());
            case LOOP -> executeLoopNode(node, instance.context());
            case HUMAN_REVIEW -> executeHumanReviewNode(node, instance);
        };

        log.info("Node {} completed with result: {}", node.name(), result);
    }

    private Object executeLlmNode(NodeDefinition node, Map<String, Object> context) {
        return "LLM response for prompt: " + node.config().get("prompt");
    }

    private Object executeApiNode(NodeDefinition node, Map<String, Object> context) {
        return "API response from: " + node.config().get("url");
    }

    private Object executeConditionNode(NodeDefinition node, Map<String, Object> context) {
        return context.containsKey("condition") ? node.branches().get("true") : node.branches().get("false");
    }

    private Object executeSkillNode(NodeDefinition node, Map<String, Object> context) {
        return "Skill executed: " + node.config().get("skillId");
    }

    private Object executeParallelNode(WorkflowDefinition definition, NodeDefinition node, Map<String, Object> context) {
        var futures = node.nextNodeIds().stream()
            .map(nextId -> {
                NodeDefinition nextNode = findNode(definition, nextId);
                return java.util.concurrent.CompletableFuture.supplyAsync(
                    () -> processNode(nextNode, context), virtualThreadExecutor);
            })
            .toList();

        return futures.stream()
            .map(java.util.concurrent.CompletableFuture::join)
            .toList();
    }

    private Object executeLoopNode(NodeDefinition node, Map<String, Object> context) {
        int iterations = (int) node.config().getOrDefault("maxIterations", 3);
        log.info("Executing loop for {} iterations", iterations);
        return "Loop completed after " + iterations + " iterations";
    }

    private Object executeHumanReviewNode(NodeDefinition node, WorkflowInstance instance) {
        log.info("Pausing for human review at node: {}", node.name());
        return "Waiting for human review";
    }

    private Object processNode(NodeDefinition node, Map<String, Object> context) {
        return switch (node.type()) {
            case LLM_CALL -> executeLlmNode(node, context);
            case API_CALL -> executeApiNode(node, context);
            case CONDITION -> executeConditionNode(node, context);
            case SKILL_EXECUTION -> executeSkillNode(node, context);
            case LOOP -> executeLoopNode(node, context);
            case HUMAN_REVIEW, PARALLEL -> "Node processed";
        };
    }

    private NodeDefinition findNode(WorkflowDefinition definition, String nodeId) {
        return definition.nodes().stream()
            .filter(n -> n.id().equals(nodeId))
            .findFirst()
            .orElse(null);
    }

    private String generateId() {
        return "wf-" + System.currentTimeMillis();
    }
}
