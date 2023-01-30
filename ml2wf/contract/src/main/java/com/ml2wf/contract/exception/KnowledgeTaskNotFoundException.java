package com.ml2wf.contract.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class KnowledgeTaskNotFoundException extends RuntimeException {

    String taskName;
    String versionName;
}
