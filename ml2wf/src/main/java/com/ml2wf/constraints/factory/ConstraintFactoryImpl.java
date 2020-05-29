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
	public Node generateRuleNode(String constraintText) {
		Node rule = document.createElement(FeatureModelNames.RULE.getName());
		Node child;
		this.parse(constraintText);
		this.processMap();
		return rule;
	}

	private void parse(String constraintText) {
		// TODO: move in ConstraintParser
		int separatorCounter = 0;
		for (List<String> constraints : this.parser.parse(constraintText)) {
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

	private void processMap() {
		Map<Integer, Queue<String>> reversedMap = ((TreeMap<Integer, Queue<String>>) this.depth).descendingMap();
		List<Integer> keyList = new ArrayList<>();
		keyList.addAll(reversedMap.keySet());
		int currentKey = keyList.get(0);
		int nextKey = keyList.get(1);
		Node result;
		do {
			Queue<String> queue = reversedMap.get(currentKey);
			String childA = queue.poll();
			String childB = queue.poll();
			Queue<String> secondQueue = reversedMap.get(nextKey);
			String parent = secondQueue.poll();
			// TODO: consider a result and a method this.createNode(parent, node[, childB]);
			if (this.isOneSide(parent)) {
				result = this.createNode(parent, childA);
			} else {
				result = this.createNode(parent, childA, childB);
			}
		} while (!reversedMap.isEmpty());
	}

	private boolean isUnary(String operator) {
		// TODO: move this method
		return operator.equals("!");
	}

	private boolean isOneSide(String operator) {
		return this.isUnary(operator) || !this.config.getOperatorsList().contains(operator);
	}

	@SuppressWarnings("unused")
	private Node createNode(String parent, Node childA, String childB) {
		return null;
	}

	@SuppressWarnings("unused")
	private Node createNode(String parent, String childA, String childB) {
		return null;
	}

	@SuppressWarnings("unused")
	private Node createNode(String parent, String child) {
		return null;
	}

	public static void main(String[] args) throws ParserConfigurationException {
		// String complexContent = "[[!(((A & B) | (B | C)) & !(C & D) => E) <=> F]] &
		// [[A => B]] eza sq [[A => !(B | C) & C]] plus [[!(!(A & B) | C > D => E)]]";
		String complexContent = "[[!(((A & B) | (B | C) | (D & E)) & !(C & D) => E) <=> F]]";
		ConstraintFactory factory = new ConstraintFactoryImpl();
		factory.generateRuleNode(complexContent);
	}
}
