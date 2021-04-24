package com.ml2wf.v2.tree.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An {@link Events#RENAMING} related implementation of the {@link AbstractTreeEvent} base class.
 *
 * @param <T> the renamed node type
 *
 * @see AbstractTreeEvent
 *
 * @since 1.1.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RenamingEvent<T> extends AbstractTreeEvent<T> {

    /**
     * The old node's name.
     */
    private final String oldName;

    /**
     * {@code RenamingEvent}'s constructor with the removed node.
     *
     * @param node      the renamed node
     * @param oldName   the old node's name
     */
    public RenamingEvent(final T node, final String oldName) {
        super(Events.RENAMING, node);
        this.oldName = oldName;
    }
}
