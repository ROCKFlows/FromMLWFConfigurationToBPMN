package com.ml2wf.util;

/**
 * This class represents a simple pair association between two objects.
 *
 * @author Nicolas Lacroix
 *
 * @param <K> the key
 * @param <V> the associated value
 *
 * @since 1.0.0
 */
public class Pair<K, V> {

    /**
     * The associative key.
     */
    private final K key;
    /**
     * The associated value.
     */
    private final V value;

    /**
     * {@code Pair}'s complete constructor.
     *
     * @param key   the key
     * @param value the associated value
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * {@code Pair}'s default constructor.
     */
    public Pair() {
        this(null, null);
    }

    /**
     * Returns the current association's {@code key}.
     *
     * @return the current association's {@code key}
     */
    public K getKey() {
        return this.key;
    }

    /**
     * Returns the current association's {@code value}.
     *
     * @return the current association's {@code value}
     */
    public V getValue() {
        return value;
    }

    /**
     * Returns whether the current {@code Pair} association is empty or not.
     *
     * @return whether the current {@code Pair} association is empty or not
     */
    public boolean isEmpty() {
        return (key == null) && (value == null);
    }
}
