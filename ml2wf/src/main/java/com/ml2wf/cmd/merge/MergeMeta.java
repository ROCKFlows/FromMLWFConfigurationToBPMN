package com.ml2wf.cmd.merge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ml2wf.cmd.AbstractCommand;
import com.ml2wf.merge.concretes.WFMetaMerger;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

/**
 * This class merges a Meta-Workflow into a FeatureModel.
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
 * @see WFMetaMerger
 * @see Command
 * @see Logger
 */
@Command(name = "--meta", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "Merge a Meta-Workflow into a FeatureModel")
public class MergeMeta extends AbstractCommand {

	@ParentCommand
	Merge parent;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(MergeMeta.class);

	@Override
	public void run() {
		try {
			this.parent.setMerger(new WFMetaMerger(this.parent.output));
			this.parent.processMerge();
			LogManager.shutdown();
		} catch (Exception e) {
			logger.fatal("Can't merge the Workflow with the FeatureModel.");
			logger.fatal(e.getMessage());
		}
	}

}
