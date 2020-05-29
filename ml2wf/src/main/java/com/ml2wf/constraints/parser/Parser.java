package com.ml2wf.constraints.parser;

import java.util.List;

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
	public List<List<String>> parse(String content);
}
