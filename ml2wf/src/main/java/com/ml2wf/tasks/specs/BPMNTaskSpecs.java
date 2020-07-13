package com.ml2wf.tasks.specs;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.BPMNTask;
import com.ml2wf.util.RegexManager;
import com.ml2wf.util.XMLManager;

/**
 * This {@code enum} contains all specifications that a {@code BPMNTask} can
 * contain.
 *
 * <p>
 *
 * Furthermore, each constant has specific behaviors specified by the
 * {@link Spec} interface that help for the retrieval and appliance of these
 * specifications.
 *
 * <p>
 *
 * Thus, it is an implementations of the {@code Spec} interface.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see Spec
 * @see BPMNTask
 *
 */
public enum BPMNTaskSpecs implements Spec<WFTask<?>> {

	// TODO: add ABSTRACT
	OPTIONAL("optional", RegexManager.getOptionalityPattern()), CATEGORY("category", RegexManager.getCategoryPattern());

	/**
	 * The current specification's name.
	 */
	private String name;
	/**
	 * Current pattern (regex) used to retrieve the current specification value.
	 *
	 * @see Pattern
	 */
	private Pattern pattern;

	/**
	 * {@code BPMNTaskSpecs}'s default constructor.
	 *
	 * @param name    specification's name
	 * @param pattern pattern used to retrieve the specification
	 */
	private BPMNTaskSpecs(String name, Pattern pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	/**
	 * Returns the current {@code name}.
	 *
	 * @return the current {@code name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the current {@code pattern}.
	 *
	 * @return the current {@code pattern}
	 */
	public Pattern getPattern() {
		return this.pattern;
	}

	@Override
	public boolean hasSpec(WFTask<?> task) {
		return task.getSpecs().containsKey(this.getName());
	}

	@Override
	public Optional<String> getSpecValue(WFTask<?> task) {
		if (this.hasSpec(task)) {
			return Optional.of(task.getSpecValue(this.getName()));
		} else {
			for (String documentation : XMLManager.getAllBPMNDocContent((Element) task.getNode())) {
				Matcher matcher = this.getPattern().matcher(documentation);
				if (matcher.matches() && (matcher.groupCount() > 0)) {
					return Optional.of(matcher.group(1));
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public void apply(WFTask<?> task) {
		Optional<String> optSpecValue;
		if (!this.hasSpec(task)) {
			optSpecValue = this.getSpecValue(task);
			if (optSpecValue.isEmpty()) {
				return;
			}
			task.addSpec(this.getName(), optSpecValue.get());
		}
	}

}
