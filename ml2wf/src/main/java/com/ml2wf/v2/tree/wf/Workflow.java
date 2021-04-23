package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.INormalizable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

/**
 * A {@link Workflow} contains a list of {@link #processes}.
 *
 * <p>
 *
 * Each {@link Process} contains the workflow's tasks and links.
 *
 * @see Process
 *
 * @since 1.1.0
 */
@JacksonXmlRootElement(localName = "bpmn2:definitions")
@NoArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Workflow extends AbstractTree<Process> implements IInstantiable {

    @JacksonXmlProperty(localName="bpmn2:process")
    @JacksonXmlElementWrapper(useWrapping = false)
    protected List<Process> processes;

    @Override
    public Process appendChild(final Process child) {
        processes.add(child);
        return child;
    }

    @Override
    public Optional<Process> removeChild(final Process child) {
        return Optional.ofNullable((processes.remove(child)) ? child : null);
    }

    @Override
    public Optional<Process> getChildWithName(final String name) {
        return processes.stream()
                .filter(p -> p.getChildWithName(name).isPresent())
                .findAny();
    }

    @Override
    public void normalize() {
        processes.forEach(INormalizable::normalize);
    }

    @Override
    public void instantiate() {
        processes.forEach(IInstantiable::instantiate);
    }
}
