package cn.teld.test.drecdevicemonitor;

import com.rabbitmq.client.*;

import java.io.IOException;

public class TestRabbitMQ_Top5 {

	private final static String QUEUE_NAME = "abcd";
//	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("40.125.172.7");
//		factory.setHost("localhost");
		factory.setPort(5672);
		factory.setAutomaticRecoveryEnabled(true);
		factory.setUsername("teld");
		factory.setPassword("Teld@teld.cn");
		factory.setVirtualHost("/");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);
		
		
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("40.125.172.7");
//		factory.setPort(5672);
//		factory.setAutomaticRecoveryEnabled(true);
//		factory.setUsername("teld");
//		factory.setPassword("Teld@teld.cn");
//		factory.setVirtualHost("/");
//		Connection connection = factory.newConnection();
//		Channel channel = connection.createChannel();
//
//		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//		Consumer consumer = new DefaultConsumer(channel) {
//			@Override
//			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
//					byte[] body) throws IOException {
//				String message = new String(body, "UTF-8");
//				System.out.println(" [x] Received '" + message + "'");
//			}
//		};
//		channel.basicConsume(QUEUE_NAME, true, consumer);
		
		
		
		
	}
}
