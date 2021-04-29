package com.ml2wf.v2.tree.wf.xml.mixins;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public interface WFDocumentationMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getId();

    @JacksonXmlCData
    String getContent();
}
