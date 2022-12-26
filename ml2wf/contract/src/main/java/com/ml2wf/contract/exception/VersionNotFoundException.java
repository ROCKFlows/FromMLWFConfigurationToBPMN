package com.ml2wf.contract.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class VersionNotFoundException extends RuntimeException {

    String versionName;
}
