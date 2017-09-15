package org.apache.mqtt.example.transcation;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.mqtt.example.connection.Config;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * 发送事务消息例子
 * 
 */
public class TransactionProducer {
	public static void main(String[] args) throws MQClientException, InterruptedException {

		TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
		TransactionMQProducer producer = new TransactionMQProducer("transaction");
		// 事务回查最小并发数
		producer.setCheckThreadPoolMinSize(2);
		// 事务回查最大并发数
		producer.setCheckThreadPoolMaxSize(2);
		producer.setNamesrvAddr(Config.NAME_SERVER_ADDRESS);
		// 队列数
		producer.setCheckRequestHoldMax(2000);
		producer.setTransactionCheckListener(transactionCheckListener);
		producer.start();

		String[] tags = new String[] { "TagA", "TagB", "TagC", "TagD", "TagE" };
		TransactionExecuterImpl tranExecuter = new TransactionExecuterImpl();
		for (int i = 0; i < 4; i++) {
			String time = DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd'T'HH:mm:ss");
			try {
				Message msg = new Message("topicTest-t", tags[i % tags.length], "KEY" + i,
						(time + "Hello RocketMQ " + i).getBytes());
				SendResult sendResult = producer.sendMessageInTransaction(msg, tranExecuter, null);
				System.out.println(sendResult);

				Thread.sleep(10);
			} catch (MQClientException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < 100000; i++) {
			Thread.sleep(1000);
		}

		producer.shutdown();

	}
}
