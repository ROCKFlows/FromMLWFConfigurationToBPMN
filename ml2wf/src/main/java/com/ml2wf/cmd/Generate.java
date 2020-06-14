package com.ml2wf.cmd;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.xml.sax.SAXException;

import com.ml2wf.generation.InstanceFactory;
import com.ml2wf.generation.InstanceFactoryImpl;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Class managing the <b>generate</b> command.
 *
 * <p>
 *
 * It calls required methods for the instantiation of a generic workflow.
 *
 * <p>
 *
 * It is an extension of the {@link AbstractCommand} base class.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see AbstractCommand
 * @see InstanceFactory
 * @see Command
 * @see Logger
 *
 */
@Command(name = "-g", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "generate a workflow")
public class Generate extends AbstractCommand {

	@Option(names = { "-i", "--input" }, required = true, arity = "1", order = 1, description = "input file")
	String input;

	@Option(names = { "-o", "--output" }, required = true, arity = "1", order = 1, description = "output directory")
	String output;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(Generate.class);

	@Override
	public void run() {
		Configurator.setLevel(getPackageName(), getVerbLevel(this.verbose));
		InstanceFactoryImpl factory;
		try {
			factory = new InstanceFactoryImpl(this.input);
			factory.getWFInstance(this.output);
			LogManager.shutdown();
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			logger.fatal("Can't instantiate the WorkFlow.");
			e.printStackTrace(); // TODO: to remove
		}
	}

}
