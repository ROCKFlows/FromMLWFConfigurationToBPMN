package com.ml2wf.v3.app.workflow.custom.bpmn.util;

import com.ml2wf.conventions.Notation;
import com.ml2wf.util.RegexManager;
import com.ml2wf.util.XMLManager;
import com.ml2wf.v3.app.workflow.custom.bpmn.BPMNWorkflowTask;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BPMNWorkflowUtils {

    // TODO: clarify this util responsibility

    /**
     * Returns whether the given {@link BPMNWorkflowTask} is optional or not based on
     * its documentation content and the {@link RegexManager#getOptionalityPattern()}.
     *
     * @param bpmnWorkflowTask  the workflow task
     *
     * @return whether the given {@link BPMNWorkflowTask} is optional or not
     *
     * @see BPMNWorkflowTask
     * @see RegexManager#getOptionalityPattern()
     */
    public static boolean isOptional(final BPMNWorkflowTask bpmnWorkflowTask) {
        String content = bpmnWorkflowTask.getDocumentation().getContent().replace(" ", "");
        var matcher = RegexManager.getOptionalityPattern().matcher(content);
        if (matcher.find() && (matcher.groupCount() > 0)) {
            return Boolean.parseBoolean(matcher.group(1));
        }
        return false;
    }

    public static boolean isAbstract(final BPMNWorkflowTask bpmnWorkflowTask) {
        String regex = String.format("%s(\\w*)", Notation.GENERIC_VOC);
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(bpmnWorkflowTask.getName());
        return matcher.find();
    }

    public static Optional<String> getReferenceName(final BPMNWorkflowTask bpmnWorkflowTask) {
        return XMLManager.getReferredTask(bpmnWorkflowTask.getDocumentation().getContent());
    }
}
