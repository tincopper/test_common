package com.tomgs.test.cli;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.input.CharSequenceReader;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static joptsimple.util.DateConverter.datePattern;
import static joptsimple.util.RegexMatcher.regex;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author tangzhongyuan
 * @create 2018-11-29 12:24
 **/
public class CliTest {

    @Test
    public void test1() {
        OptionParser parser = new OptionParser("aB?*.");
        OptionSet options = parser.parse("-a", "-B", "-?");

        System.out.println(options.has("a"));
        System.out.println(options.has("B"));
        System.out.println(options.has("?"));
        System.out.println(options.has("."));
    }

    @Test
    public void test2() {
        OptionParser parser = new OptionParser();
        parser.accepts("Dxms").withOptionalArg().ofType(Integer.class);
        parser.accepts("Dxmx").withRequiredArg().withValuesConvertedBy(regex("\\d{2}G"));

        OptionSet xms = parser.parse("-Dxms=500");
        OptionSet xmx = parser.parse("-Dxmx=10G");

        assertTrue(xms.hasArgument("Dxms"));
        List<?> dxms = xms.valuesOf("Dxms");
        assertEquals(500, dxms.get(0));

        System.out.println(xmx.valuesOf("Dxmx"));
    }

    @Test
    public void usesConvertersOnOptionArgumentsWhenTold() {
        OptionParser parser = new OptionParser();
        parser.accepts("birthdate").withRequiredArg().withValuesConvertedBy(datePattern("MM/dd/yy"));
        parser.accepts("ssn").withRequiredArg().withValuesConvertedBy(regex("\\d{3}-\\d{2}-\\d{4}"));

        OptionSet options = parser.parse("--birthdate", "02/24/05", "--ssn", "123-45-6789");

        System.out.println(options.valueOf("birthdate"));
        //assertEquals(LocalDate.of(2005, 2, 24), options.valueOf("birthdate"));
        assertEquals("123-45-6789", options.valueOf("ssn"));
    }

    @Test
    public void test3() {
        OptionParser parser = new OptionParser("D::");
        OptionSet options = parser.parse("-Dxmx=123G", "-Dxms=123M", "-Dcommand=`sh start.sh arg1 arg2`");

        System.out.println(options.has("D"));
        System.out.println(options.valuesOf("D"));
        List<?> lists = options.valuesOf("D");

        String pattern = "(\\w*|\\W*)=([\\w*|\\s*|\\W*]+)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        for (Object list : lists) {
            String args = String.valueOf(list);
            Matcher m = r.matcher(args);
            if (m.find()) {
                String group1 = m.group(1);
                String group2 = m.group(2);
                System.out.println(group1 + "--" + group2);
            }
        }
    }

    @Test
    public void test4() {
        String vms = "-Djob.class=com.tomgs.test.common.TestForClass -Dxmx=32M -Dxms=12M -Djvm.args=-Xmx=123M -Dcommand=sh start.sh arg1 arg2 -Dhive.script=test.sql";
        Map<String, String> argsMap = getArgsMap(vms);
        System.out.println(argsMap);
        System.out.println(argsMap.get("job.class"));
        System.out.println(argsMap.get("jvm.args"));
    }

    private Map getArgsMap(String options) {
        Map<String, String> map = new HashMap<>();
        String[] split = options.split("-D");
        String pattern = "([\\w*|\\W*]+)=([\\w*|\\s*|\\W*]+)";
        Pattern r = Pattern.compile(pattern);

        for (String s : split) {
            Matcher m = r.matcher(s);
            if (m.find()) {
                String group1 = m.group(1).trim();
                String group2 = m.group(2).trim();
                map.put(group1, group2);
            }
        }
        return map;
    }

    @Test
    public void test5() {
        String test = "jvm.args=-Dcommand=sh start.sh a b c";

        String[] split = test.split("=", 2);
        System.out.println(split[0]);
        System.out.println(split[1]);
    }

    @Test
    public void test6() {
        String vms = "-Djob.class=com.tomgs.test.common.TestForClass -DXmx=32M -DXms=12M -Djvm.args=-Xmx=123M -Dcommand=sh start.sh arg1 arg2 -Dhive.script=test.sql";
        String[] split = vms.split("-D");
        for (String s : split) {
            if (StringUtils.isBlank(s)) {
                continue;
            }
            //System.out.println(s);
            System.out.println("------------------");
            String[] properties = s.split("[=]", 2);
            String key = properties[0];
            String value = properties[1];
            System.out.println("key:" + key + ", value:" + value);
        }
    }

    @Test
    public void test7() throws IOException {
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream("E:\\workspace\\idea\\test-common\\src\\main\\java\\com\\tomgs\\test\\cli\\test.properties");
        properties.load(inputStream);
        String property = properties.getProperty("jvm.args");
        System.out.println(property);
    }

    @Test
    public void test8() throws IOException {
        Properties properties = new Properties();
        String vms = "-Djob.class=com.tomgs.test.common.TestForClass -DXmx=32M -DXms=12M -Djvm.args='-Xmx=123M -Dtest=123' -Dcommand=sh start.sh arg1 arg2 -Dhive.script=test.sql";
        vms = vms.replace("-D", "\n");
        CharSequenceReader reader = new CharSequenceReader(vms);
        properties.load(reader);

        String property = properties.getProperty("job.class");
        System.out.println(property);
    }
}
