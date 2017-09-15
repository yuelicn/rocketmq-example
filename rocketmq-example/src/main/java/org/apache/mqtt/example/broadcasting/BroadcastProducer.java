package org.apache.mqtt.example.broadcasting;

import java.io.UnsupportedEncodingException;

import org.apache.mqtt.example.connection.RocketProducer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class BroadcastProducer {

	public static void main(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException,
			MQBrokerException, InterruptedException {
		DefaultMQProducer producer = RocketProducer.newInstance();
		producer.setProducerGroup("broadcast");
		producer.start();
		for (int i = 0; i < 2; i++) {
			long time = System.currentTimeMillis();
			Message message = new Message("topicTest", "TagA", "orderId01",
					("hello consumer broadcast " + time).getBytes(RemotingHelper.DEFAULT_CHARSET));
			SendResult sendResult = producer.send(message);
			System.out.printf("%s%n", sendResult);
		}
		producer.shutdown();
	}
}
