package com.ml2wf.xml.mixins.tree.custom.featuremodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;

public interface FMRuleDescriptionMixin {

    @JacksonXmlCData
    String getContent();
}
