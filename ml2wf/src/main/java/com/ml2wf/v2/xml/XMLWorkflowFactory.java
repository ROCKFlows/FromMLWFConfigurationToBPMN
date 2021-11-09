package com.ml2wf.v2.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.factory.IWorkflowFactory;
import io.vavr.control.Try;

import java.io.File;

public class XMLWorkflowFactory implements IWorkflowFactory {

    private final ObjectMapper mapper;

    public XMLWorkflowFactory() {
        mapper = XMLObjectMapperFactory.getInstance().createNewObjectMapper();
    }

    @Override
    public Try<Workflow> workflowFromFile(File file) {
        return Try.of(() -> mapper.readValue(file, Workflow.class));
    }
}
