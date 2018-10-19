package com.wz;

import java.util.Arrays;

public class App {

    public static void main(String args[]) {
        String exp = "丰田&(宝马|(奔驰))";
        String text = "宝马、奥迪、丰田、奔驰";

        ICondition condition;
        try {
            System.out.println(String.format("解析:\"%s\"", exp));
            condition = Conditions.fromString(exp);
        } catch (Exception ex) {
            System.out.println(String.format("解析失败:%s", exp));
            return;
        }

        System.out.println(String.format("验证:\"%s\" 结果:%b", text, condition.valid(text)));

        System.out.println(String.format("结果集:%s", Arrays.toString(condition.getTarget().toArray())));
    }

}
