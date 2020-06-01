package com.ml2wf.constraints.factory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.constraints.config.Config;
import com.ml2wf.constraints.parser.ConstraintParser;
import com.ml2wf.constraints.parser.Parser;
import com.ml2wf.constraints.tree.BinaryTree;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;

// TODO: create a configuration file to define constraint syntax
// TODO: add singleton dp

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
 * @version 1.0
 *
 * @see ConstraintFactory
 * @see Node
 * @see Config
 * @see ConstraintParser
 *
 */
public class ConstraintFactoryImpl implements ConstraintFactory {

	/**
	 * {@code Config}'s instance that will be used for constraints parsing.
	 *
	 * @see Config
	 */
	private Config config;
	/**
	 * {@code Parser}'s instance that will parse given constraints.
	 *
	 * @see Parser
	 */
	private Parser parser;
	/**
	 * {@code Document} instance that will be used for {@code Node} creation.
	 *
	 * @see Document
	 */
	private static Document document;

	/**
	 * {@code ConstraintFactoryImpl}'s default constructor.
	 *
	 * <p>
	 *
	 * It initializes :
	 *
	 * <p>
	 *
	 * <ul>
	 * <li>the {@code Document} instance that will be used for {@code Node}
	 * creation,</li>
	 * <li>the {@code Config} instance,</li>
	 * <li>{@code ConstraintParser} instance that will be used for parsing
	 * constraints</li>
	 * </ul>
	 *
	 * @throws ParserConfigurationException
	 *
	 * @see Document
	 * @see Config
	 * @see ConstraintParser
	 */
	public ConstraintFactoryImpl() throws ParserConfigurationException {
		// Document instantiation
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		document = docBuilder.newDocument();
		// Parser instantiation
		this.config = Config.getInstance();
		this.parser = new ConstraintParser(this.config);
	}

	/**
	 * Generates a {@code List} of rule {@code Node} containing all constraints
	 * nodes from the given
	 * {@code constraintText}.
	 *
	 * <p>
	 *
	 * TODO: explain algorithm
	 *
	 * <p>
	 *
	 * <b>Note</b> that your {@code Document} instance must adopt returned nodes
	 * before adding them.
	 *
	 * @param constraintText text containing constraints
	 * @return a generated {@code Node} containing all constraints nodes
	 * @throws InvalidConstraintException
	 *
	 * @since 1.0
	 * @see Node
	 */
	@Override
	public List<Node> getRuleNodes(String constraintText) throws InvalidConstraintException {
		List<Node> rules = new ArrayList<>();
		List<BinaryTree<String>> trees = this.parser.parseContent(constraintText);
		System.out.println(trees);
		for (BinaryTree<String> tree : trees) {
			Node rule = document.createElement(FeatureModelNames.RULE.getName());
			// rules.add(this.generateNode(tree, rule));
			this.generateNode(tree, rule);
			rules.add(rule);
		}
		return rules;
	}

	/**
	 * Generates a {@code Node} according to {@code tree}'s root value and call
	 * recursively for each {@code tree}'s child.
	 *
	 * @param tree tree to convert to {@code Node}
	 * @param base base {@code Node} which will contains newly generated
	 *             {@code Node}
	 * @throws InvalidConstraintException
	 *
	 * @since 1.0
	 * @see BinaryTree
	 * @see Node
	 */
	private void generateNode(BinaryTree<String> tree, Node base) throws InvalidConstraintException {
		// TODO: iterate over all tree's nodes and call createNode for each node
		String rootValue = tree.getRoot();
		System.out.println("generating for " + rootValue);
		if (rootValue == null) {
			// stop condition
			return;
		}
		base = base.appendChild(this.createNode(rootValue));
		for (BinaryTree<String> child : Arrays.asList(tree.getLeftChild(), tree.getRightChild())) {
			if (child != null) {
				this.generateNode(child, base);
			}
		}
	}

	/**
	 * Creates a {@code Node} from a {@code String} according to {@code config} and
	 * {@code FeatureModelNames} data.
	 *
	 * @param element element to convert to {@code Node}
	 * @return a {@code Node} corresponding to the given {@code element}
	 *
	 * @since 1.0
	 * @see Node
	 * @see Config
	 * @see FeatureModelNames
	 */
	public Node createNode(String element) {
		Node node;
		if (this.config.isAnOperator(element)) {
			node = document.createElement(Config.getVocmapping().get(element).getName());
		} else {
			node = document.createElement(FeatureModelNames.VAR.getName());
			node.appendChild(document.createTextNode(element)); // TODO: check if working
		}
		return node;
	}

	/**
	 * "Pretty prints" a DOM {@code Node}.
	 *
	 * TODO: remove if not useful
	 *
	 * @param node node to print
	 * @return a {@code String} containing a description of the given {@code node}
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @since 1.0
	 * @see Node
	 */
	public static String nodeToString(Node node)
			throws TransformerFactoryConfigurationError, TransformerException {
		// pretty print a DOM Node
		StringWriter sw = new StringWriter();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.transform(new DOMSource(node), new StreamResult(sw));
		return sw.toString();
	}

	public static void main(String[] args) throws ParserConfigurationException, InvalidConstraintException,
			TransformerFactoryConfigurationError, TransformerException {
		String content = "qdfsdgfh [[!(!A&!(B|C)=>D)]]fsdeghf";
		ConstraintFactory factory = new ConstraintFactoryImpl();
		List<Node> consNodes = factory.getRuleNodes(content);
		System.out.println(nodeToString(consNodes.get(0)));
	}
}
