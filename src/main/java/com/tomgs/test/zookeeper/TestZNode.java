package com.tomgs.test.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;

public class TestZNode {
	
	private final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
	private final CuratorClient client = new CuratorClient();
	private final CuratorFramework curator = client.getCuratorFramework(connectString);
	
	@Test
	public void testUpdateNode() {
		
	}
}
