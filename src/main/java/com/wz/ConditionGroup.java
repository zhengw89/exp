package com.wz;

import java.util.HashSet;
import java.util.Set;

public class ConditionGroup implements ICondition {

    private ICondition firstCondition;

    private RelationType relationType;

    private ICondition secondCondition;

    public ICondition getFirstCondition() {
        return firstCondition;
    }

    public void setFirstCondition(ICondition firstCondition) {
        this.firstCondition = firstCondition;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public ICondition getSecondCondition() {
        return secondCondition;
    }

    public void setSecondCondition(ICondition secondCondition) {
        this.secondCondition = secondCondition;
    }

    @Override
    public boolean valid(String value) {
        switch (relationType) {
            case Or:
                return firstCondition.valid(value) || secondCondition.valid(value);
            case And:
                return firstCondition.valid(value) && secondCondition.valid(value);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public Set<Integer> getTarget() {
        Set<Integer> result = new HashSet<>();
        switch (relationType) {
            case Or:
                result.addAll(firstCondition.getTarget());
                result.addAll(secondCondition.getTarget());
                return result;
            case And:
                result.addAll(firstCondition.getTarget());
                result.retainAll(secondCondition.getTarget());
                return result;
            default:
                throw new RuntimeException();
        }
    }
}
