package bin.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bin.model.Dialog;
import bin.service.DialogService;
import bin.utility.ServiceContainer;
import bin.utility.Utilities;

public class AjaxLoad extends HttpServlet {
	private static final long serialVersionUID = 101L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.sendError(404);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// "{hasError:true,errorTxt:'error'}" // 错误
		// "{hasError:false,aDialogs:[{did:0,type:1,txt:'a'},{did:1,type:2,txt:'b'}]}"
		// 对话
		// type:1 :普通对话 type:2 上传文件成功提示

		// 得到客户端传来的当前对话Id
		String currentIdParam = (String) req.getParameter("currentId");

		// currentId转类型
		int currentId = -1;
		try {
			currentId = Integer.parseInt(currentIdParam);
		} catch (NumberFormatException ex) {
			Utilities.outputText(res, "{hasError:true,errorTxt:'currentId 不是整形'}");
			return;
		}

		// 得到客户端传来的isLoadAll参数（全部读取的标识）
		String isLoadAllParam = (String) req.getParameter("isLoadAll");
		boolean isLoadAll = Boolean.parseBoolean(isLoadAllParam);

		try {
			// 得到dialogs列表
			DialogService service = (DialogService) ServiceContainer.getService("dialogService");

			// 声明要返回的json字符串
			StringBuffer rtnText = new StringBuffer();
			rtnText.append("{hasError:false,aDialogs:[");

			// 根据是否全部读取情况返回相应字段
			if (isLoadAll) {
				// 返回全部
				List<Dialog> list = service.getAll();
				for (int i = 0; i < list.size(); i++) {
					rtnText.append("{");
					rtnText.append("did:" + list.get(i).getId() + ",");
					rtnText.append("type:" + list.get(i).getType() + ",");
					rtnText.append("txt:'" + list.get(i).getTxt() + "'");
					rtnText.append("}");
					rtnText.append(",");
				}
				// 假如有，去掉最后一个","
				if (rtnText.lastIndexOf(",") == rtnText.length() - 1) {
					rtnText.deleteCharAt(rtnText.length() - 1);
				}
			} else {
				// 只返回最新一条，没最新就不返回
				int maxId = service.getMaxId();
				if (maxId != 0 && currentId < maxId) {
					Dialog d = currentId == -1 ? service.getFirst() : service.getSingle(currentId + 1);
					rtnText.append("{");
					rtnText.append("did:" + d.getId() + ",");
					rtnText.append("type:" + d.getType() + ",");
					rtnText.append("txt:'" + d.getTxt() + "'");
					rtnText.append("}");
				}
			}
			rtnText.append("]}");
			Utilities.outputText(res, rtnText.toString());
		} catch (Throwable e) {
			res.sendError(500, e.getMessage());
		}
	}
}
