package com.ml2wf.constraints.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a binary tree structure.
 *
 * <p>
 *
 * It has a different behavior than a classical binary tree to match the needs
 * of constraints representation.
 *
 * <p>
 *
 * Indeed,
 *
 * <p>
 *
 * <ul>
 * <li>TODO: list all specific behaviors</li>
 * </ul>
 *
 * @author Nicolas Lacroix
 *
 * @param <T> type of {@code node}
 *
 * @version 1.0
 */
public class BinaryTree<T> {

	/**
	 * Root of the tree.
	 */
	private T root;
	/**
	 * Left child of the tree.
	 */
	private BinaryTree<T> leftChild;
	/**
	 * Right child of the tree.
	 */
	private BinaryTree<T> rightChild;
	/**
	 * Blocking state of the tree.
	 */
	private boolean isBlocked;

	/**
	 * {@code BinaryTree}'s full constructor.
	 *
	 * @param root       root of the tree
	 * @param leftChild  left child of the tree
	 * @param rightChild right child og the tree
	 */
	public BinaryTree(T root, BinaryTree<T> leftChild, BinaryTree<T> rightChild) {
		this.root = root;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.isBlocked = false;
	}

	/**
	 * {@code BinaryTree}'s partial constructor.
	 *
	 * <p>
	 *
	 * Initializes a new {@code BinaryTree} with {@code null} children.
	 *
	 * @param root root of the tree
	 */
	public BinaryTree(T root) {
		this(root, null, null);
	}

	/**
	 * {@code BinaryTree}'s empty constructor.
	 *
	 * <p>
	 *
	 * Initializes a new {@code BinaryTree} with {@code null} root and {@code null}
	 * children.
	 */
	public BinaryTree() {
		this(null);
	}

	/**
	 * Returns the tree's root.
	 *
	 * @return the tree's root
	 */
	public T getRoot() {
		return this.root;
	}

	/**
	 * Sets the tree's root.
	 *
	 * @param root the new root
	 */
	public void setRoot(T root) {
		this.root = root;
	}

	/**
	 * Returns the tree's left child.
	 *
	 * @return the tree's left child
	 */
	public BinaryTree<T> getLeftChild() {
		return this.leftChild;
	}

	/**
	 * Sets the tree's left child.
	 *
	 * @param leftChild the new tree's left child
	 */
	public void setLeftChild(T leftChild) {
		if (this.isBlocked) {
			return; // TODO: raise exception
		}
		this.leftChild = new BinaryTree<>(leftChild);
	}

	/**
	 * Returns the tree's right child.
	 *
	 * @return the tree's right child
	 */
	public BinaryTree<T> getRightChild() {
		return this.rightChild;
	}

	/**
	 * Sets the tree's right child.
	 *
	 * @param rightChild the new tree's right child
	 */
	public void setRightChild(T rightChild) {
		if (this.isBlocked) {
			return; // TODO: raise exception
		}
		this.rightChild = new BinaryTree<>(rightChild);
	}

	/**
	 * Returns whether the current tree is blocked or not.
	 *
	 * @return whether the current tree is blocked or not
	 */
	public boolean isBlocked() {
		return this.isBlocked;
	}

	/**
	 * Blocks or unblocks the current tree depending on the {@code block} value.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method only block the current tree and not its
	 * children.
	 *
	 * @param block the new blocking state
	 */
	public void setBlocked(boolean block) {
		// TODO: check logic (compare to block)
		// blocking node but not his children ?
		this.isBlocked = block;
	}

	/**
	 * Blocks of unblocks the current tree.
	 *
	 * <p>
	 *
	 * <b>Note</b> that blocking the current tree recursively block its children.
	 *
	 * @param block the new blocking state
	 *
	 * @since 1.0
	 */
	public void block(boolean block) {
		this.isBlocked = block;
		this.blockLeftChild(block);
		this.blockRightChild(block);
	}

	/**
	 * Blocks of unblocks the tree's left child.
	 *
	 * <p>
	 *
	 * If left child is {@code null} and {@code block} is true, then an empty
	 * {@code BinaryTree} left child is instantiated and blocked.
	 *
	 * @param block the new blocking state
	 *
	 * @since 1.0
	 */
	public void blockLeftChild(boolean block) {
		if (this.leftChild == null) {
			if (!block) {
				return;
			} else {
				this.leftChild = new BinaryTree<>(); // TODO: reuse creating methods
			}
		}
		this.leftChild.setBlocked(block);
	}

	/**
	 * Blocks of unblocks the tree's right child.
	 *
	 * <p>
	 *
	 * If left child is {@code null} and {@code block} is true, then an empty
	 * {@code BinaryTree} right child is instantiated and blocked.
	 *
	 * @param block the new blocking state
	 *
	 * @since 1.0
	 */
	public void blockRightChild(boolean block) {
		if (this.rightChild == null) {
			if (!block) {
				return;
			} else {
				this.rightChild = new BinaryTree<>(); // TODO: reuse creating methods
			}
		}
		this.rightChild.setBlocked(block);
	}

	/**
	 * TODO: behave differently than {@link #addRightChild(BinaryTree)}.
	 * TODO: improve this behavior
	 */
	public BinaryTree<T> addLeftChild(BinaryTree<T> child) {
		if (this.isBlocked) {
			return null; // TODO: raise exception
		}
		if (this.root == null) {
			// TODO: improve this behavior
			this.root = child.getRoot();
			this.leftChild = child.getLeftChild();
			this.rightChild = child.getRightChild();
			return this;
		} else {
			if (this.leftChild != null) {
				return this.rightChild.addLeftChild(child);
			} else {
				this.leftChild = child;
				return this.leftChild;
			}
		}
	}

	public BinaryTree<T> addLeftChild(T child) {
		if (this.isBlocked) {
			return null; // TODO: raise exception
		}
		return this.addLeftChild(new BinaryTree<>(child));
	}

	public BinaryTree<T> addRightChild(BinaryTree<T> child) {
		if (this.isBlocked) {
			return null; // TODO: raise exception
		}
		if (this.root == null) {
			// TODO: improve this behavior
			this.root = child.getRoot();
			this.leftChild = child.getLeftChild();
			this.rightChild = child.getRightChild();
			return this;
		} else {
			if (this.rightChild != null) {
				return this.rightChild.addRightChild(child);
			} else {
				this.rightChild = child;
				return this.rightChild;
			}
		}
	}

	public BinaryTree<T> addRightChild(T child) {
		if (this.isBlocked) {
			return null; // TODO: raise exception
		}
		return this.addRightChild(new BinaryTree<>(child));
	}

	/**
	 * Inserts the given {@code tree} when it is possible.
	 *
	 * <p>
	 *
	 * Four different behaviors can be observed,
	 *
	 * <p>
	 *
	 * <ul>
	 * <li>if the tree is empty, then the current tree is replaced by the given
	 * {@code tree}</li>
	 * <li>else if the left child is empty, then the current left child becomes the
	 * given {@code tree}</li>
	 * <li>else if the right child is empty, then the current right child becomes
	 * the given {@code tree}</li>
	 * <li>else recursively inserts</li>
	 * </ul>
	 *
	 * <p>
	 *
	 * <b>Note</b> that an insertion can fail if the tree is blocked.
	 *
	 * @param tree tree to insert
	 * @return whether the insertion succeed or not
	 */
	public boolean insertWhenPossible(BinaryTree<T> tree) {
		if (this.isBlocked) {
			return false;
		}
		if (this.root == null) {
			this.root = tree.getRoot();
			this.leftChild = tree.getLeftChild();
			this.rightChild = tree.getRightChild();
			return true;
		} else if (this.leftChild == null) {
			this.leftChild = tree;
			return true;
		} else if (this.rightChild == null) {
			this.rightChild = tree;
			return true;
		} else {
			for (BinaryTree<T> child : Arrays.asList(this.leftChild, this.rightChild)) {
				if (child.insertWhenPossible(tree)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Returns whether the current tree has children or not.
	 *
	 * @return whether the current tree has children or not
	 *
	 * @since 1.0
	 */
	public boolean hasChildren() {
		return this.hasLeftChild() || this.hasRightChild();
	}

	/**
	 * Returns whether the current tree has a left child or not.
	 *
	 * @return whether the current tree has a left child or not
	 *
	 * @since 1.0
	 */
	public boolean hasLeftChild() {
		return this.leftChild != null;
	}

	/**
	 * Returns whether the current tree has a right child or not.
	 *
	 * @return whether the current tree has a right child or not
	 *
	 * @since 1.0
	 */
	public boolean hasRightChild() {
		return this.rightChild != null;
	}

	/**
	 * Returns all nodes of the current tree.
	 *
	 * @return all nodes of the current tree
	 *
	 * @since 1.0
	 */
	public List<T> getAllNodes() {
		List<T> list = new ArrayList<>();
		list.add(this.root); // TODO: check root == null ?
		for (BinaryTree<T> child : Arrays.asList(this.leftChild, this.rightChild)) {
			// TODO: check performances issues with Arrays.asList
			if (child != null) {
				list.addAll(child.getAllNodes());
			}
		}
		return list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (this.isBlocked ? 1231 : 1237);
		result = (prime * result) + ((this.leftChild == null) ? 0 : this.leftChild.hashCode());
		result = (prime * result) + ((this.rightChild == null) ? 0 : this.rightChild.hashCode());
		result = (prime * result) + ((this.root == null) ? 0 : this.root.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: replace by a simpler method
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (this.getClass() != obj.getClass())) {
			return false;
		}
		BinaryTree<?> other = (BinaryTree<?>) obj;
		if (this.isBlocked != other.isBlocked) {
			return false;
		}
		if (this.leftChild == null) {
			if (other.leftChild != null) {
				return false;
			}
		} else if (!this.leftChild.equals(other.leftChild)) {
			return false;
		}
		if (this.rightChild == null) {
			if (other.rightChild != null) {
				return false;
			}
		} else if (!this.rightChild.equals(other.rightChild)) {
			return false;
		}
		if (this.root == null) {
			if (other.root != null) {
				return false;
			}
		} else if (!this.root.equals(other.root)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" ");
		if (this.hasLeftChild()) {
			builder.append(this.leftChild.toString());
			builder.append(" ");
		}
		if (this.root != null) {
			builder.append(this.root.toString());
			builder.append(" ");
		}
		if (this.hasRightChild()) {
			builder.append(this.rightChild.toString());
		}
		return builder.toString();
	}
}
