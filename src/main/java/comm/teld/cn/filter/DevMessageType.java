package comm.teld.cn.filter;




public class DevMessageType {
    // 告警数据通知. 0x58
    public static final byte ALARM_DATA = 0x58;
    
    // 支流模块数据通知. 0x60
    public static final byte DC_MODULAR_DATA = 0x60;
    
    // 交流模块数据通知. 0x62
    public static final byte AC_MODULAR_DATA = 0x62;
    
    // PDU数据通知. 0x64
    public static final byte PDU_DATA = 0x64;
    
    // CCU数据通知. 0x66
    public static final byte CCU_DATA = 0x66;

//        //总控箱数据通知. 0x68
//        public static final byte CONTROLBOX_DATA=0x68;
//        //充电箱变数据通知. 0x70
//        public static final byte ROOM_DATA=0x70;
        
        
        

}
