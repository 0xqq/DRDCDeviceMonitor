package comm.teld.cn.event;

import comm.teld.cn.filter.CommMsg;
import comm.teld.cn.filter.DevMessageType;

/**
 * protobuf解析不了的报文
 * 错误报文
 */
public class ErrorParseMessageEvent extends BaseDTO{
    private static final long serialVersionUID = -8157815870511521113L;

    //报文基本类型
    public int MessageType;
    public String MessageTypeStr;
    public byte[] Payload;

    public ErrorParseMessageEvent() {
        this.DTOType = BaseDTOType.ERROR_EVENT;
    }
    public void FieldFill(CommMsg msg){
        super.FieldFill(msg);
        this.MessageType = msg.MessageType;
        this.MessageTypeStr = getMessageTypeStr(MessageType);
        this.Payload = msg.Payload;
    }

    private String getMessageTypeStr(int messageType) {
        String MessageTypeStr = "Unknown";
        switch (messageType) {
        case DevMessageType.ALARM_DATA:
            MessageTypeStr="AlarmReq";
            break;
        case DevMessageType.DC_MODULAR_DATA:
            MessageTypeStr="DCModularDataNotifyReq";
            break;
        case DevMessageType.AC_MODULAR_DATA:
            MessageTypeStr="ACModularDataNotifyReq";
            break;
        case DevMessageType.PDU_DATA:
            MessageTypeStr="PduDataNotifyReq";
            break;
        case DevMessageType.CCU_DATA:
            MessageTypeStr="CcuDataNotifyReq";
            break;          
        default:
            MessageTypeStr="Unknown";
            break;
        }
        return MessageTypeStr;
    }
}
