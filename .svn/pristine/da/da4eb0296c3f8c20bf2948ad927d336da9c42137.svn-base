package comm.teld.cn.sink;

import java.nio.charset.StandardCharsets;

import org.apache.flink.streaming.util.serialization.SerializationSchema;

import comm.teld.cn.common.Utils;
import comm.teld.cn.event.BaseDTO;

public class JsonDTOSerializing<T> implements SerializationSchema<T>{
    private static final long serialVersionUID = -2766923832682633208L;

    @Override
    public byte[] serialize(T element) {
        BaseDTO baseDTO=(BaseDTO) element;
        baseDTO.FlinkPorcessEndTimeTag = System.currentTimeMillis();
        baseDTO.FlinkPorcessEndTimeTagStr = Utils.millTimeToStr(baseDTO.FlinkPorcessEndTimeTag);
        return Utils.objectToJSON(baseDTO).getBytes(StandardCharsets.UTF_8);
    }

}
