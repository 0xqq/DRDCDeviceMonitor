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
import comm.teld.cn.event.DCModularDataEvent;
import comm.teld.cn.event.ErrorParseMessageEvent;
import comm.teld.cn.filter.CommMsg;
import comm.teld.cn.filter.DevMessageType;
import monitor.protobuf.Monitorprotobuf.DCModularDataNotifyReq;
import redis.clients.jedis.Jedis;

//直流模块数据推送通知
public class DCModularRichFlatMap extends RichFlatMapFunction<CommMsg, BaseDTO> {
    private static final long serialVersionUID = 4895785441234868455L;
    private final ConfigBean configBean;
    
    public DCModularRichFlatMap(ConfigBean configBean) {
        this.configBean=configBean;
    }

    @Override
    public void flatMap(CommMsg commMsg, Collector<BaseDTO> collector) throws Exception {
        Jedis localJedis = RedisClient.getJedis(configBean);
        try {
            DCModularDataNotifyReq dcDataReq = null;
            try {
                dcDataReq = DCModularDataNotifyReq.parseFrom(commMsg.Payload);
            } catch (Exception e1) {
                System.out.println("==ERROR---DCModularRichFlatMap=PackageId=" + commMsg.PackageId + ",CtrlAddress="
                                + commMsg.CtrlAddress + ",FesSendTime=" + commMsg.FesSendTime);
                ErrorParseMessageEvent errorParseMessage = new ErrorParseMessageEvent();
                errorParseMessage.FieldFill(commMsg);
                collector.collect(errorParseMessage);
                return;
            }

            DCModularDataEvent dcEvent = new DCModularDataEvent();
            dcEvent.FieldFill(commMsg);

            dcEvent.CtrlSendTime = dcDataReq.getSendTime().getTime();
            dcEvent.CtrlSendTimeStr = Utils.millTimeToStr(dcEvent.CtrlSendTime);
            dcEvent.Reserved1 =dcDataReq.getReserved1();
            dcEvent.Reserved2 = dcDataReq.getReserved2();
            
            List<DCModularDataEvent.DCModularEventDetail> tempDCDetails = new ArrayList<>();
            dcDataReq.getDataListList().forEach(dcProtoDetail -> {
                DCModularDataEvent.DCModularEventDetail dcEventDetail = new DCModularDataEvent.DCModularEventDetail();
                dcEventDetail.DevIndex = dcProtoDetail.getDevIndex();
                dcEventDetail.SN = dcProtoDetail.getSN();
                dcEventDetail.RunState = dcProtoDetail.getRunState();
                dcEventDetail.Group = dcProtoDetail.getGroup();
                dcEventDetail.InVa = dcProtoDetail.getInVa();
                dcEventDetail.InVb = dcProtoDetail.getInVb();
                dcEventDetail.InVc = dcProtoDetail.getInVc();
                dcEventDetail.InI = Math.abs(dcProtoDetail.getInI());
                dcEventDetail.OutV = dcProtoDetail.getOutV();
                dcEventDetail.OutI = Math.abs(dcProtoDetail.getOutI());
                dcEventDetail.EnvT = dcProtoDetail.getEnvT();
                dcEventDetail.M1T = dcProtoDetail.getM1T();
                dcEventDetail.RunTime = dcProtoDetail.getRunTime();
                dcEventDetail.SwitchCount = dcProtoDetail.getSwitchCount();
                dcEventDetail.AlarmCode = dcProtoDetail.getAlarmCode();
                dcEventDetail.SinglePlateT3 = dcProtoDetail.getSinglePlateT3();
                dcEventDetail.FrameNo = dcProtoDetail.getFrameNo();
                dcEventDetail.SoftV1 = dcProtoDetail.getSoftV1();
                dcEventDetail.SoftV2 = dcProtoDetail.getSoftV2();
                dcEventDetail.SoftV3 = dcProtoDetail.getSoftV3();
                dcEventDetail.HardV = dcProtoDetail.getHardV();
                dcEventDetail.Reserved1 = dcProtoDetail.getReserved1();
                dcEventDetail.Reserved2 = dcProtoDetail.getReserved2();
                tempDCDetails.add(dcEventDetail);
            });
            dcEvent.DCModularEventDetails = tempDCDetails;

            System.out.println("直流模块数据:" + Utils.objectToJSON(dcEvent));

            collector.collect(dcEvent);
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
            return commMsg.MessageType == DevMessageType.DC_MODULAR_DATA;
        }
    };

}
