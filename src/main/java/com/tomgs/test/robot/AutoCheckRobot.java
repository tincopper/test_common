package com.tomgs.test.robot;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author tangzhongyuan
 * @create 2018-10-29 17:18
 **/
public class AutoCheckRobot {

    public static void main(String[] args) throws AWTException, IOException, DocumentException {
        Robot robot = new Robot();
        //设置Robot产生一个动作后的休眠时间,否则执行过快
        robot.setAutoDelay(1000);

        // 设置纸张大小
        Document document = new Document(PageSize.A4);

        if (args.length < 1) {
            throw new IllegalArgumentException("未添加输入参数...");
        }
        String commandPath = args[0];
        FileReader fr=new FileReader(commandPath);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split("\\s+");
            if (split.length <= 0) {
                continue;
            }
            String first = split[0];
            //注释
            if ("#".equalsIgnoreCase(first)) {
                continue;
            }
            //移动
            if ("mv".equalsIgnoreCase(first)) {
                move(robot, Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            }
            //左击
            if ("leftclick".equalsIgnoreCase(first)) {
                leftClick(robot);
            }
            //右击
            if ("rightclick".equalsIgnoreCase(first)) {
                rightClick(robot);
            }
            //sleep
            if ("sleep".equalsIgnoreCase(first)) {
                sleep(Integer.parseInt(split[1]));
            }
            //滚动 mouseWheel
            if ("mouseWheel".equalsIgnoreCase(first)) {
                robot.mouseWheel(Integer.parseInt(split[1]));
            }
            //截图
            if ("capture".equalsIgnoreCase(first)) {
                File file = new File(split[1] + ".png");
                //截取屏幕上(10,20)-(50,60)这个区域的图像的代码如下:
                capture(robot, file, Integer.parseInt(split[2]), Integer.parseInt(split[3]),
                        Integer.parseInt(split[4]), Integer.parseInt(split[5]));
            }
            //写数据
            if ("writehead".equalsIgnoreCase(first)) {
                writeHead(document, split[1], split[2]);
            }

            //写数据
            if ("writestr".equalsIgnoreCase(first)) {
                writeStr(document, split[1]);
            }

            if ("writeimage".equalsIgnoreCase(first)) {
                writeImage(document, split[1] + ".png");
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        document.close();

    }

    private static void writeImage(Document document, String imagePath) throws IOException, DocumentException {
        Image img = Image.getInstance(imagePath);
        img.setAbsolutePosition(0, 0);
        img.setAlignment(Image.LEFT);// 设置图片显示位置
        img.scaleAbsolute(500, 300);// 直接设定显示尺寸
        document.add(img);
    }

    public static void move(Robot robot, int x, int y) {
        robot.mouseMove(x, y);
    }

    public static void leftClick(Robot robot) {
        //点击鼠标
        //鼠标左键
        System.out.println("单击");
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void rightClick(Robot robot) {
        //鼠标右键
        System.out.println("右击");
        robot.mousePress(InputEvent.BUTTON3_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);

    }

    public static void sleep(int s) {
        try {
            Thread.sleep(s * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void pressKey(Robot robot, int keyCode) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }

    public static void capture(Robot robot, String fileName, int x, int y, int x2, int y2) {
        BufferedImage image = robot.createScreenCapture(new Rectangle(x, y, x2 - x, y2 - y));
        try {
            //保存截图
            File file = new File(fileName + ".png");
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void capture(Robot robot, File file, int x, int y, int x2, int y2) {
        BufferedImage image = robot.createScreenCapture(new Rectangle(x, y, x2 - x, y2 - y));
        try {
            //保存截图
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeHead(Document document, String outPath, String titleName) throws IOException, DocumentException {
        // 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File file = new File(outPath == null ? "D://report.doc" : outPath);
        RtfWriter2.getInstance(document, new FileOutputStream(file));
        document.open();

        // 设置中文字体
        BaseFont bfChinese = BaseFont.createFont(BaseFont.HELVETICA,
                BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

        // 标题字体风格
        com.lowagie.text.Font titleFont = new com.lowagie.text.Font(bfChinese, 12, com.lowagie.text.Font.BOLD);
        // // 正文字体风格
        com.lowagie.text.Font contextFont = new com.lowagie.text.Font(bfChinese, 10, com.lowagie.text.Font.NORMAL);
        Paragraph title = new Paragraph(titleName);
        // 设置标题格式对齐方式
        title.setAlignment(Element.ALIGN_CENTER);

        // title.setFont(titleFont);
        document.add(title);
    }

    public static void writeStr(Document document, String content) throws DocumentException {
        Paragraph context = new Paragraph(content + "\n");

        // 正文格式左对齐
        context.setAlignment(Element.ALIGN_LEFT);
        // context.setFont(contextFont);
        // 离上一段落（标题）空的行数
        context.setSpacingBefore(5);
        // 设置第一行空的列数
        context.setFirstLineIndent(20);
        document.add(context);
    }
}
