package com.ml2wf.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.extern.log4j.Log4j2;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;

@Log4j2
public class FMHelper {

    private static final String FM_OUT_PATH = "./target/generated/FM_INTERMEDIATE/";
    private static final String FEATURE = "feature";
    private static final String AND = "and";
    private static final String CONSTRAINTS = "constraints";
    private static final String RULE = "rule";
    public static final String IMPLY_NOTATION = "=>";
    private final Element racine;
    private final List<String> featureNameList;
    private final List<String> constraintList;

    public FMHelper(File file) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        Document document = factory.newDocumentBuilder().parse(file);
        racine = document.getDocumentElement();
        featureNameList = listFeatures();
        constraintList = listConstraints();
    }

    public FMHelper(String path) throws ParserConfigurationException, SAXException, IOException {
        this(new File(path));
    }

    public List<String> getConstraintList() {
        return new ArrayList<>(constraintList);
    }

    public List<String> getFeatureNameList() {
        return new ArrayList<>(featureNameList);
    }

    public boolean isFeature(String name) {
        return featureNameList.contains(name);
    }

    public boolean isAbstract(String name) {
        if (featureNameList.contains(name)) {
            Node n = extractFeature(name);
            return isAbstract(n);
        }
        return false;
    }

    public static FMHelper createFMHelper(String sourceFMFilename)
            throws IOException, ParserConfigurationException, SAXException {
        String sourceFMSAV = FM_OUT_PATH + sourceFMFilename.hashCode() + ".xml";
        File copiedFile = new File(sourceFMSAV);
        File sourceFile = new File(sourceFMFilename);
        FileUtils.copyFile(sourceFile, copiedFile);
        return new FMHelper(copiedFile);
    }

    //DOING
    private List<String> listConstraints() {
        log.debug("-------- List Constraints ------");
        List<String> constraints = new ArrayList<>();
        for (Node child : iterable(racine.getChildNodes())) {
            constraints.addAll(listConstraints(child));
        }
        return constraints;
    }

    //DOING
    private Collection<? extends String> listConstraints(Node n) {
        List<String> list = new ArrayList<>();
        if (n.getNodeName().contentEquals(CONSTRAINTS)) {
            list.addAll(readConstraints(n));
        }
        return list;
    }

    private Collection<? extends String> readConstraints(Node n) {
        List<String> list = new ArrayList<>();
        for (Node child : iterable(n.getChildNodes())) {
            if (child.getNodeName().equals(RULE)) {
                log.debug("Manage Rule : {}", child);
                list.add(ruleToString(child));
            }
        }
        return list;
    }

    private String ruleToString(Node ruleNode) {
        String rule = "";
        for (Node child : iterable(ruleNode.getChildNodes())) {
            if ((child != null) && ("imp".equals(child.getNodeName()))) {
                log.debug("imp : {}", child);
                rule = extractImply(child);
            }
        }
        return rule;
    }

    //IMPROVE
    private static String extractImply(Node node) {
        StringBuilder rule = new StringBuilder();
        List<String> operands = readOperands(node);
        if (operands.size() == 2) {
            rule.append(operands.get(0))
                    .append(IMPLY_NOTATION)
                    .append(operands.get(1));
        } else if (operands.size() > 2) {
            rule.append(operands.get(0))
                    .append(IMPLY_NOTATION)
                    .append("AND ");
            operands.remove(0);
            for (String op : operands) {
                rule.append(op).append(" ");
            }
        } else {
            rule.append("Unexpected operands for imply : ").append(operands);
        }
        return rule.toString();
    }

    private static List<String> readOperands(Node node) {
        List<String> operands = new ArrayList<>();
        for (Node child : iterable(node.getChildNodes())) {
            if ((child != null) && (child.getNodeName() != null)) {
                if ("var".equals(child.getNodeName())) {
                    operands.add(child.getTextContent());
                } else if ("conj".equals(child.getNodeName())) {
                    operands.addAll(readOperands(child));
                }
            }
        }
        return operands;
    }

    //Extract Feature as a Node
    public Node extractFeature(String featureName) {
        return extractFeature(racine, featureName);
    }

    private static Node extractFeature(Node parent, String featureName) {
        if (parent == null) {
            return null;
        }
        if (getFeatureName(parent).equals(featureName)) {
            return parent;
        }
        for (Node child : iterable(parent.getChildNodes())) {
            Node node = extractFeature(child, featureName);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    /*
     * List Features
     */
    private List<String> listFeatures() {
        List<String> featureList = new ArrayList<>();
        for (Node child : iterable(racine.getChildNodes())) {
            featureList.addAll(listFeatures(child));
        }
        return featureList;
    }

    private List<String> listFeatures(Node n) {
        List<String> list = new ArrayList<>();
        switch (n.getNodeName()) {
            case FEATURE:
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
        if (n == null) {
            return;
        }
        for (Node child : iterable(n.getChildNodes())) {
            list.addAll(listFeatures(child));
        }
    }

    public List<String> getChildren(String featureName) {
        Node n = extractFeature(featureName);
        List<String> list = new ArrayList<>();
        extractChildFeatures(n, list);
        List<String> copyList = new ArrayList<>();
        //TODO IMPROVE !!!
        for (String child : list) {
            if (isDirectChildOf(featureName, child)) {
                copyList.add(child);
            }
        }
        return copyList;
    }

    // todo improve it
    private static Iterable<Node> iterable(final NodeList n) {
        return () -> new Iterator<>() {

            private int index;

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

    private static String getFeatureName(Node node) {
        if ((node != null)
                && (node.getAttributes() != null)
                && (node.getAttributes().getNamedItem("name") != null)) {
            return node.getAttributes().getNamedItem("name").getNodeValue();
        }
        return "";
    }

    private static boolean isAbstract(Node node) {
        if ((node != null)
                && (node.getAttributes() != null)
                && (node.getAttributes().getNamedItem("abstract") != null)) {
            return (node.getAttributes().getNamedItem("abstract").getNodeValue().contentEquals("true"));
        }
        return false;
    }

    public boolean isDirectChildOf(String parent, String childName) {
        Node nodeParent = extractFeature(parent);
        NodeList nodes = nodeParent.getChildNodes();
        for (Node child : iterable(nodes)) {
            if (childName.contentEquals(getFeatureName(child))) {
                return true;
            }
        }
        return false;
    }

    public boolean isChildOf(String parent, String childName) {
        Node nodeParent = extractFeature(parent);
        return (extractFeature(nodeParent, childName) != null);
    }

    public String getParent(String feature) {
        //TODO IMPROVE !!!
        return featureNameList.stream()
                .filter(f -> isDirectChildOf(f, feature))
                .findFirst()
                .orElse(null);
    }
}
