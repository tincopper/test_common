package com.tomgs.test.zookeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.junit.Test;

/**
 * 测试zk选举功能
 * @author tangzhongyuan
 */
public class TestSelectLeader {
	
	private final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
	private final CuratorClient client = new CuratorClient();
	private final CuratorFramework curator = client.getCuratorFramework(connectString);
	
	@Test
	public void test() throws InterruptedException, IOException {
		String latchPath = "/test/master";
		LeaderLatch latch = new LeaderLatch(curator, latchPath, "127.0.0.1");
		try {
			latch.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		latch.addListener(new LeaderLatchListener() {

			@Override
			public void isLeader() {
				System.out.println("isLeader...");
			}

			@Override
			public void notLeader() {
				System.out.println("notLeader...");
			}
		});
		
		Thread.sleep(3000);
		
		//latch.close();
	}
	
	@Test
	public void test2() throws Exception {
		String latchPath = "/test/master";
		int count = 10;
		
		List<CuratorFramework> curators = new ArrayList<CuratorFramework>();
		List<LeaderLatch> latchs = new ArrayList<LeaderLatch>();
		TestingServer server = new TestingServer();
		
		for (int i = 0; i < count; i++) {
			CuratorFramework newClient = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(500, 3));
			LeaderLatch latch = new LeaderLatch(newClient, latchPath, "Client:" + i);
			
			curators.add(newClient);
			latchs.add(latch);
			
			newClient.start();
			latch.start();
		}
		
		Thread.sleep(3000);
		
		try {
			LeaderLatch currentLeader = null;
			for (int i = 0; i < count; i++) {
				LeaderLatch latch = latchs.get(i);
				if (latch.hasLeadership()) {
					currentLeader = latch;
				}
			}
			System.out.println(currentLeader.getId() + " is leader ...");
			//关闭此leader节点
			System.out.println("release the leader " + currentLeader.getId());
			currentLeader.close();
			//重新选举
			latchs.get(0).await(2, TimeUnit.SECONDS);
			System.out.println("Client #0 maybe is elected as the leader or not although it want to be");
			System.out.println("but the new leader is " + latchs.get(0).getLeader().getId());
			System.out.println("but the new leader is " + latchs.get(3).getLeader().getId());
			
			System.out.println("Press enter/return to quit\n");
			new BufferedReader(new InputStreamReader(System.in)).readLine();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Shutting down...");
			for (LeaderLatch latch : latchs) {
				CloseableUtils.closeQuietly(latch);
			}
			for (CuratorFramework client : curators) {
				CloseableUtils.closeQuietly(client);
			}
			CloseableUtils.closeQuietly(server);
		}
	}
}
