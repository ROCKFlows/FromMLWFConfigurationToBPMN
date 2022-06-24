package com.ml2wf.v3.workflow.custom.bpmn;

import com.ml2wf.v3.workflow.custom.CustomWorkflow;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE) // required by Jackson
@EqualsAndHashCode(callSuper = true)
@Data
public class BPMNWorkflow extends CustomWorkflow {

    private List<BPMNProcess> processes;
}
