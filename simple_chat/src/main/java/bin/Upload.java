package bin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/Upload")
@MultipartConfig
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 101L;

	public Upload() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		Part file = null;

		// 获取上传的文件
		try {
			file = req.getPart("file");
		} catch (IOException ex) {
			// org.apache.tomcat.util.http.fileupload.FileUploadBase$IOFileUploadException: 
			// 	Processing of multipart/form-data request failed. Stream ended unexpectedly
			//System.err.println(ex.getMessage() + "\n可能是用户取消上传");
			return;
		} catch (IllegalStateException e) {
			System.err.println("1");
			e.printStackTrace();
		} catch (ServletException e) {
			System.err.println("2");
			e.printStackTrace();
		}

		// 设置响应编码
		res.setContentType("text/html;charset=UTF-8");

		// 输出流
		OutputStream os = null;

		try {
			// file对象为null
			if (file == null) {
				os = res.getOutputStream();
				os.write("<script>parent.uploadCallBack(false,'上传失败：上传的文件为空')</script>".getBytes("UTF-8"));
				return;
			}

			// 获取上传的文件名
			// form-data; name="file"; filename="新建文本文档 (3).xml"
			String temp = file.getHeader("content-disposition").split(";")[2].trim();
			String fileName = temp.substring(10, temp.length() - 1);
			
			// 上传在服务器文件夹文字
			String folderPath = "uploadFiles";

			// 找到上传文件夹，没有就创建一个
			File dir = new File(getServletContext().getRealPath("/" + folderPath));
			if (!dir.exists()) {
				dir.mkdir();
			}

			// 分离文件名和扩展名
			String name = "";
			String extention = "";
			int index = fileName.lastIndexOf(".");
			if (index == -1) {
				name = fileName;
			} else {
				name = fileName.substring(0, index);
				extention = fileName.substring(index + 1);
			}

			// 避免重名就设置新的文件名
			int i = 1;
			while (new File(dir.getPath() + "//" + fileName).exists()) {
				fileName = name + "(" + i + ")." + extention;
				i++;
			}

			// 将文件写入服务器上
			try {
				file.write(dir.getPath() + "//" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
				String rtnText = "<script>parent.uploadCallBack(false,'文件写入服务器本地磁盘时出错')</script>";
				os = res.getOutputStream();
				os.write(rtnText.getBytes("UTF-8"));
			}

			// 文件路径
			String filePath = "http://" + req.getServerName() + ":"
					+ req.getServerPort() + req.getContextPath() + "/"
					+ folderPath + "/" + fileName;

			// 保存上传成功信息
			DialogManager.saveDialog(getServletContext(), "[\"" + fileName + "\",\"" + filePath + "\"]", 2);
			
			// 输出js回调函数
			String rtnText = "<script>parent.uploadCallBack(true,'" + fileName + "')</script>";
			os = res.getOutputStream();
			os.write(rtnText.getBytes("UTF-8"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			Utilities.closeOutputStream(os);
		}
	}
}
