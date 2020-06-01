package com.ml2wf.constraints.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This {@code enum} contains the default configuration of the application.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public enum DefaultConfig {

	BEFORE("before", 2, ">>"),
	AFTER("after", 2, "<<"),
	IMP("imp", 2, "=>"),
	EQU("equ", 2, "<=>"),
	CONJ("conj", 2, "&"),
	DISJ("disj", 2, "|"),
	NOT("not", 1, "!");

	/**
	 * name of the {@code DefaultConfig} entry.
	 *
	 * <p>
	 *
	 * For instance,
	 *
	 * <pre>
	 * conj, disj, equ
	 * </pre>
	 */
	private String name;
	/**
	 * arity of the {@code DefaultConfig} entry.
	 *
	 * <p>
	 *
	 * For instance,
	 *
	 * <pre>
	 * 1, 2
	 * </pre>
	 */
	private int arity;
	/**
	 * symbol of the {@code DefaultConfig} entry.
	 *
	 * <p>
	 *
	 * For instance,
	 *
	 * <pre>
	 * =>, &, |
	 * </pre>
	 */
	private String symbol;

	/**
	 * {@code DefaultConfig} constructor.
	 *
	 * @param name   name of the {@code DefaultConfig} entry
	 * @param arity  entry of the {@code DefaultConfig} entry
	 * @param symbol symbol of the {@code DefaultConfig} entry
	 */
	private DefaultConfig(String name, int arity, String symbol) {
		this.name = name;
		this.arity = arity;
		this.symbol = symbol;
	}

	/**
	 * returns the {@code DefaultConfig}'s name.
	 *
	 * @return the {@code DefaultConfig}'s name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * returns the {@code DefaultConfig}'s arity.
	 *
	 * @return the {@code DefaultConfig}'s arity
	 */
	public int getArity() {
		return this.arity;
	}

	/**
	 * Returns the {@code DefaultConfig}'s symbol.
	 *
	 * @return the {@code DefaultConfig}'s symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * Returns the default {@code DefaultConfig} with the given {@code name}.
	 *
	 * <p>
	 *
	 * <b>Note</b> that the return type if {@code Optional<DefaultConfig>}.
	 *
	 * @param name name of wished {@code DefaultConfig}
	 * @return the default {@code DefaultConfig} with the given {@code name}
	 *
	 * @since 1.0
	 * @see Optional
	 */
	public static Optional<DefaultConfig> getDefaultFor(String name) {
		return Stream.of(DefaultConfig.values()).filter(v -> v.getName().equals(name)).findFirst();
	}

	/**
	 * Returns whether the given {@code name} is a valid configuration name or not.
	 *
	 * @param name name to test
	 * @return whether the given {@code name} is a valid configuration name or not
	 *
	 * @since 1.0
	 */
	public static boolean isValidName(String name) {
		return Stream.of(DefaultConfig.values()).anyMatch(v -> v.getName().equals(name));
	}

	/**
	 * Returns a {@code List} of all default entries.
	 *
	 * @return a {@code List} of all default entries
	 */
	public static List<String[]> getDefaultEntries() {
		return Stream.of(DefaultConfig.values())
				.map(DefaultConfig::getEntry)
				.collect(Collectors.toList());
	}

	/**
	 * Returns the corresponding entry.
	 *
	 * <p>
	 *
	 * <b>Note</b> that an entry is an array of {@code String}.
	 *
	 * @return the corresponding entry
	 */
	public String[] getEntry() {
		return new String[] { this.getName(), String.valueOf(this.getArity()), this.getSymbol() };
	}

	/**
	 * Returns whether the config is complete or not.
	 *
	 * @param cfg config to test
	 * @return whether the config is complete or not
	 */
	public static boolean isComplete(Map<String, String> cfg) {
		return DefaultConfig.values().length == cfg.size();
	}
}
