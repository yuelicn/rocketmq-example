package org.apache.mqtt.example.connection;

import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

public class RocketProducer {

	private RocketProducer() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static class singleton {
		
		private static  DefaultMQProducer producer = new DefaultMQProducer();
		static {
			producer.resetClientConfig(RocketClientConfig.newInstance());
			//在发送消息时，自动创建服务器不存在的topic，默认创建的队列数 默认值 4
			producer.setDefaultTopicQueueNums(4);
			//发送消息超时时间，单位毫秒 : 默认值 10000
			producer.setSendMsgTimeout(10000);
			// 消息Body超过多大开始压缩（Consumer收到消息会自动解压缩）,单位字节 默认值 4096
			producer.setCompressMsgBodyOverHowmuch(4096);
			//如果发送消息返回sendResult，但是sendStatus!=SEND_OK,是否重试发送 默认值 FALSE
			producer.setRetryAnotherBrokerWhenNotStoreOK(false);
		}
		private static DefaultMQProducer restConfig(ClientConfig config) {
			producer.resetClientConfig(config);
			return producer;
		}
	}
	
	public static DefaultMQProducer newInstance() {
		return RocketProducer.singleton.producer;
	}

	public static DefaultMQProducer restConfigInstance(ClientConfig config) {
		return RocketProducer.singleton.restConfig(config);
	}
}
