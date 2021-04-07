package com.ml2wf.constraints.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ml2wf.conventions.enums.fm.FMNames;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;


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
 * @since 1.0.0
 */
@Log4j2
public final class ConfigImpl implements Config {

	/**
	 * Default configuration file path.
	 */
	private static final String DEFAULT_FILE_PATH = "./config/configuration.cfg";
	private static final String APPLYING_DEFAULT_MSG = "Applying default configuration...";
	/**
	 * Configuration file path.
	 */
	@Getter private String filePath;
	/**
	 * {@code Map} mapping operators symbol with {@code FeatureModelNames} elements.
	 *
	 * @see FMNames
	 */
	@Getter private Map<String, String> vocMapping;
	/**
	 * {@code List} of unary operators.
	 */
	@Getter private List<String> unaryOperators;
	/**
	 * {@code List} of binary operators.
	 */
	@Getter private List<String> binaryOperators;

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
		filePath = DEFAULT_FILE_PATH;
		readConfig();
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
	 * @see ConfigImpl
	 * @see <a href=
	 *      "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">Initialization-on-demand
	 *      holder idiom</a>
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private static final class ConfigHolder {

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
		readConfig();
	}

	@Override
	public void readConfig() {
		// initializes collections
		vocMapping = new HashMap<>();
		unaryOperators = new ArrayList<>();
		binaryOperators = new ArrayList<>();
		// get config file data
		// check if filePath is valid
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			log.error("Can't read config. The given path {} does not exist.", path);
			log.warn(APPLYING_DEFAULT_MSG);
			applyDefault();
			return;
		}
		try (Stream<String> lines = Files.lines(path)) {
			// foreach line
			lines.forEach(l -> {
				l = l.replace(" ", "");
				if (!l.startsWith("#") && !l.isBlank() && processEntry(l.split(":"))) {
					// if it is not a comment or an empty line
					// and config is complete
					// TODO: to fix
				}
			});
		} catch (IOException e) {
			log.error("Failed to read configuration file [{}].", e.getMessage());
			log.warn(APPLYING_DEFAULT_MSG);
			applyDefault();
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
			checkEntry(entry);
		} catch (InvalidConfigEntryException e) {
			log.error("Invalid configuration [{}].", e.getMessage());
			log.warn(APPLYING_DEFAULT_MSG);
			applyDefault();
			return true;
		} catch (IncompleteConfigEntryException e) {
			log.error("Incomplete configuration [{}].", e.getMessage());
			log.warn("Applying default configuration for missing entries...");
			getDefault(entry);
			return DefaultConfig.isComplete(vocMapping);
		}
		int arity = Integer.parseInt(entry[1]);
		vocMapping.put(entry[2], entry[0]);
		if (arity == 1) {
			unaryOperators.add(entry[2]);
		} else if (arity == 2) {
			binaryOperators.add(entry[2]);
		} else {
			log.error("Invalid operator's arity [{}] for [{}].", arity, entry[1]);
			log.warn("Replacing with default configuration...");
			getDefault(entry);
		}
		return DefaultConfig.isComplete(vocMapping);
	}

	/**
	 * Checks if the given {@code entry} is valid or not.
	 *
	 * <p>
	 *
	 * It not, throws the appropriate {@code Exception}.
	 *
	 * @param entry entry to check
	 *
	 * @throws InvalidConfigEntryException
	 * @throws IncompleteConfigEntryException
	 */
	private static void checkEntry(String[] entry) throws InvalidConfigEntryException, IncompleteConfigEntryException {
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
		log.debug("Getting default configuration for bad entry [{}]...", Arrays.toString(badEntry));
		Optional<DefaultConfig> optDefCfg = DefaultConfig.getDefaultFor(badEntry[0]);
		if (optDefCfg.isPresent()) {
			DefaultConfig defaultCfg = optDefCfg.get();
			processEntry(defaultCfg.getEntry());
		} else {
			log.fatal("Unknown configuration entry for [{}].", badEntry[0]);
			throw new IllegalStateException("Unknown configuration entry.");
		}
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
			processEntry(defaultEntry);
		}
	}

	/**
	 * Returns the {@code List} of operators.
	 *
	 * @return the {@code List} of operators
	 */
	public List<String> getOperatorsList() {
		return new ArrayList<>(vocMapping.keySet());
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
		return unaryOperators.contains(operator);
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
		return binaryOperators.contains(operator);
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
		return getOperatorsList().contains(character);
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
		return vocMapping.keySet().stream().limit(2).collect(Collectors.toList());
	}
}
