package org.apache.mqtt.example.simple;

import org.apache.mqtt.example.connection.RocketProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 可靠的同步类型： 使用场景：可靠的同步传输用于广泛的场景，如重要通知信息，短信通知，短信营销系统等。
 * 
 * @author yue.li3
 */
public class SyncProducer2 {

	public static void main(String[] args) throws Exception {
		// Instantiate with a producer group name.
		DefaultMQProducer producer = RocketProducer.newInstance();
		producer.setProducerGroup("sync");
		producer.start();
		for (int i = 0; i < 100; i++) {
			// Create a message instance, specifying topic, tag and message
			// body.
			Message msg = new Message("TopicTest" /* Topic */, "TagA" /* Tag */, ("Hello RocketMQ " + i)
					.getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
			);
			// Call send message to deliver message to one of brokers.
			SendResult sendResult = producer.send(msg);
			System.out.printf("%s%n", sendResult);
		}
		// Shut down once the producer instance is not longer in use.
		producer.shutdown();
	}

}
