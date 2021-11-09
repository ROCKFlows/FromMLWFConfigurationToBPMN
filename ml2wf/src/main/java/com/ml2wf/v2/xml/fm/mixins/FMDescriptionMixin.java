package com.ml2wf.v2.xml.fm.mixins;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;

public interface FMDescriptionMixin {

    @JacksonXmlCData
    String getContent();
}
