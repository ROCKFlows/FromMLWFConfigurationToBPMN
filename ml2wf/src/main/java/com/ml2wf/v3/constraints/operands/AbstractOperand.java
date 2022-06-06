package com.ml2wf.v3.constraints.operands;

import com.ml2wf.v3.constraints.operands.impl.VariableOperand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

public interface  AbstractOperand {

    @RequiredArgsConstructor
    @Getter
    enum Operands {

        VARIABLE("var", VariableOperand.class);

        private final String shortName;
        private final Class<? extends AbstractOperand> clazz;

        public static String getShortNameForClass(Class<? extends AbstractOperand> clazz) {
            return Arrays.stream(values())
                    .filter(v -> v.clazz.equals(clazz))
                    .findAny()
                    .orElseThrow()
                    .shortName;
        }

        public static Class<? extends AbstractOperand> getClassForShortName(String shortName) {
            return Arrays.stream(values())
                    .filter(v -> v.shortName.equals(shortName))
                    .findAny()
                    .orElseThrow()
                    .clazz;
        }
    }
}
