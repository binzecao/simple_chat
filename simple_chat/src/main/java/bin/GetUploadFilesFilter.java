package bin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName="GetUploadFilesFilter",urlPatterns={"/uploadFiles/*"})
public class GetUploadFilesFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse res,
			FilterChain fc) throws IOException, ServletException {
		// 地址转码后再Forward
		HttpServletRequest req = (HttpServletRequest) arg0;
		String path = new String(req.getServletPath().toString().getBytes("ISO-8859-1"),"UTF-8");
		req.setCharacterEncoding("UTF-8");
		req.getRequestDispatcher(path).forward(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
