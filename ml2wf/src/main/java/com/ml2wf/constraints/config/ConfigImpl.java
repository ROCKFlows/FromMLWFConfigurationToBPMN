package com.ml2wf.constraints.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ml2wf.conventions.enums.fm.FMNames;


/**
 * This class contains the current configuration of the application.
 *
 * <p>
 *
 * It contains methods for :
 *
 * <p>
 *
 * <ul>
 * <li>loading configuration from a configuration file,</li>
 * <li>getting tag name associated to an operator,</li>
 * <li>identifying operators arity,</li>
 * <li>retrieving full list of operators</li>
 * </ul>
 *
 * <p>
 *
 * It is an implementation of the {@link Config} interface.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 */
public class ConfigImpl implements Config {

	/**
	 * Default configuration file path.
	 */
	private static String DEFAULT_FILE_PATH = "./config/configuration.cfg";
	/**
	 * Configuration file path.
	 */
	private String filePath;
	/**
	 * {@code Map} mapping operators symbol with {@code FeatureModelNames} elements.
	 *
	 * @see FMNames
	 */
	private Map<String, String> vocMapping;
	/**
	 * {@code List} of unary operators.
	 */
	private List<String> unaryOperators;
	/**
	 * {@code List} of binary operators.
	 */
	private List<String> binaryOperators;

	/**
	 * {@code Config}'s default constructor.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this constructor is private to respect the Singleton DP
	 * implementation.
	 */
	private ConfigImpl() {
		// TODO: add cfg file path parameter ?
		this.filePath = DEFAULT_FILE_PATH;
		this.readConfig();
	}

	/**
	 * This class is a holder for the {@code Config} <b>Singleton</b> instance.
	 *
	 * <p>
	 *
	 * This Singleton version is based on the <b>Initialization-on-demand holder
	 * idiom</b> created by <b>Bill Pugh</b>
	 *
	 * @author Nicolas Lacroix
	 *
	 * @version 1.0
	 *
	 * @see ConfigImpl
	 * @see <a href=
	 *      "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">Initialization-on-demand
	 *      holder idiom</a>
	 *
	 */
	private static class ConfigHolder {

		/**
		 * {@code Config}'s unique instance.
		 *
		 * @see ConfigImpl
		 */
		private static final ConfigImpl INSTANCE = new ConfigImpl();
	}

	/**
	 * Returns the {@code Config} unique instance according to the Singleton design
	 * pattern.
	 *
	 * @return the {@code Config} unique instance according to the Singleton design
	 *         pattern
	 *
	 * @since 1.0
	 * @see ConfigHolder
	 */
	public static ConfigImpl getInstance() {
		return ConfigHolder.INSTANCE;
	}

	/**
	 * Returns the configuration file path.
	 *
	 * @return the configuration file path
	 */
	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * Sets the configuration file path.
	 *
	 * <p>
	 *
	 * <b>Note</b> that changing the configuration file path will automatically call
	 * the {@link #readConfig()} method for updating the configuration map.
	 *
	 * @param filePath the new configuration file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		this.readConfig();
	}

	@Override
	public void readConfig() {
		// initializes collections
		this.vocMapping = new HashMap<>();
		this.unaryOperators = new ArrayList<>();
		this.binaryOperators = new ArrayList<>();
		// get config file data
		// check if filePath is valid
		Path path = Paths.get(this.filePath);
		if (!Files.exists(path)) {
			// TODO: log error
			this.applyDefault();
			return;
		}
		try (Stream<String> lines = Files.lines(path)) {
			// foreach line
			lines.forEach(l -> {
				l = l.replace(" ", "");
				if (!l.startsWith("#") && !l.isBlank() && this.processEntry(l.split(":"))) {
					// if it is not a comment or an empty line
					// and config is complete
					return;
				}
			});
		} catch (IOException e) {
			// TODO : log error
			this.applyDefault();
		}
	}

	/**
	 * Fills {@code #vocMapping} according to {@code entry} content and fills
	 * {@code binaryOperators} or {code unaryOperators} according to the type of
	 * operator.
	 *
	 * <p>
	 *
	 * Returns whether the current config is complete or note.
	 *
	 * <p>
	 *
	 * Some examples of {@code entry} :
	 *
	 * <pre>
	 * <code>and : 2 : &</code>
	 * <code>not : 1 : !</code>
	 * </pre>
	 *
	 * @param entry array with <b>name : arity : symbol</b>
	 * @return whether the current config is complete or note
	 *
	 * @since 1.0
	 */
	private boolean processEntry(String[] entry) {
		// TODO: improve verifications (create a dedicated method ?)
		try {
			this.checkEntry(entry);
		} catch (InvalidConfigEntryException e) {
			// TODO: log error
			this.applyDefault();
			return true;
		} catch (IncompleteConfigEntryException e) {
			// TODO: log error
			this.getDefault(entry);
			return DefaultConfig.isComplete(this.vocMapping);
		}
		int arity = Integer.parseInt(entry[1]);
		this.vocMapping.put(entry[2], entry[0]);
		if (arity == 1) {
			this.unaryOperators.add(entry[2]);
		} else if (arity == 2) {
			this.binaryOperators.add(entry[2]);
		} else {
			// TODO: log error
			// return
			this.getDefault(entry);
		}
		return DefaultConfig.isComplete(this.vocMapping);
	}

	/**
	 * Checks if the given {@code entry} is valid or not.
	 *
	 * <p>
	 *
	 * It not, throws the appropriate {@code Exception}.
	 *
	 * @param entry entry to check
	 * @throws InvalidConfigEntryException
	 * @throws IncompleteConfigEntryException
	 */
	private void checkEntry(String[] entry) throws InvalidConfigEntryException, IncompleteConfigEntryException {
		if (entry.length == 0) {
			// TODO: log error
			throw new InvalidConfigEntryException("Empty entry.");
		}
		if (!DefaultConfig.isValidName(entry[0])) {
			// TODO: log error
			throw new InvalidConfigEntryException(String.format("Invalid entry for : %s.", entry[0]));
		}
		if ((entry.length < 3)) {
			// TODO: log error
			throw new IncompleteConfigEntryException(String.format("Incomplete entry for : %s.", entry[0]));
		}
		try {
			Integer.parseInt(entry[1]);
		} catch (NumberFormatException nfe) {
			// TODO: log error
			throw new IncompleteConfigEntryException(String.format("Incomplete entry (arity) for : %s.", entry[0]));
		}
	}

	/**
	 * Calls the {@link #processEntry(String[])} for the default entry of
	 * {@code badEntry} using the {@code DefaultConfig enum}.
	 *
	 * @param badEntry bad entry
	 *
	 * @since 1.0$
	 * @see DefaultConfig
	 */
	private void getDefault(String[] badEntry) {
		// TODO: log
		Optional<DefaultConfig> optDefCfg = DefaultConfig.getDefaultFor(badEntry[0]);
		if (optDefCfg.isPresent()) {
			DefaultConfig defaultCfg = optDefCfg.get();
			this.processEntry(defaultCfg.getEntry());
		}
		// TODO: log fatal error
		// exit
	}

	/**
	 * Applies the default configuration.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method is called if configuration file is corrupted.
	 *
	 * @since 1.0
	 */
	private void applyDefault() {
		List<String[]> defaultEntries = DefaultConfig.getDefaultEntries();
		for (String[] defaultEntry : defaultEntries) {
			this.processEntry(defaultEntry);
		}
	}

	/**
	 * Returns the vocabulary mapping.
	 *
	 * @return the vocabulary mapping
	 */
	public Map<String, String> getVocmapping() {
		return this.vocMapping;
	}

	/**
	 * Returns the {@code List} of operators.
	 *
	 * @return the {@code List} of operators
	 */
	public List<String> getOperatorsList() {
		return this.vocMapping.keySet().stream().collect(Collectors.toList());
	}

	/**
	 * Returns the {@code List} of unary operators.
	 *
	 * @return the {@code List} of unary operators
	 */
	public List<String> getUnaryOperators() {
		return this.unaryOperators;
	}

	/**
	 * Returns the {@code List} of binary operators.
	 *
	 * @return the {@code List} of binary operators
	 */
	public List<String> getBinaryOperators() {
		return this.binaryOperators;
	}

	/**
	 * Returns whether the given {@code character} is an unary operator or not.
	 *
	 * @param operator operator to test
	 * @return whether the given {@code character} is an unary operator or not
	 *
	 * @since 1.0
	 */
	public boolean isUnaryOperator(String operator) {
		return this.unaryOperators.contains(operator);
	}

	/**
	 * Returns whether the given {@code character} is a binary operator or not.
	 *
	 * @param operator operator to test
	 * @return whether the given {@code character} is a binary operator or not
	 *
	 * @since 1.0
	 */
	public boolean isBinaryOperator(String operator) {
		return this.binaryOperators.contains(operator);
	}

	/**
	 * Returns whether the given {@code character} is an operator or not.
	 *
	 * @param character character to test
	 * @return whether the given {@code character} is an operator or not
	 *
	 * @since 1.0
	 */
	public boolean isAnOperator(String character) {
		return this.getOperatorsList().contains(character);
	}

	/**
	 * Returns all order operators.
	 *
	 * <p>
	 *
	 * <b>Note</b> that order operators are the two first elements.
	 *
	 * @return all order operators
	 */
	public List<String> getOrderOperator() {
		return this.vocMapping.keySet().stream().limit(2).collect(Collectors.toList());
	}
}
