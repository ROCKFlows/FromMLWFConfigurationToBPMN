package com.ml2wf.xml.mixins.configurations;

import com.fasterxml.jackson.annotation.JsonValue;

public interface ConfigurationFeatureStatusMixin {

    @JsonValue
    String getLowercaseName();
}
