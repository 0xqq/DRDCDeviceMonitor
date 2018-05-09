package comm.teld.cn.event;

import java.util.List;

/**
 * @author jeremy
 * 直流模块数据0x60
 */
public class DCModularDataEvent extends BaseDTO {
    private static final long serialVersionUID = 4626488400859706648L;
    // 集控器发送报文的时间,UTC，精确到毫秒（到UTC1970年的毫秒）可能为0
    public long CtrlSendTime;
    //可能为null
    public String CtrlSendTimeStr;
    public List<DCModularEventDetail> DCModularEventDetails;
    public String Reserved1;
    public String Reserved2;
    
    public DCModularDataEvent() {
        this.DTOType = BaseDTOType.DC_MODULAR_EVENT;
    }
    
    public static class DCModularEventDetail {
        public int DevIndex;    //can地址
        public String SN;        //设备条码(能取到sn就填，否则可以空着)
        public int RunState;    //运行状态 0：待机 1：休眠 2：运行 3：离线 4~255预留 
        public int Group;        //所属分组
        public double InVa;    // 输入A相电压
        public double InVb;    // 输入B相电压
        public double InVc;     // 输入C相电压
        public double InI;       // 输入电流（三相平均电流）
        public double OutV;    // 输出电压
        public double OutI;     // 输出电流
        public int  EnvT;          // 环境温度
        public int  M1T;          // M1板温度
        public int RunTime;    // 总运行时间，单位是小时
        public int SwitchCount;     // 切换次数
        public int AlarmCode;       // 告警码
        public int  SinglePlateT3;  // 单板温度3
        public int FrameNo;           // 插框号
        public String SoftV1;         // 软件1版本
        public String SoftV2;         // 软件2版本
        public String SoftV3;         // 软件3版本
        public String HardV;         // 硬件版本
        public String Reserved1;   //预留1
        public String Reserved2;   //预留2
    }

}
