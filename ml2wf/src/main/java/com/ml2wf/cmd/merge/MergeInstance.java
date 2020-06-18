package com.ml2wf.cmd.merge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ml2wf.cmd.AbstractCommand;
import com.ml2wf.merge.concretes.WFInstanceMerger;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

/**
 * This class merges an Instance Workflow into a FeatureModel.
 *
 * <p>
 *
 * It is a subcommand of the {@link Merge} class.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see Merge
 * @see WFInstanceMerger
 * @see Command
 * @see Logger
 */
@Command(name = "--instance", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "Merge an Instance Workflow into a FeatureModel")
public class MergeInstance extends AbstractCommand {

	@ParentCommand
	Merge parent;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(MergeInstance.class);

	@Override
	public void run() {
		try {
			this.parent.setMerger(new WFInstanceMerger(this.parent.output));
			this.parent.processMerge();
			LogManager.shutdown();
		} catch (Exception e) {
			logger.fatal("Can't merge the Workflow with the FeatureModel.");
			logger.fatal(e.getMessage());
		}
	}
}
