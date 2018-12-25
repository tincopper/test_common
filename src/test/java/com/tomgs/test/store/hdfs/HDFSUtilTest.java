package com.tomgs.test.store.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class HDFSUtilTest {

    // HDFS文件系统的操作对象
    FileSystem fileSystem = null;

    // 配置对象
    Configuration configuration = null;

    @Test
    public void mkdir() throws IOException {
        fileSystem.mkdirs(new Path("/user/edts/original/add/test/20180911/test/"));
        System.out.println("mkdir ");
    }

    @Test
    public void mkfile() throws IOException {
        FSDataOutputStream outputStream = fileSystem.create(new Path("/user/edts/original/add/test/20180913/test/add.txt"));
        // 写入一些内容到文件中
        outputStream.write("hello hadoop\n".getBytes());
        outputStream.flush();
        outputStream.close();
    }

    @Test
    public void append() throws IOException {
        byte[] b = new byte[10 * 1024 * 1024];
        long start = System.currentTimeMillis();
        FSDataOutputStream append = fileSystem.append(new Path("/user/edts/original/add/test/20180912/test/add.txt"));
        // 写入一些内容到文件中
        append.write(b);
        append.flush();
        append.close();

        System.out.println("=====" + (System.currentTimeMillis() - start));

        long start1 = System.currentTimeMillis();
        FSDataOutputStream append1 = fileSystem.append(new Path("/user/edts/original/add/test/20180912/test/add.txt"));
        // 写入一些内容到文件中
        append1.write(b);
        append1.flush();
        append1.close();

        System.out.println("=====" + (System.currentTimeMillis() - start1));

        long start2 = System.currentTimeMillis();
        FSDataOutputStream append2 = fileSystem.append(new Path("/user/edts/original/add/test/20180912/test/add.txt"));
        // 写入一些内容到文件中
        append2.write(b);
        append2.flush();
        append2.close();

        System.out.println("=====" + (System.currentTimeMillis() - start2));
    }

    @Test
    public void delete() throws IOException {
        boolean result = fileSystem.delete(new Path("/user/edts/original/add/test/"), true);
        System.out.println("delete : " + result);
    }

    @Test
    public void listAll() throws IOException {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/user/bigdata/edts/original/cold/test/20180928/xdual/cold.txt"));
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println("这是一个：" + (fileStatus.isDirectory() ? "文件夹" : "文件"));
            System.out.println("副本系数：" + fileStatus.getReplication());
            System.out.println("大小：" + fileStatus.getLen());
            System.out.println("路径：" + fileStatus.getPath() + "\n");
        }
    }

    @Test
    public void fileSize() {
        //fileSystem.
    }

    /**
     * 查看HDFS里某个文件的内容
     */
    @Test
    public void cat() throws Exception {
        System.out.print("cat ----" + System.getProperty("line.separator"));
        // 读取文件
        long start = System.currentTimeMillis();
        FSDataInputStream in = fileSystem.open(new Path("/user/bigdata/edts/original/cold/test/20180928/person/cold.txt"));
        System.out.println("----" + (System.currentTimeMillis() - start));

        // 将文件内容输出到控制台上，第三个参数表示输出多少字节的内容
        IOUtils.copyBytes(in, System.out, 1024);
        in.close();
    }

    /**
     * 重命名文件
     */
    @Test
    public void rename() throws Exception {
        Path oldPath = new Path("/hdfsapi/test/a.txt");
        Path newPath = new Path("/hdfsapi/test/b.txt");
        // 第一个参数是原文件的名称，第二个则是新的名称
        fileSystem.rename(oldPath, newPath);
    }

    /**
     * 上传本地文件到HDFS
     */
    @Test
    public void copyFromLocalFile() throws Exception {
        Path localPath = new Path("E:/local.txt");
        Path hdfsPath = new Path("/hdfsapi/test/");
        // 第一个参数是本地文件的路径，第二个则是HDFS的路径
        fileSystem.copyFromLocalFile(localPath, hdfsPath);
    }

    /**
     * 上传大体积的本地文件到HDFS，并显示进度条
     */
    @Test
    public void copyFromLocalFileWithProgress() throws Exception {
        InputStream in = new BufferedInputStream(new FileInputStream(new File("E:/Linux Install/mysql_cluster.iso")));
        FSDataOutputStream outputStream = fileSystem.create(new Path("/hdfsapi/test/mysql_cluster.iso"), new Progressable() {
            public void progress() {
                // 进度条的输出
                System.out.print(".");
            }
        });
        IOUtils.copyBytes(in, outputStream, 4096);
        in.close();
        outputStream.close();
    }

    /**
     * 下载HDFS文件1
     * 注意：以上演示的第一种下载方式在windows操作系统上可能会报空指针错误，在windows上建议使用第二种方式
     */
    @Test
    public void copyToLocalFile1() throws Exception {
        Path localPath = new Path("E:/b.txt");
        Path hdfsPath = new Path("/hdfsapi/test/b.txt");
        fileSystem.copyToLocalFile(hdfsPath, localPath);
    }

    /**
     * 下载HDFS文件2
     * 注意：以上演示的第一种下载方式在windows操作系统上可能会报空指针错误，在windows上建议使用第二种方式
     */
    @Test
    public void copyToLocalFile2() throws Exception {
        FSDataInputStream in = fileSystem.open(new Path("/hdfsapi/test/b.txt"));
        OutputStream outputStream = new FileOutputStream(new File("E:/b.txt"));
        IOUtils.copyBytes(in, outputStream, 1024);
        in.close();
        outputStream.close();
    }

    @Before
    public void setup() throws URISyntaxException, IOException, InterruptedException {
        configuration = new Configuration();
        //第一种方式：高可用方式core-site.xml
        /*configuration.set("fs.defaultFS", "hdfs://elog");
        configuration.set("dfs.nameservices", "elog");
        configuration.set("dfs.ha.namenodes.elog", "nn1,nn2");
        configuration.set("dfs.namenode.rpc-address.elog.nn1", "nn1.hadoop:9000");
        configuration.set("dfs.namenode.rpc-address.elog.nn2", "nn2.hadoop:9000");
        configuration.set("dfs.client.failover.proxy.provider.elog", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        fileSystem = FileSystem.get(configuration);*/

        // 第一参数是服务器的URI，第二个参数是配置对象，第三个参数是文件系统的用户名
        // HDFS文件系统服务器的地址以及端口
        //String HDFS_PATH = "hdfs://nn2.hadoop:9000";
        String HDFS_PATH = "hdfs://dwhdponline-master02.gw-ec.com:8020";
        fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, "bigdata");
        System.out.println("######## HDFSAPP.setUp");
    }

    @After
    public void shutdown() throws IOException {
        configuration = null;
        if (fileSystem != null) {
            fileSystem.close();
            fileSystem = null;
        }

        System.out.println("######## HDFSAPP.tearDown");
    }
}