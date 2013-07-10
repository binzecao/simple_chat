package bin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Download2")
public class Download2 extends HttpServlet {
	private static final long serialVersionUID = 101L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, UnsupportedEncodingException {
		// 获取参数且解码
		String path = req.getParameter("name");
		path = path == null ? "" : new String(path.getBytes("ISO-8859-1"),
				"UTF-8");

		// 输入输出流
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			// 参数为空情况就输出提示
			if (path == "") {
				res.setContentType("text/html;charset=UTF-8");
				bos = new BufferedOutputStream(res.getOutputStream());
				bos.write("参数不能为空".getBytes("UTF-8"));
				return;
			}

			// 文件不存在就输出提示
			if (!new File(path).exists()) {
				res.setContentType("text/html;charset=UTF-8");
				bos = new BufferedOutputStream(res.getOutputStream());
				bos.write("文件不存在".getBytes("UTF-8"));
				return;
			}

			// 获取输入输出流
			bis = new BufferedInputStream(new FileInputStream(path));
			bos = new BufferedOutputStream(res.getOutputStream());

			// 从完整路径中截取文件名
			String fileName = path.replaceAll("^([\\s\\S]*(\\\\|/))", "");

			// 文件名转码
			String agent = req.getHeader("USER-AGENT");
			if (null != agent && -1 != agent.toUpperCase().indexOf("MSIE")) { // IE内核浏览器
				fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				fileName = fileName.replace("+", "%20");// 处理IE文件名中有空格会变成+"的问题;
			} else {// 非IE
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			// 设置响应头
			String type = ContentTypeUtil.getContentTypeByFileName(fileName);
			res.setContentType(type + ";charset=UTF-8");
			res.setContentLength(bis.available());
			res.addHeader("Content-Disposition", "attachment; filename=\"" + fileName+"\"");
			
			// 文件流输出
			byte[] bytes = new byte[2048];
			int len = -1;
			while ((len = bis.read(bytes)) > -1) {
				bos.write(bytes, 0, len);
				bos.flush();
			}
		} catch (IOException ex) {
			// 某些浏览器(如UC)下载时会报 ClientAbortException: java.net.SocketException:
			// Connection reset by peer: socket write error
			// 客户端断开,这个Servlet还在write的错，好像是不可避免的，这里先捕获,使得两个流都能顺利关闭
			if(!"org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getName()))
				 ex.printStackTrace();
		} finally {
			// 关闭流
			Utilities.closeInputStream(bis);
			Utilities.closeOutputStream(bos);
		}
	}
}
