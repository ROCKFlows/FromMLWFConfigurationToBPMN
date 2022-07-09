package com.ml2wf.core.workflow.converter;

import com.ml2wf.core.workflow.StandardWorkflow;
import com.ml2wf.core.workflow.StandardWorkflowTask;
import com.ml2wf.core.workflow.custom.bpmn.BPMNProcess;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflow;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflowTask;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class BPMNWorkflowConverter implements IWorkflowConverter<BPMNWorkflow> {

    @Override
    public @NonNull BPMNWorkflow fromStandardWorkflow(@NonNull StandardWorkflow standardWorkflow) {
        if (standardWorkflow.getTasks().isEmpty()) {
            return new BPMNWorkflow(Collections.emptyList());
        }
        List<BPMNWorkflowTask> convertedTasks = standardWorkflow.getTasks().stream()
                .map(t -> new BPMNWorkflowTask(t.getName(), t.getDescription()))
                .collect(Collectors.toList());
        List<BPMNProcess.SequenceFlow> sequenceFlows = new ArrayList<>(convertedTasks.size() - 1);
        ListIterator<BPMNWorkflowTask> taskListIterator = convertedTasks.listIterator();
        BPMNWorkflowTask source = taskListIterator.next();
        while (taskListIterator.hasNext()) {
            var target = taskListIterator.next();
            String sequenceId = String.format("Sequence_%s_%s", source.getName(), target.getName());
            var sequence = new BPMNProcess.SequenceFlow(sequenceId, source.getName(), target.getName());
            sequenceFlows.add(sequence);
            source = target;
        }
        // currently we only support one process
        BPMNProcess process = new BPMNProcess("process_1", "process_1", convertedTasks, sequenceFlows);
        return new BPMNWorkflow(Collections.singletonList(process));
    }

    @Override
    public @NonNull StandardWorkflow toStandardWorkflow(@NonNull BPMNWorkflow bpmnWorkflow) {
        if (bpmnWorkflow.getProcesses().isEmpty()) {
            return new StandardWorkflow(Collections.emptyList());
        }
        // currently we only support one process
        List<StandardWorkflowTask> convertedTasks = bpmnWorkflow.getProcesses().get(0).getTasks().stream()
                .map(t -> new StandardWorkflowTask(t.getName(), t.getDocumentation().getContent(), t.isAbstract(), t.isOptional()))
                .collect(Collectors.toList());
        return new StandardWorkflow(convertedTasks);
    }
}
