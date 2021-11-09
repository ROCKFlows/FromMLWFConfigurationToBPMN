package com.ml2wf.v2.xml.wf.mixins;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public interface WFDocumentationMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getId();

    @JacksonXmlText
    String getContent();
}
