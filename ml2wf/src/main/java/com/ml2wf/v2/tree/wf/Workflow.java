package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.AbstractTree;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * A {@link Workflow} contains a list of {@link #processes}.
 *
 * <p>
 *
 * Each {@link Process} contains the workflow's tasks and links.
 *
 * @see Process
 *
 * @since 1.1
 */
@JacksonXmlRootElement(localName = "bpmn2:definitions")
@NoArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Workflow extends AbstractTree<Process> implements IInstantiable {

    @JacksonXmlProperty(localName="bpmn2:process")
    @JacksonXmlElementWrapper(useWrapping = false)
    protected List<Process> processes;

    @Override
    public Process appendChild(Process child) {
        processes.add(child);
        return child;
    }

    @Override
    public Process removeChild(Process child) {
        return (processes.remove(child)) ? child : null;
    }

    @Override
    public void instantiate() {
        processes.forEach(IInstantiable::instantiate);
    }
}
