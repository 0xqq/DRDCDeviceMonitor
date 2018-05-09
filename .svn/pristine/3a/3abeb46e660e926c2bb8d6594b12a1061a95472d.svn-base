package comm.teld.cn.event;

import java.util.List;

/**
 * @author jeremy CCU数据0x66
 */
public class CCUDataEvent extends BaseDTO {
    private static final long serialVersionUID = 4555929229471481141L;
    // 集控器发送报文的时间,UTC，精确到毫秒（到UTC1970年的毫秒）可能为0
    public long CtrlSendTime;
    //可能为null
    public String CtrlSendTimeStr;
    public List<CCUEventDetail> CCUEventDetails;
    public String Reserved1;
    public String Reserved2;

    public CCUDataEvent() {
        this.DTOType = BaseDTOType.CCU_EVENT;
    }

    public static class CCUEventDetail {
        // can地址
        public int DevIndex;
        // 运行状态
        public int RunState;
        // 输入接触器状态
        public int InConStatus;
        // 联动接触器状态
        public int LinkConStatus;
        // 系统机型
        public int SystemType;
        // "环境温度1（℃）"
        public double EnvT;
        // "总运行时间（小时）"
        public int RunTime;
        // 直流柜额定输出功率
        public double DcCabRateP;
        // 直流柜当前输出功率
        public double DcCabCurP;
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
        // 设备条码(能取到sn就填，否则可以空着)
        public String SN;
        // 预留1
        public String Reserved1;
        // 预留2
        public String Reserved2;
    }

}
