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
 * @version 1.0
 *
 * @see BinaryTree
 *
 */
public class TestBinaryTree {

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
		this.emptyTree = new BinaryTree<>();
		this.soloRootTree = new BinaryTree<>(ROOT);
		this.fullTree = new BinaryTree<>(ROOT, this.emptyTree, this.soloRootTree);
		this.leftChild = new BinaryTree<>(LEFT);
		this.rightChild = new BinaryTree<>(RIGHT);
	}

	@AfterEach
	public void clean() {
		this.emptyTree = null;
		this.soloRootTree = null;
		this.fullTree = null;
		this.leftChild = null;
		this.rightChild = null;
	}

	/**
	 * Tests class' getters.
	 */
	@Test
	@DisplayName("Test of getters")
	public void testGetters() {
		assertNull(this.emptyTree.getRoot());
		assertNull(this.emptyTree.getLeftChild());
		assertNull(this.emptyTree.getRightChild());
		assertEquals(ROOT, this.soloRootTree.getRoot());
		assertSame(this.emptyTree, this.fullTree.getLeftChild());
		assertSame(this.soloRootTree, this.fullTree.getRightChild());
	}

	/**
	 * Tests class' setters.
	 */
	@Test
	@DisplayName("Test of setters")
	public void testSetters() {
		this.emptyTree.setRoot(ROOT);
		assertEquals(ROOT, this.emptyTree.getRoot());
		this.emptyTree.setLeftChild(LEFT);
		assertEquals(this.leftChild, this.emptyTree.getLeftChild());
		this.emptyTree.setRightChild(RIGHT);
		assertEquals(this.rightChild, this.emptyTree.getRightChild());
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
	public void testAddMethods() {
		this.emptyTree.setRoot(ROOT);
		assertEquals(ROOT, this.emptyTree.getRoot());
		this.emptyTree.setLeftChild(LEFT);
		assertEquals(this.leftChild, this.emptyTree.getLeftChild());
		this.emptyTree.setRightChild(RIGHT);
		assertEquals(this.rightChild, this.emptyTree.getRightChild());
	}

	/**
	 * Tests {@link BinaryTree#block(boolean) block} and
	 * {@link BinaryTree#isBlocked() isBlocked} methods.
	 */
	@Test
	@DisplayName("Test of (un)blocking methods")
	public void testBlock() {
		assertFalse(this.emptyTree.isBlocked());
		// test blocking
		this.fullTree.block(true);
		assertTrue(this.emptyTree.isBlocked());
		// test adding children when blocked
		assertNull(this.emptyTree.addLeftChild(LEFT));
		assertNull(this.emptyTree.addRightChild(RIGHT));
		// test setting children when blocked
		this.emptyTree.setLeftChild(LEFT);
		assertNull(this.emptyTree.getLeftChild());
		this.emptyTree.setLeftChild(RIGHT);
		assertNull(this.emptyTree.getRightChild());
		// test inserting when possible when blocked
		assertFalse(this.emptyTree.insertWhenPossible(this.fullTree));
		this.fullTree.block(false);
		assertFalse(this.emptyTree.isBlocked());
	}

}
