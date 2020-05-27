package com.ml2wf;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.ml2wf.generation.InstanceFactoryImpl;
import com.ml2wf.merge.FeatureModelMerger;

public class App {

	// WF attributes
	protected static final String WF_PATH = "../BPMN - Models/";
	protected static final String WF_FILE_NAME = "MetaModel_AD.bpmn2";
	protected static final String WF_RESULT_PATH = "../BPMN - Models/";
	protected static final String WF_RESULT_FILE_NAME = "MetaModel_AD_instance.bpmn2";
	// FM attributes
	protected static final String FM_PATH = "../";
	protected static final String FM_FILE_NAME = "model.xml";

	protected static void instantiateWF()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		InstanceFactoryImpl factory = new InstanceFactoryImpl(WF_PATH, WF_FILE_NAME);
		factory.getWFInstance();
	}

	protected static void addToFM()
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		FeatureModelMerger merger = new FeatureModelMerger(FM_PATH, FM_FILE_NAME);
		merger.mergeWithWF(WF_RESULT_PATH, WF_RESULT_FILE_NAME);
	}

	public static void main(String[] args)
			throws SAXException, IOException, ParserConfigurationException, TransformerException {
		instantiateWF();
		addToFM();
	}
}
