package comm.teld.cn.map;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;

import comm.teld.cn.common.RedisClient;
import comm.teld.cn.common.Utils;
import comm.teld.cn.common.config.ConfigBean;
import comm.teld.cn.event.BaseDTO;
import comm.teld.cn.event.ErrorParseMessageEvent;
import comm.teld.cn.event.PDUDataEvent;
import comm.teld.cn.filter.CommMsg;
import comm.teld.cn.filter.DevMessageType;
import monitor.protobuf.Monitorprotobuf.PduDataNotifyReq;
import redis.clients.jedis.Jedis;

//直流模块数据推送通知
public class PDURichFlatMap extends RichFlatMapFunction<CommMsg, BaseDTO> {
    private static final long serialVersionUID = 4895785441234868455L;
    private final ConfigBean configBean;
    
    public PDURichFlatMap(ConfigBean configBean) {
        this.configBean=configBean;
    }

    @Override
    public void flatMap(CommMsg commMsg, Collector<BaseDTO> collector) throws Exception {
        Jedis localJedis = RedisClient.getJedis(configBean);
        try {
            PduDataNotifyReq pduDataReq = null;
            try {
                pduDataReq = PduDataNotifyReq.parseFrom(commMsg.Payload);
            } catch (Exception e1) {
                System.out.println("==ERROR---PDURichFlatMap=PackageId=" + commMsg.PackageId + ",CtrlAddress="
                                + commMsg.CtrlAddress + ",FesSendTime=" + commMsg.FesSendTime);
                ErrorParseMessageEvent errorParseMessage = new ErrorParseMessageEvent();
                errorParseMessage.FieldFill(commMsg);
                collector.collect(errorParseMessage);
                return;
            }

            PDUDataEvent pduEvent = new PDUDataEvent();
            pduEvent.FieldFill(commMsg);

            pduEvent.CtrlSendTime = pduDataReq.getSendTime().getTime();
            pduEvent.CtrlSendTimeStr = Utils.millTimeToStr(pduEvent.CtrlSendTime);
            pduEvent.Reserved1=pduDataReq.getReserved1();
            pduEvent.Reserved2=pduDataReq.getReserved2();

            List<PDUDataEvent.PDUEventDetail> tempPDUDetails = new ArrayList<>();
            
            pduDataReq.getDataListList().forEach(pduProtoDetail -> {
                PDUDataEvent.PDUEventDetail pduEventDetail = new PDUDataEvent.PDUEventDetail();
                pduEventDetail.DevIndex = pduProtoDetail.getDevIndex();
                pduEventDetail.SN = pduProtoDetail.getSN();
                pduEventDetail.RunState = pduProtoDetail.getRunState();
                pduEventDetail.OutV = pduProtoDetail.getOutV();
                pduEventDetail.OutI = Math.abs(pduProtoDetail.getOutI());
                pduEventDetail.EnvT = pduProtoDetail.getEnvT();
                pduEventDetail.RadT=pduProtoDetail.getRadT();
                pduEventDetail.RunTime = pduProtoDetail.getRunTime();
                pduEventDetail.SwitchCount = pduProtoDetail.getSwitchCount();
                pduEventDetail.KWH=pduProtoDetail.getKWH();
                pduEventDetail.NegR=pduProtoDetail.getNegR();
                pduEventDetail.PosR=pduProtoDetail.getPosR();
                pduEventDetail.AlarmCode = pduProtoDetail.getAlarmCode();
                pduEventDetail.FrameNo = pduProtoDetail.getFrameNo();
                pduEventDetail.SoftV1 = pduProtoDetail.getSoftV1();
                pduEventDetail.SoftV2 = pduProtoDetail.getSoftV2();
                pduEventDetail.SoftV3 = pduProtoDetail.getSoftV3();
                pduEventDetail.HardV = pduProtoDetail.getHardV();
                pduEventDetail.Reserved1 = pduProtoDetail.getReserved1();
                pduEventDetail.Reserved2 = pduProtoDetail.getReserved2();
                tempPDUDetails.add(pduEventDetail);
            });
            pduEvent.PDUEventDetails = tempPDUDetails;

            System.out.println("交流模块数据:" + Utils.objectToJSON(pduEvent));

            collector.collect(pduEvent);
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
            return commMsg.MessageType == DevMessageType.PDU_DATA;
        }
    };

}
