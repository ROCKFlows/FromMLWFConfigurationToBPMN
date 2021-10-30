package com.ml2wf.v2.tree.events;

import lombok.ToString;

/**
 * An {@link Events#REMOVAL} related implementation of the {@link AbstractTreeEvent} base class.
 *
 * @param <T> the removed node type
 *
 * @see AbstractTreeEvent
 *
 * @since 1.1.0
 */
@ToString(callSuper = true)
public class RemovalEvent<T> extends AbstractTreeEvent<T> {

    /**
     * {@code RemovalEvent}'s constructor with the removed node.
     *
     * @param node  the removed node
     */
    public RemovalEvent(final T node) {
        super(Events.REMOVAL, node);
    }
}
