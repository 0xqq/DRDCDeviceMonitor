package cn.teld.test.drecdevicemonitor;

import comm.teld.cn.common.config.ConfigBean;
import comm.teld.cn.common.config.LoadPropertiesFile;
import comm.teld.cn.log.LoggerUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer08;



public class ByteGeneratorTest {


    public static void main(String[] args) {
    	 
        try {
        	LoadPropertiesFile loadPropertiesFile = new LoadPropertiesFile(args[0]);
            ConfigBean configBean = loadPropertiesFile.getConfigBean();
            System.out.println("配置文件:"+configBean.toString());
          
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            //env.enableCheckpointing(configBean.checkpointDuration);//启用检查点

            //数据生成器  不断生成原始报文数据，写入kafka
            ByteGenerator(env,configBean);

            //读取原始报文数据，转换为事件并写入kafka--MsgApp.java中实现的内容，本次重点测试的内容
            //MagApp(env,config);

            env.execute("ByteGenerator--");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 数据生成器  不断生成原始报文数据，写入kafka
     * 二进制
     * 只生成包体
     * @param env
     * @param config
     */
    private static void ByteGenerator(StreamExecutionEnvironment env,ConfigBean configBean) throws Exception {
        // 告警数据通知. 0x58
        FlinkKafkaProducer08<byte[]> myProducer = new FlinkKafkaProducer08<byte[]>(configBean.kafkaDeviceMonitorSourceTopic,
        		new ByteArraySchema(), configBean.kafkaDeviceMonitorSourceProperties);

        // 告警数据通知. 0x58
        env.addSource(new ByteGenerator.AlarmReqByteGenerator()).name("AlarmReq Source")
                .addSink(myProducer).name("AlarmReq Sink");
    }

}
