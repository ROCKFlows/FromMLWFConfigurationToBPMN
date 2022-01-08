package com.ml2wf.v2.testutils.assertions.tree.wf;

import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.wf.Process;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import lombok.Builder;
import lombok.NonNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;

@Builder
public class ForEachTaskAssertion {

    @NonNull final Pair<Workflow, Workflow> workflowPair;
    BiConsumer<WorkflowTask, WorkflowTask> forEachTask;
    BiConsumer<Iterator<WorkflowTask>, Iterator<WorkflowTask>> forEachTaskIteration;
    BiConsumer<Iterator<WorkflowTask>, Iterator<WorkflowTask>> afterTasksIteration;
    BiConsumer<Iterator<Process>, Iterator<Process>> afterEachProcessIteration;
    BiConsumer<Iterator<Process>, Iterator<Process>> afterProcessesIteration;

    public void verify() {
        workflowPair.passTo((workflow1, workflow2) -> {
            Iterator<Process> workflow1Iterator = workflow1.getChildren().iterator();
            Iterator<Process> workflow2Iterator = workflow2.getChildren().iterator();
            while (workflow1Iterator.hasNext() && workflow2Iterator.hasNext()) {
                Iterator<WorkflowTask> workflow1TasksIterator = workflow1Iterator.next().getChildren().iterator();
                Iterator<WorkflowTask> workflow2TasksIterator = workflow2Iterator.next().getChildren().iterator();
                while (workflow1TasksIterator.hasNext() && workflow2TasksIterator.hasNext()) {
                    WorkflowTask workflow1Task = workflow1TasksIterator.next();
                    WorkflowTask workflow2Task = workflow2TasksIterator.next();
                    Optional.ofNullable(forEachTask).ifPresent(c -> c.accept(workflow1Task, workflow2Task));
                    Optional.ofNullable(forEachTaskIteration)
                            .ifPresent(c -> c.accept(workflow1TasksIterator, workflow2TasksIterator));
                }
                Optional.ofNullable(afterTasksIteration)
                        .ifPresent(c -> c.accept(workflow1TasksIterator, workflow2TasksIterator));
                Optional.ofNullable(afterEachProcessIteration)
                        .ifPresent(c -> c.accept(workflow1Iterator, workflow2Iterator));
            }
            Optional.ofNullable(afterProcessesIteration).ifPresent(c -> c.accept(workflow1Iterator, workflow2Iterator));
        });
    }
}
