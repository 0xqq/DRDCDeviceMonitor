package comm.teld.cn.sink;

import java.io.IOException;

import org.apache.flink.streaming.util.serialization.AbstractDeserializationSchema;

public class ByteArrayDeserialization extends AbstractDeserializationSchema<byte[]> {
    
    private static final long serialVersionUID = 7776079380884102249L;

    @Override
    public byte[] deserialize(byte[] message) throws IOException {
        return message;
    }
}