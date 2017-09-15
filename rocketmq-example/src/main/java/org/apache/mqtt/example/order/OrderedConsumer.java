package org.apache.mqtt.example.order;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.mqtt.example.connection.RocketMQPushConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

public class OrderedConsumer {
	public static void main(String[] args) throws Exception {

		DefaultMQPushConsumer consumer = RocketMQPushConsumer.newInstance();
		consumer.setConsumerGroup("consumer-group-order");
		consumer.subscribe("TopicTest111", "TagA || TagC || TagD");
		consumer.registerMessageListener(new MessageListenerOrderly() {
			AtomicLong consumeTimes = new AtomicLong(0);

			@SuppressWarnings("deprecation")
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				/*
				 * 当SUCCESS时，autoCommit设置为true时比设置为false多做了2个动作，consumeRequest.
				 * getProcessQueue().commit()和this.defaultMQPushConsumerImpl.
				 * getOffsetStore().updateOffset(consumeRequest.getMessageQueue(
				 * ), commitOffset, false); ProcessQueue.commit()
				 * ：本质是删除msgTreeMapTemp里的消息，
				 * msgTreeMapTemp里的消息在上面消费时从msgTreeMap转移过来的。
				 * this.defaultMQPushConsumerImpl.getOffsetStore().updateOffset(
				 * ) ：本质是把拉消息的偏移量更新到本地，然后定时更新到broker。
				 * 那么少了这2个动作会怎么样呢，随着消息的消费进行，msgTreeMapTemp里的消息堆积越来越多，
				 * 消费消息的偏移量一直没有更新到broker导致consumer每次重新启动后都要从头开始重复消费。
				 * 就算更新了offset到broker，那么msgTreeMapTemp里的消息堆积呢？不知道这算不算bug。
				 * 所以，还是把autoCommit设置为true吧。
				 */
				context.setAutoCommit(true);
				System.out.println("msgs size = " + msgs.size());
				for (MessageExt message : msgs) {
					System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + message + "%n");
					System.out.println(new String(message.getBody()));
				}
				this.consumeTimes.incrementAndGet();
				System.out.println(this.consumeTimes.get());
				if ((this.consumeTimes.get() % 2) == 0) {
					return ConsumeOrderlyStatus.SUCCESS;
				} else if ((this.consumeTimes.get() % 3) == 0) {
					return ConsumeOrderlyStatus.ROLLBACK;
				} else if ((this.consumeTimes.get() % 4) == 0) {
					return ConsumeOrderlyStatus.COMMIT;
				} else if ((this.consumeTimes.get() % 5) == 0) {
					context.setSuspendCurrentQueueTimeMillis(3000);
					return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
				}
				return ConsumeOrderlyStatus.SUCCESS;

			}
		});
		consumer.start();
		System.out.printf("Consumer Started.%n");
	}
}
