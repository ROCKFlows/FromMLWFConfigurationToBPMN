package com.ml2wf.v2.tree.events;

import com.ml2wf.v2.tree.ITreeManipulable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * The {@link AbstractTreeEvent} describe an event that happened for an {@link ITreeManipulable}
 * implementation.
 *
 * <p>
 *
 * All supported events are described in the {@link Events} {@code enum}.
 *
 * @param <T> the involved node type
 *
 * @see ITreeManipulable
 * @see Events
 *
 * @since 1.1.0
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTreeEvent<T> {

    // TODO: limit T to tree related elements + update implementations + update doc ?

    /**
     * The {@link Events}-related event type.
     */
    protected final Events eventType;
    /**
     * The involved node.
     */
    protected final T node;

    /**
     * This {@code enum} contains all supported events for a {@link ITreeManipulable}
     * implementation.
     */
    public enum Events {

        /**
         * If a node has been added to the tree.
         */
        ADDITION,
        /**
         * If a node has been removed from the tree.
         */
        REMOVAL,
        /**
         * If a node has been renamed.
         */
        RENAMING,
        /**
         * If a node has been moved in the tree.
         */
        MOVED,
        /**
         * If a node has been instantiated.
         */
        INSTANTIATION
    }
}
