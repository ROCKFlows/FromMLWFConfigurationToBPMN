package com.ml2wf.xml.mixins.tree.custom.featuremodel;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;

public interface FMDescriptionMixin {

    @JacksonXmlCData
    String getContent();
}
