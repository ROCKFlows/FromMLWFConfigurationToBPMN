package com.ml2wf.constraints.config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ml2wf.conventions.enums.fm.FeatureModelNames;

// TODO: reorganize methods order

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
	private static String DEFAULT_FILE_PATH = "configuration.cfg";
	/**
	 * Configuration file path.
	 */
	private String filePath;
	/**
	 * {@code Map} mapping operators symbol with {@code FeatureModelNames} elements.
	 *
	 * @see FeatureModelNames
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
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL url = classLoader.getResource(this.filePath); // TODO: check getResourceAsStream performances
		URI uri;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			// TODO: log error while accessing file
			// exit
			uri = null; // TODO: to remove
		}
		try (Stream<String> lines = Files.lines(Paths.get(uri))) {
			// foreach line
			System.out.println(lines);
			lines.forEach(l -> {
				System.out.println(l);
				if (!l.startsWith("#") || l.isBlank()) {
					// if it is not a comment or an empty line
					this.processEntry(l.split(":"));
					// TODO: stop if all operators are loaded
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
	 * Some examples of {@code entry} :
	 *
	 * <pre>
	 * <code>and : 2 : &</code>
	 * <code>not : 1 : !</code>
	 * </pre>
	 *
	 * @param entry array with <b>name : arity : symbol</b>
	 *
	 * @since 1.0
	 */
	private void processEntry(String[] entry) {
		// TODO: improve verifications (create a dedicated method ?)
		if (!DefaultConfig.isValidName(entry[0])) {
			// TODO: log erro
			this.applyDefault();
			// TODO: improve (full replacement for every bad entry...)
			return;
		}
		if ((entry.length < 3)) {
			// TODO: log error
			this.getDefault(entry);
			return;
		}
		int arity;
		try {
			arity = Integer.parseInt(entry[1]);
		} catch (NumberFormatException nfe) {
			// TODO: log error
			this.getDefault(entry);
			return;
		}
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
}
