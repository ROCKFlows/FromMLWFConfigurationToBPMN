package com.ml2wf.contract.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ConfigurationNotFoundException extends RuntimeException {

    String configurationName;
}
