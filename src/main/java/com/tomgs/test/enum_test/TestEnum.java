package com.tomgs.test.enum_test;

/**
 * @author tangzhongyuan
 * @create 2018-12-13 12:13
 **/
public enum TestEnum {

    READY(10),
    PREPARING(20),
    RUNNING(30),
    PAUSED(40),
    SUCCEEDED(50),
    KILLING(55),
    KILLED(60),
    FAILED(70),
    FAILED_FINISHING(80),
    SKIPPED(90),
    DISABLED(100),
    QUEUED(110),
    FAILED_SUCCEEDED(120),
    CANCELLED(125);

    int status;

    TestEnum(final int status) {
        this.status = status;
    }

    public int getStatusValue() {
        return status;
    }

    public static void main(String[] args) {
        System.out.println(TestEnum.DISABLED);
        System.out.println(TestEnum.DISABLED.getStatusValue());
    }
}
