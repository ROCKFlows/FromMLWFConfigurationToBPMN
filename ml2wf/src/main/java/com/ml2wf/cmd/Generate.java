package com.ml2wf.cmd;

import java.io.File;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
 * @since 1.0.0
 *
 * @see AbstractCommand
 * @see InstanceFactory
 * @see Command
 * @see Logger
 */
@Command(name = "generate",
        version = "1.0.0",
        sortOptions = false,
        usageHelpWidth = 60,
        description = "generate a workflow")
@NoArgsConstructor
@Log4j2
public class Generate extends AbstractCommand {

    @Option(names = { "-i", "--input" },
            required = true,
            arity = "1",
            order = 1,
            description = "input file")
    File input;

    @Option(names = { "-o", "--output" },
            required = true,
            arity = "1",
            order = 1,
            description = "output file or directory")
    File output;

    @Override
    protected String getDefaultMessage() {
        return CANT_INSTANTIATE;
    }

    @Override
    public void run() {

        InstanceFactory factory;
        try {
            factory = new InstanceFactoryImpl(input);
            factory.getWFInstance(output);
            ((XMLManager) factory).save(output);
            LogManager.shutdown();
        } catch (Exception e) {
            logException(log, e);
        }
    }
}
