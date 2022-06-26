package com.ml2wf.v3.app.business.storage.graph;

import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoTaskVersion;
import com.ml2wf.v3.constraints.ConstraintTree;
import com.ml2wf.v3.constraints.operands.AbstractOperand;
import com.ml2wf.v3.constraints.operands.impl.VariableOperand;
import com.ml2wf.v3.constraints.operators.AbstractOperator;
import com.ml2wf.v3.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArangoConstraintsConverter implements IArangoConstraintsConverter {

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

    private AbstractOperand toAbstractOperand(ArangoConstraintOperand arangoConstraintOperand) {
        // TODO: use AbstractOperandFactory
        if (arangoConstraintOperand.getType().equals("var")) { // TODO: improve (should not rely only on var)
            return callUsingReflection(AbstractOperand.Operands.getClassForShortName(arangoConstraintOperand.getType()),
                    new Class<?>[] { String.class }, arangoConstraintOperand.getTask().getName());
        }
        return callUsingReflection(AbstractOperator.Operators.getClassForShortName(arangoConstraintOperand.getType()),
                new Class<?>[] { List.class }, arangoConstraintOperand.getOperands().stream().map(this::toAbstractOperand).collect(Collectors.toList()));
    }

    @Override
    public ConstraintTree toConstraintTree(ArangoConstraintOperand arangoConstraintOperand) {
        return new ConstraintTree((AbstractOperator) toAbstractOperand(arangoConstraintOperand));
    }

    public ArangoConstraintOperand fromAbstractOperand(AbstractOperand abstractOperand,
                                                       List<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks) {
        // TODO: improve to avoid instanceof
        if (abstractOperand instanceof VariableOperand) {
            return new ArangoConstraintOperand(
                    AbstractOperand.Operands.getShortNameForClass(abstractOperand.getClass()),
                    new ArangoTaskVersion(0, 0, 0, "unversioned"),
                    arangoStandardKnowledgeTasks.stream()
                            .filter(a -> a.getName().equals(((VariableOperand) abstractOperand).getValue()))
                            .findAny()
                            .orElseThrow() // TODO: throw custom exception in case no task in the tree but present in the constraints

            );
        }
        return new ArangoConstraintOperand(
                AbstractOperator.Operators.getShortNameForClass(((AbstractOperator) abstractOperand).getClass()),
                new ArangoTaskVersion(0, 0, 0, "unversioned"),
                ((AbstractOperator) abstractOperand).getOperands().stream()
                        .map(o -> fromAbstractOperand(o, arangoStandardKnowledgeTasks))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public ArangoConstraintOperand fromConstraintTree(ConstraintTree constraintTree,
                                                      List<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks) {
        return fromAbstractOperand(constraintTree.getOperator(), arangoStandardKnowledgeTasks);
    }

    @Override
    public List<ArangoConstraintOperand> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree,
                                                                   List<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks) {
        return standardKnowledgeTree.getConstraints().stream()
                .map(c -> fromConstraintTree(c, arangoStandardKnowledgeTasks))
                .collect(Collectors.toList());
    }
}
