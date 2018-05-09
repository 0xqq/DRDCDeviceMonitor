package comm.teld.cn.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.flink.api.common.functions.MapFunction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import comm.teld.cn.filter.CommMsg;
import comm.teld.cn.log.LoggerUtils;

public class Utils {
	public static final String TimeMillisFormat = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(TimeMillisFormat);
	private static final ObjectMapper objectMapper =new ObjectMapper();

	public static String millTimeToStr(long time) {
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return dateFormat.format(time);
	}

	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
			sb.append(' ');
		}
		return sb.toString();
	}

	public static String objectToJSON(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static CommMsg CommMsg1(byte[] buff){
		System.out.print("buff:"+buff.length);
		CommMsg msg = new CommMsg();
		try {
			int intProtocalVersion = buff[2];
			msg.ProtocolVersion = intProtocalVersion + "";
			msg.MessageType = buff[8];
			msg.CommServerCode = ByteBuffer.wrap(buff,10,2).order(ByteOrder.BIG_ENDIAN).getShort();
			msg.CtrlAddress = new String(buff,16,16, "UTF-8").trim();
			msg.FesSendTime = ByteBuffer.wrap(buff,32,8).order(ByteOrder.BIG_ENDIAN).getLong();
			// 上行RtpSendTime应该有flink填充，flink模块叫做FlinkProcessTimeTag
			msg.FlinkProcessTimeTag = System.currentTimeMillis();
			ByteBuffer bbPackageId = ByteBuffer.wrap(buff, 48, 16);
			long high = bbPackageId.getLong();
			long low = bbPackageId.getLong();
			UUID packageId = new UUID(high,low);
			msg.PackageId = packageId.toString();
			msg.Payload = Arrays.copyOfRange(buff, 64, buff.length);
			//System.out.println("msg.Payload"+bytesToHexString(msg.Payload));
			//System.out.println("flink清洗数据结果："+Utils.objectToJSON(msg));
			//LoggerUtils.sysLogger.info("msg.Payload"+bytesToHexString(msg.Payload));
			//LoggerUtils.sysLogger.info("flink清洗数据结果："+Utils.objectToJSON(msg));
			System.out.println("CtrlAddress:"+msg.CtrlAddress);
		} catch (Exception e) {
			System.out.println("报文头有问题");
			e.printStackTrace();
		}
		return msg;
	}
	public static void main(String[] args) {
//		byte[] buff = new byte[64];
////		buff [1]= 'a';
//		byte[] aa= {99,11};
//		Utils.CommMsg1(buff);
		
	}
	/**
	 * 将Byte数组转变为通用的数据包类型，以方便后续处理
	 */
	public static MapFunction<byte[], CommMsg> devMonitorMsgMapFunction = (byte[] buff) -> {
		System.out.print("buff:"+buff.length);
		CommMsg msg = new CommMsg();
		try {
			int intProtocalVersion = buff[2];
			msg.ProtocolVersion = intProtocalVersion + "";
			msg.MessageType = buff[8];
			msg.CommServerCode = ByteBuffer.wrap(buff,10,2).order(ByteOrder.BIG_ENDIAN).getShort();
			msg.CtrlAddress = new String(buff,16,16, "UTF-8").trim();
			msg.FesSendTime = ByteBuffer.wrap(buff,32,8).order(ByteOrder.BIG_ENDIAN).getLong();
			// 上行RtpSendTime应该有flink填充，flink模块叫做FlinkProcessTimeTag
			msg.FlinkProcessTimeTag = System.currentTimeMillis();
			ByteBuffer bbPackageId = ByteBuffer.wrap(buff, 48, 16);
			long high = bbPackageId.getLong();
			long low = bbPackageId.getLong();
			UUID packageId = new UUID(high,low);
			msg.PackageId = packageId.toString();
			msg.Payload = Arrays.copyOfRange(buff, 64, buff.length);
			System.out.println("msg.Payload"+bytesToHexString(msg.Payload));
			System.out.println("flink清洗数据结果："+Utils.objectToJSON(msg));
			LoggerUtils.sysLogger.info("msg.Payload"+bytesToHexString(msg.Payload));
			LoggerUtils.sysLogger.info("flink清洗数据结果："+Utils.objectToJSON(msg));
			System.out.println("CtrlAddress:"+msg.CtrlAddress);
		} catch (Exception e) {
			System.out.println("报文头有问题");
			e.printStackTrace();
		}
		return msg;
	};


}
