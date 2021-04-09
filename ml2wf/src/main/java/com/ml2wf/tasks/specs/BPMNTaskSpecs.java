package com.ml2wf.tasks.specs;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.w3c.dom.Element;

import com.ml2wf.conventions.Notation;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.util.RegexManager;
import com.ml2wf.util.XMLManager;

/**
 * This {@code enum} contains all specifications that a {@code BPMNTask} can
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
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum BPMNTaskSpecs implements WFSpec<WFTask<?>> {

    // TODO: add ABSTRACT
    OPTIONAL("optional", RegexManager.getOptionalityPattern()) {

        @Override
        public String formatSpec(String content) {
            if (content.contains(Notation.OPTIONALITY)) {
                return Notation.OPTIONALITY + " : " + true;
            }
            return "";
        }
    },
    CATEGORY("category", RegexManager.getCategoryPattern());

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
    public boolean hasSpec(WFTask<?> task) {
        return task.getSpecs().containsKey(name);
    }

    @Override
    public void apply(WFTask<?> task) {
        Optional<String> optSpecValue;
        if (!hasSpec(task)) {
            optSpecValue = getSpecValue(task);
            if (optSpecValue.isEmpty()) {
                return;
            }
            task.addSpec(name, optSpecValue.get());
        }
    }

    @Override
    public void applyAll(WFTask<?> task) {
        for (BPMNTaskSpecs spec : BPMNTaskSpecs.values()) {
            spec.apply(task);
        }
    }

    @Override
    public Optional<String> getSpecValue(WFTask<?> task) {
        if (hasSpec(task)) {
            return Optional.of(task.getSpecValue(name));
        } else {
            for (String documentation : XMLManager.getAllBPMNDocContent((Element) task.getNode())) {
                Matcher matcher = pattern.matcher(documentation.replace(" ", ""));
                if (matcher.find() && (matcher.groupCount() > 0)) {
                    return Optional.of(matcher.group(1));
                }
            }
        }
        return Optional.empty();
    }
}
