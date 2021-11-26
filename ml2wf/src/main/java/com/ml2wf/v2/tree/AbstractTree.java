package com.ml2wf.v2.tree;

import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.tree.events.AdditionEvent;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;

import java.util.*;

/**
 * An {@link AbstractTree} is a base class for any tree implementation.
 *
 * <p>
 *
 * It implements the {@link ITreeManipulable} interface defining the main tree operations
 * and is {@link IObservable} to facilitate its manipulation.
 *
 * @param <T> the type of the tree's nodes
 *
 * @see ITreeManipulable
 * @see IObservable
 * @see AbstractTreeEvent
 *
 * @since 1.1.0
 */
@EqualsAndHashCode
@RequiredArgsConstructor
@Log4j2
public abstract class AbstractTree<T> implements ITreeManipulable<T>, IObservable<AbstractTreeEvent<T>> {

    @Getter @NonNull protected final List<T> children = new ArrayList<>();
    @NonNull protected final Set<IObserver<AbstractTreeEvent<T>>> observers = new HashSet<>();
    protected AbstractInternalMemory internalMemory;

    /**
     * This inner class is the {@link AbstractTree}'s internal memory.
     *
     * <p>
     *
     * This memory allows avoiding time-consuming tree search by keeping update a {@link Map} containing
     * all useful information for manipulating a {@link AbstractTree}.
     *
     * <p>
     *
     * It observes the current {@link AbstractTree} implementation to keep its
     * structure memory consistent.
     *
     * @see AbstractTree
     * @see IObserver
     */
    protected abstract class AbstractInternalMemory implements IObserver<AbstractTreeEvent<T>> {

        @Delegate protected final Map<String, Pair<T, List<T>>> memory = new HashMap<>();

        /**
         * {@code AbstractInternalMemory}'s default constructor.
         *
         * <p>
         *
         * <b>Note</b> that this default constructor calls the {@link #subscribe(IObserver)} method
         * to comply with its {@link IObserver} nature.
         */
        protected AbstractInternalMemory() {
            subscribe(this);
        }
    }

    /**
     * An {@link ITreeIterator} implementation providing a {@link #children}'s iterator.
     *
     * <p>
     *
     * <b>Note</b> that the {@link ITreeIterator}'s methods implementations are delegated
     * to its {@link #innerIterator}.
     *
     * @see ITreeIterator
     */
    public class TreeIterator implements ITreeIterator<T> {

        @Delegate private final Iterator<T> innerIterator;

        /**
         * {@code TreeIterator}'s default constructor.
         *
         * <p>
         *
         * Instantiates its {@link #innerIterator} as a new {@link #children}'s iterator.
         */
        private TreeIterator() {
            innerIterator = children.iterator();
        }
    }

    @Override
    public ITreeIterator<T> iterator() {
        return new TreeIterator();
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public Either<String, T> appendChild(final T child) {
        children.add(child);
        notifyOnChange(new AdditionEvent<>(child, children));
        return Either.right(child);
    }

    @Override
    public Optional<T> getChildWithName(final String name) {
        // TODO: Pair<T, List<T>> childPair = internalMemory.get(name);
        // return Optional.ofNullable((childPair != null && childPair.isPresent()) ? childPair.getKey() : null);
        throw new UnsupportedOperationException("TODO: waiting for internalMemory implementation");
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
