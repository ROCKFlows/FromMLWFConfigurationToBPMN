package com.ml2wf.tasks.specs;

import java.util.regex.Pattern;

import com.ml2wf.tasks.base.Task;
import com.ml2wf.util.RegexManager;

public enum Specs implements Spec<Task> {

	OPTIONAL(RegexManager.getOptionalityPattern()) {

		@Override
		public boolean hasSpec(Task task) {
			// TODO Auto-generated method stub
			// return this.getPattern().matcher(task.getNode());
			return false;
		}

		@Override
		public String getSpecValue(Task task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(Task task) {
			// TODO Auto-generated method stub

		}
	},
	CATEGORY(RegexManager.getCategoryPattern()) {

		@Override
		public boolean hasSpec(Task task) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getSpecValue(Task task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(Task task) {
			// TODO Auto-generated method stub

		}

	};

	private Pattern pattern;

	private Specs(Pattern pattern) {
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return this.pattern;
	}

}
