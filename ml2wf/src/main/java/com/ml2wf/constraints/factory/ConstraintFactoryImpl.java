package com.ml2wf.constraints.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.constraints.config.ConfigImpl;
import com.ml2wf.constraints.config.DefaultConfig;
import com.ml2wf.constraints.parser.ConstraintParser;
import com.ml2wf.constraints.parser.Parser;
import com.ml2wf.constraints.tree.BinaryTree;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.merge.base.BaseMergerImpl;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.util.Pair;

/**
 * This class is a factory for {@code Node} from constraints.
 *
 * <p>
 *
 * It is able to generate a {@code List<Node>} from a text containing
 * constraints according to the given {@code Config} instance using the
 * {@code ConstraintParser} class.
 *
 * <p>
 *
 * It is an implementation of the {@link ConstraintFactory} interface.
 *
 * @author Nicolas Lacroix
 *
 * @see ConstraintFactory
 * @see Node
 * @see ConfigImpl
 * @see ConstraintParser
 *
 * @since 1.0.0
 */
@Log4j2
public class ConstraintFactoryImpl implements ConstraintFactory {

	/**
	 * {@code Config}'s instance that will be used for constraints parsing.
	 *
	 * @see ConfigImpl
	 */
	private final ConfigImpl config;
	/**
	 * {@code Parser}'s instance that will parse given constraints.
	 *
	 * @see Parser
	 */
	private final Parser parser;
	/**
	 * {@code Document} instance that will be used for {@code Node} creation.
	 *
	 * @see Document
	 */
	private final Document document;

	/**
	 * {@code ConstraintFactoryImpl}'s complete constructor.
	 *
	 * <p>
	 *
	 * It initializes :
	 *
	 * <p>
	 *
	 * <ul>
	 * <li>the {@code Config} instance,</li>
	 * <li>{@code ConstraintParser} instance that will be used for parsing
	 * constraints</li>
	 * </ul>
	 *
	 * @param document document used to create nodes
	 *
	 * @see Document
	 * @see ConfigImpl
	 * @see ConstraintParser
	 */
	public ConstraintFactoryImpl(Document document)  {
		// Document instantiation
		this.document = document;
		// Parser instantiation
		this.config = ConfigImpl.getInstance();
		this.parser = new ConstraintParser(this.config);
	}

	/**
	 * Generates a {@code List} of rule {@code Node} containing all constraints
	 * nodes from the given {@code constraintText}.
	 *
	 * <p>
	 *
	 * Pseudo-code :
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>
	 * function getRuleNodes(constraintText: String)
	 *   rules <- empty list
	 *   trees <- parser.parseContent(constraintText)
	 *   foreach tree in trees do
	 *     rule <- document.createElement(RULE)
	 *     generateNode(tree, rule)
	 *     rules.Add(rule)
	 *   end
	 *   return rules
	 * end
	 * </code>
	 * </pre>
	 *
	 * <p>
	 *
	 * <b>Note</b> that your {@code Document} instance must adopt returned nodes
	 * before adding them.
	 *
	 * @param constraintText text containing constraints
	 * @return a generated {@code Node} containing all constraints nodes
	 *
	 * @throws UnresolvedConflict if an unresolved conflict is detected
	 *
	 * @see Node
	 */
	@Override
	public List<Node> getRuleNodes(String constraintText) throws UnresolvedConflict {
		List<Node> rules = new ArrayList<>();
		List<BinaryTree<String>> trees = this.parser.parseContent(constraintText);
		for (BinaryTree<String> tree : trees) {
			this.managedMissingCriteria(tree);
			Node rule = this.document.createElement(FMNames.RULE.getName());
			this.generateRuleNode(tree, rule);
			rules.add(rule);
		}
		return rules;
	}

	@Override
	public List<Pair<FMTask, Node>> getOrderNodes(String constraintText) {
		List<Pair<FMTask, Node>> pairs = new ArrayList<>();
		Node description;
		if (this.parser.isOrderConstraint(constraintText)) {
			List<BinaryTree<String>> trees = this.parser.parseContent(constraintText);
			for (BinaryTree<String> tree : trees) {
				description = this.document.createElement(FMNames.DESCRIPTION.getName());
				// get involved nodes
				List<String> taskNames = tree.getAllNodes().stream().filter(n -> !this.config.isAnOperator(n))
						.collect(Collectors.toList());
				List<FMTask> tasks = taskNames.stream().map(TasksManager::getFMTaskWithName).map(o -> o.orElse(null))
						.collect(Collectors.toList());
				// get and add LCA
				FMTask lca = (tasks.contains(null)) ? null : FMTask.getLCA(tasks);
				// set description node
				description.setTextContent(tree.toString());
				// add new pair to list
				pairs.add(new Pair<>(lca, description));
			}
		}
		return pairs;
	}

	@Override
	public String getAssociationConstraint(String globalTask, List<String> tasksNames) {
		return Notation.getConstraintDelimiterLeft() + globalTask + DefaultConfig.IMP.getSymbol()
				+ String.join(DefaultConfig.CONJ.getSymbol(), tasksNames) + Notation.getConstraintDelimiterRight();
	}

	/**
	 * Creates missing criteria to keep constraints despite of some missing
	 * criterias.
	 *
	 * @param tree tree to manage missing criterias
	 *
	 * @throws UnresolvedConflict if an unresolved conflict is detected
	 */
	private void managedMissingCriteria(BinaryTree<String> tree) throws UnresolvedConflict {
		Set<String> taskNames = TasksManager.getTasks().stream().map(Task::getName).collect(Collectors.toSet());
		List<String> missingNames = tree.getAllNodes().stream().filter(n -> (n != null) && !this.config.isAnOperator(n))
				.collect(Collectors.toList());
		missingNames.removeAll(taskNames);
		FMTask parentTask = BaseMergerImpl.getUnmanagedGlobalTask(BaseMergerImpl.UNMANAGED_FEATURES);
		FMTask featureTask;
		for (String missingName : missingNames) {
			log.warn("Creating the missing criteria : {}.", missingName);
			Element feature = AbstractMerger.createFeatureWithAbstract(missingName, false);
			featureTask = AbstractMerger.getTaskFactory().createTask(feature);
			AbstractMerger.insertNewTask(parentTask, featureTask);
		}
	}

	/**
	 * Generates a {@code Node} according to {@code tree}'s root value and call
	 * recursively for each {@code tree}'s child.
	 *
	 * <p>
	 *
	 * Pseudo-code :
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>
	 * function generateNode(tree: BinaryTree, base: Node)
	 *   rootValue <- tree.root
	 *   if rootValue == null then
	 *     return
	 *   end
	 *   base <- base.appendChild(createNode(rootValue))
	 *   foreach child in (tree.leftChild, tree.rightChild) do
	 *     if child != null then
	 *       generateNode(child, base)
	 *     end
	 *   end
	 * end
	 * </code>
	 * </pre>
	 *
	 * <p>
	 *
	 * @param tree tree to convert to {@code Node}
	 * @param base base {@code Node} which will contains newly generated
	 *             {@code Node}
	 *
	 * @see BinaryTree
	 * @see Node
	 */
	private void generateRuleNode(BinaryTree<String> tree, Node base) {
		String rootValue = tree.getRoot();
		if (rootValue == null) {
			// stop condition
			return;
		}
		base = base.appendChild(this.createNode(rootValue));
		for (BinaryTree<String> child : Arrays.asList(tree.getLeftChild(), tree.getRightChild())) {
			if (child != null) {
				this.generateRuleNode(child, base);
			}
		}
	}

	/**
	 * Creates a {@code Node} from a {@code String} according to {@code config} and
	 * {@code FeatureModelNames} data.
	 *
	 * @param element element to convert to {@code Node}
	 *
	 * @return a {@code Node} corresponding to the given {@code element}
	 *
	 * @see Node
	 * @see ConfigImpl
	 * @see FMNames
	 */
	public Node createNode(String element) {
		Node node;
		if (this.config.isAnOperator(element)) {
			node = this.document.createElement(this.config.getVocMapping().get(element));
		} else {
			node = this.document.createElement(FMNames.VAR.getName());
			node.appendChild(this.document.createTextNode(element));
		}
		return node;
	}
}
