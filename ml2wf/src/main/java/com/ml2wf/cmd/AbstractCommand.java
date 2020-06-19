package com.ml2wf.cmd;

import java.util.Arrays;
import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import com.ml2wf.App;

import picocli.CommandLine.Model;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

/**
 * This class is an abstraction of a ml2wf command.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public abstract class AbstractCommand implements Runnable {

	@Spec
	Model.CommandSpec spec;

	@Option(names = { "-v",
			"--verbose" }, arity = "1", order = 1, defaultValue = "0", description = "verbose mode (0=OFF,1=FATAL,2=ERROR,3=WARN,4=INFO,5=DEBUG,6=TRACE,7=ALL")
	public void processVerboseArg(int verboseLevel) {
		Configurator.setLevel(getPackageName(), getVerbLevel(verboseLevel));
	}

	/**
	 * Package name to change the logger level.
	 */
	private static final String PACKAGE_NAME = App.class.getPackageName();
	/**
	 * Default verbosity {@code Level}.
	 */
	private static final Level DEFAULT_VERB_LEVEL = Level.INFO;

	/**
	 * Returns the {@code PACKAGE_NAME}.
	 *
	 * @return the {@code PACKAGE_NAME}
	 */
	protected static String getPackageName() {
		return PACKAGE_NAME;
	}

	/**
	 * Returns the verbosity {@code Level} according to the given {@code level}
	 * integer.
	 *
	 * @return the verbosity {@code Level} according to the given {@code level}
	 *         integer
	 *
	 * @since 1.0
	 * @see Level
	 */
	protected static Level getVerbLevel(int level) {
		try {
			Optional<Level> verboseLevel = Arrays.asList(Level.values()).stream()
					.filter(l -> l.intLevel() == (level * 100))
					.findAny();
			if (verboseLevel.isPresent()) {
				return verboseLevel.get();
			}
		} catch (NumberFormatException nfe) {
			return DEFAULT_VERB_LEVEL;
		}
		return DEFAULT_VERB_LEVEL;
	}

}
