package com.ml2wf.constraints.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
 * @since 1.0.0
 */
public class BinaryTree<T> {

    /**
     * Root of the tree.
     */
    @Getter @Setter
    private T root;
    /**
     * Left child of the tree.
     */
    @Getter private BinaryTree<T> leftChild;
    /**
     * Right child of the tree.
     */
    @Getter private BinaryTree<T> rightChild;
    /**
     * Blocking state of the tree.
     */
    @Getter private boolean isBlocked;

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
        isBlocked = false;
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
     * Sets the tree's left child.
     *
     * @param leftChild the new tree's left child
     */
    public void setLeftChild(T leftChild) {
        if (isBlocked) {
            return; // TODO: raise exception
        }
        this.leftChild = new BinaryTree<>(leftChild);
    }

    /**
     * Sets the tree's right child.
     *
     * @param rightChild the new tree's right child
     */
    public void setRightChild(T rightChild) {
        if (isBlocked) {
            return; // TODO: raise exception
        }
        this.rightChild = new BinaryTree<>(rightChild);
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
        isBlocked = block;
    }

    /**
     * Blocks of unblocks the current tree.
     *
     * <p>
     *
     * <b>Note</b> that blocking the current tree recursively block its children.
     *
     * @param block the new blocking state
     */
    public void block(boolean block) {
        isBlocked = block;
        blockLeftChild(block);
        blockRightChild(block);
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
     */
    public void blockLeftChild(boolean block) {
        if (leftChild == null) {
            if (!block) {
                return;
            } else {
                leftChild = new BinaryTree<>(); // TODO: reuse creating methods
            }
        }
        leftChild.setBlocked(block);
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
     */
    public void blockRightChild(boolean block) {
        if (rightChild == null) {
            if (!block) {
                return;
            } else {
                rightChild = new BinaryTree<>(); // TODO: reuse creating methods
            }
        }
        rightChild.setBlocked(block);
    }

    /**
     * TODO: behave differently than {@link #addRightChild(BinaryTree)}.
     * TODO: improve this behavior
     */
    public BinaryTree<T> addLeftChild(BinaryTree<T> child) {
        if (isBlocked) {
            return null; // TODO: raise exception
        }
        if (root == null) {
            // TODO: improve this behavior
            root = child.getRoot();
            leftChild = child.getLeftChild();
            rightChild = child.getRightChild();
            return this;
        } else {
            if (leftChild != null) {
                return rightChild.addLeftChild(child);
            } else {
                leftChild = child;
                return leftChild;
            }
        }
    }

    public BinaryTree<T> addLeftChild(T child) {
        if (isBlocked) {
            return null; // TODO: raise exception
        }
        return addLeftChild(new BinaryTree<>(child));
    }

    public BinaryTree<T> addRightChild(BinaryTree<T> child) {
        if (isBlocked) {
            return null; // TODO: raise exception
        }
        if (root == null) {
            // TODO: improve this behavior
            root = child.getRoot();
            leftChild = child.getLeftChild();
            rightChild = child.getRightChild();
            return this;
        } else {
            if (rightChild != null) {
                return rightChild.addRightChild(child);
            } else {
                rightChild = child;
                return rightChild;
            }
        }
    }

    public BinaryTree<T> addRightChild(T child) {
        if (isBlocked) {
            return null; // TODO: raise exception
        }
        return addRightChild(new BinaryTree<>(child));
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
     *
     * @return whether the insertion succeed or not
     */
    public boolean insertWhenPossible(BinaryTree<T> tree) {
        if (isBlocked) {
            return false;
        }
        if (root == null) {
            root = tree.getRoot();
            leftChild = tree.getLeftChild();
            rightChild = tree.getRightChild();
        } else if (leftChild == null) {
            leftChild = tree;
        } else if (rightChild == null) {
            rightChild = tree;
        } else {
            return Stream.of(leftChild, rightChild).anyMatch(c -> c.insertWhenPossible(tree));
        }
        return true;
    }

    /**
     * Returns whether the current tree has children or not.
     *
     * @return whether the current tree has children or not
     *
     * @since 1.0
     */
    public boolean hasChildren() {
        return hasLeftChild() || hasRightChild();
    }

    /**
     * Returns whether the current tree has a left child or not.
     *
     * @return whether the current tree has a left child or not
     *
     * @since 1.0
     */
    public boolean hasLeftChild() {
        return leftChild != null;
    }

    /**
     * Returns whether the current tree has a right child or not.
     *
     * @return whether the current tree has a right child or not
     *
     * @since 1.0
     */
    public boolean hasRightChild() {
        return rightChild != null;
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
        list.add(root); // TODO: check root == null ?
        for (BinaryTree<T> child : Arrays.asList(leftChild, rightChild)) {
            // TODO: check performances issues with Arrays.asList
            if (child != null) {
                list.addAll(child.getAllNodes());
            }
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BinaryTree<?> that = (BinaryTree<?>) o;
        return isBlocked == that.isBlocked &&
                Objects.equals(root, that.root) &&
                Objects.equals(leftChild, that.leftChild) &&
                Objects.equals(rightChild, that.rightChild);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, leftChild, rightChild, isBlocked);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (hasLeftChild()) {
            builder.append(leftChild.toString());
        }
        if (root != null) {
            builder.append(" ");
            builder.append(root.toString());
            builder.append(" ");
        }
        if (hasRightChild()) {
            builder.append(rightChild.toString());
        }
        return builder.toString();
    }
}
