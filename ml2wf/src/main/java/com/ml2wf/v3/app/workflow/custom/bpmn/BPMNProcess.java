package com.ml2wf.v3.app.workflow.custom.bpmn;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE) // required by Jackson
@Data
public class BPMNProcess {

    private String id;
    private String name;
    private List<BPMNWorkflowTask> tasks;
    private List<SequenceFlow> sequenceFlows;

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE) // required by Jackson
    @Data
    public static class SequenceFlow {

        private String id;
        private String sourceRef;
        private String targetRef;
    }
}
