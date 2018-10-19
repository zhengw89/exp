package com.wz;

import java.util.Set;

public interface ICondition {

    boolean valid(String value);

    Set<Integer> getTarget();
}
