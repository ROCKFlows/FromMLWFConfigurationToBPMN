package com.ml2wf.v2.tree.fm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.ml2wf.v2.tree.events.RenamingEvent;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link FeatureModelTask} is an extension of a {@link FeatureModelStructure} that
 * contains additional information such as {@link #isAbstract} and {@link #isMandatory} status.
 * It also has a {@link #name} and a {@link Description} instance.
 *
 * @see FeatureModelStructure
 * @see Description
 *
 * @since 1.1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(of = {"name", "isAbstract", "isMandatory"})
public class FeatureModelTask extends FeatureModelStructure {

    @JsonIgnore
    private FeatureModelTask parent;
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(localName = "abstract", isAttribute = true)
    private boolean isAbstract;
    @JacksonXmlProperty(localName = "mandatory", isAttribute = true)
    private boolean isMandatory;
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
     * @since 1.1.0
     */
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static final class Description {

        @JacksonXmlText
        private String content = "";

        public static Description fromDocumentation(WorkflowTask.Documentation documentation) {
            return new Description(documentation.getContent());
        }
    }

    /**
     * Returns a {@link FeatureModelTask} instance based on the given {@link WorkflowTask}.
     *
     * @param workflowTask  the {@link WorkflowTask}'s instance
     *
     * @return the resulting {@link FeatureModelTask}
     */
    public static FeatureModelTask fromWorkflowTask(WorkflowTask workflowTask) {
        var description = Description.fromDocumentation(workflowTask.getDocumentation());
        List<Description> descriptions = new ArrayList<>();
        descriptions.add(description);
        return new FeatureModelTask(null, workflowTask.getName(), workflowTask.isAbstract(),
                !workflowTask.isOptional(), descriptions);
    }

    @Override
    public void normalize() {
        String oldName = name;
        name = name.trim().replace(" ", "_");
        if (!name.equals(oldName)) {
            notifyOnChange(new RenamingEvent<>(this, oldName));
        }
        super.normalize();
    }
}
