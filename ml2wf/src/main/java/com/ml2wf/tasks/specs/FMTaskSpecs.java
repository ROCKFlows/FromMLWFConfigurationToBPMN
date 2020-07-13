package com.ml2wf.tasks.specs;

import java.util.regex.Pattern;

import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.util.RegexManager;

public enum FMTaskSpecs implements Spec<FMTask> {

	OPTIONAL(RegexManager.getOptionalityPattern()) {

		@Override
		public boolean hasSpec(FMTask task) {
			// TODO Auto-generated method stub
			// return this.getPattern().matcher(task.getNode());
			return false;
		}

		@Override
		public String getSpecValue(FMTask task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(FMTask task) {
			task.addSpec(OPTIONAL, this.getSpecValue(task));
		}
	},
	CATEGORY(RegexManager.getCategoryPattern()) {

		@Override
		public boolean hasSpec(FMTask task) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getSpecValue(FMTask task) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(FMTask task) {
			// TODO Auto-generated method stub

		}

	};

	private Pattern pattern;

	private FMTaskSpecs(Pattern pattern) {
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return this.pattern;
	}

}
