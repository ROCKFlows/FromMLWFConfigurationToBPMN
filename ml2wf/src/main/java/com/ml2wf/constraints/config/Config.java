package com.ml2wf.constraints.config;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ml2wf.conventions.enums.fm.FeatureModelNames;

/**
 * TODO
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 */
public class Config {

	/**
	 * Configuration file path.
	 */
	private static String FILE_PATH = "configuration.cfg";
	/**
	 * {@code Map} mapping operators symbol with {@code FeatureModelNames} elements.
	 *
	 * @see FeatureModelNames
	 */
	private static final Map<String, FeatureModelNames> vocMapping;
	static {
		Map<String, FeatureModelNames> aMap = new HashMap<>();
		aMap.put(">>", null); // before : TODO
		aMap.put("<<", null); // after : TODO
		aMap.put("=>", FeatureModelNames.IMPLIES); // implies
		aMap.put("!", FeatureModelNames.NOT); // not
		aMap.put("<=>", FeatureModelNames.EQUIVALENT); // equivalent
		aMap.put("&", FeatureModelNames.CONJ); // and
		aMap.put("|", FeatureModelNames.DISJ); // or
		vocMapping = Collections.unmodifiableMap(aMap);
	}

	/**
	 * {@code Config}'s default constructor.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this constructor is private to respect the Singleton DP
	 * implementation.
	 */
	private Config() {
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
	 * @see Config
	 * @see <a href=
	 *      "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">Initialization-on-demand
	 *      holder idiom</a>
	 *
	 */
	private static class ConfigHolder {

		/**
		 * {@code Config}'s unique instance.
		 *
		 * @see Config
		 */
		private static final Config INSTANCE = new Config();
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
	public static Config getInstance() {
		return ConfigHolder.INSTANCE;
	}

	/**
	 * Returns the configuration file path.
	 *
	 * @return the configuration file path
	 */
	public static String getFilePath() {
		return FILE_PATH;
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
	 *
	 * @since 1.0
	 */
	public void setFilePath(String filePath) {
		FILE_PATH = filePath;
		this.readConfig();
	}

	/**
	 * Reads the configuration file at the {@code filePath} location and initializes
	 * the {@code vocMapping Map}.
	 *
	 * @since 1.0
	 */
	public void readConfig() {
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL url = classLoader.getResource(FILE_PATH);
		try {
			Files.lines(Paths.get(url.getPath())).forEach((l) -> {
				String[] content = l.split(":");
				// TODO: create with factory ?
			});
		} catch (IOException e) {
			// TODO : log error
			// TODO: switch to default values ?
		}
	}

	public static Map<String, FeatureModelNames> getVocmapping() {
		return vocMapping;
	}

	public List<String> getOperatorsList() {
		return vocMapping.keySet().stream().collect(Collectors.toList());
	}

	public List<String> getBinaryOperators() {
		List<String> binary = this.getOperatorsList();
		binary.removeAll(this.getUnaryOperators());
		return binary;
	}

	public List<String> getUnaryOperators() {
		return new ArrayList<>(Arrays.asList("!")); // TODO: replace with config file
	}

	public boolean isUnaryOperator(String operator) {
		return this.getUnaryOperators().contains(operator); // TODO: change considering the config file
	}

	public boolean isAnOperator(String character) {
		return this.getOperatorsList().contains(character);
	}
}
