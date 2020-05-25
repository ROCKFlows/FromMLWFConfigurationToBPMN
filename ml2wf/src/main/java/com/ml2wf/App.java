package com.ml2wf;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.ml2wf.generation.InstanceFactoryImpl;

public class App {

	protected static final String PATH = "../BPMN - Models/";
	protected static final String FILE_NAME = "MetaModel_AD.bpmn2";

	public static void main(String[] args)
			throws SAXException, IOException, ParserConfigurationException, TransformerException {
		InstanceFactoryImpl factory = new InstanceFactoryImpl(PATH, FILE_NAME);
		factory.getWFInstance();
	}
}
