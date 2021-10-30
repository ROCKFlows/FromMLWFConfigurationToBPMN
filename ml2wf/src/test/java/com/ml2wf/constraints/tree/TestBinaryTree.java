package com.ml2wf.constraints.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This class tests the {@link BinaryTree} class.
 *
 * <p>
 *
 * Tests are executed with the <a href="https://junit.org/junit5/">JUnit
 * framework</a>.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 *
 * @see BinaryTree
 */
class TestBinaryTree {

	/**
	 * Default root name.
	 */
	private static final String ROOT = "ROOT";
	/**
	 * Left child's root name.
	 */
	private static final String LEFT = "LEFT";
	/**
	 * Right child's root name.
	 */
	private static final String RIGHT = "RIGHT";
	/**
	 * An empty {@code BinaryTree}.
	 */
	private BinaryTree<String> emptyTree;
	/**
	 * A {@code BinaryTree} only containing a root.
	 */
	private BinaryTree<String> soloRootTree;
	/**
	 * A full {@code BinaryTree}.
	 */
	private BinaryTree<String> fullTree;
	/**
	 * A left child {@code BinaryTree}.
	 */
	private BinaryTree<String> leftChild;
	/**
	 * A right child {@code BinaryTree}.
	 */
	private BinaryTree<String> rightChild;

	@BeforeEach
	public void setUp() {
		emptyTree = new BinaryTree<>();
		soloRootTree = new BinaryTree<>(ROOT);
		fullTree = new BinaryTree<>(ROOT, emptyTree, soloRootTree);
		leftChild = new BinaryTree<>(LEFT);
		rightChild = new BinaryTree<>(RIGHT);
	}

	@AfterEach
	public void clean() {
		emptyTree = null;
		soloRootTree = null;
		fullTree = null;
		leftChild = null;
		rightChild = null;
	}

	/**
	 * Tests class' getters.
	 */
	@Test
	@DisplayName("Test of getters")
	void testGetters() {
		assertNull(emptyTree.getRoot());
		assertNull(emptyTree.getLeftChild());
		assertNull(emptyTree.getRightChild());
		assertEquals(ROOT, soloRootTree.getRoot());
		assertSame(emptyTree, fullTree.getLeftChild());
		assertSame(soloRootTree, fullTree.getRightChild());
	}

	/**
	 * Tests class' setters.
	 */
	@Test
	@DisplayName("Test of setters")
	void testSetters() {
		emptyTree.setRoot(ROOT);
		assertEquals(ROOT, emptyTree.getRoot());
		emptyTree.setLeftChild(LEFT);
		assertEquals(leftChild, emptyTree.getLeftChild());
		emptyTree.setRightChild(RIGHT);
		assertEquals(rightChild, emptyTree.getRightChild());
	}

	/**
	 * Tests :
	 *
	 * <p>
	 *
	 * <ul>
	 *
	 * <li>{@link BinaryTree#addLeftChild(Object) addLeft(T child)}</li>
	 * <li>{@link BinaryTree#addLeftChild(BinaryTree) addLeft(BinaryTree<T>
	 * child)}</li>
	 * <li>{@link BinaryTree#addRightChild(Object) addRight(T child)}</li>
	 * <li>{@link BinaryTree#addRightChild(Object) addRight(BinaryTree<T>
	 * child)}</li>
	 * </ul>
	 */
	@Test
	@DisplayName("Test of setters")
	void testAddMethods() {
		emptyTree.setRoot(ROOT);
		assertEquals(ROOT, emptyTree.getRoot());
		emptyTree.setLeftChild(LEFT);
		assertEquals(leftChild, emptyTree.getLeftChild());
		emptyTree.setRightChild(RIGHT);
		assertEquals(rightChild, emptyTree.getRightChild());
	}

	/**
	 * Tests {@link BinaryTree#block(boolean) block} and
	 * {@link BinaryTree#isBlocked() isBlocked} methods.
	 */
	@Test
	@DisplayName("Test of (un)blocking methods")
	void testBlock() {
		assertFalse(emptyTree.isBlocked());
		// test blocking
		fullTree.block(true);
		assertTrue(emptyTree.isBlocked());
		// test adding children when blocked
		assertNull(emptyTree.addLeftChild(LEFT));
		assertNull(emptyTree.addRightChild(RIGHT));
		// test setting children when blocked
		emptyTree.setLeftChild(LEFT);
		assertNull(emptyTree.getLeftChild());
		emptyTree.setLeftChild(RIGHT);
		assertNull(emptyTree.getRightChild());
		// test inserting when possible when blocked
		assertFalse(emptyTree.insertWhenPossible(fullTree));
		fullTree.block(false);
		assertFalse(emptyTree.isBlocked());
	}
}
