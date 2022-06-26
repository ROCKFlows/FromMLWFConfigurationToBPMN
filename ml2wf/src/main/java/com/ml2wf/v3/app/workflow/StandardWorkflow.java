package com.ml2wf.v3.app.workflow;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class StandardWorkflow implements IWorkflow {

    private List<StandardWorkflowTask> tasks;

    public List<StandardWorkflowTask> getTasksMatching(@NonNull Predicate<StandardWorkflowTask> predicate) {
        return tasks.stream().filter(predicate).collect(Collectors.toList());
    }

    public boolean hasTasks() {
        return !tasks.isEmpty();
    }

    public Either<String, StandardWorkflowTask> appendTask(@NonNull StandardWorkflowTask task) {
        if (getTaskWithName(task.getName()).isPresent()) {
            return Either.left("Task name already present in workflow.");
        }
        tasks.add(task);
        return Either.right(task);
    }

    public Optional<StandardWorkflowTask> removeTask(@NonNull StandardWorkflowTask task) {
        return (tasks.remove(task)) ? Optional.of(task) : Optional.empty();
    }

    public Optional<StandardWorkflowTask> getTaskWithName(@NonNull String name) {
        return getTaskMatching(t -> t.getName().equals(name));
    }

    public Optional<StandardWorkflowTask> getTaskMatching(@NonNull Predicate<StandardWorkflowTask> predicate) {
        return tasks.stream().filter(predicate).findAny();
    }

    public boolean hasTaskWithName(@NonNull String name) {
        return getTaskWithName(name).isPresent();
    }
}
