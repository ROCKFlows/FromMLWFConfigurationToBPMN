package com.ml2wf.tasks.specs;

import org.w3c.dom.Element;

public enum Specs implements Spec {

	ABSTRACT("abstract") {

		@Override
		public boolean hasSpec(Element element) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getSpecValue(Element element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(Element element) {
			// TODO Auto-generated method stub

		}
	},
	OPTIONAL("optional") {

		@Override
		public boolean hasSpec(Element element) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getSpecValue(Element element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(Element element) {
			// TODO Auto-generated method stub

		}
	},
	CATEGORY("category") {

		@Override
		public boolean hasSpec(Element element) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getSpecValue(Element element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void apply(Element element) {
			// TODO Auto-generated method stub

		}

	};

	private String name;

	private Specs(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
