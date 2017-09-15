package org.apache.mqtt.example.simple;

import org.apache.mqtt.example.connection.RocketProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
/**
 * 单向传输:
 * 		适用场景：单向传输用于要求中等可靠性的情况，如日志收集。
 * @author yue.li3
 */
public class OnewayProducer {
	public static void main(String[] args) throws Exception {
		// Instantiate with a producer group name.
		DefaultMQProducer producer = RocketProducer.newInstance();
		producer.setProducerGroup("sync");
		// Launch the instance.
		producer.start();
		for (int i = 0; i < 100; i++) {
			// Create a message instance, specifying topic, tag and message
			// body.
			Message msg = new Message("TopicTest" /* Topic */, "TagA" /* Tag */, ("Hello RocketMQ " + i)
					.getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
			);
			// Call send message to deliver message to one of brokers.
			producer.sendOneway(msg);

		}
		// Shut down once the producer instance is not longer in use.
		producer.shutdown();
	}
}
