package com.ml2wf.v2.tree.fm.xml.mixins;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;

public interface FMRuleDescriptionMixin {

    @JacksonXmlCData
    String getContent();
}
