package com.ml2wf.cmd.merge;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.ml2wf.cmd.AbstractCommand;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.merge.base.BaseMerger;
import com.ml2wf.util.XMLManager;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

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
@Command(name = "-m", version = "1.0", subcommands = { MergeMeta.class,
		MergeInstance.class }, sortOptions = false, usageHelpWidth = 60, description = "import a worklow in a FeatureModel")
public abstract class Merge extends AbstractCommand {

	@Option(names = { "-i",
			"--input" }, required = true, arity = "1", order = 1, description = "input file", scope = ScopeType.INHERIT)
	File input;

	@Option(names = { "-o",
			"--output" }, required = true, arity = "1", order = 1, description = "output file", scope = ScopeType.INHERIT)
	File output;

	@Option(names = { "-f",
			"--full" }, arity = "0", order = 1, description = "Full merge (including meta/instance association", scope = ScopeType.INHERIT)
	boolean fullMerge;

	@Option(names = { "-b",
			"--backup" }, arity = "0", order = 1, description = "backup the original FeatureModel file before any modification", scope = ScopeType.INHERIT)
	boolean backUp;

	private BaseMerger merger;

	public void setMerger(BaseMerger merger) {
		this.merger = merger;
	}

	protected void processMerge() throws Exception {
		this.merger.mergeWithWF(this.backUp, this.fullMerge, this.input);
		((XMLManager) this.merger).save();
	}

}
