package com.ml2wf.v2.tree.wf;

import com.ml2wf.conventions.Notation;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.tree.events.InstantiationEvent;
import com.ml2wf.v2.tree.events.RenamingEvent;
import com.ml2wf.v2.tree.wf.util.WorkflowTaskUtil;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

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
@Data
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id", "name"})
@Log4j2
public class WorkflowTask implements INormalizable, IInstantiable, IObservable<AbstractTreeEvent<WorkflowTask>> {

    private String id;
    private String name;
    private Documentation documentation;
    private final Set<IObserver<AbstractTreeEvent<WorkflowTask>>> observers = new HashSet<>();

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
    @Data
    public static final class Documentation {

        private String id;
        private String content = "";
    }

    public boolean isAbstract() {
        return name.trim().endsWith(Notation.GENERIC_VOC.toLowerCase(Locale.ROOT));
    }

    public boolean isOptional() {
        return WorkflowTaskUtil.isOptional(this);
    }

    @Override
    public void normalize() {
        String oldName = name;
        name = name.trim().replace(" ", "_");
        if (!name.equals(oldName)) {
            notifyOnChange(new RenamingEvent<>(this, oldName));
        }
    }

    @Override
    public void instantiate() {
        if (documentation == null) {
            documentation = new Documentation(String.format("documentation_%s", name), "");
        }
        String formatPattern = (documentation.content.isBlank()) ? "refersTo: %s%s" : "refersTo: %s%n%s";
        documentation.content = String.format(formatPattern, name, documentation.content);
        name = String.format("%s_TODO", name);
        notifyOnChange(new InstantiationEvent<>(this));
    }

    @Override
    public void subscribe(@NonNull final IObserver<AbstractTreeEvent<WorkflowTask>> observer) {
        observers.add(observer);
        log.trace("Observer {} has subscribed to {}", observer, this);
    }

    @Override
    public void unsubscribe(@NonNull final IObserver<AbstractTreeEvent<WorkflowTask>> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyOnChange(@NonNull final AbstractTreeEvent<WorkflowTask> event) {
        observers.forEach(o -> o.update(event));
    }
}
