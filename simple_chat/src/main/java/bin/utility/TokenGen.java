package bin.utility;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <b>TokenGen使用方法:</b><br>
 * <p>
 * 1、首先在调用generateToken();
 * </p>
 * <p>
 * 2、再将token值保存到request中XXXX;
 * </p>
 * <p>
 * 3、再在jsp的form中加个标签 < input type="hidden" name="XXXX"
 * value="${requestScope.XXXX}" />
 * <p>
 * 4、在该form的目的Servlet中获取实例,调用其 checkValue()
 * 判断是否重复提交，调用checkValue()会删除session中的token
 * </p>
 * */
public class TokenGen {
	private String value;

	private void setValue(String value) {
		this.value = value;
	}

	/** 获取token值 */
	public String getValue() {
		return value;
	}

	/** 根据名字获取token实例 */
	public static TokenGen getToken(String tokenName, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			return null;
		}
		return (TokenGen) session.getAttribute(tokenName);
	}

	/** 生成token并放入session */
	public static String generateToken(String tokenName, HttpServletRequest req) {
		TokenGen token = new TokenGen();
		String value = Math.random() + "" + new Date().getTime();
		token.setValue(value);
		req.getSession().setAttribute(tokenName, token);
		return value;
	}

	/** 判断Token是否合法,如果合法顺手将之从Session中移除 */
	public synchronized boolean checkValue(String tokenName, String rTokenValue, HttpServletRequest req) {
		if (tokenName == null || tokenName.trim().equals("")) {
			return false;
		}
		if (rTokenValue == null || rTokenValue.trim().equals("")) {
			return false;
		}

		HttpSession session = req.getSession(false);
		if (session == null) {
			return false;
		}

		TokenGen sToken = (TokenGen) session.getAttribute(tokenName);
		if (sToken == null || sToken.getValue().equals("")) {
			return false;
		}

		// 检查完就删掉session中的token
		if (session != null) {
			session.removeAttribute(tokenName);
		}

		return sToken.getValue().equals(rTokenValue);
	}
}
