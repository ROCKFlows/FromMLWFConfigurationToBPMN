package com.ml2wf.v2.tree.fm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.AbstractTree;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JacksonXmlRootElement(localName = "extendedFeatureModel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class FeatureModel extends AbstractTree<FeatureModelTask> {

    @JacksonXmlProperty(localName = "struct")
    private FeatureModelStructure structure;

    @Override
    public FeatureModelTask appendChild(FeatureModelTask child) {
        // delegates to structure
        return structure.appendChild(child);
    }

    @Override
    public FeatureModelTask removeChild(FeatureModelTask child) {
        // delegates to structure
        return structure.removeChild(child);
    }
}
