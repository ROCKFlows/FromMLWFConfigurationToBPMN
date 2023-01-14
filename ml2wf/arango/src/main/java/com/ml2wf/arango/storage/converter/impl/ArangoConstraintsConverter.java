package com.ml2wf.arango.storage.converter.impl;

import com.ml2wf.arango.storage.converter.IArangoConstraintsConverter;
import com.ml2wf.arango.storage.dto.ArangoConstraintOperand;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.core.constraints.operands.AbstractOperand;
import com.ml2wf.core.constraints.operands.impl.VariableOperand;
import com.ml2wf.core.constraints.operators.AbstractOperator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArangoConstraintsConverter implements IArangoConstraintsConverter {

    public ArangoConstraintOperand fromAbstractOperand(AbstractOperand abstractOperand,
                                                       List<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks) {
        // TODO: improve to avoid instanceof
        if (abstractOperand instanceof VariableOperand) {
            return new ArangoConstraintOperand(
                    AbstractOperand.Operands.getShortNameForClass(abstractOperand.getClass()),
                    null,
                    arangoStandardKnowledgeTasks.stream()
                            .filter(a -> a.getName().equals(((VariableOperand) abstractOperand).getValue()))
                            .findAny()
                            .orElseThrow() // TODO: throw custom exception in case no task in the tree but present in the constraints

            );
        }
        return new ArangoConstraintOperand(
                AbstractOperator.Operators.getShortNameForClass(((AbstractOperator) abstractOperand).getClass()),
                null,
                ((AbstractOperator) abstractOperand).getOperands().stream()
                        .map(o -> fromAbstractOperand(o, arangoStandardKnowledgeTasks))
                        .collect(Collectors.toList())
        );
    }
}
