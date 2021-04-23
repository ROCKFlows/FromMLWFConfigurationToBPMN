package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.ml2wf.conventions.Notation;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.tree.wf.util.WorkflowTaskUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Locale;

/**
 * A {@link WorkflowTask} is a {@link Workflow} task identified by an {@link #id},
 * has a {@link #name} and can be documented with a {@link Documentation} instance.
 *
 * @see Workflow
 * @see Documentation
 *
 * @since 1.1.0
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class WorkflowTask implements INormalizable, IInstantiable {

    @JacksonXmlProperty(isAttribute = true)
    private String id;
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(localName = "bpmn2:documentation")
    private Documentation documentation;

    /**
     * A {@link Documentation} is defined by an {@link #id} and has a {@link #content}.
     *
     * <p>
     *
     * It provides additional information about a {@link WorkflowTask}.
     *
     * @see WorkflowTask
     *
     * @since 1.1.0
     */
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static final class Documentation {

        @JacksonXmlProperty(isAttribute = true)
        private String id;
        @JacksonXmlText
        private String content = "";
    }

    @JsonIgnore
    public boolean isAbstract() {
        return name.trim().endsWith(Notation.GENERIC_VOC.toLowerCase(Locale.ROOT));
    }

    @JsonIgnore
    public boolean isOptional() {
        return WorkflowTaskUtil.isOptional(this);
    }

    @Override
    public void normalize() {
        name = name.trim().replace(" ", "_");
    }

    @Override
    public void instantiate() {
        if (documentation == null) {
            documentation = new Documentation(String.format("documentation_%s", name), "");
        }
        String formatPattern = (documentation.content.isBlank()) ? "refersTo: %s%s" : "refersTo: %s%n%s";
        documentation.content = String.format(formatPattern, name, documentation.content);
        name = String.format("%s_TODO", name);
    }
}
