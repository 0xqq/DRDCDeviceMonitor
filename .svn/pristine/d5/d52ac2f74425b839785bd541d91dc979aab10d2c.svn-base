package comm.teld.cn.map;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

import comm.teld.cn.common.RedisClient;
import comm.teld.cn.common.Utils;
import comm.teld.cn.common.config.ConfigBean;
import comm.teld.cn.event.ACModularDataEvent;
import comm.teld.cn.event.BaseDTO;
import comm.teld.cn.event.ErrorParseMessageEvent;
import comm.teld.cn.filter.CommMsg;
import comm.teld.cn.filter.DevMessageType;
import monitor.protobuf.Monitorprotobuf.ACModularDataNotifyReq;
import redis.clients.jedis.Jedis;

//直流模块数据推送通知
public class ACModularRichFlatMap extends RichFlatMapFunction<CommMsg, BaseDTO> {
    private static final long serialVersionUID = 4895785441234868455L;
    private final ConfigBean configBean;
    
    public ACModularRichFlatMap(ConfigBean configBean) {
        this.configBean=configBean;
    }

    @Override
    public void flatMap(CommMsg commMsg, Collector<BaseDTO> collector) throws Exception {
        Jedis localJedis = RedisClient.getJedis(configBean);
        try {
            ACModularDataNotifyReq acDataReq = null;
            try {
                acDataReq = ACModularDataNotifyReq.parseFrom(commMsg.Payload);
            } catch (Exception e1) {
                System.out.println("==ERROR---ACModularRichFlatMap=PackageId=" + commMsg.PackageId + ",CtrlAddress="
                                + commMsg.CtrlAddress + ",FesSendTime=" + commMsg.FesSendTime);
                ErrorParseMessageEvent errorParseMessage = new ErrorParseMessageEvent();
                errorParseMessage.FieldFill(commMsg);
                collector.collect(errorParseMessage);
                return;
            }

            ACModularDataEvent acEvent = new ACModularDataEvent();
            acEvent.FieldFill(commMsg);

            acEvent.CtrlSendTime = acDataReq.getSendTime().getTime();
            acEvent.CtrlSendTimeStr = Utils.millTimeToStr(acEvent.CtrlSendTime);
            acEvent.Reserved1=acDataReq.getReserved1();
            acEvent.Reserved2=acDataReq.getReserved2();

            List<ACModularDataEvent.ACModularEventDetail> tempACDetails = new ArrayList<>();
            
            acDataReq.getDataListList().forEach(acProtoDetail -> {
                ACModularDataEvent.ACModularEventDetail acEventDetail = new ACModularDataEvent.ACModularEventDetail();
                acEventDetail.DevIndex = acProtoDetail.getDevIndex();
                acEventDetail.SN = acProtoDetail.getSN();
                acEventDetail.AlarmCode = acProtoDetail.getAlarmCode();
                acEventDetail.RunState = acProtoDetail.getRunState();
                acEventDetail.InV = acProtoDetail.getInV();
                acEventDetail.InI = Math.abs(acProtoDetail.getInI());
                acEventDetail.OutV = acProtoDetail.getOutV();
                acEventDetail.OutI = Math.abs(acProtoDetail.getOutI());
                acEventDetail.EnvT = acProtoDetail.getEnvT();
                acEventDetail.InT = acProtoDetail.getInT();
                acEventDetail.GunT=acProtoDetail.getGunT();
                acEventDetail.RunTime = acProtoDetail.getRunTime();
                acEventDetail.SwitchCount = acProtoDetail.getSwitchCount();
                acEventDetail.ACRatedOutP=acProtoDetail.getACRatedOutP();
                acEventDetail.OutP=acProtoDetail.getOutP();
                acEventDetail.OutQ=acProtoDetail.getOutQ();
                acEventDetail.S=acProtoDetail.getS();
                acEventDetail.CPF=acProtoDetail.getCPF();
                acEventDetail.CF=acProtoDetail.getCF();
                acEventDetail.KWH=acProtoDetail.getKWH();
                acEventDetail.Relay=acProtoDetail.getRelay();
                acEventDetail.Connect=acProtoDetail.getConnect();
                acEventDetail.FrameNo = acProtoDetail.getFrameNo();
                acEventDetail.SoftV1 = acProtoDetail.getSoftV1();
                acEventDetail.SoftV2 = acProtoDetail.getSoftV2();
                acEventDetail.SoftV3 = acProtoDetail.getSoftV3();
                acEventDetail.HardV = acProtoDetail.getHardV();
                acEventDetail.Reserved1 = acProtoDetail.getReserved1();
                acEventDetail.Reserved2 = acProtoDetail.getReserved2();
                tempACDetails.add(acEventDetail);
            });
            acEvent.ACModularEventDetails = tempACDetails;

            System.out.println("交流模块数据:" + Utils.objectToJSON(acEvent));

            collector.collect(acEvent);
        } finally {
            if (localJedis != null) {
                localJedis.close();
            }
        }
    }

    public static FilterFunction<CommMsg> filterFun = new FilterFunction<CommMsg>() {
        private static final long serialVersionUID = -3634906316441928256L;

        @Override
        public boolean filter(CommMsg commMsg) throws Exception {
            return commMsg.MessageType == DevMessageType.AC_MODULAR_DATA;
        }
    };

}
