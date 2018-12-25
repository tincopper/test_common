package com.tomgs.test.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 使用原生客户端练习
 * @author tangzhongyuan
 *
 */
public class ZKConnection {
	
	private final CountDownLatch connectedSingal = new CountDownLatch(1);
	private ThreadLocal<ZooKeeper> zooKeeperLocal = new ThreadLocal<ZooKeeper>();
	
	/**
	 * 获取连接
	 * @param connectString
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public ZooKeeper getConnection(final String connectString) throws IOException, InterruptedException {
		int timeout = 4000;
		ZooKeeper zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				System.out.println(event.getPath() + "-->" + event.getType());
				if (event.getState() == KeeperState.SyncConnected) {//说明成功连接
					System.out.println("连接 [" + connectString + "] 成功...");
					connectedSingal.countDown();
				}
			}
		});
		if (!connectedSingal.await(timeout, TimeUnit.MILLISECONDS)) {
			System.out.println("连接 [" + connectString + "] 超时.....");
		}
		
		zooKeeperLocal.set(zooKeeper);
		return zooKeeperLocal.get();
	}
	
	/**
	 * 关闭连接
	 * @throws InterruptedException
	 */
	public void close() throws InterruptedException {
		if (zooKeeperLocal.get() != null) {
			try {
				zooKeeperLocal.get().close();
			} finally {
				zooKeeperLocal.remove();
			}
		}
	}
}
