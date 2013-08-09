package bin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFileChooser;

public class AjaxLoadServerFiles extends HttpServlet {
	private static final long serialVersionUID = 101L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, UnsupportedEncodingException {

		// 设置响应头
		res.setHeader("Cache-Control", "no-cache");
		res.setContentType("text/html;charset=UTF-8");

		// 从请求获取请求目录地址,"F:\save"
		String dirParam = req.getParameter("dir");
		if (dirParam == null) {
			dirParam = "";
		} else {
			dirParam = java.net.URLDecoder.decode(dirParam, "UTF-8");
		}

		// 假如有，就去掉结尾全部的"\"，再加上一个斜杠,这是为了保证传来的 "F:" 和 "F:\"保持一致,最终变成 "F:\
		if (!dirParam.equals("")) {
			dirParam = dirParam.replaceAll("\\\\+$", "") + "\\";
		}

		// 需要用的工具对象
		JFileChooser jc = new JFileChooser();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		StringBuffer folderStr = new StringBuffer("");
		StringBuffer filesStr = new StringBuffer("");

		// 输出流
		OutputStream os = null;

		try {
			// 如果目录参数不为空，就返回创建该目录的文件对象
			if (!dirParam.equals("")) {
				// 先声明目录文件对象
				File dir = new File(dirParam);

				// 检查目录是否存在
				try {
					if (!dir.exists()) {
						os = res.getOutputStream();
						os.write("{error:'该目录不存在'}".getBytes("UTF-8"));
						return;
					}
				} catch (SecurityException ex) {
					os = res.getOutputStream();
					os.write(("{error:'" + ex.getMessage() + "'}").getBytes("UTF-8"));
					return;
				}

				// 校正用户给的路径与真实情况的大小写不正确 和 多个斜杠的错误，而且保证路径最后带"\"，方便最后输出返回
				dirParam = dir.getCanonicalPath().replaceAll("\\\\+$", "") + "\\";

				// 声明目录下的文件数组
				File[] aFiles = dir.listFiles();

				// 文件无法打开的情况
				if (aFiles == null) {
					os = res.getOutputStream();
					os.write("{error:'文件夹无法打开，没权限或已删除'}".getBytes("UTF-8"));
					return;
				}

				/*
				 * 输出json字符串格式 { currentDir:"D:\\a\\",
				 * folders:[{name:"迅雷下载",date:'2013/06/09
				 * 05:42:53',type:'文件夹'}...],
				 * files:{name:'aa',date:'1999/12/30',type:'mp4',size:'19990
				 * kb',{...},...] }
				 */

				// 遍历目录
				for (int i = 0; i < aFiles.length; i++) {
					if (aFiles[i].isDirectory()) {
						// 文件夹
						folderStr.append("{");
						folderStr.append("name:\"" + aFiles[i].getName() + "\",");
						folderStr.append("date:\"" + sdf.format(new Date(aFiles[i].lastModified())) + "\",");
						folderStr.append("type:\"" + jc.getTypeDescription(aFiles[i]) + "\"");
						folderStr.append("},");
					} else {
						// 文件
						filesStr.append("{");
						filesStr.append("name:\"" + aFiles[i].getName() + "\",");
						filesStr.append("date:'" + sdf.format(new Date(aFiles[i].lastModified())) + "',");
						filesStr.append("type:\"" + jc.getTypeDescription(aFiles[i]) + "\",");
						filesStr.append("size:'" + Utilities.getScale((float) aFiles[i].length() / 1024, 2, BigDecimal.ROUND_HALF_UP) + " KB'");
						filesStr.append("},");
					}
				}
			} else {
				// 参数为空，返回盘符
				File[] aFiles = File.listRoots();

				// 遍历磁盘
				for (int i = 0; i < aFiles.length; i++) {
					// 磁盘
					folderStr.append("{");
					folderStr.append("name:'" + aFiles[i] + "',");
					folderStr.append("date:'" + sdf.format(new Date(aFiles[i].lastModified())) + "',");
					folderStr.append("type:'" + jc.getTypeDescription(aFiles[i]) + "',");
					folderStr.append("size:'" + Utilities.getScale((float) aFiles[i].getFreeSpace() / (1024 * 1024 * 1024), 2, BigDecimal.ROUND_DOWN) + " GB/" + Utilities.getScale((float) aFiles[i].getTotalSpace() / (1024 * 1024 * 1024), 2, BigDecimal.ROUND_DOWN) + " GB'");
					folderStr.append("},");
				}
			}

			// 假如有，去掉 folderStr 和 filesStr 最后一个","
			if (!folderStr.toString().equals("") && folderStr.lastIndexOf(",") == folderStr.length() - 1) {
				folderStr.deleteCharAt(folderStr.length() - 1);
			}
			if (!filesStr.toString().equals("") && filesStr.lastIndexOf(",") == filesStr.length() - 1) {
				filesStr.deleteCharAt(filesStr.length() - 1);
			}

			// 声明要返回的json字符串
			StringBuffer rtnText = new StringBuffer();

			// 这里的currentDir要输出
			rtnText.append("{currentDir:\"" + dirParam + "\",");
			rtnText.append("folders:[" + folderStr + "],");
			rtnText.append("files:[" + filesStr + "]}");

			// 输出json字符串
			os = res.getOutputStream();

			// 这里有关键一步,如："F:\save\"，因为客户端用eval()解释，所以这里帮它多加一个"\"，当作js的转义字符，变成"F:\\save\\"。
			os.write(rtnText.toString().replaceAll("\\\\", "\\\\\\\\").getBytes("UTF-8"));
			os.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			Utilities.closeOutputStream(os);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
