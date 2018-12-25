package com.tomgs.test.file;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tangzhongyuan
 * @create 2018-12-17 11:24
 **/
public class CleanFileThread extends Thread {

    private final Object executionDirDeletionSync = new Object();
    private String workDir = "F:\\test\\ejob-bigdata-test\\batch_add_job";
    final ScheduledExecutorService schedule;

    public CleanFileThread() {
        schedule = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        schedule.scheduleAtFixedRate(new CleanFileThread(), 5,  6, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        File file = new File(workDir);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File dir : files) {
            long modified = dir.lastModified();
            long time = System.currentTimeMillis() - modified;
            long pastDay = (time) / (1000 * 60 * 60 * 24);
            if (pastDay < 3) {
                continue;
            }
            synchronized (this.executionDirDeletionSync) {
                /*try {
                    FileUtils.deleteDirectory(dir);
                } catch (final IOException e) {
                    //logger.error("Error cleaning execution dir " + exDir.getPath(), e);
                }*/
            }
        }
    }

    public static void main(String[] args) {
        CleanFileThread thread = new CleanFileThread();
        thread.start();
    }
}
