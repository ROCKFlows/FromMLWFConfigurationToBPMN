package com.ml2wf.util;

import lombok.Data;

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
@Data
public final class Pair<K, V> {

    /**
     * The associative key.
     */
    private K key;
    /**
     * The associated value.
     */
    private V value;

    /**
     * {@code Pair}'s complete constructor.
     *
     * @param key   the key
     * @param value the associated value
     */
    public Pair(final K key, final V value) {
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
     * Returns whether the current {@code Pair} association is empty or not.
     *
     * @return whether the current {@code Pair} association is empty or not
     */
    public boolean isEmpty() {
        return (key == null) && (value == null);
    }

    /**
     * Returns whether the current {@code Pair} association is present or not.
     *
     * @return whether the current {@code Pair} association is present or not
     */
    public boolean isPresent() {
        return !isEmpty();
    }
}
