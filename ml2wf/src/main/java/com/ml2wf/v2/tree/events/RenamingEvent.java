package com.ml2wf.v2.tree.events;

import com.ml2wf.v2.tree.Identifiable;
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
public class RenamingEvent<T extends Identifiable<I>, I> extends AbstractTreeEvent<T> { // TODO: to rename

    /**
     * The old node's identity.
     */
    private final I oldIdentity;

    /**
     * {@code RenamingEvent}'s constructor with the removed node.
     *
     * @param node      the renamed node
     * @param oldIdentity   the old node's name
     */
    public RenamingEvent(final T node, final I oldIdentity) {
        super(Events.RENAMING, node);
        this.oldIdentity = oldIdentity;
    }
}
