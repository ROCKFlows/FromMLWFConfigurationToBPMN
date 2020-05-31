package com.ml2wf.constraints.tree;

import java.util.Arrays;
import java.util.Iterator;

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
public class BinaryTree<T> implements Iterator<T> {

	private T node;
	private BinaryTree<T> rightChild;
	private BinaryTree<T> leftChild;
	private boolean isBlocked;

	public BinaryTree(T node, BinaryTree<T> rightChild, BinaryTree<T> leftChild) {
		this.node = node;
		this.rightChild = rightChild;
		this.leftChild = leftChild;
		this.isBlocked = false;
	}

	public BinaryTree(T node) {
		this(node, null, null);
	}

	public BinaryTree() {
		this(null);
	}

	public T getNode() {
		return this.node;
	}

	public void setNode(T node) {
		this.node = node;
	}

	public BinaryTree<T> getRightChild() {
		return this.rightChild;
	}

	public void setRightChild(T rightChild) {
		if (this.isBlocked) {
			return; // TODO: raise exception
		}
		this.rightChild = new BinaryTree<>(rightChild);
	}

	public BinaryTree<T> getLeftChild() {
		return this.leftChild;
	}

	public void setLeftChild(T leftChild) {
		if (this.isBlocked) {
			return; // TODO: raise exception
		}
		this.leftChild = new BinaryTree<>(leftChild);
	}

	public boolean isBlocked() {
		return this.isBlocked;
	}

	public void setBlocked(boolean block) {
		this.isBlocked = block;
	}

	public BinaryTree<T> addRightChild(BinaryTree<T> child) {
		if (this.isBlocked) {
			return null; // TODO: raise exception
		}
		if (this.node == null) {
			// TODO: improve this behavior
			this.node = child.getNode();
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
	 * TODO: behave differently than {@link #addRightChild(BinaryTree)}.
	 */
	public BinaryTree<T> addLeftChild(BinaryTree<T> child) {
		if (this.isBlocked) {
			return null; // TODO: raise exception
		}
		if (this.node == null) {
			// TODO: improve this behavior
			this.node = child.getNode();
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

	public boolean insertWhenPossible(BinaryTree<T> tree) {
		if (this.isBlocked) {
			return false;
		}
		if (this.node == null) {
			this.node = tree.getNode();
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

	public void block(boolean block) {
		this.isBlocked = block;
		this.blockLeftChild(block);
		this.blockRightChild(block);
	}

	public void blockLeftChild(boolean block) {
		if (this.leftChild == null) {
			this.leftChild = new BinaryTree<>(); // TODO: reuse creating methods
		}
		this.leftChild.setBlocked(block);
	}

	public void blockRightChild(boolean block) {
		if (this.rightChild == null) {
			this.rightChild = new BinaryTree<>(); // TODO: reuse creating methods
		}
		this.rightChild.setBlocked(block);
	}

	@Override
	public boolean hasNext() {
		return (this.leftChild != null) && (this.rightChild != null);
	}

	@Override
	public T next() {
		if (this.hasNext()) {
			if (this.leftChild != null) {
				return this.leftChild.next();
			} else {
				return this.rightChild.next();
			}
		}
		return this.node;
	}

	public StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
		// https://stackoverflow.com/a/27153988
		if (this.rightChild != null) {
			this.rightChild.toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
		}
		sb.append(prefix).append(isTail ? "└── " : "┌── ").append(this.node).append("(").append(this.isBlocked)
				.append(")\n");
		if (this.leftChild != null) {
			this.leftChild.toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
		}
		return sb;
	}

	@Override
	public String toString() {
		return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
	}
}
