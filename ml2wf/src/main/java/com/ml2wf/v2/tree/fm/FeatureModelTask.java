package com.ml2wf.v2.tree.fm;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * A {@link FeatureModelTask} is an extension of a {@link FeatureModelStructure} that
 * contains additional information such as {@link #isAbstract} and {@link #isMandatory} status.
 * It also has a {@link #name} and a {@link Description} instance.
 *
 * @see FeatureModelStructure
 * @see Description
 *
 * @since 1.1
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class FeatureModelTask extends FeatureModelStructure {

    @JacksonXmlProperty(localName = "abstract", isAttribute = true)
    private boolean isAbstract;
    @JacksonXmlProperty(localName = "mandatory", isAttribute = true)
    private boolean isMandatory;
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(localName = "description")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Setter(AccessLevel.PRIVATE)
    private List<Description> descriptions;

    /**
     * A {@link Description} has a {@link #content} providing additional
     * information about a {@link FeatureModelTask}.
     *
     * @see FeatureModelTask
     *
     * @since 1.1
     */
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    static final class Description {

        @JacksonXmlText
        private String content = "";
    }
}
