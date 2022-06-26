package com.ml2wf.v3.app.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml2wf.v3.app.workflow.custom.bpmn.BPMNWorkflow;
import io.vavr.control.Try;

import java.io.File;

public class XMLWorkflowFactory {

    private final ObjectMapper mapper;

    public XMLWorkflowFactory() {
        mapper = XMLObjectMapperFactory.getInstance().createNewObjectMapper();
    }

    public Try<BPMNWorkflow> workflowFromFile(File file) {
        return Try.of(() -> mapper.readValue(file, BPMNWorkflow.class));
    }
}
