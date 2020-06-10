package com.ml2wf.cmd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import com.ml2wf.merge.WFMerger;
import com.ml2wf.merge.complete.WFInstanceMerger;
import com.ml2wf.merge.complete.WFMetaMerger;

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
 * @see WFMerger
 * @see Command
 * @see Logger
 *
 */
@Command(name = "-s", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "saves a worklow as a task in a FeatureModel")
public class Save extends AbstractCommand {

	@Option(names = { "-i",
			"--input" }, required = true, arity = "2", order = 1, description = "input files (meta workflow and instance workflow)")
	String[] input;

	@Option(names = { "-o", "--output" }, required = true, arity = "1", order = 1, description = "output file")
	String output;

	@Option(names = { "-b",
			"--backup" }, arity = "0", order = 1, description = "backup the original FeatureModel file before any modification")
	boolean backUp;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(Save.class);

	@Override
	public void run() {
		// TODO: process Merge verifications
		Configurator.setLevel(getPackageName(), getVerbLevel(this.verbose));
		WFMerger merger;
		try {
			merger = new WFMetaMerger(this.output);
			((WFMetaMerger) merger).mergeWithWF(this.backUp, this.input[0]);
			merger = new WFInstanceMerger(this.output);
			((WFInstanceMerger) merger).mergeWithWF(this.backUp, this.input[1]);
			LogManager.shutdown();
		} catch (Exception e) {
			logger.fatal("Can't merge the Workflow with the FeatureModel.");
			logger.fatal(e.getMessage());
			// CommandLine.usage(this.spec, );
		}
	}
}
