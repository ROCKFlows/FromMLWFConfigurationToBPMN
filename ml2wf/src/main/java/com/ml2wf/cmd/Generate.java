package com.ml2wf.cmd;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@Command(name = "generate", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "generate a workflow")
public class Generate extends AbstractCommand {

	@Option(names = { "-i", "--input" }, required = true, arity = "1", order = 1, description = "input file")
	File input;

	@Option(names = { "-o", "--output" }, required = true, arity = "1", order = 1, description = "output directory")
	File output;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(Generate.class);

	// TODO: to comment if kept
	private static final String CANT_INSTANTIATE = "Can't instantiate the WorkFlow.";

	@Override
	public void run() {

		InstanceFactoryImpl factory;
		try {
			// TODO: improve file verification and logs position
			if (!this.input.isFile()) {
				logger.fatal(CANT_INSTANTIATE);
				logger.fatal("The input is not a file.");
				return;
			} else if (!this.output.isFile()) {
				logger.fatal(CANT_INSTANTIATE);
				logger.fatal("The output is not a file.");
				return;
			}
			factory = new InstanceFactoryImpl(this.input);
			factory.getWFInstance(this.output);
			LogManager.shutdown();
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			logger.fatal(CANT_INSTANTIATE);
			logger.fatal(e.getMessage());
		}
	}

}
