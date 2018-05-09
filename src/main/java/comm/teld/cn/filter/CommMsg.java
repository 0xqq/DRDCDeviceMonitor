package comm.teld.cn.filter;

import java.io.Serializable;

/**
 * V3的通信报文
 */
public class CommMsg implements Serializable{
    private static final long serialVersionUID = 3188195893863980597L;

    /**
     * 协议版本号，第一版为0x01
     */
    public String ProtocolVersion;

    /**
     * 报文基本类型
     */
    public byte MessageType;
    /**
     * 集控器地址
     */
    public String CtrlAddress;
    /**
     * 报文消息体
     */
    public byte[] Payload;

    /**
     * 前置服务器时间，精确到毫秒（到UTC1970年的毫秒）
     */
    public long FesSendTime;
    /**
     * Flink处理时间戳
     */
    public long FlinkProcessTimeTag;

    /**
     * 通信节点编号
     */
    public short CommServerCode;

    /**
     * 数据包标识
     */
    public String PackageId;

}
