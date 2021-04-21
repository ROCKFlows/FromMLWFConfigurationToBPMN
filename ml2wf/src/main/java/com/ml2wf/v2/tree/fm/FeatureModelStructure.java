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

/**
 * A {@link FeatureModelStructure} contains the {@link FeatureModelTask} instances of
 * a {@link FeatureModel}.
 *
 * <p>
 *
 * As specified by the {@link FeatureModel} class, three kinds of tasks are differentiated.
 * These tasks are stored into three distinct collections :
 *
 * <ul>
 *     <li>the {@link #children} contains the <b>and</b> tasks</li>
 *     <li>the {@link #childrenLeaves} contains the <b>features</b></li>
 *     <li>the {@link #alternativeChildren} contains the <b>alternative</b> tasks</li>
 * </ul>
 *
 * @see FeatureModel
 * @see FeatureModelTask
 *
 * @since 1.1
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
public class FeatureModelStructure extends AbstractTree<FeatureModelTask>  {

    @JacksonXmlProperty(localName="and")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FeatureModelTask> children = new ArrayList<>();
    @JacksonXmlProperty(localName="feature")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FeatureModelTask> childrenLeaves = new ArrayList<>();
    @JacksonXmlProperty(localName="alt")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FeatureModelTask> alternativeChildren = new ArrayList<>();

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
