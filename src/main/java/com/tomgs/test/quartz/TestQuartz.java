package com.tomgs.test.quartz;

import org.quartz.Calendar;
import org.quartz.CronExpression;
import org.quartz.TriggerUtils;
import org.quartz.impl.calendar.CronCalendar;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tangzhongyuan
 * @create 2018-10-25 14:56
 **/
public class TestQuartz {

    public static void main(String[] args) throws ParseException {

        if (null instanceof String) {
            System.out.println("---------");
        }

        String cronExpresiion = "0 0/10 * * * ?";
        List<String> nextExecTime = getNextExecTime(cronExpresiion, 3);
        for (String s : nextExecTime) {
            System.out.println("time:" + s);
        }

        String preExecTime = getPreExecTime(cronExpresiion);
        System.out.println(preExecTime);
    }

    /**
     *  获取最近numTimes次数的时间
     * @param cronExpression cron表达式
     * @param numTimes       下一(几)次运行的时间
     * @return
     */
    public static List<String> getNextExecTime(String cronExpression, Integer numTimes) {
        List<String> list = new ArrayList<>();
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        try {
            cronTriggerImpl.setCronExpression(cronExpression);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 这个是重点，一行代码搞定
        List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, numTimes);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date date : dates) {
            list.add(dateFormat.format(date));
        }
        return list;
    }

    public static String getPreExecTime(String cronExpression) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CronExpression cronExpression1 = new CronExpression(cronExpression);

        Date parse = dateFormat.parse("2018-10-25 19:34:00");
        Date nextValidTimeAfter = cronExpression1.getNextValidTimeAfter(parse);
        //如果nextValidTimeAfter大或者等于当前时间，说明是传入的执行时间是最近一次的执行时间
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();

        try {
            cronTriggerImpl.setCronExpression(cronExpression);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Date> dates = TriggerUtils.computeFireTimesBetween(cronTriggerImpl, null, new Date(), new Date());

        Date previousFireTime = cronTriggerImpl.getPreviousFireTime();

        String preExecTime = dateFormat.format(previousFireTime);

        return preExecTime;
    }
}
