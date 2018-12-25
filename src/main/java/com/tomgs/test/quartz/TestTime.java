package com.tomgs.test.quartz;

import java.util.Date;

/**
 * @author tangzhongyuan
 * @create 2018-12-14 15:16
 **/
public class TestTime {

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date + "==" + date.getTime());
        Date when = new Date();
        System.out.println(when + "==" + when.getTime());
        boolean before = date.before(when);
        System.out.println(before);
    }

}
