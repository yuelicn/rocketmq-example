package org.apache.mqtt.example.scheduled;

import java.util.List;

import org.apache.mqtt.example.connection.RocketMQPushConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

public class ScheduledConsumer {

	public static void main(String[] args) throws MQClientException {
		
		DefaultMQPushConsumer consumer = RocketMQPushConsumer.newInstance();
		consumer.setConsumerGroup("scheduled");
		consumer.subscribe("scheduled-topic", "*");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.println("msgs size = " + msgs.size());
				 for (MessageExt message : msgs) {
                     // Print approximate delay time period
                     System.out.println("Receive message[msgId=" + message.getMsgId() + "] "
                             + (System.currentTimeMillis() - message.getStoreTimestamp()) + "ms later");
                 }
                 return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
             }
         });
         // Launch consumer
         consumer.start();
	}

}
