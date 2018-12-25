package com.tomgs.test.zookeeper;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

public class TestDistributedLock {
	
	private final static String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
	/*private final static String connectString = "127.0.0.1:2181";*/
	private final static CuratorClient client = new CuratorClient();
	private final static CuratorFramework curator = client.getCuratorFramework(connectString);

	InterProcessMutex lock = new InterProcessMutex(curator, "/root/lock");
	
	@Test
	public void test() throws Exception {
		if (lock.acquire(3000L, TimeUnit.SECONDS)) {
			try {
				// do some business
				System.out.println("lock sucess ...");
			} finally {
				lock.release();
			}
		}
	}
}
