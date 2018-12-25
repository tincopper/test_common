package com.tomgs.test.zookeeper;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.junit.Test;

/**
 * curator客户端测试类
 * @author tangzhongyuan
 *
 */
public class TestCuratorfFrameWork {
	
	private final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
	private final CuratorClient client = new CuratorClient();
	private final CuratorFramework curator = client.getCuratorFramework(connectString);
	
	@Test
	public void testConnectionIsSuccess() {
		//CuratorFramework curator = client.getCuratorFramework(connectString);
		//判断连接状态
		CuratorFrameworkState state = curator.getState();
		Assert.assertTrue("连接失败...", state == CuratorFrameworkState.STARTED);
	}
	
	@Test
	public void testCreateNodePath() throws Exception {
		
		//创建节点
		String pathInfo = curator.create().creatingParentsIfNeeded()
			.withMode(CreateMode.PERSISTENT)//节点持久化类型
			.withACL(Ids.OPEN_ACL_UNSAFE)//节点安全
			.inBackground(new BackgroundCallback() {//节点回调
				@Override
				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                    System.out.println("Code：" + event.getResultCode());  
                    System.out.println("Type：" + event.getType());  
                    System.out.println("Path：" + event.getPath());  
				}
			})
			.forPath("/root/children1", "测试".getBytes());//需要创建的节点路径
		
		Thread.sleep(3000);//休息3s方便看到回调
		System.out.println(pathInfo);//pathInfo 如果第一次创建则返回创建的节点信息，如果已经创建则返回null
	}
	
	@Test
	public void testGetNodePath() throws Exception {
		//查询节点
		List<String> path = curator.getChildren().forPath("/");//查询根节点
		System.out.println(path);
		path = curator.getChildren().forPath("/root");//获取子节点信息
		System.out.println(path);//获取子节点信息
	}
	
	@Test
	public void testGetNodeData() throws Exception {
		//获取节点数据
		curator.sync();//获取最新数据
		byte[] pathData = curator.getData().forPath("/root/children1");
		System.out.println("节点数据：" + new String(pathData));
	}
	
	@Test
	public void testCheckNodeExists() throws Exception {
		//检查节点是否存在
		Stat stat = curator.checkExists().forPath("/root/children1");
		System.out.println(stat);
	}
	
	@Test
	public void testUpdateNodeData() throws Exception {
		//修改节点数据
		Stat stat = curator.setData().forPath("/root/children1", "修改内容".getBytes());
		System.out.println(stat);
		
		//修改节点，没有修改的方法，只能先删除再新建，或者直接新建
		/*CuratorTransaction transaction = newClient.inTransaction();//开启事物
		Collection<CuratorTransactionResult> commit = transaction.check().forPath("/root/children1")
				//.and().delete().forPath("/root/children1")
				.and().create().forPath("/root/children2")
				.and().commit();
		Iterator<CuratorTransactionResult> iterator = commit.iterator();
		for (;iterator.hasNext();) {
			CuratorTransactionResult result = iterator.next();
			System.out.println("Type: " + result.getType());
			System.out.println("ForPath: " + result.getForPath());
			System.out.println("ResultPath: " + result.getResultPath());
			System.out.println("ResultStat: " + result.getResultStat());
		}*/
		
	}
	
	@Test
	public void testDeleteNodePath() throws Exception {
		//删除节点
		/* guaranteed()方法：
		 * 解决这个边界情况：由于连接问题，删除节点可能会失败。 此外，如果节点是短暂的，节点将不会自动删除，因为会话仍然有效。 这可能会对锁定实现造成严重破坏。
		 * 如果设置了保证，Curator将记录失败的节点删除并尝试在后台删除它们直到成功。
		 * 注意：删除失败后，您仍然会收到异常。 但是，您可以放心，只要org.apache.curator.framework.CuratorFramework实例处于打开状态，就会尝试删除该节点。
		 * 
		 * deletingChildrenIfNeeded() 方法表示如果存在子节点的话，同时删除子节点 
		 */
		curator.delete().guaranteed().deletingChildrenIfNeeded().inBackground(new CuratorDeleteBackgroundCallBack()).forPath("/root/children2");
		//关闭连接
		curator.close();
	}
}
