package org.apache.mqtt.example.connection;

import org.apache.rocketmq.client.ClientConfig;

public class RocketClientConfig {

	private RocketClientConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	// 单例模式 设置ClientConfig 的公共配置属性
	private static class Singleton {

		private static ClientConfig clientConfig = new ClientConfig();

		static {
			// 客户端本机 IP 地址，某些机器会发生无法识别客户端IP地址情况，需要应用在代码中强制指定
			//clientConfig.setClientIP("192.168.28.94");
			// Name Server 地址列表，多个 NameServer 地址用分号 隔开
			clientConfig.setNamesrvAddr(Config.NAME_SERVER_ADDRESS);
			// 客户端实例名称，客户端创建的多个 Producer、 Consumer 实际是共用一个内部实例（这个实例包含
			// 网络连接、线程资源等）,默认值:DEFAULT
			clientConfig.setInstanceName("DEFAULT");
			// 通信层异步回调线程数,默认值4
			clientConfig.setClientCallbackExecutorThreads(10);
			// 轮询 Name Server 间隔时间，单位毫秒,默认：30000
			clientConfig.setPollNameServerInterval(30000);
			// 向 Broker 发送心跳间隔时间，单位毫秒,默认：30000
			clientConfig.setHeartbeatBrokerInterval(30000);
			// 持久化 Consumer 消费进度间隔时间，单位毫秒,默认：5000
			clientConfig.setPersistConsumerOffsetInterval(10000);
		}
	}

	public static ClientConfig newInstance() {
		return Singleton.clientConfig;
	}
	
	public void rest(ClientConfig config) {
		Singleton.clientConfig.resetClientConfig(config);
	}

	@Override
	public String toString() {
		return Singleton.clientConfig.toString();
	}
	

}
