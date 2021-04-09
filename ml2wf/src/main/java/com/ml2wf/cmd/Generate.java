package com.ml2wf.cmd;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ml2wf.generation.InstanceFactory;
import com.ml2wf.generation.InstanceFactoryImpl;
import com.ml2wf.util.XMLManager;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Class managing the <b>generate</b> command.
 *
 * <p>
 *
 * It calls required methods for the instantiation of a generic workflow.
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
 * @see InstanceFactory
 * @see Command
 * @see Logger
 *
 */
@Command(name = "generate", version = "1.0", sortOptions = false, usageHelpWidth = 60, description = "generate a workflow")
public class Generate extends AbstractCommand {

    @Option(names = { "-i", "--input" }, required = true, arity = "1", order = 1, description = "input file")
    File input;

    @Option(names = { "-o",
            "--output" }, required = true, arity = "1", order = 1, description = "output file or directory")
    File output;

    /**
     * Logger instance.
     *
     * @since 1.0
     * @see Logger
     */
    private static final Logger logger = LogManager.getLogger(Generate.class);

    @Override
    protected String getDefaultMessage() {
        return CANT_INSTANTIATE;
    }

    @Override
    public void run() {

        InstanceFactory factory;
        try {
            factory = new InstanceFactoryImpl(this.input);
            factory.getWFInstance(this.output);
            ((XMLManager) factory).save(this.output);
            LogManager.shutdown();
        } catch (Exception e) {
            this.logException(logger, e);
        }
    }

}
