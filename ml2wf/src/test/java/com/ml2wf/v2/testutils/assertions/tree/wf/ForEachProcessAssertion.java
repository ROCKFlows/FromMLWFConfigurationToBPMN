package com.ml2wf.v2.testutils.assertions.tree.wf;

import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.wf.Process;
import com.ml2wf.v2.tree.wf.Workflow;
import lombok.Builder;
import lombok.NonNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;

@Builder
public class ForEachProcessAssertion {

    @NonNull final Pair<Workflow, Workflow> workflowPair;
    BiConsumer<Process, Process> forEachProcess;
    BiConsumer<Iterator<Process>, Iterator<Process>> afterEachProcessIteration;
    BiConsumer<Iterator<Process>, Iterator<Process>> afterProcessesIteration;

    public void verify() {
        workflowPair.passTo((workflow1, workflow2) -> {
            Iterator<Process> workflow1Iterator = workflow1.getChildren().iterator();
            Iterator<Process> workflow2Iterator = workflow2.getChildren().iterator();
            while (workflow1Iterator.hasNext() && workflow2Iterator.hasNext()) {
                Process workflow1Process = workflow1Iterator.next();
                Process workflow2Process = workflow2Iterator.next();
                Optional.ofNullable(forEachProcess).ifPresent(c -> c.accept(workflow1Process, workflow2Process));
                Optional.ofNullable(afterEachProcessIteration)
                        .ifPresent(c -> c.accept(workflow1Iterator, workflow2Iterator));
            }
            Optional.ofNullable(afterProcessesIteration).ifPresent(c -> c.accept(workflow1Iterator, workflow2Iterator));
        });
    }
}
