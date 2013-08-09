package bin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 普通下载，不支持断点续传
 */
public class Download2 extends HttpServlet {
	private static final long serialVersionUID = 101L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, UnsupportedEncodingException {
		// 获取文件名且解码
		String fileName = req.getParameter("name");
		fileName = fileName == null ? "" : new String(fileName.getBytes("ISO-8859-1"), "UTF-8");

		// 获取文件所在目录
		String dir = req.getParameter("dir");
		String folderName = (String) getServletContext().getAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME);
		dir = dir == null ? getServletContext().getRealPath("/" + folderName) : new String(dir.getBytes("ISO-8859-1"), "UTF-8");

		// 输入输出流
		OutputStream os = null;
		InputStream is = null;

		try {
			// 参数为空情况就输出提示
			if (dir.equals("") || fileName.equals("")) {
				res.setContentType("text/html;charset=UTF-8");
				os = res.getOutputStream();
				os.write("参数不能为空<script>alert('参数不能为空')</script>".getBytes("UTF-8"));
				return;
			}

			File file = new File(dir, fileName);

			// 文件不存在就输出提示
			if (!file.exists()) {
				res.setContentType("text/html;charset=UTF-8");
				os = res.getOutputStream();
				os.write("文件不存在<script>alert('文件不存在')</script>".getBytes("UTF-8"));
				// res.sendError(404);
				return;
			}

			// 获取输入输出流
			is = new FileInputStream(dir + File.separator + fileName);
			os = res.getOutputStream();

			// 设置响应头
			String type = ContentTypeUtil.getContentTypeByFileName(fileName);
			res.setContentType(type + ";charset=UTF-8");
			res.setContentLength(is.available());
			res.addHeader("Content-Disposition", "attachment; filename=\"" + Utilities.encodeFileName(req, fileName) + "\"");

			// 文件流输出
			byte[] bytes = new byte[2048];
			int len = -1;
			while ((len = is.read(bytes)) > -1) {
				os.write(bytes, 0, len);
				os.flush();
			}
		} catch (IOException ex) {
			// 某些浏览器(如UC)下载时会报 ClientAbortException: java.net.SocketException:
			// Connection reset by peer: socket write error
			// 客户端断开,这个Servlet还在write的错，好像是不可避免的，这里先捕获,使得两个流都能顺利关闭
			if (!"org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getName())){
				ex.printStackTrace();
				Utilities.outputText(res, ex.getMessage());
			}
		} finally {
			// 关闭流
			Utilities.closeInputStream(is);
			Utilities.closeOutputStream(os);
		}
	}
}
