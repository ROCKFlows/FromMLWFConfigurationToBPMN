package com.ml2wf.v2.tree;

import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An {@link AbstractTree} is a base class for any tree implementation.
 *
 * <p>
 *
 * It implements the {@link ITreeManipulable} interface defining the main tree operations
 * and is {@link IObservable} to facilitate its manipulation.
 *
 * <p>
 *
 * An {@link AbstractTree} contains {@link Identifiable} children which allows it to use an
 * {@link TreeInternalMemory} to improve its performances when manipulating them.
 *
 * @param <T> the type of the tree's nodes
 *
 * @see ITreeManipulable
 * @see IObservable
 * @see Identifiable
 * @see AbstractTreeEvent
 *
 * @since 1.1.0
 */
@EqualsAndHashCode
@RequiredArgsConstructor
@Log4j2
public abstract class AbstractTree<T extends Identifiable<I>, I> implements ITreeManipulable<T, I>,
        IObservable<AbstractTreeEvent<T>> {

    @NonNull protected final Set<IObserver<AbstractTreeEvent<T>>> observers = new HashSet<>();
    @NonNull @Delegate protected final TreeInternalMemory<I, T> internalMemory = new TreeInternalMemory<>();

    /**
     * An {@link ITreeIterator} implementation providing its children iterator.
     *
     * <p>
     *
     * <b>Note</b> that the {@link ITreeIterator}'s methods implementations are delegated
     * to its {@link #innerIterator}.
     *
     * @see ITreeIterator
     */
    public class TreeIterator implements ITreeIterator<T> {

        // TODO: should we keep this class ?

        @Delegate private final Iterator<T> innerIterator;

        /**
         * {@code TreeIterator}'s default constructor.
         *
         * <p>
         *
         * Instantiates its {@link #innerIterator} using the tree's {@link #internalMemory}'s values.
         */
        private TreeIterator() {
            innerIterator = internalMemory.getChildren().iterator();
        }
    }

    @Override
    public ITreeIterator<T> iterator() {
        return new TreeIterator();
    }

    @Override
    public void subscribe(@NonNull final IObserver<AbstractTreeEvent<T>> observer) {
        observers.add(observer);
        log.trace("Observer {} has subscribed to {}", observer, this);
    }

    @Override
    public void unsubscribe(@NonNull final IObserver<AbstractTreeEvent<T>> observer) {
        observers.remove(observer);
        log.trace("Observer {} has unsubscribed from {}", observer, this);
    }

    @Override
    public void notifyOnChange(@NonNull final AbstractTreeEvent<T> event) {
        observers.forEach(o -> o.update(event));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
