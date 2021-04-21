package com.ml2wf.v2.tree.fm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;

@JacksonXmlRootElement(localName = "extendedFeatureModel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
public class FeatureModel  {

    @JacksonXmlProperty(localName = "struct")
    @Delegate
    private FeatureModelStructure structure;
}
