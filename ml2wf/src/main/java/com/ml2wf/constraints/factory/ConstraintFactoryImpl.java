package com.ml2wf.constraints.factory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.constraints.config.Config;
import com.ml2wf.constraints.parser.ConstraintParser;
import com.ml2wf.constraints.parser.Parser;
import com.ml2wf.constraints.tree.BinaryTree;

// TODO: create a configuration file to define constraint syntax
// TODO: add singleton dp

public class ConstraintFactoryImpl implements ConstraintFactory {

	private Config config;
	private Parser parser;
	private static Document document;

	public ConstraintFactoryImpl() throws ParserConfigurationException {
		// Document instantiation
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		document = docBuilder.newDocument();
		// Parser instantiation
		this.config = new Config(""); // TODO
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
		for (BinaryTree<String> tree : trees) {
			rules.add(this.generateNode(tree));
		}
		return rules;
	}

	private Node generateNode(BinaryTree<String> tree) throws InvalidConstraintException {
		// TODO: iterate over all tree's nodes and call createNode for each node
		return null;
	}

	public Node createNode(String child) {
		// TODO
		return null;
	}

	/**
	 * "Pretty prints" a DOM {@code Node}.
	 *
	 * TODO: to remove if not useful
	 *
	 * @param node node to print
	 * @return a {@code String} containing a description of the given {@code node}
	 *
	 * @since 1.0
	 * @see Node
	 */
	public static String nodeToString(Node node) {
		// pretty print a DOM Node
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}

	public static void main(String[] args) throws ParserConfigurationException, InvalidConstraintException {
		String content = "qdfsdgfh [[!(!AB&!(B|C)=>D)]]fsdeghf";
		ConstraintParser parser = new ConstraintParser(new Config(""));
		List<BinaryTree<String>> treeResult = parser.parseContent(content);
		System.out.println(treeResult);
	}
}
