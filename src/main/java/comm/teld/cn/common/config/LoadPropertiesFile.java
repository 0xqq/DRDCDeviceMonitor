package comm.teld.cn.common.config;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class LoadPropertiesFile {

	private Properties m_prop = null;

	public LoadPropertiesFile(String configFileName) {
		readPropertiesFile(configFileName);
	}

    private void readPropertiesFile(String configFileName) {
        try (InputStream inputStream = LoadPropertiesFile.class
                        .getResourceAsStream("/" + configFileName.trim() + ".properties")) {
            m_prop = new Properties();
            m_prop.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private String getPropValueByKey(String key) throws Exception {
		return m_prop.getProperty(key);
	}

	private Properties getPropValueSetByKey(String key) throws Exception {
		Properties properties = new Properties();
		Set<String> propKeys = m_prop.stringPropertyNames();
		for (String propKey : propKeys) {
			if (propKey.startsWith(key)) {
				properties.put(propKey.replaceFirst(key + ".", ""), m_prop.getProperty(propKey).trim());
			}
		}
		return properties;
	}

	public ConfigBean getConfigBean() throws Exception {
		ConfigBean configBean = new ConfigBean();
		configBean.environmentDataCenter =getPropValueByKey("environmentDataCenter").trim();
		configBean.checkpointDuration=Long.parseLong(getPropValueByKey("flink.checkpointduration").trim());
		configBean.kafkaDeviceMonitorSourceTopic = getPropValueByKey("kafkaDeviceMonitorSourceTopic").trim();
		configBean.kafkaDeviceMonitorSourceProperties = getPropValueSetByKey("kafkaDeviceMonitorSourceProperties");
		
		configBean.drdcIsSentinelModel=Boolean.getBoolean(getPropValueByKey("redisDRDC.isSentinelModel").trim());
		configBean.drdcMasterName=getPropValueByKey("redisDRDC.masterName").trim();
		configBean.drdcSentinels=Arrays.asList(getPropValueByKey("redisDRDC.sentinels").split(",")).stream().collect(Collectors.toSet());
		configBean.drdcPassword=getPropValueByKey("redisDRDC.password");
		configBean.drdcIndexDB=Integer.parseInt(getPropValueByKey("redisDRDC.indexDB").trim());
		configBean.drdcTimeout=Integer.parseInt(getPropValueByKey("redisDRDC.timeout").trim());
		configBean.drdcPoolSize=Integer.parseInt(getPropValueByKey("redisDRDC.poolSize").trim());
		configBean.drdcAloneHost=getPropValueByKey("redisDRDC.aloneHost").trim();
		configBean.drdcAlonePort=Integer.parseInt(getPropValueByKey("redisDRDC.alonePort").trim());
		
		configBean.kafkaDeviceMonitorSinkTopic=getPropValueByKey("kafkaDeviceMonitorSinkTopic").trim();
		configBean.kafkaDeviceMonitorSinkProperties=getPropValueSetByKey("kafkaDeviceMonitorSinkProperties");

        configBean.rMQDeviceMonitorSinkHost = getPropValueByKey("rMQDeviceMonitorSink.host").trim();
        configBean.rMQDeviceMonitorSinkPort = Integer.parseInt(getPropValueByKey("rMQDeviceMonitorSink.port").trim());
        configBean.rMQDeviceMonitorSinkUername = getPropValueByKey("rMQDeviceMonitorSink.uername").trim();
        configBean.rMQDeviceMonitorSinkPassword = getPropValueByKey("rMQDeviceMonitorSink.password").trim();
        configBean.rMQDeviceMonitorSinkVirtualHost = getPropValueByKey("rMQDeviceMonitorSink.virtualHost").trim();
        configBean.rMQDeviceMonitorSinkQueueName = getPropValueByKey("rMQDeviceMonitorSink.queueName").trim();
        
		return configBean;
	}

}