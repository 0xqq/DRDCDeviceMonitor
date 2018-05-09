package comm.teld.cn.log;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;



public class LoggerUtils {

    public static Logger recordLogger =null;
    public static Logger sysLogger = null;
    
    public static void initSysLogger(Properties prop) {
    	//读取日志路径，建立目录
    	String logFilePath=prop.getProperty("logFileUrl");
    	if(logFilePath==null || logFilePath.equals("")) {
    		System.err.println("setting.properties: logFilePath error");
    		Runtime.getRuntime().exit(1);
    	}
    	File logFile=new File(logFilePath);
    	if(!logFile.exists())
    		logFile.mkdirs();
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if ((null != handlers) && (handlers.length > 0)) {
        	for(Handler handler : handlers) {
        		if (handler instanceof ConsoleHandler) {
        			rootLogger.removeHandler(handler);
        		}
        	}
        }
        sysLogger = Logger.getLogger("comm.sysLogger");
        sysLogger.setUseParentHandlers(false);
        sysLogger.setLevel(Level.FINE);
        FileHandler fileHandler = null;
		try {
			// (rotating=3 size=20MB) should be a parameter
			fileHandler = new FileHandler(logFilePath+"comm%g.log", 20000000, 3,true); //"D:\\logs\\rtu\\"
			fileHandler.setFormatter(new SyslogFormatter());
			fileHandler.setLevel(Level.FINE);
		} catch (IOException ie) {
			ie.printStackTrace();
			return;
		}
        sysLogger.addHandler(fileHandler);
    }

    public static void initRecordLogger(Properties prop) {
    	//读取日志路径，建立目录
    	String logFilePath=prop.getProperty("logFileUrl");
    	if(logFilePath==null || logFilePath.equals("")) {
    		System.err.println("setting.properties: logFilePath error");
    		Runtime.getRuntime().exit(1);
    	}
    	File logFile=new File(logFilePath);
    	if(!logFile.exists())
    		logFile.mkdirs();
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if ((null != handlers) && (handlers.length > 0)) {
        	for(Handler handler : handlers) {
        		if (handler instanceof ConsoleHandler) {
        			rootLogger.removeHandler(handler);
        		}
        	}
        }
        recordLogger = Logger.getLogger("record.sysLogger");
        recordLogger.setUseParentHandlers(false);
        recordLogger.setLevel(Level.FINE);
        FileHandler fileHandler = null;
		try {
			// (rotating=3 size=20MB) should be a parameter
			fileHandler = new FileHandler(logFilePath+"record%g.log", 20000000, 3,true); //"D:\\logs\\rtu\\"
			fileHandler.setFormatter(new SyslogFormatter());
			fileHandler.setLevel(Level.FINE);
		} catch (IOException ie) {
			ie.printStackTrace();
			return;
		}
        recordLogger.addHandler(fileHandler);
    }
}
