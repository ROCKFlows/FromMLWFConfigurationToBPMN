package com.ml2wf.v2.tree.wf.factory;

import com.ml2wf.v2.tree.wf.Workflow;
import io.vavr.control.Try;

import java.io.File;

public interface IWorkflowFactory {

    Try<Workflow> workflowFromFile(File file);
}
