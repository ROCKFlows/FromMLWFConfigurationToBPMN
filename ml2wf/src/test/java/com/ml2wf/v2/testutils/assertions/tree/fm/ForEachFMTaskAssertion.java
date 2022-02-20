package com.ml2wf.v2.testutils.assertions.tree.fm;

import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
import lombok.Builder;
import lombok.NonNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;

@Builder
public class ForEachFMTaskAssertion {

    @NonNull final Pair<FeatureModel, FeatureModel> featureModelPair;
    BiConsumer<FeatureModelTask, FeatureModelTask> forEachTask;
    BiConsumer<Iterator<FeatureModelTask>, Iterator<FeatureModelTask>> forEachTaskIteration;
    BiConsumer<Iterator<FeatureModelTask>, Iterator<FeatureModelTask>> afterTasksIteration;

    private void _verify(Iterator<FeatureModelTask> featureModel1TasksIterator,
                         Iterator<FeatureModelTask> featureModel2TasksIterator) {
        while (featureModel1TasksIterator.hasNext() && featureModel2TasksIterator.hasNext()) {
            FeatureModelTask featureModel1Task = featureModel1TasksIterator.next();
            FeatureModelTask featureModel2Task = featureModel2TasksIterator.next();
            Optional.ofNullable(forEachTask).ifPresent(c -> c.accept(featureModel1Task, featureModel2Task));
            Optional.ofNullable(forEachTaskIteration)
                    .ifPresent(c -> c.accept(featureModel1TasksIterator, featureModel2TasksIterator));
            _verify(featureModel1Task.getChildren().iterator(), featureModel2Task.getChildren().iterator());
        }
        afterTasksIteration.accept(featureModel1TasksIterator, featureModel2TasksIterator);
    }

    public void verify() {
        featureModelPair.passTo(((featureModel1, featureModel2) -> {
            var featureModel1TasksIterator = featureModel1.getChildren().iterator();
            var featureModel2TasksIterator = featureModel2.getChildren().iterator();
            _verify(featureModel1TasksIterator, featureModel2TasksIterator);
        }));
    }
}
