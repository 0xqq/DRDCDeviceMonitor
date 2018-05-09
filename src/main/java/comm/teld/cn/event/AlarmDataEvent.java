package comm.teld.cn.event;

import java.util.List;

/**
 * @author jeremy 告警数据0x58
 */
public class AlarmDataEvent extends BaseDTO {
    private static final long serialVersionUID = 8086311931135289658L;

    // can地址 0表示无
    public int CanIndex;
    //设备条码
    public String SN="";
    //设备类型
    public int DevDescType;
 // 数据传送原因 0:默认；1突发；2召唤应答；3周期上送
    public int AlarmSendReason;
    //预警发生时间 1970 UTC毫秒
    public long AlarmTime;
    public String AlarmReserved1="";
    public String AlarmReserved2="";
    // 告警数据列表
    public List<AlarmEventDetail> AlarmEventDetails;
    
    public AlarmDataEvent() {
        this.DTOType = BaseDTOType.ALARM_EVENT;
    }

    public static class AlarmEventDetail {
        public int Code; // 告警码
        public int State; // 告警状态(0缺省, 1发生故障, 2恢复)
        public String Reserved1=""; // 预留1
        public String Reserved2=""; // 预留2
    }

}
