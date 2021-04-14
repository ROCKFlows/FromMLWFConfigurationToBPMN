package com.ml2wf.cmd;

import java.io.File;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
 * @since 1.0.0
 *
 * @see AbstractCommand
 * @see Command
 * @see Logger
 */
@Command(name = "build",
        version = "1.0.0",
        sortOptions = false,
        usageHelpWidth = 60,
        description = "build a knowledge tree (FeatureModel) from a set of meta-workflows and instance workflows")
@NoArgsConstructor
@Log4j2
public class Build extends AbstractCommand {

    @Option(names = { "-f", "--feature" },
            required = true,
            arity = "1",
            order = 1,
            description = "FeatureModel file")
    File featureModel;

    @Option(names = { "-m", "--meta" },
            required = true,
            arity = "1",
            order = 1,
            description = "meta-workflows directory")
    File metaDirectory;

    @Option(names = { "-i", "--instance" },
            required = true,
            arity = "1",
            order = 1,
            description = "instance-workflows directory")
    File instanceDirectory;

    @Option(names = { "-b", "--backup" },
            arity = "0",
            order = 1,
            description = "backup the original FeatureModel file before any modification")
    boolean backUp;

    /**
     * {@code BaseMerger}'s instance.
     */
    private BaseMerger merger;

    /**
     * Calls the {@code BaseMerger}'s implementation for the meta-workflows.
     *
     * <p>
     *
     * <b>Note</b> that it will call it with <b>completeMerge=true</b>.
     *
     * @throws Exception
     */
    private void processMeta() throws Exception {
        merger = new WFMetaMerger(featureModel);
        merger.mergeWithWF(backUp, true, metaDirectory);
        backUp = false; // allows only one backup
    }

    /**
     * Calls the {@code BaseMerger}'s implementation for the instance-workflows.
     *
     * <p>
     *
     * <b>Note</b> that it will call it with <b>completeMerge=true</b>.
     *
     * @throws Exception
     */
    private void processInstances() throws Exception {
        this.merger = new WFInstanceMerger(featureModel);
        this.merger.mergeWithWF(backUp, true, instanceDirectory);
    }

    @Override
    protected String getDefaultMessage() {
        return CANT_MERGE;
    }

    @Override
    public void run() {
        log.info("Processing using the FeatureModel : {}...", this.featureModel);
        try {
            log.info("Processing meta-workflows in directory : {}...", this.metaDirectory);
            processMeta();
            ((XMLManager) merger).save();
            log.info("Processing instance-workflows in directory : {}...", this.instanceDirectory);
            processInstances();
            ((XMLManager) merger).save();
            LogManager.shutdown();
        } catch (Exception e) {
            logException(log, e);
        }
    }
}
