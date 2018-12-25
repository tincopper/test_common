package com.tomgs.test.base;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author tangzhongyuan
 * @create 2018-11-30 14:41
 **/
public class BeanToMap {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Test
    public void test1() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        BeanToMap map = new BeanToMap();
        map.setAge(10);
        map.setName("tomgs");

        Map describe = BeanUtils.describe(map);
        System.out.println(describe);

        BeanMap beanMap = new BeanMap(map);

        System.out.println(beanMap.toString());

        System.out.println(System.getProperty("user.dir"));
    }
}
