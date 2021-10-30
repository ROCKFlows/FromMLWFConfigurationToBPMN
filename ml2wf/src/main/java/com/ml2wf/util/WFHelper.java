package com.ml2wf.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WFHelper {

    private static final String TASK = "bpmn2:task";
    private static final String USER_TASK = "bpmn2:userTask";

    private final Element racine;

    private final List<String> taskNameList;

    // TODO MANAGE CONSTRAINTS
    //  private List<String> constraintList = new ArrayList<>();

    public WFHelper(String path) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(path));
        racine = document.getDocumentElement();
        taskNameList = listTasks();
        //TODO constraintList = listConstraints();
    }

    /**
     * @return List of task names without any treatment
     */
    public List<String> getTaskNamesList() {
        return new ArrayList<>(taskNameList);
    }

    private static String getTaskName(Node node) {
        if ( (node != null) && (node.getAttributes() != null) && (node.getAttributes().getNamedItem("name") != null)) {
            return node.getAttributes().getNamedItem("name").getNodeValue();
        }
        return "";
    }

    private List<String> listTasks(Node n) {
        List<String> list = new ArrayList<>();
        switch (n.getNodeName()) {
            case TASK :
            case USER_TASK :
                list.add(getTaskName(n));
                break;
            default:
                extractChildTasks(n, list);
                break;
        }
        return list;
    }

    /*
     * List Features
     */
    private List<String> listTasks() {
        List<String> taskList = new ArrayList<>();
        for (Node child : iterable(racine.getChildNodes())) {
            taskList.addAll(listTasks(child));
        }
        return taskList;
    }

    private void extractChildTasks(Node n, List<String> list) {
        for (Node child : iterable(n.getChildNodes())) {
            list.addAll(listTasks(child));
        }
    }

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
}
