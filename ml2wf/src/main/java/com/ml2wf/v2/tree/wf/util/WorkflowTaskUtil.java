package com.ml2wf.v2.tree.wf.util;

import com.ml2wf.util.RegexManager;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * An utilitarian class dedicated to the {@link WorkflowTask} class.
 *
 * @see WorkflowTask
 *
 * @since 1.1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkflowTaskUtil {

    // TODO: clarify this util responsibility

    /**
     * Returns whether the given {@link WorkflowTask} is optional or not based on
     * its documentation content and the {@link RegexManager#getOptionalityPattern()}.
     *
     * @param workflowTask  the workflow task
     *
     * @return whether the given {@link WorkflowTask} is optional or not
     *
     * @see WorkflowTask
     * @see RegexManager#getOptionalityPattern()
     */
    public static boolean isOptional(WorkflowTask workflowTask) {
        String content = workflowTask.getDocumentation().getContent().replace(" ", "");
        var matcher = RegexManager.getOptionalityPattern().matcher(content);
        if (matcher.find() && (matcher.groupCount() > 0)) {
            return Boolean.parseBoolean(matcher.group(1));
        }
        return false;
    }
}
