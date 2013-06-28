package bin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
			throws ServletException, IOException {
		// 获取文件名
		String fileName = req.getParameter("name");
		// 参数为空就
		if(fileName==null || fileName == ""){
			OutputStream os = res.getOutputStream();
			res.setContentType("charset=GBK");
			os.write("<script>alert('文件不存在')</script>".getBytes("gbk"));
			if(os!=null) os.close();
			return;
		}
		
		// 文件名转码
		fileName = new String(fileName.getBytes("iso-8859-1"),"gbk");
		
		// 文件夹文字
		String folderPath = "uploadFiles";
		
		// 文件相对路径
		String fileRealPath = folderPath+"/"+fileName;
		
		// 获取文件输入流
		InputStream is = getServletContext().getResourceAsStream(fileRealPath);
		
		// 获取jsp响应输出流f
		OutputStream os = res.getOutputStream();
		
		// 文件是否存在
		if(is == null){
			res.setContentType("charset=GBK");
			os.write("<script>alert('文件不存在')</script>".getBytes("gbk"));
		}else{
			// 响应设置
			res.setContentLength(is.available());
			res.setContentType("charset=GBK");
			res.addHeader("Content-Disposition", "attachment; filename=" + 
					 new String(fileName.getBytes("GBk"),"ISO-8859-1"));
		
			// 输出文件流
			byte[] bytes = new byte[512];
			int len = 0;
			while((len = is.read(bytes))>-1){
				os.write(bytes, 0, len);
			}
		}
		
		os.flush();
		if(os !=null) os.close();
		if(is !=null) is.close();
	}
}
