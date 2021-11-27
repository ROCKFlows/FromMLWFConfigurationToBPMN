package com.ml2wf.v2.tree.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An {@link Events#ADDITION} related implementation of the {@link AbstractTreeEvent} base class.
 *
 * @param <T> the added node type
 *
 * @see AbstractTreeEvent
 *
 * @since 1.1.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AdditionEvent<T> extends AbstractTreeEvent<T> {

    /**
     * {@code AdditionEvent}'s constructor with the added node and its location.
     *
     * @param node      the added node
     */
    public AdditionEvent(final T node) {
        super(Events.ADDITION, node);
    }
}
