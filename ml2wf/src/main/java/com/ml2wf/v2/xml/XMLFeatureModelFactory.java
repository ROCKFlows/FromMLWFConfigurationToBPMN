package com.ml2wf.v2.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.factory.IFeatureModelFactory;
import io.vavr.control.Try;

import java.io.File;

public class XMLFeatureModelFactory implements IFeatureModelFactory {

    private final ObjectMapper mapper;

    public XMLFeatureModelFactory() {
        mapper = XMLObjectMapperFactory.getInstance().createNewObjectMapper();
    }

    @Override
    public Try<FeatureModel> featureModelFromFile(final File file) {
        return Try.of(() -> mapper.reader()
                .withRootName("extendedFeatureModel")
                .readValue(file, FeatureModel.class));
    }
}
