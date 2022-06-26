package com.ml2wf.v3.app;

import io.vavr.control.Either;
import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface ITaskContainer<T extends INamedElement> {
    
    Collection<T> getTasks();

    Collection<T> getTasksMatching(@NonNull Predicate<T> predicate);

    boolean hasTasks();

    Either<String, T> appendTask(@NonNull T task);

    Optional<T> removeTask(@NonNull T task);

    Optional<T> getTaskWithName(@NonNull String name);

    Optional<T> getTaskMatching(@NonNull Predicate<T> predicate);

    boolean hasTaskWithName(@NonNull String name);
}
