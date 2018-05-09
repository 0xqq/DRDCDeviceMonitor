package cn.teld.test.drecdevicemonitor;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import com.google.protobuf.InvalidProtocolBufferException;

import comm.teld.cn.event.AlarmDataEvent;
import comm.teld.cn.filter.CommMsg;
import monitor.protobuf.Monitorprotobuf;
import monitor.protobuf.Monitorprotobuf.AlarmReq;

/**
 * @author wangwb
 * 测试数据写人kafka
 */
public class ByteGenerator {
	private static final long serialVersionUID = 119007289730474249L;
	private static boolean running = true;
	public static byte[] headerByteGenerator(byte eventType) throws UnsupportedEncodingException {
		byte[] header = new byte[64];
		//起始符，固定为0x63
		header[0] = 0x63;
		//报文头的长度，目前为0x30
		header[1] = 0x30;
		//协议版本号，第一版为0x01
		header[2] = 0x01;
		//消息体编码类型，Protobuf为0x01
		header[3] = 0x01;
		// 报文Payload的长度
		byte[] payloadLength = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE).order(ByteOrder.BIG_ENDIAN).putInt(20).array();
		header[4] = payloadLength[0];
		header[5] = payloadLength[1];
		header[6] = payloadLength[2];
		header[7] = payloadLength[3];
		//报文的基本类型，详见MessageType定义
		header[8] =eventType;
		//通信节点编号
		byte[] commCode = ByteBuffer.allocate(Short.SIZE/Byte.SIZE).order(ByteOrder.BIG_ENDIAN).putShort((short)1).array();
		header[10] = commCode[0];
		header[11] = commCode[1];
		//字符形式的集控器地址
		byte[] ctrl = "1000000002310002".getBytes("UTF-8");
		for(int i=0; i<ctrl.length; i++){
			header[16 +i] = ctrl[i];
		}
		//前置发送报文的时间(UTC格式)，精确到毫秒
		byte[] fesSendTime = ByteBuffer.allocate(Long.SIZE/Byte.SIZE).order(ByteOrder.BIG_ENDIAN).putLong(System.currentTimeMillis()).array();
		for(int i=0; i<fesSendTime.length; i++){
			header[32 + i] = fesSendTime[i];
		}
		byte[] Guid = "daae2439-78dd".getBytes("UTF-8");
		for(int i=0;i<Guid.length;i++){
			header[48+i] = Guid[i];
		}

System.out.println("header"+header);
		return header;
	}
	//java 合并两个byte数组
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}
	//告警数据通知. 0x58
	public static class AlarmReqByteGenerator  implements SourceFunction<byte[]>{
		private static final long serialVersionUID = 1L;
		long i = 0;
		int j = 0 ;
		@Override
		public void run(SourceContext<byte[]> sourceContext) throws Exception {
			byte[] header = headerByteGenerator((byte) 0x58);
			while(running) {
				System.out.println("111111111111111");
				Monitorprotobuf.AlarmReq.Builder builder = Monitorprotobuf.AlarmReq.newBuilder();
				//随机生成8位数devIndex
				int devIndex = getRandNum(1,99999999);
				builder.setDevIndex(devIndex);

				//随机生成ReasonValue
				//builder.setReasonValue(new Random().nextInt(4));
				builder.setReason(Monitorprotobuf.SendReason.DataChange);
				//builder.setReasonValue(3);
				//builder.setDevTypeValue(new Random().nextInt(16));
				builder.setDevType(Monitorprotobuf.DevDescType.PDU);

				//随机生成11位SN
				String str = randomString(11);
				
				builder.setSN("021607009680TP500K41AA"+j);
				System.out.println("告警数据通知.0x58:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date()))+"    "+j);
				j++;
				//当前系统时间
				monitor.protobuf.Monitorprotobuf.DateTime timeBuild = Monitorprotobuf.DateTime.newBuilder().setTime(System.currentTimeMillis()).build();
				builder.setAlarmTime(timeBuild);
				for(int i=0;i<3;i++){
					Monitorprotobuf.AlarmDataType.Builder alarmDataType = Monitorprotobuf.AlarmDataType.newBuilder();
					alarmDataType.setAlarmState(Monitorprotobuf.AlarmStateEnumType.AbNormal);
					alarmDataType.setAlarmCode(new Random().nextInt(20));
					builder.addAlarmDataList(i, alarmDataType);
				}

				byte[] bytes = builder.build().toByteArray();
				sourceContext.collect(byteMerger(header,bytes));
//				sourceContext.collect(bytes);
				System.out.println("sourceContext:"+bytes.length);
				Thread.sleep(2000);
			}
		}
		@Override
		public void cancel() {
			running = false;
		}
	}
	//生成随机数
	public static int getRandNum(int min, int max) {
		int randNum = min + (int)(Math.random() * ((max - min) + 1));
		return randNum;
	}
	/**
	 * 产生随机字符串
	 * */
	private static Random randGen = null;
	private static char[] numbersAndLetters = null;
	public static final String randomString(int length) {

		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
					"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
			//numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char [] randBuffer = new char[length];
		for (int i=0; i<randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
			//randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
		}
		return new String(randBuffer);
	}
	public static void main(String[] args) throws UnsupportedEncodingException, InvalidProtocolBufferException {
		System.out.println(Monitorprotobuf.SendReason.CallAnswer_VALUE);
		byte[] header = headerByteGenerator((byte) 0x58);
		System.out.println("告警数据通知.0x58:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date())));
		Monitorprotobuf.AlarmReq.Builder builder = Monitorprotobuf.AlarmReq.newBuilder();
		//随机生成8位数devIndex
		int devIndex = getRandNum(1,99999999);
		builder.setDevIndex(devIndex);

		//随机生成ReasonValue
		//builder.setReasonValue(new Random().nextInt(4));
		//builder.setReason(Monitorprotobuf.SendReason.DataChange);
		builder.setReasonValue(0);
		//builder.setDevTypeValue(new Random().nextInt(16));
		builder.setDevType(Monitorprotobuf.DevDescType.CCU);

		//随机生成11位SN
		String str = randomString(11);
		builder.setSN(str);

		//当前系统时间
		monitor.protobuf.Monitorprotobuf.DateTime timeBuild = Monitorprotobuf.DateTime.newBuilder().setTime(System.currentTimeMillis()).build();
		builder.setAlarmTime(timeBuild);
		for(int i=0;i<3;i++){
			Monitorprotobuf.AlarmDataType.Builder alarmDataType = Monitorprotobuf.AlarmDataType.newBuilder();
			alarmDataType.setAlarmState(Monitorprotobuf.AlarmStateEnumType.AbNormal);
			alarmDataType.setAlarmCode(new Random().nextInt(20));
			builder.addAlarmDataList(i, alarmDataType);
		}

		byte[] bytes = builder.build().toByteArray();
		CommMsg msg =new CommMsg();
		msg.Payload = Arrays.copyOfRange(bytes, 0, bytes.length);
		AlarmReq alarmReq = AlarmReq.parseFrom(msg.Payload);
		AlarmDataEvent alarmEvent = new AlarmDataEvent();
		alarmEvent.CanIndex = alarmReq.getDevIndex();
        alarmEvent.SN = alarmReq.getSN();
        alarmEvent.DevDescType = alarmReq.getDevType().getNumber();
        alarmEvent.AlarmSendReason = alarmReq.getReason().getNumber();
        alarmEvent.AlarmTime = alarmReq.getAlarmTime().getTime();
        alarmEvent.AlarmReserved1 = alarmReq.getReserved1();
        alarmEvent.AlarmReserved2 = alarmReq.getReserved2();
        System.out.println("alarmEvent.DevDescType:"+alarmReq.getDevType().getNumber());
        System.out.println("alarmEvent.AlarmSendReason:"+alarmReq.getReason().getNumber());
        System.out.println("alarmEvent.AlarmSendReason1:"+alarmReq.getReason());
	}
}
