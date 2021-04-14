package com.ml2wf.cmd;

import java.io.File;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
 * @since 1.0.0
 *
 * @see AbstractCommand
 * @see AbstractMerger
 * @see Command
 * @see Logger
 */
@Command(name = "merge",
        version = "1.0.0",
        sortOptions = false,
        usageHelpWidth = 60,
        description = "import a worklow in a FeatureModel")
@NoArgsConstructor
@Log4j2
public class Merge extends AbstractCommand {

    @ArgGroup(multiplicity = "1")
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
     */
    static class Exclusive {

        @Option(names = "--meta")
        boolean meta;
        @Option(names = "--instance")
        boolean instance;
    }

    @Option(names = { "-i", "--input" },
            required = true,
            arity = "1",
            order = 1,
            description = "input file")
    File input;

    @Option(names = { "-o", "--output" },
            required = true,
            arity = "1",
            order = 2,
            description = "output file")
    File output;

    @Option(names = { "-f", "--full" },
            arity = "0",
            order = 3,
            description = "Full merge (including meta/instance association")
    boolean fullMerge;

    @Option(names = { "-b", "--backup" },
            arity = "0",
            order = 4,
            description = "backup the original FeatureModel file before any modification")
    boolean backUp;

    /**
     * {@code BaseMerger}'s instance.
     */
    private BaseMerger merger;

    protected void processMerge() throws Exception {
        merger.mergeWithWF(backUp, fullMerge, input);
        ((XMLManager) merger).save();
    }

    @Override
    protected String getDefaultMessage() {
        return CANT_MERGE;
    }

    @Override
    public void run() {
        try {
            merger = (exclusive.meta) ? new WFMetaMerger(output) : new WFInstanceMerger(output);
            processMerge();
        } catch (Exception e) {
            logException(log, e);
        }
    }
}
