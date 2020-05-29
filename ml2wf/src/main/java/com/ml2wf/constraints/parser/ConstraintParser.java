package com.ml2wf.constraints.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ml2wf.constraints.config.Config;

/**
 * This class is a constraint parser.
 *
 * <p>
 *
 * More precisely, it provides methods to extract constraints from a given text
 * based on the given {@code Config}'s instance.
 *
 * <p>
 *
 * It is an implementation of the {@link Parser} interface.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see Parser
 * @see Config
 */
public class ConstraintParser implements Parser {

	/**
	 * {@code Config}'s instance used to define delimiters and regex patterns.
	 *
	 * @see Config
	 */
	private Config config;

	/**
	 * {@code ConstraintParser}'s default instance.
	 *
	 * @param cfg the {@code Config} instance
	 *
	 * @see Config
	 */
	public ConstraintParser(Config cfg) {
		this.setConfig(cfg);
	}

	/**
	 * Returns the {@code Config} instance.
	 *
	 * @return the {@code Config} instance
	 *
	 * @see Config
	 */
	public Config getConfig() {
		return this.config;
	}

	/**
	 * Sets the {@code Config} instance.
	 *
	 * @see Config
	 */
	public void setConfig(Config cfg) {
		this.config = cfg;
	}

	@Override
	public List<List<String>> parse(String content) {
		// TODO: search for a better return type
		List<List<String>> result = new ArrayList<>();
		List<String> constraintParts = this.getConstraintParts(content.replace(" ", ""));
		StringTokenizer tokenizer;
		List<String> partResult;
		for (String part : constraintParts) {
			partResult = new ArrayList<>();
			tokenizer = new StringTokenizer(part, "(.*)", true);
			while (tokenizer.hasMoreTokens()) {
				partResult.add(tokenizer.nextToken());
			}
			result.add(new ArrayList<>(partResult));
		}
		return result;
	}

	/**
	 * Returns all constraint parts in the given {@code textContent}.
	 *
	 * <p>
	 *
	 * A constraint part is for instance :
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>[[(!(A & B) | C)]]</code>
	 * </pre>
	 *
	 * where <code>[[</code> and <code>]]</code> are delimiters.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method uses <b>regex</b> for the extraction.
	 *
	 * @param textContent text containing constraint parts
	 * @return all constraint parts in the given {@code textContent}
	 *
	 * @since 1.0
	 */
	private List<String> getConstraintParts(String textContent) {
		String regex = String.format("%s(.*?)%s", Pattern.quote("[["), Pattern.quote("]]"));
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE); // TODO: remove Pattern.MULTILINE ?
		Matcher matcher = pattern.matcher(textContent);
		List<String> constraintParts = new ArrayList<>();
		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				constraintParts.add(matcher.group(i));
			}
		}
		return constraintParts;
	}
}
