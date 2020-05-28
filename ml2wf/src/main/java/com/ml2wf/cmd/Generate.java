package com.ml2wf.cmd;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import com.ml2wf.generation.InstanceFactoryImpl;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

/**
 * Class managing the <b>generate</b> command.
 *
 * <p>
 *
 * It calls required methods for the instantiation of a generic workflow.
 *
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see InstanceFactoryImpl
 * @see Command
 * @see Logger
 *
 */
@Command(name = "-g", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "generate a workflow")
public class Generate implements Runnable {

	@Spec
	Model.CommandSpec spec;

	@Option(names = { "-i", "--input" }, arity = "1", order = 1, description = "input file")
	String input;

	@Option(names = { "-o", "--output" }, arity = "1", order = 1, description = "output directory")
	String output;

	@Option(names = { "-v", "--verbose" }, arity = "0", order = 1, description = "verbose mode")
	boolean verbose;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(Generate.class);

	@Override
	public void run() {
		InstanceFactoryImpl factory;
		try {
			factory = new InstanceFactoryImpl(this.input);
			factory.getWFInstance(this.output);
			LogManager.shutdown();
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			logger.fatal("Can't instantiate the WorkFlow.");
			// CommandLine.usage(this.spec, );
		}
	}

}
