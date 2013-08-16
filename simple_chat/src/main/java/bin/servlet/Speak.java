package bin.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bin.model.Dialog;
import bin.service.DialogService;
import bin.utility.ServiceContainer;
import bin.utility.Utilities;

public class Speak extends HttpServlet {
	private static final long serialVersionUID = 101L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.sendError(404);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 得到客户端发来的内容和对话最大id
		String txt = (String) req.getParameter("content");

		// 保存对话
		Dialog dialog = new Dialog();
		dialog.setTxt(txt);
		dialog.setType(1);
		dialog.setDate(new Date());
		dialog.setClientIP(req.getRemoteAddr());

		try {
			DialogService service = (DialogService) ServiceContainer.getService("dialogService");
			if (service.save(dialog)) {
				Utilities.outputText(res, "{\"hasError\":false}");
				return;
			}
			String str = "{\"hasError\":true,\"errorText\":\"提交失败，请重新提交或者刷新页面重试\"}";
			Utilities.outputText(res, str);
		} catch (Throwable e) {
			String str = "{\"hasError\":true,\"errorText\":\"" + e.getMessage() + "\"}";
			Utilities.outputText(res, str);
		}
	}
}
