package com.ml2wf.contract.storage.graph.converter;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.core.constraints.ConstraintTree;
import com.ml2wf.core.constraints.operands.AbstractOperand;
import com.ml2wf.core.constraints.operators.AbstractOperator;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public interface IGraphConstraintsConverter<T extends GraphStandardKnowledgeTask<V>, V extends GraphTaskVersion> {

    private static AbstractOperand callUsingReflection(Class<? extends AbstractOperand> clazz,
                                                       Class<?>[] parametersTypes, Object... parameters) {
        // TODO: replace by AbstractOperandFactory
        try {
            return clazz.getDeclaredConstructor(parametersTypes).newInstance(parameters);
        } catch (ReflectiveOperationException roe) {
            roe.printStackTrace();
            throw new RuntimeException("TODO roe");
        }
    }

    private AbstractOperand toAbstractOperand(GraphConstraintOperand<V> graphConstraintOperand) {
        // TODO: use AbstractOperandFactory
        if (graphConstraintOperand.getType().equals("var")) { // TODO: improve (should not rely only on var)
            return callUsingReflection(
                    AbstractOperand.Operands.getClassForShortName(graphConstraintOperand.getType()),
                    new Class<?>[] { String.class },
                    graphConstraintOperand.getTask().getName()
            );
        }
        return callUsingReflection(
                AbstractOperator.Operators.getClassForShortName(graphConstraintOperand.getType()),
                new Class<?>[] { List.class },
                graphConstraintOperand.getOperands()
                        .stream()
                        .map(this::toAbstractOperand)
                        .collect(Collectors.toList())
        );
    }

    default ConstraintTree toConstraintTree(GraphConstraintOperand<V> graphConstraintOperand) {
        return new ConstraintTree((AbstractOperator) toAbstractOperand(graphConstraintOperand));
    }

    GraphConstraintOperand<V> fromAbstractOperand(AbstractOperand abstractOperand, List<T> graphStandardKnowledgeTasks); // TODO: factorize using factory

    default GraphConstraintOperand<V> fromConstraintTree(ConstraintTree constraintTree, List<T> graphStandardKnowledgeTasks) {
        return fromAbstractOperand(constraintTree.getOperator(), graphStandardKnowledgeTasks);
    }

    default List<? extends GraphConstraintOperand<V>> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree,
                                              List<T> graphStandardKnowledgeTasks) {
        return standardKnowledgeTree.getConstraints().stream()
                .map(c -> fromConstraintTree(c, graphStandardKnowledgeTasks))
                .collect(Collectors.toList());
    }
}
