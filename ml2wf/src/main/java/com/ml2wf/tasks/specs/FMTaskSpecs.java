package com.ml2wf.tasks.specs;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.util.RegexManager;

/**
 * This {@code enum} contains all specifications that a {@code FMTask} can
 * contain.
 *
 * <p>
 *
 * Furthermore, each constant has specific behaviors specified by the
 * {@link Spec} interface that help for the retrieval and appliance of these
 * specifications.
 *
 * <p>
 *
 * Thus, it is an implementations of the {@code Spec} interface.
 *
 * @author Nicolas Lacroix
 *
 * @see Spec
 * @see FMTask
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum FMTaskSpecs implements Spec<FMTask> {

    // TODO: add ABSTRACT
    OPTIONAL("optional", RegexManager.getOptionalityPattern()) {

        @Override
        public void apply(FMTask task) {
            // TODO: factorize with other apply implementations
            Optional<String> optSpecValue = this.getSpecValue(task);
            if (optSpecValue.isEmpty()) {
                return;
            }
            Element featureAttribute = AbstractMerger.createFeatureAttribute(getName(),
                    Boolean.parseBoolean(optSpecValue.get()));
            task.getNode().appendChild(featureAttribute);
            task.addSpec(getName(), optSpecValue.get());
        }

    },
    CATEGORY("category", RegexManager.getCategoryPattern()) {

        @Override
        public void apply(FMTask task) {
            Optional<String> optSpecValue = getSpecValue(task);
            if (optSpecValue.isEmpty()) {
                return;
            }
            Element featureAttribute = AbstractMerger.createFeatureAttribute(getName(), optSpecValue.get());
            task.getNode().appendChild(featureAttribute);
            task.addSpec(getName(), optSpecValue.get());
        }

    };

    /**
     * The current specification's name.
     */
    private final String name;
    /**
     * Current pattern (regex) used to retrieve the current specification value.
     *
     * @see Pattern
     */
    private final Pattern pattern;

    @Override
    public boolean hasSpec(FMTask task) {
        return task.getSpecs().containsKey(name);
    }

    @Override
    public Optional<String> getSpecValue(FMTask task) {
        // TODO: factorize with BPMNTaskSpecs#getSpecValue(FMTask)
        if (hasSpec(task)) {
            return Optional.of(task.getSpecValue(this.getName()));
        }
        NodeList docNodes = ((Element) task.getNode()).getElementsByTagName(FMNames.DESCRIPTION.getName());
        if (docNodes.getLength() == 0) {
            return Optional.empty();
        }
        Node docNode = docNodes.item(0);
        Matcher matcher = this.getPattern().matcher(docNode.getTextContent());
        if (matcher.matches() && (matcher.groupCount() > 0)) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    @Override
    public void applyAll(FMTask task) {
        for (FMTaskSpecs spec : FMTaskSpecs.values()) {
            spec.apply(task);
        }
    }
}
