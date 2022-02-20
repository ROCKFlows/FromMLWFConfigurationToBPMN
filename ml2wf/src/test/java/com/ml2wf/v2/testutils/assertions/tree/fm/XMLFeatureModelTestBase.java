package com.ml2wf.v2.testutils.assertions.tree.fm;

import com.ml2wf.util.FileHandler;
import com.ml2wf.util.Pair;
import com.ml2wf.v2.testutils.TreeTestBase;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.factory.IFeatureModelFactory;
import com.ml2wf.v2.xml.XMLFeatureModelFactory;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

public class XMLFeatureModelTestBase extends TreeTestBase<IFeatureModelFactory> {

    // TODO: factorize with XMLWorkflowTestBase

    /**
     * Meta workflows' directory.
     */
    protected static final String FM_DIRECTORY = "./feature_models/";

    /**
     * {@code XMLFeatureModelTestBase}'s default constructor.
     */
    public XMLFeatureModelTestBase() {
        super(new XMLFeatureModelFactory());
    }

    /**
     * Returns a {@code Stream} containing all FeatureModels located under the
     * {@link #FM_DIRECTORY} directory.
     *
     * @return a {@code Stream} containing all FeatureModels located under the
     *         {@link #FM_DIRECTORY} directory
     *
     * @throws URISyntaxException
     *
     * @see FileUtils
     */
    protected static Stream<File> fmFiles() throws URISyntaxException {
        File instanceDir = new File(Objects.requireNonNull(classLoader.getResource(FM_DIRECTORY)).toURI());
        return new HashSet<>(FileUtils.listFiles(instanceDir, FileHandler.getFMExtensions(), true)).stream();
    }

    protected Pair<FeatureModel, FeatureModel> getReferenceNormalizedFeatureModels(File file) {
        FeatureModel featureModelToNormalize = getFeatureModelFromFile(file);
        featureModelToNormalize.normalize();
        return new Pair<>(getFeatureModelFromFile(file), featureModelToNormalize);
    }

    @SneakyThrows
    protected FeatureModel getFeatureModelFromFile(File file) {
        Try<FeatureModel> tryFeatureModel = factory.featureModelFromFile(file);
        return tryFeatureModel.getOrElseThrow(f -> f);
    }
}
