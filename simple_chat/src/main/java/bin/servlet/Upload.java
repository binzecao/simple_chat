package bin.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import bin.model.Dialog;
import bin.service.DialogService;
import bin.utility.ServiceContainer;
import bin.utility.ServletContextParams;
import bin.utility.Utilities;

public class Upload extends HttpServlet {
	private static final long serialVersionUID = 101L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		// 获取上传文件的大小
		long size = req.getContentLength();

		// 获取系统最大上传文件大小
		long maxSize = (Long) getServletContext().getAttribute(ServletContextParams.UPLOADFILRS_MAX_SIZE);

		// 比较大小,超出返回错误信息
		if (size > maxSize) {
			String rtnText = "上传失败(1): 文件不能大于" + maxSize / 1024 / 1024 + " MB";
			outputText(res, false, rtnText);
			return;
		}

		// 获取上传文件夹名字
		String folderPath = (String) getServletContext().getAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME);

		// 获取上传在服务器文件夹的路径
		String uploadFolderPath = getServletContext().getRealPath("/" + folderPath);
		File dir = new File(uploadFolderPath);

		// 设置缓冲区目录
		String bufFolderPath = getServletContext().getRealPath("/fileBufferFolder");
		File bufFolderFile = new File(bufFolderPath);

		// 有些服务商连mkdirs()都不允许
		try {
			if (!bufFolderFile.exists()) {
				bufFolderFile.mkdirs();
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} catch (SecurityException ex) {
			ex.printStackTrace();
			outputText(res, false, ex.getMessage());
			return;
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Set factory constraints
		factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
		factory.setRepository(bufFolderFile);// 设置缓冲区目录

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(maxSize); // 设置最大文件尺寸

		// 设置响应编码
		res.setContentType("text/html;charset=UTF-8");

		// 回调函数参数
		boolean arg0 = false;
		String arg1 = "";

		try {
			// 上传文件,并解析出所有的表单字段，包括普通字段和文件字段
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> it = items.iterator();

			// 遍历表单文件
			while (it.hasNext()) {
				FileItem fi = (FileItem) it.next();
				// 判断是否是文件字段，否为文件字段
				if (!fi.isFormField()) {
					String fileName = fi.getName();
					if (fileName != null) {
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

						// 同步块，将正在写入服务器的文件的文件名保存起来，在循环读取判断是否重复,写完后将application中的name移除掉
						// 避免重名就设置新的文件名
						synchronized (this) {
							ServletContext sc = getServletContext();
							List<String> aNames = (List<String>) sc.getAttribute("currentUsingNames");
							if (aNames == null) {
								aNames = new ArrayList<String>();
								sc.setAttribute("currentUsingNames", aNames);
							}
							boolean isRepeat;
							int i = 1;
							do {
								isRepeat = new File(dir.getPath(), fileName).exists();
								if (!isRepeat)
									isRepeat = aNames.contains(fileName);
								if (isRepeat)
									fileName = name + "(" + i++ + ")." + extention;
							} while (isRepeat);
							aNames.add(fileName);
							sc.setAttribute("currentUsingNames", aNames);
						}

						// // 避免重名就设置新的文件名，不考虑并发情况
						// int i = 1;
						// while (new File(dir.getPath(), fileName).exists()) {
						// fileName = name + "(" + i++ + ")." + extention;
						// }

						// 写文件
						File savedFile = new File(uploadFolderPath, fileName);
						fi.write(savedFile);

						// 与上面同步块一起用，移除暂存的文件名
						synchronized (this) {
							ServletContext sc = getServletContext();
							List<String> aNames = (List<String>) sc.getAttribute("currentUsingNames");
							if (aNames != null) {
								int i = aNames.indexOf(fileName);
								if (i > -1)
									aNames.remove(i);
								sc.setAttribute("currentUsingNames", aNames);
							}
						}

						// 网络访问的文件路径
						String webPath = "http://" + req.getServerName() + (req.getServerPort() == 80 ? "" : ":" + req.getServerPort()) + req.getContextPath() + "/" + folderPath + "/" + fileName;

						// 保存上传成功信息至数据库
						Dialog dialog = new Dialog();
						dialog.setTxt(java.net.URLEncoder.encode("[\"" + fileName + "\",\"" + webPath + "\"]", "UTF-8").replaceAll("\\+", "%20"));
						dialog.setType(2);
						dialog.setDate(new Date());
						dialog.setClientIP(req.getRemoteAddr());
						DialogService service = (DialogService) ServiceContainer.getService("dialogService");
						service.save(dialog);

						arg0 = true;
						arg1 = fileName;
					}
				}
			}
		} catch (FileUploadException ex) {
			if (ex instanceof SizeLimitExceededException) {
				// 文件超过预设大小的错误
				arg1 = "上传失败: 文件不能大于" + maxSize / 1024 / 1024 + " MB";
			} else {
				arg1 = "上传失败: " + ex.getMessage();
			}
		} catch (Exception e) {
			arg1 = "上传失败: " + e.getMessage();
		} catch (Throwable e) {
			arg1 = "上传失败: " + e.getMessage();
		} finally {
			// 响应输出
			outputText(res, arg0, arg1);
		}
	}

	private void outputText(HttpServletResponse res, boolean isSuccess, String text) {
		Utilities.outputText(res, "<script>parent.uploadCallBack(" + isSuccess + ",'" + text + "')</script>".toString());
	}
}
