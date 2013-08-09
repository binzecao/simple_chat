package bin;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SystemInitListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("servlet context destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("servlet context Initializing...");
		ServletContext sc = sce.getServletContext();

		String folderName = sc.getInitParameter("UPLOADFILES_FOLDER_NAME");
		folderName = folderName == null ? "" : folderName;

		String maxSize = sc.getInitParameter("UPLOADFILRS_MAX_SIZE");
		long max_size = maxSize == null ? 10485760 : Long.parseLong(maxSize);

		sc.setAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME, folderName);
		sc.setAttribute(ServletContextParams.UPLOADFILRS_MAX_SIZE, max_size);
	}
}
