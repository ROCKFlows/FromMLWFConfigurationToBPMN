package com.ml2wf.v2.tree.fm.factory;

import com.ml2wf.v2.tree.fm.FeatureModel;
import io.vavr.control.Try;

import java.io.File;

public interface IFeatureModelFactory {

    Try<FeatureModel> featureModelFromFile(File file);
}
