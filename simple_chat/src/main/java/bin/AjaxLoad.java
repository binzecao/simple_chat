package bin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxLoad extends HttpServlet {
	private static final long serialVersionUID = 101L;

	public AjaxLoad() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		// "{hasError:true,errorTxt:'error'}" // 错误
		// "{hasError:false,aDialogs:[{did:0,type:1,txt:'a'},{did:1,type:2,txt:'b'}]}"
		// 对话
		// type:1 :普通对话 type:2 上传文件成功提示

		// 基本设置
		res.setContentType("text/html;charset=UTF-8");
		res.setHeader("Cache-Control", "no-cache");

		OutputStream os = null;
		try {
			os = res.getOutputStream();

			// 得到客户端传来的当前对话Id
			String currentIdParam = (String) req.getParameter("currentId");

			// currentId转类型
			int currentId = -1;
			try {
				currentId = Integer.parseInt(currentIdParam);
			} catch (NumberFormatException ex) {
				os.write("{hasError:true,errorTxt:'currentId 不是整形'}".getBytes());
				return;
			}

			// 得到客户端传来的isLoadAll参数（全部读取的标识）
			String isLoadAllParam = (String) req.getParameter("isLoadAll");
			boolean isLoadAll = Boolean.parseBoolean(isLoadAllParam);

			// 得到dialogs列表
			ArrayList<Dialog> list = DialogManager.getDialogsList(getServletContext());

			// 声明要返回的json字符串
			StringBuffer rtnText = new StringBuffer();
			rtnText.append("{hasError:false,aDialogs:[");

			// 根据是否全部读取情况返回相应字段
			if (isLoadAll) {
				// 返回全部
				for (int i = 0; i < list.size(); i++) {
					rtnText.append("{");
					rtnText.append("did:" + list.get(i).getId() + ",");
					rtnText.append("type:" + list.get(i).getType() + ",");
					rtnText.append("txt:'" + java.net.URLEncoder.encode(list.get(i).getTxt(), "UTF-8").replaceAll("\\+", "%20") + "'");
					rtnText.append("}");
					rtnText.append(",");
				}
				// 假如有，去掉最后一个","
				if (rtnText.lastIndexOf(",") == rtnText.length() - 1) {
					rtnText.deleteCharAt(rtnText.length() - 1);
				}
			} else {
				// 只返回最新一条，没最新就不返回
				if (list.size() != 0 && currentId < list.get(list.size() - 1).getId()) {
					rtnText.append("{");
					rtnText.append("did:" + list.get(currentId + 1).getId() + ",");
					rtnText.append("type:" + list.get(currentId + 1).getType() + ",");
					rtnText.append("txt:'" + java.net.URLEncoder.encode(list.get(currentId + 1).getTxt(), "UTF-8").replaceAll("\\+", "%20") + "'");
					rtnText.append("}");
				}
			}
			rtnText.append("]}");

			// 返回客户端
			os.write(rtnText.toString().getBytes("UTF-8"));
			os.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			Utilities.closeOutputStream(os);
		}
	}
}
