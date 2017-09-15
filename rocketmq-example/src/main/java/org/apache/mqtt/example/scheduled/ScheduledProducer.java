package org.apache.mqtt.example.scheduled;

import org.apache.mqtt.example.connection.RocketProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class ScheduledProducer {

	public static void main(String[] args) throws Exception {

		DefaultMQProducer producer = RocketProducer.newInstance();
		producer.setProducerGroup("scheduled");
		producer.start();
		int totalMessagesToSend = 15;
		for (int i = 0; i < totalMessagesToSend; i++) {
			long time = System.currentTimeMillis();
			Message message = new Message("scheduled-topic",
					("hello scheduled:" + time).getBytes(RemotingHelper.DEFAULT_CHARSET));
			// This message will be delivered to consumer 5 seconds later.
			// messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m
			// 20m 30m 1h 2h
			// 这个配置项配置了从1级开始，各级延时的时间，可以修改这个指定级别的延时时间；
			// 时间单位支持：s、m、h、d，分别表示秒、分、时、天；
			// 默认值就是上面声明的，可手工调整；
			// 默认值已够用，不建议修改这个值。
			message.setDelayTimeLevel(1);
			producer.send(message);
		}
		// Shutdown producer after use.
		producer.shutdown();

	}

}
