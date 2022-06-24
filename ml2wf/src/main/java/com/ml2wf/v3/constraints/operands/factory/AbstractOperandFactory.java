package com.ml2wf.v3.constraints.operands.factory;

import com.ml2wf.util.Pair;
import com.ml2wf.v3.constraints.operands.AbstractOperand;
import com.ml2wf.v3.constraints.operators.AbstractOperator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AbstractOperandFactory {

    // TODO: improve architecture

    private static AbstractOperand callUsingReflection(Class<? extends AbstractOperand> clazz,
                                                       Class<?>[] parametersTypes, Object... parameters) {
        try {
            return clazz.getDeclaredConstructor(parametersTypes).newInstance(parameters);
        } catch (ReflectiveOperationException roe) {
            roe.printStackTrace();
            throw new RuntimeException("TODO roe");
        }
    }

    private static AbstractOperand forString(String operandName, String value) {
        return callUsingReflection(AbstractOperand.Operands.getClassForShortName(operandName),
                new Class<?>[] { String.class }, value);
    }

    private static AbstractOperand forMap(String operandName, Map<?, ?> elements) {
        List<AbstractOperand> operands = elements.entrySet().stream()
                // TODO: filter non string keys
                .map(e -> {
                    if (e.getValue() instanceof List) {
                        return ((List<?>) e.getValue()).stream()
                                .map(v -> new Pair<>(e.getKey(), v))
                                .collect(Collectors.toList());
                    }
                    return List.of(new Pair<>(e.getKey(), e.getValue())); // TODO : remove List.of
                })
                .flatMap(Collection::stream)
                .map(e -> AbstractOperandFactory.createOperand((String) e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return callUsingReflection(AbstractOperator.Operators.getClassForShortName(operandName),
                new Class<?>[] { List.class }, operands);
    }

    public static AbstractOperand createOperand(String operandName, Object element) {
        // TODO: manage NoSuchElementException for getClassForShortName
        if (element instanceof String) {
            return forString(operandName, (String) element);
        }
        if (element instanceof Map) {
            return forMap(operandName, (Map<?, ?>) element);
        }
        throw new UnsupportedOperationException("TODO AbstractOperandFactory::createOperand " + element.getClass());
    }
}
