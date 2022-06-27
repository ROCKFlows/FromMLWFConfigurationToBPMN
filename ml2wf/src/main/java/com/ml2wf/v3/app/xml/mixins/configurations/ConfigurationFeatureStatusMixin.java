package com.ml2wf.v3.app.xml.mixins.configurations;

import com.fasterxml.jackson.annotation.JsonValue;

public interface ConfigurationFeatureStatusMixin {

    @JsonValue
    String getLowercaseName();
}
