package com.tomgs.test.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试静态与非静态的区别
 * @author tangzhongyuan
 * @create 2018-11-21 20:47
 **/
public class StaticTest {

    private static final Map<String, String> map = new HashMap<>();
    private Map<String, String> map2 = new HashMap<>();

    public void put(String key, String value) {
        map.put(key, value);
        map2.put(key, value);
    }

    public int getMap1Size() {
        return map.size();
    }

    public int getMap2Size() {
        return map2.size();
    }

    public static void main(String[] args) {
        StaticTest test = new StaticTest();
        test.put("1", "a");

        StaticTest test1 = new StaticTest();
        test1.put("2", "b");

        StaticTest test3 = new StaticTest();
        System.out.println(test3.getMap1Size());
        System.out.println(test3.getMap2Size());
    }

}
