package com.ml2wf.conventions.enums.fm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ml2wf.conventions.enums.TaskTagsSelector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This {@code enum} contains handled tags' names according to the
 * <a href="https://featureide.github.io/">FeatureIDE
 * framework</a>.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum FMNames implements TaskTagsSelector {

    // general tags
    FEATURE_MODEL("featureModel"), EXTENDED_FEATURE_MODEL("extendedFeatureModel"), PROPERTIES("properties"),
    STRUCT("struct"), GRAPHICS("graphics"),
    DESCRIPTION("description"),
    // task tags
    FEATURE("feature"), ALT("alt"), AND("and"),
    // constraint tags
    CONSTRAINTS("constraints"), RULE("rule"), IMPLIES("imp"), NOT("not"), EQUIVALENT("equ"), CONJ("conj"), DISJ("disj"),
    VAR("var"),
    // attribute tags
    ATTRIBUTE("attribute"),
    // reserved tags
    SELECTOR("");

    /**
     * Tag name.
     */
    private final String name;

    /**
     * Returns whether the given tag is a FeatureModel task tag's name or not.
     *
     * @param tag tag to check
     * @return whether the given tag is a FeatureModel task tag's name or not
     */
    public boolean isFMTask(String tag) {
        return this.getTaskTags().contains(tag);
    }

    @Override
    public List<String> getTaskTags() {
        return new ArrayList<>(Arrays.asList(FEATURE.getName(), AND.getName(), ALT.getName()));
    }
}
