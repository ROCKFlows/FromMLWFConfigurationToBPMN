package com.ml2wf.v2.tree.fm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.AbstractTree;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A {@link FeatureModel} has a {@link FeatureModelStructure} containing its
 * {@link FeatureModelTask}s.
 *
 * <p>
 *
 * It is an extension of the {@link AbstractTree} class but delegated its contract methods
 * to its {@link #structure}.
 *
 * <p>
 *
 * Three kinds of tasks are differentiated :
 *
 * <ul>
 *     <li>a <b>feature</b> is a leaf</li>
 *     <li>an <b>and</b> task is a task specified by another task</li>
 *     <li>an <b>alternative</b> task contains an alternative group of tasks</li>
 * </ul>
 *
 * @see FeatureModelStructure
 * @see FeatureModelTask
 *
 * @since 1.1
 */
@JacksonXmlRootElement(localName = "extendedFeatureModel") // TODO: differentiate extended
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FeatureModel extends AbstractTree<FeatureModelTask> {

    @JacksonXmlProperty(localName = "struct")
    private FeatureModelStructure structure;

    /**
     * {@inheritDoc}
     *
     * <p>
     *
     * <b>Note</b> that this operation is delegated to its {@link #structure}.
     *
     * @param child the child
     *
     * @return the appended child
     */
    @Override
    public FeatureModelTask appendChild(FeatureModelTask child) {
        // delegates to structure
        return structure.appendChild(child);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *
     * <b>Note</b> that this operation is delegated to its {@link #structure}.
     *
     * @param child the child
     *
     * @return the removed child
     */
    @Override
    public FeatureModelTask removeChild(FeatureModelTask child) {
        // delegates to structure
        return structure.removeChild(child);
    }
}
