package cn.teld.test.drecdevicemonitor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitMQTestClient2 {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        // �������ӹ���
        ConnectionFactory factory = new ConnectionFactory();
        //����RabbitMQ��ַ
        factory.setHost("localhost");
        //����һ���µ�����
        Connection connection = factory.newConnection();
        //����һ��ͨ��
        Channel channel = connection.createChannel();
        //����Ҫ��ע�Ķ���
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println("Customer Waiting Received messages");
        //DefaultConsumer��ʵ����Consumer�ӿڣ�ͨ������һ��Ƶ����
        // ���߷�����������Ҫ�Ǹ�Ƶ������Ϣ�����Ƶ��������Ϣ���ͻ�ִ�лص�����handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");
            }
        };
        //�Զ��ظ�����Ӧ�� -- RabbitMQ�е���Ϣȷ�ϻ���
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}