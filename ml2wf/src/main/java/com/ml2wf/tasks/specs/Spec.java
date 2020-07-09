package com.ml2wf.tasks.specs;

import org.w3c.dom.Element;

public interface Spec {

	public boolean hasSpec(Element element);

	public String getSpecValue(Element element);

	public void apply(Element element);
}
