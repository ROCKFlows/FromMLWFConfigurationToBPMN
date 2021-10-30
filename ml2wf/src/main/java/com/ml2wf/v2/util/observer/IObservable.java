package com.ml2wf.v2.util.observer;

/**
 * An object observable by any {@code IObserver<T>} according to
 * the <b>Observer design pattern</b>.
 *
 * @param <E>   the returned type on notification
 *
 * @see IObserver
 *
 * @since 1.1.0
 */
public interface IObservable<E> {

    /**
     * Subscribes the given {@code observer} to the current
     * observable object.
     *
     * @param observer  the observer to subscribe
     */
    void subscribe(IObserver<E> observer);

    /**
     * Unsubscribes the given {@code observer} to the current
     * observable object.
     *
     * @param observer  the observer to unsubscribe
     */
    void unsubscribe(IObserver<E> observer);

    /**
     * Notifies every subscribed observer.
     *
     * @param event the event
     */
    void notifyOnChange(E event);
}
