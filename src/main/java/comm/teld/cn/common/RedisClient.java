package comm.teld.cn.common;

import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import comm.teld.cn.common.config.ConfigBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class RedisClient {
    
    private static volatile Pool<Jedis> pool = null;
    private static final GenericObjectPoolConfig CONFIG = new GenericObjectPoolConfig();
    static {
        CONFIG.setMinIdle(32); // 初始化链接
        CONFIG.setTestOnBorrow(true);
        CONFIG.setTestOnReturn(false);
        CONFIG.setTestWhileIdle(false);
        CONFIG.setBlockWhenExhausted(true); // false
        CONFIG.setMaxWaitMillis(5000); // 1000毫秒 获取不到链接的等待时间
    }

    public static Jedis getJedis(ConfigBean conf) {
        if (pool == null) {
            synchronized (RedisClient.class) {
                if (pool == null) {
                    CONFIG.setMaxTotal(conf.drdcPoolSize);
                    CONFIG.setMaxIdle(conf.drdcPoolSize);
                    if (conf.drdcIsSentinelModel) {
                        pool = new JedisSentinelPool(conf.drdcMasterName, conf.drdcSentinels, CONFIG, conf.drdcTimeout,
                                        conf.drdcPassword, conf.drdcIndexDB);
                    } else {
                        pool = new JedisPool(CONFIG, conf.drdcAloneHost, conf.drdcAlonePort, conf.drdcTimeout,
                                        conf.drdcPassword, conf.drdcIndexDB);
                    }
                }
            }
        }
        return pool.getResource();
    }
   
    public static List<String> getStationInfo(Jedis jedis, String hashId){
        return jedis.hmget(hashId, "StaCode", 
                                                   "StaName", 
                                                   "OperMaintain", 
                                                   "StaProCode", 
                                                   "CtrlVersionCode", 
                                                   "CtrlType",
                                                   "ActualOper", 
                                                   "StaType", 
                                                   "StaTypeName",
                                                   "StaIndustryType",
                                                   "StaCity",
                                                   "OperType",
                                                   "IsTestStation",
                                                   "LastUpdateTime"
                                    );
    }
    
//    public static String getPoolInfo() {
//    	if (pool == null) {
//    		return null;
//    	}
//		StringBuilder sb = new StringBuilder();
//		sb.append("CurrentHostMaster:");
//		if (GlbData.useRedisSentinel) {
//			sb.append(((JedisSentinelPool) pool).getCurrentHostMaster());
//		} else {
//			sb.append("not use redis sentinel");
//		}
//		sb.append(",MaxBorrowWaitTimeMillis:");
//		sb.append(pool.getMaxBorrowWaitTimeMillis());
//		sb.append(",MeanBorrowWaitTimeMillis:");
//		sb.append(pool.getMeanBorrowWaitTimeMillis());
//		sb.append(",NumActive:");
//		sb.append(pool.getNumActive());
//		sb.append(",NumIdle:");
//		sb.append(pool.getNumIdle());
//		sb.append(",NumWaiters:");
//		sb.append(pool.getNumWaiters());
//		return sb.toString();
//	}
}
