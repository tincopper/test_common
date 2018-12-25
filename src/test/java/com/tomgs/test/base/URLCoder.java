package com.tomgs.test.base;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author tangzhongyuan
 * @create 2018-11-24 10:03
 **/
public class URLCoder {

    @Test
    public void testURLEncoder() throws UnsupportedEncodingException {
        String groupName = "tomgs_java_group";
        String jobName = "job名称*&#@.php";

        String encodeGroupName = URLEncoder.encode(groupName, "UTF-8");
        String encodeJobName = URLEncoder.encode(jobName, "UTF-8");

        System.out.println(encodeGroupName);
        System.out.println(encodeJobName);

        String decodeGroupName = URLDecoder.decode(encodeGroupName, "UTF-8");
        String decodeJobName = URLDecoder.decode(encodeJobName, "UTF-8");

        System.out.println(decodeGroupName);
        System.out.println(decodeJobName);
    }

    @Test
    public void testURLDecoder() throws UnsupportedEncodingException {
        String groupName = "group名字";
        String jobName = "job";

        String decodeGroupName = URLDecoder.decode(groupName, "UTF-8");
        String decodeJobName = URLDecoder.decode(jobName, "UTF-8");

        System.out.println(decodeGroupName);
        System.out.println(decodeJobName);
    }

    @Test
    public void testFormatURL() {
        String value = "/api/job/pause/%s/%s";
        String test = formatURL(value, "tomgs_java_group", "job名称*&#@");
        System.out.println(test);
    }

    public String formatURL(String value, Object... args) {

        for (int i = 0; i < args.length; i++) {
            String arg = String.valueOf(args[i]);
            try {
                String encodeArg = URLEncoder.encode(arg, "UTF-8");
                args[i] = encodeArg;
            } catch (UnsupportedEncodingException e) {
            }
        }
        return String.format(value, args);
    }
}
