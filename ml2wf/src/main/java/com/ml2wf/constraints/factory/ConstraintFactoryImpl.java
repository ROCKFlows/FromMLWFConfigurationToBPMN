package com.ml2wf.constraints.factory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ml2wf.constraints.config.Config;
import com.ml2wf.constraints.parser.ConstraintParser;
import com.ml2wf.constraints.parser.Parser;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;

// TODO: create a configuration file to define constraint syntax
// TODO: add singleton dp

public class ConstraintFactoryImpl implements ConstraintFactory {

	private Config config;
	private Parser parser;
	private static Document document;
	private Map<Integer, Queue<String>> depth;

	public ConstraintFactoryImpl() throws ParserConfigurationException {
		// Document instantiation
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		document = docBuilder.newDocument();
		// Parser instantiation
		this.parser = new ConstraintParser(null);
		this.config = new Config(""); // TODO
		this.depth = new TreeMap<>();
	}

	public Map<Integer, Queue<String>> getDepth() {
		return this.depth;
	}

	/**
	 * Generates a rule {@code Node} containing all constraints nodes from the given
	 * {@code constraintText}.
	 *
	 * <p>
	 *
	 * Here is the <b>algorithm</b> (pseudocode) :
	 *
	 * <p>
	 *
	 * <code>
	 * for each keyword in parse(constraintText):
	 * <pre>get involved tasks</pre>
	 * <pre>generate corresponding node</pre>
	 * return generated_nodes
	 * </code>
	 *
	 * <p>
	 *
	 * <b>Note</b> that your {@code Document} instance must adopt returned nodes
	 * before adding them.
	 *
	 * @param constraintText text containing constraints
	 * @return a generated {@code Node} containing all constraints nodes
	 *
	 * @since 1.0
	 * @see Node
	 */
	@Override
	public List<Node> getRuleNodes(String constraintText) {
		List<Node> rules = new ArrayList<>();
		this.parse(constraintText);
		Node ruleNode = document.createElement(FeatureModelNames.RULE.getName());
		ruleNode = this.generateNodes(ruleNode);
		return rules;
	}

	public void parse(String constraintText) {
		// TODO: move in ConstraintParser
		int separatorCounter = 0;
		for (List<String> constraints : this.parser.parseContent(constraintText)) {
			for (String element : constraints) {
				// TODO: check parser utility
				if (element.equals("(")) {
					separatorCounter++;
				} else if (element.equals(")")) {
					separatorCounter--;
				} else {
					if (!this.depth.containsKey(separatorCounter)) {
						this.depth.put(separatorCounter, new ArrayDeque<String>());
					}
					this.depth.get(separatorCounter).add(element);
				}
			}
		}
	}

	private Node generateNodes(Node root) {
		Map<Integer, Queue<String>> reversedMap = ((TreeMap<Integer, Queue<String>>) this.depth).descendingMap();
		List<Integer> keyList = new ArrayList<>();
		keyList.addAll(reversedMap.keySet());
		if (reversedMap.isEmpty()) {
			return root;
		}
		if (reversedMap.size() == 1) {
			root.appendChild(this.createNode(reversedMap.get(0).poll()));
			reversedMap.clear();
			return root;
		}
		int currentKey = keyList.get(0);
		int nextKey = keyList.get(1);
		while (!reversedMap.isEmpty()) {
			Queue<String> childrenQueue = reversedMap.get(currentKey);
			Queue<String> parentQueue = reversedMap.get(nextKey);
			String parent = parentQueue.poll();
			String childA = childrenQueue.poll();
			if (this.isOneSide(parent)) {
				root = this.createNode(parent, childA);
			} else {
				String childB = childrenQueue.poll();
				root = this.createNode(parent, childA, childB);
			}
			if (childrenQueue.isEmpty()) {
				reversedMap.remove(currentKey);
			}
		}
		return root;
	}

	private boolean isUnary(String operator) {
		// TODO: move this method
		return operator.equals("!");
	}

	private boolean isOneSide(String operator) {
		return this.isUnary(operator) || !this.config.getOperatorsList().contains(operator);
	}

	private Node createNode(String parent, String childA, String childB) {
		return null;
	}

	private Node createNode(String parent, String child) {
		return null;
	}

	private Node createNode(String child) {
		return null;
	}

	public static void main(String[] args) throws ParserConfigurationException {
		// String complexContent = "[[!(((A & B) | (B | C)) & !(C & D) => E) <=> F]] &
		// [[A => B]] eza sq [[A => !(B | C) & C]] plus [[!(!(A & B) | C > D => E)]]";
		String complexContent = "[[!(((A & B | E) | (B | C) | (D & E)) & !(C & D) => E) <=> F]] & [[testA | testB]]";
		// ConstraintFactoryImpl factory = new ConstraintFactoryImpl();
		// factory.parse(complexContent);
		// factory.getRuleNode(complexContent);
	}
}
