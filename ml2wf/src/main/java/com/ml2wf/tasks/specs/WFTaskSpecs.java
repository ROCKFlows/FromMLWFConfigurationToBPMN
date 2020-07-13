package com.ml2wf.tasks.specs;

import java.util.regex.Pattern;

import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.util.RegexManager;

public enum WFTaskSpecs implements Spec<WFTask> {

	OPTIONAL(RegexManager.getOptionalityPattern()) {

		@Override
		public boolean hasSpec(WFTask task) {
			// TODO Auto-generated method stub
			// return this.getPattern().matcher(task.getNode());
			return false;
		}

		@Override
		public String getSpecValue(WFTask task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(WFTask task) {
			// TODO Auto-generated method stub

		}
	},
	CATEGORY(RegexManager.getCategoryPattern()) {

		@Override
		public boolean hasSpec(WFTask task) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getSpecValue(WFTask task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(WFTask task) {
			// TODO Auto-generated method stub

		}

	};

	private Pattern pattern;

	private WFTaskSpecs(Pattern pattern) {
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return this.pattern;
	}

}
