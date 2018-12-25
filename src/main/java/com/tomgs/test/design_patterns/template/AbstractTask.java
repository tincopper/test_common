package com.tomgs.test.design_patterns.template;

import java.util.Map;

/**
 * @author tangzhongyuan
 * @create 2018-11-15 11:27
 **/
public abstract class AbstractTask implements Task {

    public abstract void preTaskExecute(Map<String, String> taskContext);

    public abstract void taskExecute(Map<String, String> taskContext);

    public abstract void postTaskExecute(Map<String, String> taskContext);

    @Override
    public void execute(Map<String, String> taskContext) {
        //do task class pre execute self logic
        //这里也可对这个context进行转换缩小对子类的功能范围
        preTaskExecute(taskContext);
        //do task exe self logic
        taskExecute(taskContext);
        //do task class post execute self logic
        postTaskExecute(taskContext);
    }
}
