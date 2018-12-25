package com.tomgs.test.enum_test;

import com.mchange.v2.codegen.bean.PropsToStringGeneratorExtension;

/**
 * @author chengchuantuo
 * @description: this is a class .
 * @date 2018-11-07 10:49
 **/
//@RequiredArgsConstructor
//@Getter
public enum JobTypeEnum {

    SHELL(0, "shell"),

    PHP(1, "php"),

    JAVA(2, "java"),

    HADOOP_JAVA(3, "hadoopJava"),

    HADOOP_ASYNC_JAVA(4, "hadoopAsyncJava"),

    COMMAND(5, "command"),

    HADOOP_SHELL(6, "hadoopShell"),

    SPARK(7, "spark"),

    ASYNC_SPARK(8, "asyncSpark"),

    HIVE(9, "hive");

    private final int code;

    private final String name;

    JobTypeEnum(int i, String typeName) {
        this.code = i;
        this.name = typeName;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        System.out.println(JobTypeEnum.ASYNC_SPARK);
    }
}
