package com.ml2wf.v2.tree.events;

import com.ml2wf.v2.tree.ITreeManipulable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * An {@link Events#MOVED} related implementation of the {@link AdditionEvent} base class.
 *
 * @param <T> the moved node type
 *
 * @since 1.1.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MovedEvent<T extends ITreeManipulable<T>> extends AbstractTreeEvent<T> {

    /**
     * The old node location.
     */
    private final List<T> oldLocation;
    /**
     * The new node location.
     */
    private final List<T> newLocation;

    /**
     * {@code MovedEvent}'s constructor with the moved node, its old and new locations.
     *
     * @param node          the old node location
     * @param oldLocation   the old node location
     * @param newLocation   the new node location
     */
    public MovedEvent(T node, final List<T> oldLocation, final List<T> newLocation) {
        super(Events.MOVED, node);
        this.oldLocation = oldLocation; // TODO: fix sonarlint issue
        this.newLocation = newLocation; // TODO: fix sonarlint issue
    }
}
