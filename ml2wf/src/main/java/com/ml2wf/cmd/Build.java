package com.ml2wf.cmd;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Class managing the <b>save</b> command.
 *
 * <p>
 *
 * It calls required methods for saving the current workflow instance as a
 * global task
 * into the FeatureModel.
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
 * @see Command
 * @see Logger
 *
 */
@Command(name = "build", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "build a knowledge tree (FeatureModel) from a set of meta-workflows and instance workflows")
public class Build extends AbstractCommand {

	@Option(names = { "-f",
			"--feature" }, required = true, arity = "2", order = 1, description = "FeatureModel directory")
	Path featureModel;

	@Option(names = { "-m",
			"--meta" }, required = true, arity = "2", order = 1, description = "meta-workflows directory")
	Path metaDirectory;

	@Option(names = { "-i",
			"--instance" }, required = true, arity = "2", order = 1, description = "instance-workflows directory")
	Path instanceDirectory;

	@Option(names = { "-b",
			"--backup" }, arity = "0", order = 1, description = "backup the original FeatureModel file before any modification")
	boolean backUp;

	/**
	 * {@code WFMerger}'s instance.
	 */
	// private WFMerger merger;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(Build.class);

	private void processMeta() {

	}

	private void processInstances() {

	}

	@Override
	public void run() {
		// TODO: process Merge verifications
		Configurator.setLevel(getPackageName(), getVerbLevel(this.verbose));
		try {
			// TODO-
			LogManager.shutdown();
		} catch (Exception e) {
			logger.fatal("Can't merge the Workflow with the FeatureModel.");
			logger.fatal(e.getMessage());
		}
	}
}
