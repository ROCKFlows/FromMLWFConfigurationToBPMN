package com.ml2wf.v3.app.workflow.converter;

import com.ml2wf.v3.app.workflow.custom.CustomWorkflow;
import com.ml2wf.v3.app.workflow.StandardWorkflow;
import lombok.NonNull;

public interface IWorkflowConverter<T extends CustomWorkflow> {

    @NonNull T fromStandardWorkflow(@NonNull final StandardWorkflow standardWorkflow);

    @NonNull StandardWorkflow toStandardWorkflow(@NonNull final T customWorkflow);
}
