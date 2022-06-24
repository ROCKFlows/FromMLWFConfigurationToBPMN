package com.ml2wf.v3.workflow.converter;

import com.ml2wf.v3.workflow.StandardWorkflow;
import com.ml2wf.v3.workflow.custom.CustomWorkflow;
import lombok.NonNull;

public interface IWorkflowConverter<T extends CustomWorkflow> {

    @NonNull T fromStandardWorkflow(@NonNull final StandardWorkflow standardWorkflow);

    @NonNull StandardWorkflow toStandardWorkflow(@NonNull final T customWorkflow);
}
