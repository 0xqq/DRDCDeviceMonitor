package comm.teld.cn.event;

import java.util.List;

/**
 * @author jeremy PDU数据0x64
 */
public class PDUDataEvent extends BaseDTO {
    private static final long serialVersionUID = 4555929229471481141L;
    // 集控器发送报文的时间,UTC，精确到毫秒（到UTC1970年的毫秒）可能为0
    public long CtrlSendTime;
    //可能为null
    public String CtrlSendTimeStr;
    public List<PDUEventDetail> PDUEventDetails;
    public String Reserved1;
    public String Reserved2;

    public PDUDataEvent() {
        this.DTOType = BaseDTOType.PDU_EVENT;
    }

    public static class PDUEventDetail {
        // can地址
        public int DevIndex;
        // 设备条码(能取到sn就填，否则可以空着)
        public String SN;
        // 运行状态
        public int RunState;
        // "输出电压（V）"
        public double OutV;
        // "输出电流（A）"
        public double OutI;
        // "环境温度1（℃）"
        public double EnvT;
        // "散热器温度（℃）"
        public int RadT = 7;
        // "总运行时间（小时）"
        public int RunTime;
        // 切换次数
        public int SwitchCount;
        // "电能表读数（KWH）"
        public double KWH;
        // 绝缘电阻负对地阻值
        public int NegR;
        // 绝缘电阻正对地阻值
        public int PosR;
        // 告警码
        public int AlarmCode;
        // 插框号
        public int FrameNo;
        // 软件1版本
        public String SoftV1;
        // 软件2版本
        public String SoftV2;
        // 软件3版本
        public String SoftV3;
        // 硬件版本
        public String HardV;
        // 预留1
        public String Reserved1;
        // 预留2
        public String Reserved2;
    }

}
