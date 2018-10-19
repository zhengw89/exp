package com.wz;

public enum RelationType {

    And("&"),
    Or("|");

    private String txt;

    RelationType(String txt) {
        this.txt = txt;
    }

    public static RelationType fromText(String text) {
        for (RelationType type : RelationType.values()) {
            if (type.txt.equals(text)) return type;
        }
        return null;
    }
}
