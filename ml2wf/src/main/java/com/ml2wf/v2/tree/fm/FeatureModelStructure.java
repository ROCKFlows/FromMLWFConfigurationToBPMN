package com.ml2wf.v2.tree.fm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v2.tree.AbstractTree;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
public class FeatureModelStructure extends AbstractTree<FeatureModelTask>  {

    @JacksonXmlProperty(localName="and")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FeatureModelTask> children = new ArrayList<>();
    @JacksonXmlProperty(localName="alt")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FeatureModelTask> alternativeChildren = new ArrayList<>();
    @JacksonXmlProperty(localName="feature")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FeatureModelTask> childrenLeaves = new ArrayList<>();

    @Override
    public FeatureModelTask appendChild(FeatureModelTask child) {
        if (childrenLeaves.isEmpty()) {
            childrenLeaves.add(child);
        } else {
            children.add(child);
        }
        // TODO: allow capability to add alternative child
        return child;
    }

    @Override
    public FeatureModelTask removeChild(FeatureModelTask child) {
        if (childrenLeaves.remove(child)) {
            return child;
        }
        if (alternativeChildren.remove(child)) {
            return child;
        }
        return (childrenLeaves.remove(child)) ? child : null;
    }
}
