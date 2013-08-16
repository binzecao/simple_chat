package bin.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bin.utility.Log4jUtils;
import bin.utility.Utilities;

public class ShowLog extends HttpServlet {
	private static final long serialVersionUID = 101L;
	private static Logger log = Logger.getLogger(ShowLog.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		String path = Log4jUtils.getLogFilePath();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
			StringBuffer sb = new StringBuffer();
			sb.append("<div>");
			String sbuf;
			while ((sbuf = br.readLine()) != null) {
				sb.append(sbuf + "<br/>");
			}
			sb.append("</div>");
			sb.append("<script>");
			sb.append("var str = document.getElementsByTagName('div')[0].innerHTML;");
			sb.append("str=str.replace(/\"(.*?)\"\\s*<br\\s*\\/?>/gi,'\"<span>$1</span>\"<br>');");
			sb.append("document.body.innerHTML = str+'<style>span{color:red;font-weight:bold}</style>';");
			sb.append("</script>");
			Utilities.outputText(res, sb.toString());
		} catch (IOException ex) {
			log.error(ex.getMessage());
			Utilities.outputText(res, "error message:" + ex.getMessage());
		} catch (NullPointerException ex) {
			log.debug(ex.getMessage());
			Utilities.outputText(res, "plz init the Log4j first, error message:" + ex.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		Utilities.outputText(res, "error in ShowLog");
	}
}
