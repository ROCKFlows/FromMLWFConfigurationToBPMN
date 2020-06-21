package com.ml2wf.cmd;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ml2wf.merge.base.BaseMerger;
import com.ml2wf.merge.concretes.WFInstanceMerger;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.util.XMLManager;

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
@Command(name = "save", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "saves a worklow as a task in a FeatureModel")
public class Save extends AbstractCommand {

	@Option(names = { "-i",
			"--input" }, required = true, arity = "2", order = 1, description = "input file (meta workflow and instance workflow)")
	File[] input;

	@Option(names = { "-o", "--output" }, required = true, arity = "1", order = 1, description = "output file")
	File output;

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
		BaseMerger merger;
		try {
			// meta wf
			merger = new WFMetaMerger(this.output);
			merger.mergeWithWF(this.backUp, true, this.input[0]);
			((XMLManager) merger).save();
			// instance wf
			merger = new WFInstanceMerger(this.output);
			merger.mergeWithWF(this.backUp, true, this.input[1]);
			((XMLManager) merger).save();
			LogManager.shutdown();
		} catch (Exception e) {
			logger.fatal("Can't merge the Workflow with the FeatureModel.");
			logger.fatal(e.getMessage());
		}
	}
}
