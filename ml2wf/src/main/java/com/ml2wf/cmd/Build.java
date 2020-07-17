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
 * Class managing the <b>build</b> command.
 *
 * <p>
 *
 * It calls required methods for building the given FeatureModel from a set of
 * meta-workflows and instance workflows.
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
			"--feature" }, required = true, arity = "1", order = 1, description = "FeatureModel file")
	File featureModel;

	@Option(names = { "-m",
			"--meta" }, required = true, arity = "1", order = 1, description = "meta-workflows directory")
	File metaDirectory;

	@Option(names = { "-i",
			"--instance" }, required = true, arity = "1", order = 1, description = "instance-workflows directory")
	File instanceDirectory;

	@Option(names = { "-b",
			"--backup" }, arity = "0", order = 1, description = "backup the original FeatureModel file before any modification")
	boolean backUp;

	/**
	 * {@code BaseMerger}'s instance.
	 */
	private BaseMerger merger;

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(Build.class);

	/**
	 * Calls the {@code BaseMerger}'s implementation for the meta-workflows.
	 *
	 * <p>
	 *
	 * <b>Note</b> that it will call it with <b>completeMerge=true</b>.
	 *
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	private void processMeta() throws Exception {
		this.merger = new WFMetaMerger(this.featureModel);
		this.merger.mergeWithWF(this.backUp, true, this.metaDirectory);
		this.backUp = false; // allows only one backup
	}

	/**
	 * Calls the {@code BaseMerger}'s implementation for the instance-workflows.
	 *
	 * <p>
	 *
	 * <b>Note</b> that it will call it with <b>completeMerge=true</b>.
	 *
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	private void processInstances() throws Exception {
		this.merger = new WFInstanceMerger(this.featureModel);
		this.merger.mergeWithWF(this.backUp, true, this.instanceDirectory);
	}

	@Override
	public void run() {
		logger.info("Processing using the FeatureModel : {}...", this.featureModel);
		try {
			logger.info("Processing meta-workflows in directory : {}...", this.metaDirectory);
			this.processMeta();
			((XMLManager) this.merger).save();
			logger.info("Processing instance-workflows in directory : {}...", this.instanceDirectory);
			this.processInstances();
			((XMLManager) this.merger).save();
			LogManager.shutdown();
		} catch (Exception e) {
			logger.fatal(CANT_MERGE);
			logException(logger, e);
		}
	}
}
