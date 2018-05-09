package comm.teld.cn.common.config;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ConfigBean implements Serializable{
    private static final long serialVersionUID = 2055149560137985457L;
    
    public String environmentDataCenter=null;  //运行环境
    public long checkpointDuration=60000;

    public String kafkaDeviceMonitorSourceTopic = null;
    public Properties kafkaDeviceMonitorSourceProperties = null;
    
    public boolean drdcIsSentinelModel=true;
    public String drdcMasterName=null;
    public Set<String> drdcSentinels=new HashSet<String>(3);
    public String drdcPassword=null;
    public int drdcIndexDB;
    public int drdcTimeout;
    public int drdcPoolSize;
    public String drdcAloneHost;
    public int drdcAlonePort;
    
    public String kafkaDeviceMonitorSinkTopic=null;
    public Properties kafkaDeviceMonitorSinkProperties=null;
    
    public String rMQDeviceMonitorSinkHost;
    public int rMQDeviceMonitorSinkPort = 5672;
    public String rMQDeviceMonitorSinkUername;
    public String rMQDeviceMonitorSinkPassword;
    public String rMQDeviceMonitorSinkVirtualHost = "/";
    public String rMQDeviceMonitorSinkQueueName="";
    @Override
    public String toString() {
        return "ConfigBean [environmentDataCenter=" + environmentDataCenter + ", checkpointDuration="
                        + checkpointDuration + ", kafkaDeviceMonitorSourceTopic=" + kafkaDeviceMonitorSourceTopic
                        + ", kafkaDeviceMonitorSourceProperties=" + kafkaDeviceMonitorSourceProperties
                        + ", drdcIsSentinelModel=" + drdcIsSentinelModel + ", drdcMasterName=" + drdcMasterName
                        + ", drdcSentinels=" + drdcSentinels + ", drdcPassword=" + drdcPassword + ", drdcIndexDB="
                        + drdcIndexDB + ", drdcTimeout=" + drdcTimeout + ", drdcPoolSize=" + drdcPoolSize
                        + ", drdcAloneHost=" + drdcAloneHost + ", drdcAlonePort=" + drdcAlonePort
                        + ", kafkaDeviceMonitorSinkTopic=" + kafkaDeviceMonitorSinkTopic
                        + ", kafkaDeviceMonitorSinkProperties=" + kafkaDeviceMonitorSinkProperties
                        + ", rMQDeviceMonitorSinkHost=" + rMQDeviceMonitorSinkHost + ", rMQDeviceMonitorSinkPort="
                        + rMQDeviceMonitorSinkPort + ", rMQDeviceMonitorSinkUername=" + rMQDeviceMonitorSinkUername
                        + ", rMQDeviceMonitorSinkPassword=" + rMQDeviceMonitorSinkPassword
                        + ", rMQDeviceMonitorSinkVirtualHost=" + rMQDeviceMonitorSinkVirtualHost + "]";
    }
    
    
}
