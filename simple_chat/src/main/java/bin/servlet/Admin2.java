package bin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bin.utility.DataSource;
import bin.utility.Log4jUtils;
import bin.utility.TokenGen;
import bin.utility.Utilities;

public class Admin2 extends HttpServlet {
	private static final long serialVersionUID = 101L;
	private static Logger log = Logger.getLogger(Admin2.class);
	private static String tokenName = "Admin2_Token";

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 正常情况下这步要有权限判断才决定显示
		try {
			DataSource ds = DataSource.getInstance();
			req.setAttribute("url", ds.getUrl());
			req.setAttribute("username", ds.getUsername());
			req.setAttribute("password", ds.getPassword());
			req.setAttribute("configFilePath", Log4jUtils.getConfigFilePath());
			req.setAttribute(tokenName, TokenGen.generateToken(tokenName, req));
			req.getRequestDispatcher("/WEB-INF/admin2.jsp").forward(req, res);
		} catch (NullPointerException e) {
			Utilities.outputText(res, e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String sLink = "<br><a href='" + req.getContextPath() + "/Admin2'>return</a>";

		// 查验token
		String rTokenValue = req.getParameter(tokenName);
		TokenGen token = TokenGen.getToken(tokenName, req);
		if (token == null || !token.checkValue(tokenName, rTokenValue, req)) {
			res.sendRedirect(getServletContext().getContextPath() + "/Admin2");
			return;
		}

		String action = req.getParameter("action");
		// 根据action办事
		if (action == null || action.equals("")) {
			Utilities.outputText(res, "error in Admin2");
		} else if (action.equalsIgnoreCase("reInitLog4j")) {
			// 尝试读取配置文件,重新初始化Log4j设置
			try {
				Log4jUtils.initialize(getServletContext());
			} catch (Throwable e) {
				Utilities.OutputErrorMsg(res, e);
				return;
			}
			log.info("Re initialized the Log4j setting successful");
			Utilities.outputText(res, "Re initialized the Log4j setting" + sLink);
		} else if (action.equalsIgnoreCase("resetLog4jPath")) {
			// 重设Log4j配置路径
			String newPath = req.getParameter("path");
			try {
				Log4jUtils.initialize(newPath, getServletContext());
			} catch (Throwable e) {
				Utilities.OutputErrorMsg(res, e);
				return;
			}
			Utilities.outputText(res, "Reset the Log4j Path to \"" + newPath + "\"" + sLink);
		} else if (action.equalsIgnoreCase("resetDataSource")) {
			// 重设DataSource连接字符串
			String url = req.getParameter("url");
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			DataSource.resetConneciontString(url, username, password);
			Utilities.outputText(res, url + "<br>" + username + "<br>" + password + sLink);
		}
	}
}
