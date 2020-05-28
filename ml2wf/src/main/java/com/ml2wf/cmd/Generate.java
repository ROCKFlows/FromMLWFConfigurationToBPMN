package com.ml2wf.cmd;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.ml2wf.generation.InstanceFactoryImpl;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "-g", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "generate a workflow")
public class Generate implements Runnable {

	@Option(names = { "-i", "--input" }, arity = "1", order = 1, description = "input file")
	String input;

	@Option(names = { "-o", "--output" }, arity = "1", order = 1, description = "output directory")
	String output;

	@Option(names = { "-v", "--verbose" }, arity = "0", order = 1, description = "verbose mode")
	boolean verbose;

	@Override
	public void run() {
		InstanceFactoryImpl factory;
		try {
			factory = new InstanceFactoryImpl(this.input);
			factory.getWFInstance(this.output);
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			// TODO: log the crash
		}

	}

}
