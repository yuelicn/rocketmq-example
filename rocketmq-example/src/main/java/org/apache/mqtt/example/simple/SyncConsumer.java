package org.apache.mqtt.example.simple;

import java.util.List;

import org.apache.mqtt.example.connection.RocketMQPushConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

public class SyncConsumer {
	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = RocketMQPushConsumer.newInstance();
		consumer.setConsumerGroup("sync");
		consumer.subscribe("TopicTest-sync", "TagA || TagC || TagD");

		consumer.registerMessageListener(new MessageListenerOrderly() {


			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				context.setAutoCommit(true);
				System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
				System.out.println(new String(msgs.get(0).getBody()));
				return ConsumeOrderlyStatus.SUCCESS;
			}
		});

		consumer.start();

		System.out.printf("Consumer Started.%n");
	}
}
