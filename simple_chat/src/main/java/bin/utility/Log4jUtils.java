package bin.utility;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jUtils {
	private static Logger log = Logger.getLogger(Log4jUtils.class);
	private static String configFilePath;
	private static String logFilePath;
	
	/** 初始化Log4j(配置文件路径从web.xml读取)*/
	public static void initialize(ServletContext sc) throws Throwable{
		String prefix = sc.getRealPath("/");
		String suffix = sc.getInitParameter("log4j");
		String path = prefix + suffix;//log4j.properties 路径
		initialize(path, sc);
	}
	
	/** 初始化Log4j(自定义配置文件路径) */
	public static void initialize(String path,ServletContext sc) throws Throwable{
		Properties props = new Properties();
		String prefix = sc.getRealPath("/");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			props.load(fis);
			String path1 = prefix + props.getProperty("log4j.appender.R.File");
			props.setProperty("log4j.appender.R.File", path1);
			PropertyConfigurator.configure(props);
			configFilePath = path;
			logFilePath = path1;
			log.info("initialize Log4j successful");
		}finally {
			Utilities.closeInputStream(fis);
		}
	}
	
	// 这两个方法正常情况下是要做权限检查
	/** 获取配置文件路径*/
	public static String getConfigFilePath() {
		return configFilePath;
	}
	/** 获取日志文件路径*/
	public static String getLogFilePath() {
		return logFilePath;
	}
}
