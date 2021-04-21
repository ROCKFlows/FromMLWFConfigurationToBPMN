package com.ml2wf.v2.tree.fm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.ml2wf.v2.tree.ITreeManipulable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FeatureModelTask implements ITreeManipulable<FeatureModelTask> {

    // TODO: use inheritance with structure

    @JacksonXmlProperty(localName = "abstract", isAttribute = true)
    private boolean isAbstract;
    @JacksonXmlProperty(localName = "mandatory", isAttribute = true)
    private boolean isMandatory;
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(localName = "description")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Setter(AccessLevel.PRIVATE)
    private List<Description> descriptions = new ArrayList<>();
    @JacksonXmlProperty(localName="and")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Setter(AccessLevel.PRIVATE)
    private List<FeatureModelTask> children = new ArrayList<>();
    @JacksonXmlProperty(localName="alt")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Setter(AccessLevel.PRIVATE)
    private List<FeatureModelTask> alternativeChildren = new ArrayList<>();
    @JacksonXmlProperty(localName="feature")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Setter(AccessLevel.PRIVATE)
    private List<FeatureModelTask> childrenLeaves = new ArrayList<>();

    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Setter
    @ToString
    static final class Description {

        @JacksonXmlText
        private String content = "";
    }

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
