package com.ml2wf.tasks.specs;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.util.RegexManager;

/**
 * This {@code enum} contains all specifications that a {@code FMTask} can
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
 * @see FMTask
 *
 */
public enum FMTaskSpecs implements Spec<FMTask> {

	// TODO: add ABSTRACT
	OPTIONAL("optional", RegexManager.getOptionalityPattern()) {

		@Override
		public Optional<String> getSpecValue(FMTask task) {
			if (this.hasSpec(task)) {
				return Optional.of(task.getSpecValue(this.getName()));
			} else {
				NodeList docNodes = ((Element) task.getNode()).getElementsByTagName(FMNames.DESCRIPTION.getName());
				if (docNodes.getLength() == 0) {
					return Optional.empty();
				}
				Node docNode = docNodes.item(0);
				Matcher matcher = this.getPattern().matcher(docNode.getTextContent());
				if (matcher.matches() && (matcher.groupCount() > 0)) {
					return Optional.of(matcher.group(1));
				}
			}
			return Optional.empty();
		}

		@Override
		public void apply(FMTask task) {
			Optional<String> optSpecValue;
			if (!this.hasSpec(task)) {
				optSpecValue = this.getSpecValue(task);
				if (optSpecValue.isEmpty()) {
					return;
				}
				NodeList docNodes = ((Element) task.getNode()).getElementsByTagName(FMNames.DESCRIPTION.getName());
				if (docNodes.getLength() == 0) {
					return;
				}
				// TODO
				task.addSpec(this.getName(), optSpecValue.get());
			}
		}

	},
	CATEGORY("category", RegexManager.getCategoryPattern()) {

		@Override
		public Optional<String> getSpecValue(FMTask task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(FMTask task) {
			// TODO Auto-generated method stub

		}

	};

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
	 * {@code FMTaskSpecs}'s default constructor.
	 *
	 * @param name    specification's name
	 * @param pattern pattern used to retrieve the specification
	 */
	private FMTaskSpecs(String name, Pattern pattern) {
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
	public boolean hasSpec(FMTask task) {
		return task.getSpecs().containsKey(this.getName());
	}

}
