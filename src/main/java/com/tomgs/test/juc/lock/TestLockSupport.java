package com.tomgs.test.juc.lock;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 工具的使用
 * 1. park
 * 2. unpark
 * <p>
 * 主要用途：
 * 　　当前线程需要唤醒另一个线程，但是只确定它会进入阻塞，但不确定它是否已经进入阻塞，因此不管是否已经进入阻塞，还是准备进入阻塞，都将发放一个通行准许。
 * <p>
 * 正确用法：
 * 　　把LockSupport视为一个sleep()来用，只是sleep()是定时唤醒，LockSupport既可以定时唤醒，也可以由其它线程唤醒。
 *
 * @author tangzhongyuan
 * @create 2018-09-06 16:37
 **/
public class TestLockSupport {

    /**
     * 子线程等待主线程发放许可
     */
    @Test
    public void test1() {
        Thread thread = new Thread() {
            public void run() {
                System.out.println("子线程 -> 测试通行许可！");
                LockSupport.park(); //阻塞直到调用upark
                //LockSupport.parkNanos(1000); //阻塞直到时间到了或者调用了unpark
                System.out.println("子线程 -> 已通行！");
            }
        };
        thread.start();

        System.out.println("主线程 -> 休眠1秒！");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程 -> 发放通行许可于子线程！");
        LockSupport.unpark(thread);

        /*
            运行结果：
            主线程 -> 休眠1秒！
            子线程 -> 测试通行许可！
            主线程 -> 发放通行许可于子线程！
            子线程 -> 已通行！
         */
    }

    /**
     * 主线程提前发放许可给子线程<br/>
     * PS: 不管提前发放多少次，只用于一次性使用
     */
    @Test
    public void test2() {
        Thread thread = new Thread() {
            public void run() {
                System.out.println("子线程 -> 休眠1秒！");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("子线程 -> 测试通行许可！");
                LockSupport.park();
                System.out.println("子线程 -> 已通行！");
            }
        };
        thread.start();

        System.out.println("主线程 -> 提前发放通行许可于子线程！");
        LockSupport.unpark(thread);

        /*
            运行结果：
            主线程 -> 提前发放通行许可于子线程！
            子线程 -> 休眠1秒！
            子线程 -> 测试通行许可！
            子线程 -> 已通行！
         */
    }

    /**
     * 子线程传递数据给主线程
     */
    @Test
    public void test3() {
        Thread thread = new Thread() {
            public void run() {
                System.out.println("子线程 -> 测试通行许可！并提供通行证：A");
                LockSupport.park(new String("A"));
                System.out.println("子线程 -> 已通行！");
            }
        };
        thread.start();

        System.out.println("主线程 -> 休眠1秒！");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程 -> 检查并处理子线程的通行证：" + LockSupport.getBlocker(thread));
        System.out.println("主线程 -> 许可子线程通行！");
        LockSupport.unpark(thread);

        /*
            运行结果：
            主线程 -> 休眠1秒！
            子线程 -> 测试通行许可！并提供通行证：A
            主线程 -> 检查并处理子线程的通行证：A
            主线程 -> 许可子线程通行！
            子线程 -> 已通行！
         */
    }

}
