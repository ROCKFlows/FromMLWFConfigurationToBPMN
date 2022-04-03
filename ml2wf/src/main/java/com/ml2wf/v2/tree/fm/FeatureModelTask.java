package com.ml2wf.v2.tree.fm;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.ml2wf.v2.tree.Identifiable;
import com.ml2wf.v2.tree.events.RenamingEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link FeatureModelTask} is an extension of a {@link FeatureModelStructure} that
 * contains additional information such as {@link #isAbstract} and {@link #isMandatory} status.
 * It also has a {@link #name} and a {@link Description} instance.
 *
 * @see FeatureModelStructure
 * @see Description
 * <p>
 * TODO: improve doc (Identifiable part)
 * @since 1.1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, of = {"parent", "name", "isAbstract", "isMandatory", "descriptions"})
@ToString(of = {"name", "isAbstract", "isMandatory"})
public class FeatureModelTask extends FeatureModelStructure implements Identifiable<String> {

    private FeatureModelTask parent;
    private String name;
    private boolean isAbstract;
    private boolean isMandatory;
    private List<Description> descriptions;

    @SuppressWarnings("unused")
    private FeatureModelTask() {
        // used by Jackson for deserialization
        super(new ArrayList<>());
        this.descriptions = new ArrayList<>();
    }

    /**
     * {@code FeatureModelTask}'s constructor with a nullable parent, a name, the {@link #isAbstract} and
     * {@link #isMandatory} status, some {@link Description}s and its children {@link FeatureModelTask}s.
     *
     * @param parent       the new task's parent
     * @param name         the new task's parent
     * @param isAbstract   the new task's {@link #isAbstract} status
     * @param isMandatory  the new task's {@link #isMandatory} status
     * @param descriptions the new task's descriptions
     * @param children     the new task's children
     */
    private FeatureModelTask(FeatureModelTask parent, @NonNull String name, boolean isAbstract, boolean isMandatory,
                             @NonNull List<Description> descriptions, @NonNull List<FeatureModelTask> children) {
        super(children);
        this.parent = parent;
        this.name = name;
        this.isAbstract = isAbstract;
        this.isMandatory = isMandatory;
        this.descriptions = new ArrayList<>(descriptions);
        children.forEach(c -> c.setParent(this));
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FeatureModelTaskFactory {

        public static FeatureModelTask createTask(@NonNull String name, boolean isAbstract, boolean isMandatory) {
            return new FeatureModelTask(null, name, isAbstract, isMandatory, new ArrayList<>(), new ArrayList<>());
        }

        public static FeatureModelTask createTask(@NonNull String name, boolean isAbstract, boolean isMandatory,
                                                  @NonNull Collection<String> descriptions) {
            List<Description> fmDescriptions = descriptions.stream()
                    .map(Description::new)
                    .collect(Collectors.toList());
            return new FeatureModelTask(null, name, isAbstract, isMandatory, fmDescriptions, new ArrayList<>());
        }
    }

    /**
     * A {@link Description} has a {@link #content} providing additional
     * information about a {@link FeatureModelTask}.
     *
     * @see FeatureModelTask
     * @since 1.1.0
     */
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Data
    public static final class Description {

        private String content = "";

        public String toString() {
            return content;
        }
    }

    @Override
    public @NonNull String getIdentity() {
        return name;
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

    @Override
    public Iterator<FeatureModelTask> iterator() {
        return (hasChildren()) ? Iterables.concat(getChildren()).iterator() :
                Iterators.singletonIterator(FeatureModelTask.this);
    }
}
