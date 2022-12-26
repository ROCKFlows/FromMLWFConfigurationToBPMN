package com.ml2wf.core.workflow.converter;

import com.ml2wf.core.workflow.StandardWorkflow;
import com.ml2wf.core.workflow.custom.CustomWorkflow;
import lombok.NonNull;

public interface IWorkflowConverter<T extends CustomWorkflow> {

    @NonNull T fromStandardWorkflow(@NonNull final StandardWorkflow standardWorkflow);

    @NonNull StandardWorkflow toStandardWorkflow(@NonNull final T customWorkflow);
}