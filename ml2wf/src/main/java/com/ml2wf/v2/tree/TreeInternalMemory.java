package com.ml2wf.v2.tree;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;

/**
 * This class is a tree's internal memory.
 *
 * <p>
 *
 * It allows avoiding time-consuming tree search by keeping update a {@link Map} containing
 * all useful information for manipulating a tree.
 *
 * @since 1.1.0
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class TreeInternalMemory<I, T extends Identifiable<I>> {

    @NonNull private final Map<I, T> internalMemory = new LinkedHashMap<>();

    @NonNull public Collection<T> getChildren() {
        return new ArrayList<>(internalMemory.values()); // TODO: check memory cost
    }

    public boolean hasChildren() {
        return !internalMemory.isEmpty();
    }

    public boolean hasChildWithIdentity(@NonNull final I identity) {
        return internalMemory.containsKey(identity);
    }

    public Either<String, T> appendChild(final T child) {
        internalMemory.put(child.getIdentity(), child);
        return Either.right(child);
    }

    public Optional<T> removeChild(@NonNull final T child) {
        return Optional.ofNullable(internalMemory.remove(child.getIdentity()));
    }

    public Optional<T> getChildWithIdentity(@NonNull final I identity) {
        return Optional.ofNullable(internalMemory.get(identity));
    }
}
