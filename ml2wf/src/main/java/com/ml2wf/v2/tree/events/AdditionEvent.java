package com.ml2wf.v2.tree.events;

import com.ml2wf.v2.tree.ITreeManipulable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * An {@link Events#ADDITION} related implementation of the {@link AdditionEvent} base class.
 *
 * @param <T> the added node type
 *
 * @since 1.1.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AdditionEvent<T extends ITreeManipulable<T>> extends AbstractTreeEvent<T> {

    /**
     * The node location.
     */
    private final List<T> location;

    /**
     * {@code AdditionEvent}'s constructor with the added node and its location.
     *
     * @param node      the added node
     * @param location  the node location
     */
    public AdditionEvent(final T node, final List<T> location) {
        super(Events.ADDITION, node);
        this.location = location; // TODO: fix sonarlint issue
    }
}
