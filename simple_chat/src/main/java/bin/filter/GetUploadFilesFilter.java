package bin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import bin.utility.ServletContextParams;

/** 访问上传文件夹的静态资源的转码过滤器 */
public class GetUploadFilesFilter implements Filter {
	private ServletContext sc;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		// 地址转码后再Forward
		HttpServletRequest hReq = (HttpServletRequest) req;
		String uploadFolder = (String) sc.getAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME);

		// 匹配servletPath是否以上传文件夹开头，true认为是访问静态资源
		if (hReq.getServletPath().startsWith("/" + uploadFolder + "/")) {
			String path = new String(hReq.getServletPath().toString().getBytes("ISO-8859-1"), "UTF-8");
			req.setCharacterEncoding("UTF-8");
			req.getRequestDispatcher(path).forward(req, res);
		} else {
			fc.doFilter(req, res);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		sc = arg0.getServletContext();
	}

	@Override
	public void destroy() {
	}
}
