package com.ml2wf.util;

/**
 * This class represents a simple pair association between two objects.
 *
 * @author Nicolas Lacroix
 *
 * @param <K> the key
 * @param <V> the associated value
 *
 * @version 1.0
 */
public class Pair<K, V> {

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
		return this.value;
	}

	/**
	 * Updates the {@code Pair} with a new key-value association.
	 *
	 * <p>
	 *
	 * <b>Note</b> that updating the {@code Pair} will override the previous
	 * association.
	 *
	 * @param key   the new key
	 * @param value the new value
	 *
	 * @since 1.0
	 */
	public void updatePair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Returns whether the current {@code Pair} association is empty or not.
	 *
	 * @return whether the current {@code Pair} association is empty or not
	 *
	 * @since 1.0
	 */
	public boolean isEmpty() {
		return (this.key == null) && (this.value == null);
	}

}
