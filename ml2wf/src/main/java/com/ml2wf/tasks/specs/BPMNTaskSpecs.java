package com.ml2wf.tasks.specs;

import java.util.regex.Pattern;

import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.BPMNTask;
import com.ml2wf.util.RegexManager;

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

	OPTIONAL(RegexManager.getOptionalityPattern()) {

		@Override
		public boolean hasSpec(WFTask<?> task) {
			// TODO Auto-generated method stub
			// return this.getPattern().matcher(task.getNode());
			return false;
		}

		@Override
		public String getSpecValue(WFTask<?> task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(WFTask<?> task) {
			// TODO Auto-generated method stub

		}
	},
	CATEGORY(RegexManager.getCategoryPattern()) {

		@Override
		public boolean hasSpec(WFTask<?> task) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getSpecValue(WFTask<?> task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(WFTask<?> task) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * Current pattern (regex) used to retrieve the current specification value.
	 *
	 * @see Pattern
	 */
	private Pattern pattern;

	/**
	 * {@code BPMNTaskSpecs}'s default constructor.
	 */
	private BPMNTaskSpecs(Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * Returns the current {@code pattern}.
	 *
	 * @return the current {@code pattern}
	 */
	public Pattern getPattern() {
		return this.pattern;
	}

}
