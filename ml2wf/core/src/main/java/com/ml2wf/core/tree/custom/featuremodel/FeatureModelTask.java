package com.ml2wf.core.tree.custom.featuremodel;

import com.ml2wf.v2.tree.Identifiable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, of = {"parent", "name", "isAbstract", "isMandatory", "descriptions", "version"})
@ToString(of = {"name", "isAbstract", "isMandatory", "version"})
public class FeatureModelTask extends FeatureModelStructure implements Identifiable<String> {

    private FeatureModelTask parent;
    private String name;
    private boolean isAbstract;
    private boolean isMandatory;
    private String version;
    private List<Description> descriptions;

    @SuppressWarnings("unused")
    private FeatureModelTask() {
        // used by Jackson for deserialization
        super(new ArrayList<>());
        this.descriptions = new ArrayList<>();
    }

    public FeatureModelTask(String name, boolean isAbstract, boolean isMandatory, String version,
                            Description description, List<FeatureModelTask> children) {
        super(children);
        this.name = name;
        this.isAbstract = isAbstract;
        this.isMandatory = isMandatory;
        this.version = version;
        this.descriptions = new ArrayList<>(Collections.singletonList(description));
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Data
    public static final class Description {

        private String content = "";

        public String toString() {
            return content;
        }
    }

    @Override
    public @NonNull String getIdentity() {
        return name;
    }
}
