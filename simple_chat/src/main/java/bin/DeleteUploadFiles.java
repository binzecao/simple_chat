package bin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteUploadFiles extends HttpServlet {
	private static final long serialVersionUID = 101L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 返回所需
		res.setContentType("text/html;charset=UTF-8");
		StringBuffer rtnText = new StringBuffer();

		// 得到选中checkbox的值
		String[] names = req.getParameterValues("fileNames");

		// 判断
		if (names == null || names.length == 0) {
			rtnText.append("请先选择文件，再删除!");
		} else {
			// 循环删除文件
			for (String name : names) {
				File file = null;
				String realName = null;
				try {
					realName = java.net.URLDecoder.decode(name, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					rtnText.append(realName + " 删除失败(文件名解码失败)!<br/>");
					continue;
				}
				String uploadFolder = (String) getServletContext().getAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME);
				file = new File(getServletContext().getRealPath("/" + uploadFolder) + "/" + realName);
				if (!file.exists()) {
					rtnText.append(realName + " 删除失败!(文件不存在)<br/>");
					continue;
				}
				if (file.delete())
					rtnText.append(realName + " 已被删除!<br/>");
				else
					rtnText.append(realName + " 删除失败!<br/>");
			}
		}

		// 重定向
		req.getSession().setAttribute("DeleteUploadFiles_hint", rtnText.toString());
		res.sendRedirect(getServletContext().getContextPath() + "/ShowUploadFiles");
	}
}
