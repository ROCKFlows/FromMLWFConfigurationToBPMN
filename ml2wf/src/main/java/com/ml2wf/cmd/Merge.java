package com.ml2wf.cmd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.merge.tasks.WFTasksMerger;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Class managing the <b>merge</b> command.
 *
 * <p>
 *
 * It calls required methods for the importation of a instantiated workflow's
 * tasks to a
 * FeatureModel.
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
 * @see AbstractMerger
 * @see Command
 * @see Logger
 *
 */
@Command(name = "-m", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "import a worklow in a FeatureModel")
public class Merge extends AbstractCommand {

	@Option(names = { "-i", "--input" }, required = true, arity = "1", order = 1, description = "input file")
	String input;

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
	private static final Logger logger = LogManager.getLogger(Merge.class);

	@Override
	public void run() {
		Configurator.setLevel(getPackageName(), getVerbLevel(this.verbose));
		WFTasksMerger merger;
		try {
			merger = new WFTasksMerger(this.output);
			merger.mergeWithWF(this.backUp, this.input);
			LogManager.shutdown();
		} catch (Exception e) {
			logger.fatal("Can't merge the Workflow with the FeatureModel.");
			logger.fatal(e.getMessage());
		}
	}
}
