package comm.teld.cn.map;

import java.util.HashMap;
import java.util.List;

import org.apache.flink.api.common.functions.RichMapFunction;

import comm.teld.cn.common.RedisClient;
import comm.teld.cn.common.Utils;
import comm.teld.cn.common.config.ConfigBean;
import comm.teld.cn.event.BaseDTO;
import comm.teld.cn.filter.CommMsg;
import monitor.protobuf.Monitorprotobuf.MonitorSignalNotifyReq;
import monitor.protobuf.Monitorprotobuf.MonitorSignalType;
import monitor.protobuf.Monitorprotobuf.MonitorStateNotifyReq;
import monitor.protobuf.Monitorprotobuf.MonitorStateType;
import monitor.protobuf.Monitorprotobuf.MonitorTelemetryNotifyReq;
import monitor.protobuf.Monitorprotobuf.MonitorTelemetryType;
import redis.clients.jedis.Jedis;


@Deprecated //遥信，遥测，状态演示时使用
public class DevRuningMonitorRichMap extends RichMapFunction<CommMsg, BaseDTO> {

	private static final long serialVersionUID = 7339306172016060630L;
	 private final ConfigBean configBean;
	
	public DevRuningMonitorRichMap(ConfigBean configBean) {
	    this.configBean=configBean;
	}
	
	@Override
	public BaseDTO map(CommMsg value) throws Exception {
		Jedis jedis = null;
        try {
            jedis = RedisClient.getJedis(configBean);
            if (null != jedis) {
        		String hashKey="";
        		String hashField="";
				String hashValue = "";
				long lastUpdateTime=0;
        		switch (value.MessageType) {
        		case 0x52: // yao ce
        			MonitorTelemetryNotifyReq telemetryNotifyReq = MonitorTelemetryNotifyReq.parseFrom(value.Payload);
        			lastUpdateTime=telemetryNotifyReq.getSendTime().getTime();
        			List<MonitorTelemetryType> telemetryList = telemetryNotifyReq.getDataListList();
        			HashMap<String, String> teleHashMap=new HashMap<>();
        			for (MonitorTelemetryType telemetory : telemetryList) {
						int devIndex = telemetory.getDevIndex();
						String sn = telemetory.getDevName();
						hashKey = "dcmodular".equals(telemetory.getDevType()) ? "MD" : "acmodular".equals(telemetory.getDevType()) ? "ND" : "ErrorPreKey";
						if ("ErrorPreKey".equals(hashKey)) {
							System.err.println("EventProcessor, Error DevType:" + telemetory.getDevType() + ", "	+ value.CtrlAddress + ", " + devIndex);
							continue;
						}
						if(hashKey.equals("MD")) { //direct
							if ("".equals(sn) || "".equals(value.CtrlAddress)) {
								System.err.println("EventProcessor, Error devIndex:" + value.CtrlAddress + ", sn:" + sn);
								continue;
							}
							hashKey = hashKey + ":" + value.CtrlAddress + ":" + sn;
						} else {
							if (devIndex==0 || "".equals(value.CtrlAddress)) {
								System.err.println("EventProcessor, Error devIndex:" + value.CtrlAddress + ", devIndex:" + devIndex);
								continue;
							}
							hashKey = hashKey + ":" + value.CtrlAddress + ":" + devIndex;
						}
						hashField = telemetory.getMeasureName();
						hashValue = String.valueOf(telemetory.getValue());
						teleHashMap.put(hashField, hashValue);
						teleHashMap.put("LastUpdateTime", Utils.millTimeToStr(lastUpdateTime));
        			}
                    if (teleHashMap.size() > 0) {
                        jedis.hmset(hashKey, teleHashMap);
                    }
        			break;
        		case 0x54: // yao xin
					MonitorSignalNotifyReq signalNotifyReq = MonitorSignalNotifyReq.parseFrom(value.Payload);
					List<MonitorSignalType> singnalList = signalNotifyReq.getDataListList();
					lastUpdateTime=signalNotifyReq.getSendTime().getTime();
					HashMap<String, String> signHashMap=new HashMap<>();
					for (MonitorSignalType signal : singnalList) {
						int devIndex = signal.getDevIndex();
						String sn = signal.getDevName();
						hashKey = "dcmodular".equals(signal.getDevType()) ? "MD" : "acmodular".equals(signal.getDevType()) ? "ND" : "ErrorPreKey";
						if ("ErrorPreKey".equals(hashKey)) {
							System.err.println("EventProcessor, Error DevType:" + signal.getDevType() + ", "
									+ value.CtrlAddress + ", " + devIndex);
							continue;
						}
						if(hashKey.equals("MD")) { //direct
							if ("".equals(sn) || "".equals(value.CtrlAddress)) {
								System.err.println("EventProcessor, Error devIndex:" + value.CtrlAddress + ", sn:" + sn);
								continue;
							}
							hashKey = hashKey + ":" + value.CtrlAddress + ":" + sn;
						} else {
							if (devIndex==0 || "".equals(value.CtrlAddress)) {
								System.err.println("EventProcessor, Error devIndex:" + value.CtrlAddress + ", devIndex:" + devIndex);
								continue;
							}
							hashKey = hashKey + ":" + value.CtrlAddress + ":" + devIndex;
						}
						hashField = signal.getMeasureName();
						hashValue = String.valueOf(signal.getValue().getNumber());
						signHashMap.put(hashField, hashValue);
						signHashMap.put("LastUpdateTime", Utils.millTimeToStr(lastUpdateTime));
					}
                    if (signHashMap.size() > 0) {
                        jedis.hmset(hashKey, signHashMap);
                    }
        			break;
        		case 0x56: //state
        			MonitorStateNotifyReq stateNotifyReq = MonitorStateNotifyReq.parseFrom(value.Payload);
        			List<MonitorStateType> stateList = stateNotifyReq.getDataListList();
        			lastUpdateTime=stateNotifyReq.getSendTime().getTime();
        			HashMap<String, String> stateHashMap = new HashMap<>();
        			for(MonitorStateType state: stateList) {
        				int devIndex=state.getDevIndex();
        				String sn = state.getDevName();
        				hashKey = "dcmodular".equals(state.getDevType()) ? "MD": "acmodular".equals(state.getDevType()) ? "ND" : "ErrorPreKey";
						if (hashKey == "ErrorPreKey") {
							System.err.println("EventProcessor, Error DevType:" + state.getDevType() + ", " + value.CtrlAddress + ", " + devIndex);
							continue;
						}
						if(hashKey.equals("MD")) { //direct
							if ("".equals(sn) || "".equals(value.CtrlAddress)) {
								System.err.println("EventProcessor, Error devIndex:" + value.CtrlAddress + ", sn:" + sn);
								continue;
							}
							hashKey = hashKey + ":" + value.CtrlAddress + ":" + sn;
						} else {
							if (devIndex==0 || "".equals(value.CtrlAddress)) {
								System.err.println("EventProcessor, Error devIndex:" + value.CtrlAddress + ", devIndex:" + devIndex);
								continue;
							}
							hashKey = hashKey + ":" + value.CtrlAddress + ":" + devIndex;
						}
						hashField = state.getMeasureName();
						hashValue = String.valueOf(state.getValue());
						stateHashMap.put(hashField, hashValue);
						stateHashMap.put("LastUpdateTime", Utils.millTimeToStr(lastUpdateTime));
        			}
                    if (stateHashMap.size() > 0) {
                        jedis.hmset(hashKey, stateHashMap);
                    }
        			break;
        		default:
        			System.err.println("EventProcessor, unknown messageType " + value.MessageType + ", " + value.CtrlAddress);
        			break;
        		}
            } else {
                System.err.println("jedis is null");
            }
        } catch (Exception e) {
        	System.err.println( "EventProcessor run(), " +e.toString()+", "+Utils.millTimeToStr(System.currentTimeMillis())+", "+value.CtrlAddress+", message type"+value.MessageType+", "+Utils.bytesToHexString(value.Payload));
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
		return new BaseDTO();
	}
	
}
