package com.ml2wf.constraints.factory;

import java.util.List;

import org.w3c.dom.Node;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.tasks.exceptions.InvalidTaskException;
import com.ml2wf.util.Pair;

/**
 * This interface provides a method for the <b>generation of constraint
 * nodes</b>.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public interface ConstraintFactory {

	/**
	 * Generates a rule {@code Node} containing all constraints nodes from the given
	 * {@code constraintText}.
	 *
	 * @param constraintText text containing constraints
	 * @return a {@code List} of generated constraint nodes
	 * @throws InvalidConstraintException
	 * @throws InvalidTaskException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 * @see Node
	 */
	public List<Node> getRuleNodes(String constraintText)
			throws InvalidConstraintException, InvalidTaskException, UnresolvedConflict;

	/**
	 * Returns a {@code List} of {@code Pair} containing the LCA {@code FMTask} as
	 * key and the descriptive {@code Node} as value.
	 *
	 * @param constraintText text containing constraints
	 * @return a {@code List} of {@code Pair} containing the LCA {@code FMTask} as
	 *         left element and the descriptive {@code Node} as right element
	 *
	 * @since 1.0
	 * @see Pair
	 */
	public List<Pair<FMTask, Node>> getOrderNodes(String constraintText);

	/**
	 * Returns an <b>implication</b> association of the {@code globalTask} with the
	 * {@code tasksNames}.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method returns this association using the
	 * {@code DefaultConfig}'s symbols.
	 *
	 * @param globalTask global task
	 * @param tasksNames tasks implied by the {@code global task}
	 * @return an implication association of the {@code globalTask} with the
	 *         {@code tasksNames}
	 */
	public String getAssociationConstraint(String globalTask, List<String> tasksNames);
}
