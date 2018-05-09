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
import comm.teld.cn.event.CCUDataEvent;
import comm.teld.cn.event.ErrorParseMessageEvent;
import comm.teld.cn.filter.CommMsg;
import comm.teld.cn.filter.DevMessageType;
import monitor.protobuf.Monitorprotobuf.CcuDataNotifyReq;
import redis.clients.jedis.Jedis;

//直流模块数据推送通知
public class CCURichFlatMap extends RichFlatMapFunction<CommMsg, BaseDTO> {
    private static final long serialVersionUID = 4895785441234868455L;
    private final ConfigBean configBean;
    
    public CCURichFlatMap(ConfigBean configBean) {
        this.configBean=configBean;
    }

    @Override
    public void flatMap(CommMsg commMsg, Collector<BaseDTO> collector) throws Exception {
        Jedis localJedis = RedisClient.getJedis(configBean);
        try {
            CcuDataNotifyReq ccuDataReq = null;
            try {
                ccuDataReq = CcuDataNotifyReq.parseFrom(commMsg.Payload);
            } catch (Exception e1) {
                System.out.println("==ERROR---CCURichFlatMap=PackageId=" + commMsg.PackageId + ",CtrlAddress="
                                + commMsg.CtrlAddress + ",FesSendTime=" + commMsg.FesSendTime);
                ErrorParseMessageEvent errorParseMessage = new ErrorParseMessageEvent();
                errorParseMessage.FieldFill(commMsg);
                collector.collect(errorParseMessage);
                return;
            }

            CCUDataEvent ccuEvent = new CCUDataEvent();
            ccuEvent.FieldFill(commMsg);

            ccuEvent.CtrlSendTime = ccuDataReq.getSendTime().getTime();
            ccuEvent.CtrlSendTimeStr = Utils.millTimeToStr(ccuEvent.CtrlSendTime);
            ccuEvent.Reserved1=ccuDataReq.getReserved1();
            ccuEvent.Reserved2=ccuDataReq.getReserved2();

            List<CCUDataEvent.CCUEventDetail> tempCCUDetails = new ArrayList<>();
            
            ccuDataReq.getDataListList().forEach(ccuProtoDetail -> {
                CCUDataEvent.CCUEventDetail ccuEventDetail = new CCUDataEvent.CCUEventDetail();
                ccuEventDetail.DevIndex = ccuProtoDetail.getDevIndex();
                ccuEventDetail.RunState = ccuProtoDetail.getRunState();
                ccuEventDetail.InConStatus=ccuProtoDetail.getInConStatus();
                ccuEventDetail.LinkConStatus=ccuProtoDetail.getLinkConStatus();
                ccuEventDetail.SystemType=ccuProtoDetail.getSystemType();
                ccuEventDetail.EnvT = ccuProtoDetail.getEnvT();
                ccuEventDetail.RunTime = ccuProtoDetail.getRunTime();
                ccuEventDetail.DcCabRateP=ccuProtoDetail.getDcCabRateP();
                ccuEventDetail.DcCabCurP=ccuProtoDetail.getDcCabCurP();
                ccuEventDetail.AlarmCode = ccuProtoDetail.getAlarmCode();
                ccuEventDetail.FrameNo = ccuProtoDetail.getFrameNo();
                ccuEventDetail.SoftV1 = ccuProtoDetail.getSoftV1();
                ccuEventDetail.SoftV2 = ccuProtoDetail.getSoftV2();
                ccuEventDetail.SoftV3 = ccuProtoDetail.getSoftV3();
                ccuEventDetail.HardV = ccuProtoDetail.getHardV();
                ccuEventDetail.SN = ccuProtoDetail.getSN();
                ccuEventDetail.Reserved1 = ccuProtoDetail.getReserved1();
                ccuEventDetail.Reserved2 = ccuProtoDetail.getReserved2();
                tempCCUDetails.add(ccuEventDetail);
            });
            ccuEvent.CCUEventDetails = tempCCUDetails;

            System.out.println("交流模块数据:" + Utils.objectToJSON(ccuEvent));

            collector.collect(ccuEvent);
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
            return commMsg.MessageType == DevMessageType.CCU_DATA;
        }
    };

}
