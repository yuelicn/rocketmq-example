package org.apache.mqtt.example.simple;

import org.apache.mqtt.example.connection.RocketProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
/**
 * 可靠异步消息类型：
 * 		使用场景：异步传输通常用于响应时间敏感的业务场景
 * @author yue.li3
 */
public class SyncProducer {

	public static void main(String[] args) throws Exception {
		// Instantiate with a producer group name.
		DefaultMQProducer producer = RocketProducer.newInstance();
		producer.setProducerGroup("sync");
		producer.setRetryTimesWhenSendAsyncFailed(0);
		producer.start();
		for (int i = 0; i < 5; i++) {
			final int index = i;
			// Create a message instance, specifying topic, tag and message
			// body.
			Message msg = new Message("TopicTest-sync", "TagA", "OrderID188",
					"Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
			producer.send(msg, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
					System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
				}

				@Override
				public void onException(Throwable e) {
					System.out.printf("%-10d Exception %s %n", index, e);
					e.printStackTrace();
				}
			});
		}
		// Shut down once the producer instance is not longer in use.
		producer.shutdown();
	}

}
