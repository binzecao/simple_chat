package bin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Download")
public class Download extends HttpServlet {
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
		String fileName = req.getParameter("name");
		fileName = fileName == null ? "" : new String(fileName.getBytes("ISO-8859-1"), "UTF-8");

		// 输入输出流
		OutputStream os = null;
		InputStream is = null;
		
		try {			
			// 参数为空情况就输出提示
			if (fileName == "") {
				res.setContentType("text/html;charset=UTF-8");
				os = res.getOutputStream();
				os.write("<script>alert('参数不能为空')</script>".getBytes("UTF-8"));
				return;
			}

			// 文件夹文字
			String folderPath = "uploadFiles";

			// 文件相对路径
			String fileRealPath = folderPath + "/" + fileName;

			// 获取文件输入流
			is = getServletContext().getResourceAsStream(fileRealPath);

			// 文件不存在
			if (is == null) {
				res.setContentType("text/html;charset=UTF-8");
				os = res.getOutputStream();
				os.write("<script>alert('文件不存在')</script>".getBytes("UTF-8"));
				return;
			}

			// Servlet输出流
			os = res.getOutputStream();

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
			res.setContentLength(is.available());
			res.addHeader("Content-Disposition", "attachment; filename=\"" + fileName+"\"");
			
			// 文件流输出
			byte[] bytes = new byte[2048];
			int len = 0;
			while ((len = is.read(bytes)) > -1) {
				os.write(bytes, 0, len);
				os.flush();
			}
		}

		catch (IOException ex) {
			// 某些浏览器(如UC)下载时会报 ClientAbortException: java.net.SocketException:
			// Connection reset by peer: socket write error
			// 客户端断开,这个Servlet还在write的错，好像是不可避免的，这里先捕获,使得两个流都能顺利关闭
			if(!"org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getName()))
				 ex.printStackTrace();
		} finally {
			// 关闭流
			Utilities.closeInputStream(is);
			Utilities.closeOutputStream(os);
		}
	}
}
