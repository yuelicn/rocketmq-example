package org.apache.mqtt.example.filter;

import java.io.UnsupportedEncodingException;

import org.apache.mqtt.example.connection.RocketProducer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class FilterProducer {
	public static void main(String[] args) throws UnsupportedEncodingException, MQClientException, RemotingException,
			MQBrokerException, InterruptedException {

		DefaultMQProducer producer = RocketProducer.newInstance();
		producer.setProducerGroup("filter");
		producer.start();
		for (int i = 0; i < 10; i++) {
			Message message = new Message("topic-filter", "TagA",
					("hello file - " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
			message.putUserProperty("a", String.valueOf(i));
			SendResult sendResult = producer.send(message);
			System.out.printf("%s%n", sendResult);
		}
		producer.shutdown();
	}

}
