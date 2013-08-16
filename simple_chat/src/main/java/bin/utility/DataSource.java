package bin.utility;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataSource {
	private static Logger log = Logger.getLogger(DataSource.class);
	private static DataSource instance;
	private String url;
	private String username;
	private String password;

	private DataSource() {
		if (instance != null) {
			throw new RuntimeException("Invalid to create a new instance");
		}
	}

	/** 初始化 */
	public static void initialize(String path) {
		instance = new DataSource();
		String url = null, username = null, password = null;
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
			NodeList nl = doc.getElementsByTagName("database");
			Element node = (Element) nl.item(0);
			NodeList params = node.getElementsByTagName("param");
			for (int i = 0; i < params.getLength(); i++) {
				Element param = (Element) params.item(i);
				String name = param.getAttribute("name");
				if (name.equalsIgnoreCase("url")) {
					url = param.getFirstChild().getNodeValue();
				}
				if (name.equalsIgnoreCase("username")) {
					username = param.getFirstChild().getNodeValue();
				}
				if (name.equalsIgnoreCase("password")) {
					password = param.getFirstChild().getNodeValue();
				}
			}
			instance.url = url;
			instance.username = username;
			instance.password = password;
			log.info("parse\"" + path + "\" successful, the jdbc url is: " + url);
		} catch (SAXException e) {
			log.error("parse\"" + path + "\" failed,error message:\"" + e.getMessage() + "\"");
		} catch (IOException e) {
			log.error("parse\"" + path + "\" failed,error message:\"" + e.getMessage() + "\"");
		} catch (ParserConfigurationException e) {
			log.error("parse\"" + path + "\" failed,error message:\"" + e.getMessage() + "\"");
		} catch (NullPointerException e) {
			log.error("parse\"" + path + "\" failed,error message:\"" + e.getMessage() + "\"");
		}
	}

	/** 获取实例 */
	public static DataSource getInstance() throws NullPointerException {
		if (instance == null) {
			throw new NullPointerException("dataSource instance has not be created");
		}
		return instance;
	}

	/** 获取连接 */
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	/** 关闭连接资源 */
	public static void closeConnResouces(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

	/** 重新设置连接字符串 */
	public static void resetConneciontString(String url, String username, String password) {
		instance.url = url;
		instance.username = username;
		instance.password = password;
		log.info("reset DataSource Connection String successful, the url is \"" + url + "\"");
	}
	
	// 这三个方法正常情况下是要做权限检查
	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
