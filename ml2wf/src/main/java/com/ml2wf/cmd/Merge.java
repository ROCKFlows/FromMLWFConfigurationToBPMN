package com.ml2wf.cmd;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.merge.base.BaseMerger;
import com.ml2wf.merge.concretes.WFInstanceMerger;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.util.XMLManager;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Class managing the <b>merge</b> command.
 *
 * <p>
 *
 * It calls required methods for the importation of a instantiated workflow's
 * tasks to a FeatureModel.
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

	@ArgGroup(exclusive = true, multiplicity = "1")
	Exclusive exclusive;

	/**
	 * Exclusive {@code class} containing the <b>meta/instance</b> options.
	 *
	 * <p>
	 *
	 * An exclusive {@code class} contains required options that can't be both
	 * given.
	 *
	 * <p>
	 *
	 * More informations :
	 * <a href="https://picocli.info/#_mutually_exclusive_options">Mutually
	 * exclusive options</a>
	 *
	 * @author Nicolas Lacroix
	 *
	 * @see ArgGroup
	 *
	 */
	static class Exclusive {

		@Option(names = "--meta")
		boolean meta;
		@Option(names = "--instance")
		boolean instance;
	}

	@Option(names = { "-i",
			"--input" }, required = true, arity = "1", order = 1, description = "input file")
	File input;

	@Option(names = { "-o",
			"--output" }, required = true, arity = "1", order = 2, description = "output file")
	File output;

	@Option(names = { "-f",
			"--full" }, arity = "0", order = 3, description = "Full merge (including meta/instance association")
	boolean fullMerge;

	@Option(names = { "-b",
			"--backup" }, arity = "0", order = 4, description = "backup the original FeatureModel file before any modification")
	boolean backUp;

	private BaseMerger merger;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(Merge.class);

	protected void processMerge() throws Exception {
		this.merger.mergeWithWF(this.backUp, this.fullMerge, this.input);
		((XMLManager) this.merger).save();
	}

	@Override
	public void run() {
		try {
			this.merger = (this.exclusive.meta) ? new WFMetaMerger(this.output) : new WFInstanceMerger(this.output);
			this.processMerge();
		} catch (Exception e) {
			logger.fatal("Can't merge the Workflow with the FeatureModel.");
			logger.fatal(e.getMessage());
		}

	}

}
