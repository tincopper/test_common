package com.tomgs.test.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author tangzhongyuan
 * @create 2018-12-24 14:30
 **/
public class FileStatistics {

    static int fileSums = 0;

    public static void main(String[] args) {
        FilenameFilter filenameFilter = (dir, name) -> name.endsWith("txt") || name.endsWith("xlsx");

        String filePath = "C:\\Users\\tangzhongyuan\\Desktop\\otter工单";
        filePath = "F:\\otter工单\\test";
        File fileDir = new File(filePath);
        File[] files = fileDir.listFiles();
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                File[] childFiles = childFile.listFiles(filenameFilter);
                fileSums += childFiles.length;
            }
        }

        System.out.println("文件个数：" + fileSums);
    }
}
