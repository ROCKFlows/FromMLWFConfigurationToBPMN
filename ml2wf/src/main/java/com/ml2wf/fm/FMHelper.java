package com.ml2wf.fm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.xml.sax.SAXException;

import com.ml2wf.conventions.enums.TaskTagsSelector;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.util.XMLManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FMHelper {


	private static final Logger logger = LogManager.getLogger(FMHelper.class);


	private static final String FEATURE = "feature";
	private static final String AND = "and";
	private static final String CONSTRAINTS = "constraints";
	private static final String RULE = "rule";
	private static final String IMPLY_NOTATION = "=>";
	
	private DocumentBuilder builder;
	private Document document;
	private Element racine;

	private List<String> featureNameList = new ArrayList<>();
	private List<String> constraintList = new ArrayList<>();

	public List<String> getConstraintList() {
		return new ArrayList<>(constraintList);
	}

	public List<String> getFeatureNameList() {
		return new ArrayList<>(featureNameList);
	}

	public boolean isFeature(String name) {
		return featureNameList.contains(name);
	}


	
	public FMHelper(String path) throws ParserConfigurationException, SAXException, IOException {

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
		
		builder = factory.newDocumentBuilder();
		document = builder.parse(new File(path));
		racine = document.getDocumentElement();
		featureNameList = listFeatures();
		System.out.println("apres construction du FM " + featureNameList);
		constraintList = listConstraints();
	}

//DOING
	private List<String> listConstraints() {
		logger.debug("-------- List Constraints ------");
		List<String> constraints = new ArrayList<>();
		for (Node child : iterable(racine.getChildNodes())) {
			constraints.addAll(listConstraints(child));
		}
		return constraints;
	}
	
	
//DOING
	private Collection<? extends String> listConstraints(Node n) {
		List<String> list = new ArrayList<>();
		switch (n.getNodeName()) {
		case CONSTRAINTS:
			list.addAll(readConstraints(n));
			break;
		}
		return list;
	}

	private Collection<? extends String>  readConstraints(Node n) {
		List<String> list = new ArrayList<>();
		for (Node child : iterable(n.getChildNodes())) {
			if (child.getNodeName().equals(RULE)){
				logger.debug("Manage Rule : {}",child);
				list.add(ruleToString(child));
			}
		}
		return list;
	}

	private String ruleToString(Node ruleNode) {
		String rule = "";
		for (Node child : iterable(ruleNode.getChildNodes())) {
			if ( (child != null) 
					&& (child.getNodeName() != null) )
			{
				if (child.getNodeName().equals("imp")) {
					logger.debug("imp : {}",child);
					rule = extractImply(child); 
				}
				//else {
				//	rule = child.getNodeName() + " : " + child.getNodeValue();
				//}
			}
		}
		return rule;
	}

	//IMPROVE
	private String extractImply(Node node) {
		String rule = "";
		List<String> operands = readOperands(node);
		if ((operands.size() == 2) ) {
			rule += operands.get(0);
			rule += IMPLY_NOTATION;
			rule += operands.get(1);
		} else
			rule += "Unexpected operands for imply : " +operands;
		return rule;
	}



	private List<String> readOperands(Node node) {
		List<String> operands = new ArrayList<>();
		for (Node child : iterable(node.getChildNodes())) {
			if ( (child != null) 
					&& (child.getNodeName() != null) )
			{
				if (child.getNodeName().equals("var")) {
						operands.add(child.getTextContent());
				}
			}
		}
		return operands;
	}

	//Extract Feature as a Node
	public Node extractFeature(String featureName) {
		return 	extractFeature(racine, featureName);
	}

	private Node extractFeature(Node parent, String featureName) {
		if (parent == null)
			return null;
		if (getFeatureName(parent).equals(featureName)) 
			return parent;
		for (Node child : iterable(parent.getChildNodes())) {
			Node node = extractFeature(child, featureName);
			if (node != null)
				return node;
		}
		return null;
	}



	/*
	 * List Features
	 */
	private List<String> listFeatures() {
		List<String> featureList = new ArrayList<>();
		for (Node child : iterable(racine.getChildNodes())) {
			featureList.addAll(listFeature(child));
		}
		return featureList;
	}

	private List<String> listFeature(Node n) {
		List<String> list = new ArrayList<>();
		switch (n.getNodeName()) {
		case FEATURE:
			list.add(getFeatureName(n));
			extractChildFeatures(n, list);
			break;
		case AND:
			list.add(getFeatureName(n));
			extractChildFeatures(n, list);
			break;
		default:
			extractChildFeatures(n, list);
			break;
		}
		return list;
	}

	private void extractChildFeatures(Node n, List<String> list) {
		for (Node child : iterable(n.getChildNodes())) {
			list.addAll(listFeature(child));
		}
	}
	// todo improve it
	private static Iterable<Node> iterable(final NodeList n) {
		return new Iterable<Node>() {
			@Override
			public Iterator<Node> iterator() {

				return new Iterator<Node>() {

					int index = 0;

					@Override
					public boolean hasNext() {
						return index < n.getLength();
					}

					@Override
					public Node next() {
						if (hasNext()) {
							return n.item(index++);
						} else {
							throw new NoSuchElementException();
						}
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}




	private String getFeatureName(Node node) { 
		if ( (node != null) 
				&& (node.getAttributes() != null)
				&& (node.getAttributes().getNamedItem("name") != null) )
		{
			return node.getAttributes().getNamedItem("name").getNodeValue(); 
		}
		else return "";
	}




	public boolean isDirectChildOf(String parent, String childName) {
		Node nodeParent = extractFeature(parent);
		NodeList nodes = nodeParent.getChildNodes();
		for (Node child : iterable(nodes)) { 
			if (childName.contentEquals(getFeatureName(child)) ) 
				return true; }

		return false;
	}


	public boolean isChildOf(String parent, String childName) {
		Node nodeParent = extractFeature(parent);
		return(extractFeature(nodeParent, childName) != null);
	}



	public Document getDocument() {
		return document;
	}


}
