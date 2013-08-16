package bin.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bin.service.DialogService;
import bin.utility.ServiceContainer;
import bin.utility.ServletContextParams;
import bin.utility.TokenGen;

public class Admin extends HttpServlet {
	private static final long serialVersionUID = 101L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 这里Get方法不进行下面post方法
		// 生成token
		generateToken(req);
		req.getRequestDispatcher("/WEB-INF/admin.jsp").forward(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String sOperation = req.getParameter("operation");

		if (sOperation != null) {
			if (sOperation.equals("setParams")) {
				// 修改参数
				setParams(req, res);
			} else if (sOperation.equals("clearDialogs")) {
				// 清除对话
				clearDialogs(req, res);
			} else {

			}
		}
		// 生成token
		generateToken(req);
		req.getRequestDispatcher("/WEB-INF/admin.jsp").forward(req, res);
	}

	// 修改系统参数
	private void setParams(HttpServletRequest req, HttpServletResponse res) {
		List<String> hints = new ArrayList<String>();// 操作信息
		boolean isSuccess = false; // 操作成功标识

		// token设置
		String tokenName = "SET_PARAMS_TOKEN";
		String rTokenValue = req.getParameter(tokenName);

		// 重复提交检查
		if (!TokenGen.getToken(tokenName, req).checkValue(tokenName, rTokenValue, req)) {
			// 表单已经提交过了
			hints.add("页面刷新、重复操作或操作过期!");
		} else {
			// 获取参数
			String folderName = req.getParameter("UPLOADFILES_FOLDER_NAME");
			String sMaxSize = req.getParameter("UPLOADFILRS_MAX_SIZE");

			// 验证提交参数正确性
			if (folderName == null || folderName.trim().equals("")) {
				hints.add("UPLOADFILES_FOLDER_NAME 不能为空");
			}
			if (Pattern.compile("^[\\s\\S]*(\\\\|/|:|\\*|\\?|\"|<|>|\\|)+[\\s\\S]*$").matcher(folderName).matches()) {
				hints.add("UPLOADFILES_FOLDER_NAME 不能包含" + "\\" + "/" + ":" + "*" + "?" + "\"" + "<" + ">" + "|");
			}
			if (sMaxSize == null || sMaxSize.trim().equals("")) {
				hints.add("UPLOADFILRS_MAX_SIZE 不能为空");
			}
			if (!Pattern.compile("^(-1)|(\\d+)").matcher(sMaxSize).matches()) {
				hints.add("UPLOADFILRS_MAX_SIZE 必须为-1或以上整数，不包含任何特殊字符");
			}
			long maxSize = -1L;
			try {
				maxSize = Long.parseLong(sMaxSize);
			} catch (NumberFormatException ex) {
				hints.add("UPLOADFILRS_MAX_SIZE 太大超出范围");
			}

			// 通过验证,保存参数
			if (hints.size() == 0) {
				getServletContext().setAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME, folderName);
				getServletContext().setAttribute(ServletContextParams.UPLOADFILRS_MAX_SIZE, maxSize);
				isSuccess = true;
			}
		}
		req.setAttribute("setParams_hints", hints);
		req.setAttribute("setParams_isSuccess", isSuccess);
	}

	// 清除对话
	private void clearDialogs(HttpServletRequest req, HttpServletResponse res) {
		// token设置
		String tokenName = "CLEAR_DIALOGS_TOKEN";
		String rTokenValue = req.getParameter(tokenName);
		TokenGen token = TokenGen.getToken(tokenName, req);

		// 重复提交检查
		if (token != null && token.checkValue(tokenName, rTokenValue, req)) {
			// 通过检验
			DialogService service = (DialogService) ServiceContainer.getService("dialogService");
			try {
				service.removeAll();
			} catch (Throwable e) {
				req.setAttribute("clearDialogs_isSuccess", false);
				req.setAttribute("clearDialogs_hint", e.getMessage());
				return;
			}
			req.setAttribute("clearDialogs_isSuccess", true);
		} else {
			// 表单已经提交过了
			req.setAttribute("clearDialogs_isSuccess", false);
			req.setAttribute("clearDialogs_hint", "重复操作或操作过期!");
		}
	}

	private void generateToken(HttpServletRequest req) {
		// 生成token
		String tokenName1 = "SET_PARAMS_TOKEN";
		String tokenName2 = "CLEAR_DIALOGS_TOKEN";
		String tokenValue1 = TokenGen.generateToken(tokenName1, req);
		String tokenValue2 = TokenGen.generateToken(tokenName2, req);
		req.setAttribute(tokenName1, tokenValue1);
		req.setAttribute(tokenName2, tokenValue2);
	}
}
