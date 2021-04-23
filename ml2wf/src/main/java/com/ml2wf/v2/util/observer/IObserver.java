package com.ml2wf.v2.util.observer;

/**
 * An object capable of observing any {@code IObservable} object by
 * registering to this object according to the <b>Observer design pattern</b>.
 *
 * @param <E>   the returned type on update
 *
 * @see IObservable
 */
@FunctionalInterface
public interface IObserver<E> {

    /**
     * Updates the current observer using the given {@code element}
     * provided by the observed object.
     *
     * @param element   the element provided by the observed object
     */
    void update(E element);
}
