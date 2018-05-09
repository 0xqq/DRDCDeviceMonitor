package cn.teld.test.drecdevicemonitor;

import org.apache.flink.streaming.util.serialization.AbstractDeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;

import java.io.IOException;

public class ByteArraySchema extends AbstractDeserializationSchema<byte[]> implements SerializationSchema<byte[]> {
    @Override
    public byte[] deserialize(byte[] message) throws IOException {
        return message;
    }

    @Override
    public byte[] serialize(byte[] element) {
        return element;
    }
}
