package com.ml2wf.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class contains useful static methods for XML treatment.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public class XMLTool {

	// TODO: factorize other class using this one

	/**
	 * {@code XMLTool}'s default constructor.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this constructor is <code>private</code>.
	 */
	private XMLTool() {

	}

	/**
	 * Preprocess the given XML file before any treatment.
	 *
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 *
	 * @since 1.0
	 */
	public static Document preprocess(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(file);
		document.getDocumentElement().normalize();
		return document;
	}

	/**
	 * Returns the {@code Document} according to the specified {@code url}.
	 *
	 * @param url url of the xml file
	 * @return the {@code Document} according to the specified {@code url}.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 *
	 * @see Document
	 * @see URL
	 */
	public static Document getDocumentFromURL(URL url) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(url.openStream());
		document.getDocumentElement().normalize();
		return document;
	}

	/**
	 * Returns a {@code List} version of the {@code NodeList} parameter.
	 *
	 * @param nodes {@code NodeList} source
	 * @return a {@code List} version of the {@code NodeList} parameter
	 *
	 * @since 1.0
	 *
	 * @see List
	 * @see NodeList
	 */
	public static List<Node> nodeListAsList(NodeList nodes) {
		List<Node> lNodes = new ArrayList<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			lNodes.add(nodes.item(i));
		}
		return lNodes;
	}
}
