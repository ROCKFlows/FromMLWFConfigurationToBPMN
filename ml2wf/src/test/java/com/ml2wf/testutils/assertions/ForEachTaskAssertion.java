package com.ml2wf.testutils.assertions;

import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.ITreeIterator;
import com.ml2wf.v2.tree.wf.Process;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import lombok.Builder;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.BiConsumer;

@Builder
public class ForEachTaskAssertion {

    @NonNull final Pair<Workflow, Workflow> workflowPair;
    BiConsumer<WorkflowTask, WorkflowTask> forEachTask;
    BiConsumer<ITreeIterator<WorkflowTask>, ITreeIterator<WorkflowTask>> afterTasksIteration;
    BiConsumer<ITreeIterator<Process>, ITreeIterator<Process>> afterProcessesIteration;

    public void verify() {
        workflowPair.passTo((workflow1, workflow2) -> {
            ITreeIterator<Process> workflow1Iterator = workflow1.iterator();
            ITreeIterator<Process> workflow2Iterator = workflow2.iterator();
            while (workflow1Iterator.hasNext() && workflow2Iterator.hasNext()) {
                ITreeIterator<WorkflowTask> referenceTasksIterator = workflow1Iterator.next().iterator();
                ITreeIterator<WorkflowTask> instantiatedTasksIterator = workflow2Iterator.next().iterator();
                while (referenceTasksIterator.hasNext() && instantiatedTasksIterator.hasNext()) {
                    WorkflowTask referenceTask = referenceTasksIterator.next();
                    WorkflowTask instantiatedTask = instantiatedTasksIterator.next();
                    Optional.ofNullable(forEachTask).ifPresent(c -> c.accept(referenceTask, instantiatedTask));
                }
                Optional.ofNullable(afterTasksIteration)
                        .ifPresent(c -> c.accept(referenceTasksIterator, instantiatedTasksIterator));
            }
            Optional.ofNullable(afterProcessesIteration).ifPresent(c -> c.accept(workflow1Iterator, workflow2Iterator));
        });
    }
}
