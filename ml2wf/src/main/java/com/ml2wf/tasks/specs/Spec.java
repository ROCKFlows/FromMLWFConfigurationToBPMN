package com.ml2wf.tasks.specs;

public interface Spec<T> {

	public boolean hasSpec(T element);

	public String getSpecValue(T element);

	public void apply(T element);
}
