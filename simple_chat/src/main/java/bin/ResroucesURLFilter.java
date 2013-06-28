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

@WebFilter(filterName="ResroucesURLFilter",urlPatterns={"/uploadFiles/*"})
public class ResroucesURLFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse res,
			FilterChain fc) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) arg0;
		String path = new String(req.getServletPath().toString().getBytes("iso-8859-1"),"utf-8");
		System.out.println(path);
		req.setCharacterEncoding("GBK");
		req.getRequestDispatcher(path).forward(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	
}
