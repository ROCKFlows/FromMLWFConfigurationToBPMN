package com.ml2wf.v3.app.business.storage.graph.contracts.dto;

public interface GraphTaskVersion {

    int getMajor();

    int getMinor();

    int getPatch();

    String getName();
}
