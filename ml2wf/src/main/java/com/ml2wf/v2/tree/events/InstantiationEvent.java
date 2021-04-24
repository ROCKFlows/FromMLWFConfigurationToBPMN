package com.ml2wf.v2.tree.events;

import lombok.ToString;

/**
 * An {@link Events#INSTANTIATION} related implementation of the {@link AbstractTreeEvent} base class.
 *
 * @param <T> the removed node type
 *
 * @see AbstractTreeEvent
 *
 * @since 1.1.0
 */
@ToString(callSuper = true)
public class InstantiationEvent<T> extends AbstractTreeEvent<T> {

    /**
     * {@code RemovalEvent}'s constructor with the instantiated node.
     *
     * @param node  the removed node
     */
    public InstantiationEvent(final T node) {
        super(Events.INSTANTIATION, node);
    }
}
