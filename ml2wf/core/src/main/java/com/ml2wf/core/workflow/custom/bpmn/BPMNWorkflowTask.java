package com.ml2wf.core.workflow.custom.bpmn;

import com.ml2wf.core.conventions.Notation;
import com.ml2wf.core.workflow.custom.bpmn.util.BPMNWorkflowUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // required by Jackson
@Data
public class BPMNWorkflowTask {

    private static int taskCounter = 0;
    private String id;
    private String name;
    private Documentation documentation = new Documentation();

    public BPMNWorkflowTask(String name, String documentation) {
        this.id = Notation.TASK_ID_PREFIX + (++taskCounter) + name;
        this.name = name;
        this.documentation = new Documentation(this.name, documentation);
    }

    public BPMNWorkflowTask(String name) {
        this(name, "");
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE) // required by Jackson
    @Data
    public static final class Documentation {

        private String id;
        private String content = "";

        public Documentation(String name, String content) {
            id = Notation.DOCUMENTATION_VOC + name;
            this.content = content;
        }
    }

    public boolean isAbstract() {
        return BPMNWorkflowUtils.isAbstract(this);
    }

    public boolean isOptional() {
        return BPMNWorkflowUtils.isOptional(this);
    }
}
