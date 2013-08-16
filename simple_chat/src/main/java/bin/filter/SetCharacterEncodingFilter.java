package bin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SetCharacterEncodingFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		// 将所有请求的编码都转成UTF-8，这里主要解决Header的中文问题
		// 如form-data; name="file";
		// filename="新建文本文档 (3).xml",在eclipse运行没问题，直接放在Tomcat上乱码...
		req.setCharacterEncoding("UTF-8");
		fc.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
