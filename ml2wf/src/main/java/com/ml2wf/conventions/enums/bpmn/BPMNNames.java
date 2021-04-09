package com.ml2wf.conventions.enums.bpmn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ml2wf.conventions.enums.TaskTagsSelector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This {@code enum} contains handled tags' names according to the
 * <a href="http://www.bpmn.org/">BPMN standard</a>.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum BPMNNames implements TaskTagsSelector {

    // general tags
    PROCESS("bpmn2:process"), INCOMING("bpmn2:incoming"), OUTGOING("bpmn2:outgoing"),
    EXTENSION("bpmn2:extensionElements"), STYLE("ext:style"), DOCUMENTATION("bpmn2:documentation"),
    ANNOTATION("bpmn2:textAnnotation"), TEXT("bpmn2:text"), DIAGRAM("bpmndi:BPMNDiagram"), PLANE("bpmndi:BPMNPlane"),
    PROPERTY("bpmn2:property"),
    // task tags
    TASK("bpmn2:task"), USERTASK("bpmn2:userTask"), SERVICETASK("bpmn2:serviceTask"),
    // positional tags
    SHAPE("bpmndi:BPMNShape"), BOUNDS("dc:Bounds"), LABEL("bpmndi:BPMNLabel"),
    // reserved tags
    SELECTOR("");

    /**
     * Tag name.
     */
    private final String name;

    /**
     * Returns whether the given tag is a BPMN task tag's name or not.
     *
     * @param tag tag to check
     * @return whether the given tag is a BPMN task tag's name or not
     */
    public boolean isBPMNTask(String tag) {
        return this.getTaskTags().contains(tag);
    }

    @Override
    public List<String> getTaskTags() {
        return new ArrayList<>(Arrays.asList(USERTASK.getName(), TASK.getName(), SERVICETASK.getName()));
    }
}
