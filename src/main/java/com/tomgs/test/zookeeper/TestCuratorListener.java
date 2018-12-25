package com.tomgs.test.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

/**
 * curator监听器测试
 * @author tangzhongyuan
 *
 */
public class TestCuratorListener {
	
	/*private final static String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";*/
	private final static String connectString = "127.0.0.1:2181";
	private final static CuratorClient client = new CuratorClient();
	private final static CuratorFramework curator = client.getCuratorFramework(connectString);
	
	/**
	 * NodeCache：监听节点的新增、修改操作，删除操作不会监听
	 * 
	 */
	@Test
	public void testNodeCache() throws Exception {
		String path = "/test";
		final NodeCache cache = new NodeCache(curator, path, false);
		cache.start(true);
		//只会监听节点的创建和修改，删除不会监听  
		cache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				 System.out.println("路径：" + cache.getCurrentData().getPath());  
		         System.out.println("数据：" + new String(cache.getCurrentData().getData()));  
		         System.out.println("状态：" + cache.getCurrentData().getStat());
			}
		});
		
		if (curator.checkExists().forPath(path) == null) {
			System.out.println("********* 新增节点");
			String result = curator.create().creatingParentsIfNeeded().forPath(path, "测试".getBytes());
			System.out.println(result);
			Thread.sleep(3000);
		}
		
		System.out.println("********* 修改节点数据");
		curator.setData().forPath(path, "test".getBytes());
		Thread.sleep(3000);
		
		System.out.println("********* 删除节点");
		
		curator.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
		Thread.sleep(5000);
		
		cache.close();
		curator.close();
	}
	
	/**
	 * PathChildrenCache：监听子节点的新增、修改、删除操作。
	 */
	@Test
	public void testPathChildrenCache() throws Exception {
		String path = "/test2";
		//第三个参数cacheData如果为true，则除了统计信息外，还会缓存节点内容
		PathChildrenCache cache = new PathChildrenCache(curator, path, true);
		/** 
         *  如果不填写这个参数，则无法监听到子节点的数据更新 
         *	如果参数为StartMode.BUILD_INITIAL_CACHE，则会预先创建之前指定的/test2节点 
         *	如果参数为StartMode.POST_INITIALIZED_EVENT，效果与BUILD_INITIAL_CACHE相同，只是不会预先创建/test2节点 
         *	如果参数为StartMode.NORMAL时，与不填写参数是同样的效果，不会监听子节点的数据更新操作 
         */  
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
					throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED: //新增子节点
					System.out.println("CHILD_ADDED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +  
		                    new String(event.getData().getData()) + "，状态：" + event.getData().getStat()); 
					break;
				case CHILD_UPDATED: //修改子节点
					System.out.println("CHILD_UPDATED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +  
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
					break;
				case CHILD_REMOVED: //删除子节点
					 System.out.println("CHILD_REMOVED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +  
	                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
					break;
				default:
					break;
				}
			}
		});
		/*
		if (curator.checkExists().forPath(path) == null) {
			String result = curator.create().forPath(path, "测试2".getBytes());
			System.out.println(result);
			Thread.sleep(3000);
		}
		*/
		System.out.println("********* 新增节点数据");
		curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path + "/children1", "子节点数据".getBytes());
		Thread.sleep(3000);
		
		//经测试，不会监听到本节点的数据变更，只会监听到指定节点下子节点数据的变更  
		System.out.println("********* 修改父节点数据");
        curator.setData().forPath(path, "456".getBytes());
        Thread.sleep(3000);
        
        System.out.println("********* 修改子节点数据");
        curator.setData().forPath(path + "/children1", "c1新内容".getBytes());
        Thread.sleep(3000);
        
        System.out.println("********* 删除节点");
        curator.delete().guaranteed().deletingChildrenIfNeeded().forPath(path + "/children1");
        Thread.sleep(3000);
        
		cache.close();
		curator.close();
	}
	
	/**
	 * TreeCache：既可以监听节点的状态，又可以监听子节点的状态
	 * @throws Exception 
	 */
	@Test
	public void testTreeCache() throws Exception {
		
		Builder builder = CuratorFrameworkFactory.builder().connectString(connectString)
				.retryPolicy(new ExponentialBackoffRetry(1500, 3));
		builder.sessionTimeoutMs(6000);
		builder.connectionTimeoutMs(6000);
		CuratorFramework client = builder.build();
		//client.getConnectionStateListenable().addListener(new ZkConnectionStateListenerImpl());
		client.start();
		
		//String path = "/test3" + "/children1" + "/children1-1";
        String path = "/test3/children1/children1-1";
		TreeCache cache = new TreeCache(client, path);
		cache.start();
		cache.getListenable().addListener(new TreeCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event)
					throws Exception {
				switch (event.getType()) {
				case NODE_ADDED: //新增节点
					System.out.println("NODE_ADDED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +  
		                    event.getData());
					break;
				case NODE_UPDATED: //修改节点
					System.out.println("NODE_UPDATED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            event.getData());
					break;
				case NODE_REMOVED: //删除节点
					 System.out.println("NODE_REMOVED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                             event.getData());
					break;
				default:
					System.out.println("---->" + event.getType());
					break;
				}
			}
		});
		
		//cache.close();
		
		/*if (curator.checkExists().forPath(path) == null) {
			System.out.println("********* 创建父节点");
			curator.create().forPath(path);
			Thread.sleep(1000);
		}
		
		System.out.println("********* 创建子节点");
		curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path + "/children1", "子节点数据".getBytes());
		Thread.sleep(1000);
		//curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path + "/children2", "子节点数据".getBytes());
		//Thread.sleep(3000);
		
		System.out.println("********* 创建子节点的子节点");
		curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path + "/children1" + "/children1-1", "子节点数据".getBytes());
		Thread.sleep(1000);
		
		//System.out.println("********* 修改根节点数据");
        //curator.setData().forPath(path, "456".getBytes());
        //Thread.sleep(3000);
        
        System.out.println("********* 修改子节点数据");
        curator.setData().forPath(path + "/children1", "c1新内容".getBytes());
        Thread.sleep(3000);
        
        System.out.println("********* 修改子子节点数据");
        curator.setData().forPath(path + "/children1" + "/children1-1", "c1新内容".getBytes());
        Thread.sleep(1000);*/
        /*
         *这样删除不会触发监听
        System.out.println("********* 删除节点");
        curator.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(3000);
        */

        if (curator.checkExists().forPath(path) == null) {
            System.out.println("********* 创建父节点");
            curator.create().forPath(path);
            Thread.sleep(1000);
        }

        System.out.println("********* 删除子子节点");
        String childNode = "/test3" + "/children1" + "/children1-1";
        curator.delete().forPath(childNode);
        curator.sync();
        Thread.sleep(1000);
        
        System.out.println("********* 删除子节点");
        curator.delete().forPath(path + "/children1");
        curator.sync();
        Thread.sleep(1000);
        
        System.out.println("********* 删除根节点");
        curator.delete().forPath(path);
        curator.sync();
        Thread.sleep(1000);
		
        System.in.read();
        
		//cache.close();
		//curator.close();
	}

	@Test
	public void test11() throws Exception {
		TreeCache cache = TreeCache.newBuilder(curator, "/ejob/session/test_bigdata_executor/127.0.0.1:8088").build();
		cache.getListenable().addListener(new TreeCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event)
					throws Exception {
				switch (event.getType()) {
				case NODE_ADDED: //新增节点
					System.out.println("NODE_ADDED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            event.getData());
					break;
				case NODE_UPDATED: //修改节点
					System.out.println("NODE_UPDATED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            event.getData());
					break;
				case NODE_REMOVED: //删除节点
					 System.out.println("NODE_REMOVED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                             event.getData());
					break;
				default:
					System.out.println("---->" + event.getType());
					break;
				}
			}
		});
        cache.start();

        System.in.read();
	}
}
