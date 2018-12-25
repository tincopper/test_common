package com.tomgs.test.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.junit.Test;

/**
 * zookeeper原生客户端测试类
 * @author tangzhongyuan
 *
 */
public class TestZookeeper {
	
	private final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
	private final ZKConnection conn = new ZKConnection();
	
	@Test
	public void testZKConnection() throws IOException, InterruptedException {
		ZooKeeper zk = conn.getConnection(connectString);
		Assert.assertTrue(zk.getState().isConnected());
	}
	
	@Test
	public void testCreateNode() throws Exception {
		ZooKeeper zk = conn.getConnection(connectString);
		
		zk.register(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("=========" + event);
			}
		});
		
		final String path = "/pathRoot";
		byte[] data = "test".getBytes();
		String result;
		if (zk.exists(path, false) == null) {
			//创建根节点
			result = zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println(result);
		}
		//System.out.println("节点[" + path + "]已存在...");
		Stat stat = zk.exists(path, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println(path + "节点监听器：" + event.getType());
			}
		});
		System.out.println(stat);
		//创建子节点
		final String childrenNode = path + "/childrenPath";
		if (zk.exists(childrenNode, false) == null) {
			result = zk.create(childrenNode, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println(result);
		}
		//检查节点是否存在
		stat = zk.exists(childrenNode, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println(childrenNode + "节点监听器1：" + event.getType());
			}
		});
		System.out.println(stat);
		//设置节点数据
		stat = zk.setData(childrenNode, data, -1);
		System.out.println(stat);
		//删除节点
		zk.delete(childrenNode, -1);
		//关闭连接
		zk.close();
	}
}
