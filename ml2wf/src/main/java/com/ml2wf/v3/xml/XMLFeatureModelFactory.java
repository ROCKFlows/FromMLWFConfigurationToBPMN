package com.ml2wf.v3.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml2wf.v3.tree.custom.featuremodel.FeatureModel;
import io.vavr.control.Try;

import java.io.File;

public class XMLFeatureModelFactory {

    private final ObjectMapper mapper;

    public XMLFeatureModelFactory() {
        mapper = XMLObjectMapperFactory.getInstance().createNewObjectMapper();
    }

    public Try<FeatureModel> featureModelFromFile(final File file) {
        return Try.of(() -> mapper.reader()
                .withRootName("extendedFeatureModel")
                .readValue(file, FeatureModel.class));
    }
}
