package comm.teld.cn.sink;

import java.io.IOException;

import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;
import org.apache.flink.streaming.util.serialization.SerializationSchema;

public class EORMQSink<E> extends org.apache.flink.streaming.connectors.rabbitmq.RMQSink<E>{

	public EORMQSink(RMQConnectionConfig rmqConnectionConfig, String queueName, SerializationSchema schema) {
		super(rmqConnectionConfig, queueName, schema);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void setupQueue() throws IOException
	{
		channel.queueDeclare(queueName, true, false, false, null);
	}
}
