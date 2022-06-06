package com.ml2wf.v3.xml.mixins.tree.custom.featuremodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;

public interface FMDescriptionMixin {

    @JacksonXmlCData
    String getContent();
}
