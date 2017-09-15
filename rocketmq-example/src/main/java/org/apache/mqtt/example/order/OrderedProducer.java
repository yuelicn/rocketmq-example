package org.apache.mqtt.example.order;

import java.util.List;

import org.apache.mqtt.example.connection.RocketProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class OrderedProducer {
	public static void main(String[] args) throws Exception {
		DefaultMQProducer producer = RocketProducer.newInstance();
		producer.setProducerGroup("consumer-group-order");
		producer.start();
		String[] tags = new String[] { "TagA", "TagC", "TagD" };
		for (int i = 0; i < 20; i++) {
			// orderId 可以根据自己的业务来定义、 可以是订单id 或者 商户id 等、 相同的id 会有序的分到一个quenen
			int orderId = i % 2;
			long time = System.currentTimeMillis();
			// Create a message instance, specifying topic, tag and message
			// body.
			Message msg = new Message("TopicTest111", tags[i % tags.length], "KEY" + i,
					("Hello RocketMQ " + time).getBytes(RemotingHelper.DEFAULT_CHARSET));
			SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
				@Override
				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
					Integer id = (Integer) arg;
					int index = id % mqs.size();
					return mqs.get(index);
				}
			}, orderId);

			System.out.printf("%s%n", sendResult);
		}
		// server shutdown
		producer.shutdown();
	}
}
