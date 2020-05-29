package com.ml2wf.constraints.parser;

import java.util.List;
import java.util.Map;

/**
 * This interface provides a method to extract constraints from a given text.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 */
public interface Parser {

	/**
	 * Parses {@code content} and returns TODO
	 *
	 * @param content text to parse
	 * @return
	 */
	public List<List<String>> parseContent(String content);

	public Map<String, List<String>> parseExpression(String expression);
}
