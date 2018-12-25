package com.tomgs.test.el;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * EL表达式测试
 *
 * @author tangzhongyuan
 * @create 2018-12-20 19:51
 **/
public class ElTest {

    @Test
    public void test1() {

        MapContext mapContext = new MapContext();
        mapContext.set("DATE", Calendar.getInstance().getTime());
        mapContext.set("TIMESTAMP", Calendar.getInstance().getTimeInMillis());
        mapContext.set("DAY", Calendar.getInstance());

        JexlEngine jexl = new JexlEngine();

        String innerExpression = "DATE";
        Expression e = jexl.createExpression(innerExpression);
        Object result = e.evaluate(mapContext);
        System.out.println(result);

        String timeStamp = "TIMESTAMP";
        e = jexl.createExpression(timeStamp);
        Object result2 = e.evaluate(mapContext);
        System.out.println(result2);

        String day = "TIMESTAMP";
        e = jexl.createExpression(timeStamp);
        Object result3 = e.evaluate(mapContext);
        System.out.println(result3);

        String evealute = "1+1+1";
        e = jexl.createExpression(evealute);
        Object result4 = e.evaluate(mapContext);
        System.out.println(result4);
    }
}
