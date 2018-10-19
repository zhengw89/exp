package com.wz;

import java.util.*;
import java.util.stream.Collectors;

public class Conditions {

    private static ThreadLocal<Map<String, ICondition>> threadLocal = ThreadLocal.withInitial(HashMap::new);
    private static Set<Character> CHAR_SET = new HashSet<>();

    static {
        CHAR_SET.add('(');
        CHAR_SET.add(')');
        CHAR_SET.add('|');
        CHAR_SET.add('&');
    }

    static ICondition fromString(String exp) {
        List<String> stringItems = split(exp);
        return fromStringItems(stringItems);
    }

    private static List<String> split(String exp) {
        StringBuilder tempStr = new StringBuilder();
        List<String> result = new ArrayList<>();
        for (Character c : exp.toCharArray()) {
            if (CHAR_SET.contains(c)) {
                if (tempStr.length() != 0) {
                    result.add(tempStr.toString().trim());
                    tempStr = new StringBuilder();
                }
                result.add(c.toString());
            } else {
                tempStr.append(c.toString());
            }
        }

        if (tempStr.length() != 0) {
            result.add(tempStr.toString().trim());
        }

        return result;
    }

    private static ICondition fromStringItems(List<String> items) {
        return fromObjItems(items.stream().map(Object::toString).collect(Collectors.toList()));
    }

    private static ICondition fromObjItems(List<Object> items) {
        int lastLeftIndex = -1;
        for (int i = items.size(); i > 0; i--) {
            if ("(".equals(items.get(i - 1))) {
                lastLeftIndex = i - 1;
                break;
            }
        }

        int lastRightIndex = -1;
        if (lastLeftIndex > -1) {
            for (int i = lastLeftIndex; i < items.size(); i++) {
                if (")".equals(items.get(i))) {
                    lastRightIndex = i;
                    break;
                }
            }
            if (lastRightIndex == -1) {
                throw new RuntimeException();
            }
        }

        if (lastLeftIndex == -1) {
            return fromSimpleItems(items.stream().map(Object::toString).collect(Collectors.toList()));
        } else {
            ICondition condition = fromSimpleItems(items.subList(lastLeftIndex + 1, lastRightIndex).stream().map(Object::toString).collect(Collectors.toList()));
            String key = UUID.randomUUID().toString();
            threadLocal.get().put(key, condition);

            if (lastRightIndex >= lastLeftIndex) {
                items.subList(lastLeftIndex, lastRightIndex + 1).clear();
            }
            items.add(lastLeftIndex, key);

            return fromObjItems(items);
        }
    }

    private static ICondition fromSimpleItems(List<String> items) {
        if (items.size() == 1) {
            if (threadLocal.get().containsKey(items.get(0))) {
                return threadLocal.get().get(items.get(0));
            } else {
                ConditionItem conditionItem = new ConditionItem();
                conditionItem.setValue(items.get(0));
                return conditionItem;
            }
        }

        String value;

        value = items.get(items.size() - 3);
        ICondition item1;
        if (threadLocal.get().containsKey(value)) {
            item1 = threadLocal.get().get(value);
        } else {
            ConditionItem ci = new ConditionItem();
            ci.setValue(value);
            item1 = ci;
        }

        RelationType relationType = RelationType.fromText(items.get(items.size() - 2));

        value = items.get(items.size() - 1);
        ICondition item2;
        if (threadLocal.get().containsKey(value)) {
            item2 = threadLocal.get().get(value);
        } else {
            ConditionItem ci = new ConditionItem();
            ci.setValue(value);
            item2 = ci;
        }

        ConditionGroup group = new ConditionGroup();
        group.setFirstCondition(item1);
        group.setRelationType(relationType);
        group.setSecondCondition(item2);

        for (int i = items.size() - 4; i > 0; i -= 2) {
            RelationType tr = RelationType.fromText(items.get(i));

            ICondition condition;
            if (threadLocal.get().containsKey(items.get(i - 1))) {
                condition = threadLocal.get().get(items.get(i - 1));
            } else {
                ConditionItem ci = new ConditionItem();
                ci.setValue(items.get(i - 1));
                condition = ci;
            }

            ConditionGroup preGroup = group;
            group = new ConditionGroup();
            group.setFirstCondition(condition);
            group.setRelationType(tr);
            group.setSecondCondition(preGroup);
        }

        return group;
    }
}
