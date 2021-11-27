package com.ml2wf.v2.tree.fm;

import com.ml2wf.v2.tree.AbstractTree;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link FeatureModel} has a {@link FeatureModelStructure} containing its
 * {@link FeatureModelTask}s.
 *
 * <p>
 *
 * It is an extension of the {@link AbstractTree} class but delegates its contract methods
 * to its {@link #structure}.
 *
 * @see AbstractTree
 * @see FeatureModelStructure
 * @see FeatureModelTask
 *
 * @since 1.1.0
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FeatureModel extends AbstractTree<FeatureModelTask, String> {

    private FeatureModelStructure structure;
    @Getter @NonNull private final Set<FeatureModelRule> constraints = new LinkedHashSet<>();

    @Override
    public @NonNull Collection<FeatureModelTask> getChildren() {
        // delegates to structure
        return structure.getChildren();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *
     * <b>Note</b> that this operation is delegated to its {@link #structure}.
     */
    @Override
    public boolean hasChildren() {
        // delegates to structure
        return structure.hasChildren();
    }

    @Override
    public boolean hasChildWithIdentity(@NonNull String identity) {
        return super.hasChildWithIdentity(identity);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *
     * <b>Note</b> that this operation is delegated to its {@link #structure}.
     *
     * @param child the child
     *
     * @return the appended child
     */
    @Override
    public Either<String, FeatureModelTask> appendChild(final FeatureModelTask child) {
        // delegates to structure
        return structure.appendChild(child);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *
     * <b>Note</b> that this operation is delegated to its {@link #structure}.
     *
     * @param child the child
     *
     * @return the removed child
     */
    @Override
    public Optional<FeatureModelTask> removeChild(final FeatureModelTask child) {
        // delegates to structure
        return structure.removeChild(child);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *
     * <b>Note</b> that this operation is delegated to its {@link #structure}.
     *
     * @param name  the name of the requested child
     *
     * @return the removed child
     */
    @Override
    public Optional<FeatureModelTask> getChildWithIdentity(final String name) {
        // delegates to structure
        return structure.getChildWithIdentity(name);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *
     * <b>Note</b> that this operation is delegated to its {@link #structure}.
     */
    @Override
    public void normalize() {
        // delegates to structure
        structure.normalize();
    }
}
