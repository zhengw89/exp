package com.wz;

import java.util.*;

public class ConditionItem implements ICondition {

    private static HashMap<String, Set<Integer>> DATA_MAP = new HashMap<>();

    static {
        DATA_MAP.put("宝马", new HashSet<>(Arrays.asList(1, 2, 3, 4)));
        DATA_MAP.put("奥迪", new HashSet<>(Arrays.asList(3, 4, 5, 6)));
        DATA_MAP.put("丰田", new HashSet<>(Arrays.asList(1, 4, 6, 7)));
        DATA_MAP.put("奔驰", new HashSet<>(Arrays.asList(2, 8, 9, 7)));
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean valid(String value) {
        return value.contains(this.value);
    }

    @Override
    public Set<Integer> getTarget() {
        if (DATA_MAP.containsKey(this.value)) {
            return DATA_MAP.get(this.value);
        }
        return new HashSet<>();
    }
}
