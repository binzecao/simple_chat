package bin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Speak extends HttpServlet {
	private static final long serialVersionUID = 101L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 提交对话不能通过get方法
		Utilities.outputText(res, "error in speak");
	}

	protected synchronized void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 得到客户端发来的内容和对话最大id
		String txt = (String) req.getParameter("content");

		// 保存对话
		DialogManager.saveDialog(getServletContext(), txt, 1);
	}
}
