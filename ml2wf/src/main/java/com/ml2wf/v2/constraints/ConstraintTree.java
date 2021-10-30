package com.ml2wf.v2.constraints;

import com.ml2wf.v2.constraints.operands.AbstractOperand;
import com.ml2wf.v2.constraints.operands.factory.AbstractOperandFactory;
import com.ml2wf.v2.constraints.operators.AbstractOperator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;

@Data
@AllArgsConstructor
public class ConstraintTree {

    @NonNull private AbstractOperator operator;

    public static ConstraintTree fromMap(@NonNull final Map<String, Object> map) {
        if (map.isEmpty()) {
            throw new UnsupportedOperationException("Can't create a ConstraintTree from empty mapping.");
        }
        if (map.keySet().size() > 1) {
            throw new UnsupportedOperationException("Can't create a ConstraintTree from multi-key mapping.");
        }
        Map.Entry<String, Object> topEntry = map.entrySet().iterator().next();
        AbstractOperand operand = AbstractOperandFactory.createOperand(topEntry.getKey(), topEntry.getValue());
        if (!(operand instanceof AbstractOperator)) {
            throw new UnsupportedOperationException("TODO ConstraintTree::fromMap");
        }
        return new ConstraintTree((AbstractOperator) operand);
    }
}
