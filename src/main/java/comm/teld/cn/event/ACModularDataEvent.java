package comm.teld.cn.event;

import java.util.List;

/**
 * @author jeremy
 * 交流模块数据0x62
 */
public class ACModularDataEvent extends BaseDTO {
    private static final long serialVersionUID = 4555929229471481141L;
    // 集控器发送报文的时间,UTC，精确到毫秒（到UTC1970年的毫秒）可能为0
    public long CtrlSendTime;
    //可能为null
    public String CtrlSendTimeStr;
    public List<ACModularEventDetail> ACModularEventDetails;
    public String Reserved1;
    public String Reserved2;
    
    public ACModularDataEvent() {
        this.DTOType = BaseDTOType.AC_MODULAR_EVENT;
    }
    
    public static class ACModularEventDetail {
        // can地址
        public int DevIndex;
        // 设备条码(能取到sn就填，否则可以空着)
        public String SN;
        // 告警码
        public int AlarmCode;
        // 运行状态
        public int RunState;
        // "输入电压（V）"
        public double InV;
        // "输入电流（A）"
        public double InI;
        // "输出电压（V）"
        public double OutV;
        // "输出电流（A）"
        public double OutI;
        // "环境温度1（℃）"
        public double EnvT;
        // "设备内部温度（℃）"
        public int InT;
        // "设备枪座温度（℃）"
        public int GunT;
        // "总运行时间（小时）"
        public int RunTime;
        // 充电次数
        public int SwitchCount;
        // "交流额定输出功率（KW）"
        public double ACRatedOutP;
        // "当前输出有功功率（KW）"
        public double OutP;
        // "当前输出无功功率（var）"
        public double OutQ;
        // "当前输出视在功率（VA）"
        public double S;
        // 当前功率因数
        public double CPF;
        // "当前频率（Hz）"
        public double CF;
        // "电能表读数（KWH）"
        public double KWH;
        // 输出继电器状态
        public int Relay;
        // 输出接触器状态
        public int Connect;
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
