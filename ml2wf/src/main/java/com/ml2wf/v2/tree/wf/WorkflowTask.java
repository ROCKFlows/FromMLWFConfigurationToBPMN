package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A {@link WorkflowTask} is a {@link Workflow} task identified by an {@link #id},
 * has a {@link #name} and can be documented with a {@link Documentation} instance.
 *
 * @see Workflow
 * @see Documentation
 *
 * @since 1.1
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkflowTask implements IInstantiable {

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
     * @since 1.1
     */
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Setter
    @ToString
    static final class Documentation {

        @JacksonXmlProperty(isAttribute = true)
        private String id;
        @JacksonXmlText
        private String content = "";
    }

    @Override
    public void instantiate() {
        if (documentation == null) {
            documentation = new Documentation(String.format("documentation_%s", name), "");
        }
        String formatPattern = (documentation.content.isBlank()) ? "refersTo: %s%s" : "refersTo: %s%n%s";
        documentation.content = String.format(formatPattern, name, documentation.content);
        name = String.format("%s_TODO", name);
        // TODO: manage optionality
    }
}
