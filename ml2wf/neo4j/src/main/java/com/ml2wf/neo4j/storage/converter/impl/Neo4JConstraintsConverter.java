package com.ml2wf.neo4j.storage.converter.impl;

import com.ml2wf.contract.exception.VariableTaskNotFoundException;
import com.ml2wf.core.constraints.operands.AbstractOperand;
import com.ml2wf.core.constraints.operands.impl.VariableOperand;
import com.ml2wf.core.constraints.operators.AbstractOperator;
import com.ml2wf.neo4j.storage.dto.Neo4JConstraintOperand;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.converter.INeo4JConstraintsConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Neo4JConstraintsConverter implements INeo4JConstraintsConverter {

    @Override
    public Neo4JConstraintOperand fromAbstractOperand(AbstractOperand abstractOperand,
                                                      List<Neo4JStandardKnowledgeTask> standardKnowledgeTasks) {
        // TODO: improve to avoid instanceof
        if (abstractOperand instanceof VariableOperand) {
            return new Neo4JConstraintOperand(
                    AbstractOperand.Operands.getShortNameForClass(abstractOperand.getClass()),
                    new Neo4JTaskVersion(0, 0, 0, "unversioned"),
                    standardKnowledgeTasks.stream()
                            .filter(a -> a.getName().equals(((VariableOperand) abstractOperand).getValue()))
                            .findAny()
                            .orElseThrow(() -> new VariableTaskNotFoundException((VariableOperand) abstractOperand))
            );
        }
        return new Neo4JConstraintOperand(
                AbstractOperator.Operators.getShortNameForClass(((AbstractOperator) abstractOperand).getClass()),
                new Neo4JTaskVersion(0, 0, 0, "unversioned"),
                ((AbstractOperator) abstractOperand).getOperands().stream()
                        .map(o -> fromAbstractOperand(o, standardKnowledgeTasks))
                        .collect(Collectors.toList())
        );
    }
}
