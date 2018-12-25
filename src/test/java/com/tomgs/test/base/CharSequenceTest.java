package com.tomgs.test.base;

import java.util.UUID;

/**
 * @author tangzhongyuan
 * @create 2018-11-22 18:28
 **/
public class CharSequenceTest {

    public static void main(String[] args) {
        CharSequence cs = new String("abc");
        System.out.println(cs.toString());

        String test = "8a84848d6711b7c10167400b46953ce9";
        //String substring = test.substring(5);
        System.out.println(test);

        String result = "retry" + UUID.randomUUID().toString().substring(0, 8) + test.substring(13);
        System.out.println(result);
    }
}
