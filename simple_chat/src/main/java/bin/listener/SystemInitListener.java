package bin.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import bin.utility.DataSource;
import bin.utility.Log4jUtils;
import bin.utility.ServiceContainer;
import bin.utility.ServletContextParams;

public class SystemInitListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Logger log = Logger.getLogger(SystemInitListener.class);
		log.info("System has shut down");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();

		// 初始化log4j
		try {
			Log4jUtils.initialize(sc);
		} catch (Throwable e) {
			System.err.println(e.getMessage());
		}
		
		// 获取日志对象
		Logger log = Logger.getLogger(SystemInitListener.class);
		log.info("System start initializing...");

		// 初始化系统参数
		String folderName = sc.getInitParameter("UPLOADFILES_FOLDER_NAME");
		folderName = folderName == null ? "uploadFiles" : folderName;

		String maxSize = sc.getInitParameter("UPLOADFILRS_MAX_SIZE");
		long max_size = maxSize == null ? 10485760 : Long.parseLong(maxSize);

		sc.setAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME, folderName);
		sc.setAttribute(ServletContextParams.UPLOADFILRS_MAX_SIZE, max_size);

		// 初始化数据库连接
		DataSource.initialize(sc.getRealPath("/WEB-INF/datastore.xml"));
		
		// 初始化service容器
		ServiceContainer.initialize(sc.getRealPath("/WEB-INF/service.xml"));
	}
}
