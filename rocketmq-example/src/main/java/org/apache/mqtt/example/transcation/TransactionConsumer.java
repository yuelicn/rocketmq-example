package org.apache.mqtt.example.transcation;

import java.util.List;

import org.apache.mqtt.example.connection.RocketMQPushConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

public class TransactionConsumer {

	public static void main(String[] args) throws MQClientException {
		DefaultMQPushConsumer consumer = RocketMQPushConsumer.newInstance();
		consumer.setConsumerGroup("transaction");
		consumer.subscribe("topicTest-t", "TagA || TagB || TagC || TagD || TagE");
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.printf(Thread.currentThread().getName() + " consumerB Receive New Messages: " + msgs + "%n");
				System.out.printf( "consumerB Receive New Messages: body " + new String(msgs.get(0).getBody()) + "%n");
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.out.printf("Broadcast ConsumerB Started.%n");
	}

}
