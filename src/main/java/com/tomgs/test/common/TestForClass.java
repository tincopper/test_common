package com.tomgs.test.common;

/**
 * @author tangzhongyuan
 * @create 2018-12-10 15:21
 **/
public class TestForClass {

    private String name;

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = "com.tomgs.test.common.TestForClass ";
        Class<?> aClass = Class.forName(className.trim());
        Object o = aClass.newInstance();
        System.out.println(o);
    }

}
