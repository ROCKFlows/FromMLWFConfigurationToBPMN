package com.ml2wf.v2.tree.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An {@link Events#MOVED} related implementation of the {@link AbstractTreeEvent} base class.
 *
 * @param <T> the moved node type
 *
 * @see AbstractTreeEvent
 *
 * @since 1.1.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MovedEvent<T> extends AbstractTreeEvent<T> {

    /**
     * {@code MovedEvent}'s constructor with the moved node, its old and new locations.
     *
     * @param node          the old node location
     */
    public MovedEvent(T node) {
        super(Events.MOVED, node);
        throw new UnsupportedOperationException("TODO");
    }
}
