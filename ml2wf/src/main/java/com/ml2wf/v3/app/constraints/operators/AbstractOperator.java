package com.ml2wf.v3.app.constraints.operators;

import com.ml2wf.v3.app.constraints.operators.impl.ConjunctionOperator;
import com.ml2wf.v3.app.constraints.operators.impl.DisjunctionOperator;
import com.ml2wf.v3.app.constraints.operators.impl.ImplicationOperator;
import com.ml2wf.v3.app.constraints.operators.impl.NegationOperator;
import com.ml2wf.v3.app.constraints.operands.AbstractOperand;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public abstract class AbstractOperator implements AbstractOperand {

    protected final List<AbstractOperand> operands;

    protected AbstractOperator(final List<AbstractOperand> operands) {
        this.operands = Collections.unmodifiableList(operands);
    }

    protected AbstractOperator() {
        this(new ArrayList<>());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Operators {

        CONJUNCTION("conj", ConjunctionOperator.class),
        DISJUNCTION("disj", DisjunctionOperator.class),
        IMPLICATION("imp", ImplicationOperator.class),
        NEGATION("not", NegationOperator.class);

        private final String shortName;
        private final Class<? extends AbstractOperator> clazz;

        public static String getShortNameForClass(Class<? extends AbstractOperator> clazz) {
            return Arrays.stream(values())
                    .filter(v -> v.clazz.equals(clazz))
                    .findAny()
                    .orElseThrow()
                    .shortName;
        }

        public static Class<? extends AbstractOperator> getClassForShortName(String shortName) {
            return Arrays.stream(values())
                    .filter(v -> v.shortName.equals(shortName))
                    .findAny()
                    .orElseThrow()
                    .clazz;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + operands;
    }
}
