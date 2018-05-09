package comm.teld.cn.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

//
public class SyslogFormatter extends Formatter {
	private final Date dat = new Date();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	@Override
	public String format(LogRecord record) { // synchronized
		dat.setTime(record.getMillis());
		String dateStr = sdf.format(dat);
		
		String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = " : " + sw.toString();
        }
        
        String thrnameORchanid = "";
        if (null != record.getParameters()) {
        	thrnameORchanid = " : " + (String) record.getParameters()[0];
        }

        return dateStr + " [" + record.getLevel() + "] ["
	        	+ record.getThreadID() + thrnameORchanid + "]: "
	        	+ record.getMessage() + throwable + "\n";
	}
	
}
