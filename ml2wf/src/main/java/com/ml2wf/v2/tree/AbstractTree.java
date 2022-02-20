package com.ml2wf.v2.tree;

import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
@Log4j2
public abstract class AbstractTree<T extends Identifiable<I>, I> implements ITreeManipulable<T, I>, // TODO: rework extend Identifiable to have access cf parent
        IObservable<AbstractTreeEvent<T>> {

    @NonNull protected final Set<IObserver<AbstractTreeEvent<T>>> observers = new HashSet<>();
    @NonNull private final List<T> children = new ArrayList<>();
    // TODO: @NonNull @Delegate protected final TreeInternalMemory<I, T> internalMemory = new TreeInternalMemory<>();

    protected AbstractTree(@NonNull Collection<T> children) {
        for (T child : children) {
            if (appendChild(child).isLeft()) {
                log.error("Can't add task {} for workflow.", child.getIdentity());
            }
        }
    }

    @Override
    public List<T> getChildren() {
        // do not return a copy as jackson needs the reference
        return children;
    }

    @Override
    public Collection<T> getChildrenMatching(@NonNull Predicate<T> predicate) {
        return children.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public Either<String, T> appendChild(T child) {
        // TODO: check if already have a parent
        children.add(child);
        // TODO: set parent
        return Either.right(child);
    }

    @Override
    public Optional<T> removeChild(T child) {
        // TODO: set parent to null if is child of this
        return (children.remove(child)) ? Optional.of(child) : Optional.empty();
    }

    @Override
    public Optional<T> getChildWithIdentity(@NonNull I identity) {
        return children.stream().filter(c -> c.getIdentity().equals(identity)).findAny();
    }

    @Override
    public Optional<T> getChildMatching(@NonNull Predicate<T> predicate) {
        return children.stream().filter(predicate).findAny();
    }

    @Override
    public boolean hasChildWithIdentity(@NonNull I identity) {
        return getChildWithIdentity(identity).isPresent();
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
