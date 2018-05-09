package comm.teld.cn.event;

import java.io.Serializable;
import java.util.List;

import comm.teld.cn.common.Utils;
import comm.teld.cn.filter.CommMsg;


public class BaseDTO implements Serializable {
    private static final long serialVersionUID = 8857072945544204348L;

    // 集控地址
    public String CtrlAddress="";
    // 充电前置协议版本号， 目前只有3
    public String CtrlProtoVersion="";
    // 充电前置节点编号 这个待定，需要充电前置定义
    public short CommServerCode;
    // 充电前置发送的报文Id
    public String PackageId="";
    //充电前置发送时间 到UTC1970年的毫秒 e.g. 1523533161444
    public long FesSendTime;
    //充电前置发送时间 可读北京时间  e.g. 2018-04-12 19:39:21.444
    public String FesSendTimeStr="";
    // 事件服务类型 告警字段: DeviceAlarm
    public String DTOType="";
    // Flink处理开始时间戳 1970年毫秒数 
     public long FlinkPorcessTimeTag;
    //Flink处理开始时间戳为可读北京时间  e.g. 2018-04-12 19:39:21.444
    public String FlinkPorcessTimeTagStr="";
    // Flink处理结束时间戳 同上
    public long FlinkPorcessEndTimeTag;
    public String FlinkPorcessEndTimeTagStr="";
    
    public String StaCode="";                //所属电站编号
    public String StaName="";               //电站名称
    public String StaOperMaintain="";       //所属运维公司ID
    public String StaProCode="";               //电站的项目编号
    public String StaCtrlVersionCode="";   //集控器软件大版本
    public String StaCtrlType="";               //集控器类型
    public String StaActualOper="";           //所属运营公司ID
    public String StaType="";                    //电站类型CODE
    public String StaTypeName="";           //电站类型名称
    public String StaIndustryType="";       //场站类型CODE
    public String StaCity="";                      //所属城市CODE
    public String StaOperType="";             //运营类型CODE
    public String StaIsTestStation="";        //是否测试站
    public String StaLastUpdateTime="";   //Unix时间戳,最后更新时间
    
    public BaseDTO() {
    }

    public void FieldFill(CommMsg msg) {
        this.CtrlAddress = msg.CtrlAddress;
        this.FesSendTime=msg.FesSendTime;
        this.FesSendTimeStr = Utils.millTimeToStr(msg.FesSendTime);
        this.FlinkPorcessTimeTag = msg.FlinkProcessTimeTag;
        this.FlinkPorcessTimeTagStr = Utils.millTimeToStr(msg.FlinkProcessTimeTag);
        this.CtrlProtoVersion = msg.ProtocolVersion;
        this.CommServerCode = msg.CommServerCode;
        this.PackageId = msg.PackageId;
    }

    public void stationFieldFill(List<String> statInfo) {
        this.StaCode = statInfo.get(0);
        this.StaName = statInfo.get(1);
        this.StaOperMaintain = statInfo.get(2);
        this.StaProCode = statInfo.get(3);
        this.StaCtrlVersionCode = statInfo.get(4);
        this.StaCtrlType = statInfo.get(5);
        this.StaActualOper = statInfo.get(6);
        this.StaType = statInfo.get(7);
        this.StaTypeName = statInfo.get(8);
        this.StaIndustryType = statInfo.get(9);
        this.StaCity = statInfo.get(10);
        this.StaOperType = statInfo.get(11);
        this.StaIsTestStation = statInfo.get(12);
        this.StaLastUpdateTime = statInfo.get(13);
    }
}
