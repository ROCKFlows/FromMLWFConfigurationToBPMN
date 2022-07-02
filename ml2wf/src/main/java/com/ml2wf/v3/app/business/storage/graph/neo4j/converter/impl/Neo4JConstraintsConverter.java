package com.ml2wf.v3.app.business.storage.graph.neo4j.converter.impl;

import com.ml2wf.v3.app.business.storage.graph.neo4j.converter.INeo4JConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import com.ml2wf.v3.app.constraints.ConstraintTree;
import com.ml2wf.v3.app.constraints.operands.AbstractOperand;
import com.ml2wf.v3.app.constraints.operands.impl.VariableOperand;
import com.ml2wf.v3.app.constraints.operators.AbstractOperator;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Neo4JConstraintsConverter implements INeo4JConstraintsConverter {

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

    private AbstractOperand toAbstractOperand(Neo4JConstraintOperand constraintOperand) {
        // TODO: use AbstractOperandFactory
        if (constraintOperand.getType().equals("var")) { // TODO: improve (should not rely only on var)
            return callUsingReflection(AbstractOperand.Operands.getClassForShortName(constraintOperand.getType()),
                    new Class<?>[] { String.class }, constraintOperand.getTask().getName());
        }
        return callUsingReflection(AbstractOperator.Operators.getClassForShortName(constraintOperand.getType()),
                new Class<?>[] { List.class }, constraintOperand.getOperands().stream().map(this::toAbstractOperand).collect(Collectors.toList()));
    }

    @Override
    public ConstraintTree toConstraintTree(Neo4JConstraintOperand constraintOperand) {
        return new ConstraintTree((AbstractOperator) toAbstractOperand(constraintOperand));
    }

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
                            .orElseThrow() // TODO: throw custom exception in case no task in the tree but present in the constraints

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

    @Override
    public Neo4JConstraintOperand fromConstraintTree(ConstraintTree constraintTree,
                                                     List<Neo4JStandardKnowledgeTask> standardKnowledgeTasks) {
        return fromAbstractOperand(constraintTree.getOperator(), standardKnowledgeTasks);
    }

    @Override
    public List<Neo4JConstraintOperand> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree,
                                                                  List<Neo4JStandardKnowledgeTask> standardKnowledgeTasks) {
        return standardKnowledgeTree.getConstraints().stream()
                .map(c -> fromConstraintTree(c, standardKnowledgeTasks))
                .collect(Collectors.toList());
    }
}
