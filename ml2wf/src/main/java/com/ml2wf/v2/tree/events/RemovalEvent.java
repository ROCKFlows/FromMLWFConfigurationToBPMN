package com.ml2wf.v2.tree.events;

import com.ml2wf.v2.tree.ITreeManipulable;
import lombok.ToString;

/**
 * An {@link Events#REMOVAL} related implementation of the {@link AdditionEvent} base class.
 *
 * @param <T> the removed node type
 *
 * @since 1.1.0
 */
@ToString(callSuper = true)
public class RemovalEvent<T extends ITreeManipulable<T>> extends AbstractTreeEvent<T> {

    /**
     * {@code RemovalEvent}'s constructor with the removed node.
     *
     * @param node  the removed node
     */
    public RemovalEvent(final T node) {
        super(Events.REMOVAL, node);
    }
}
