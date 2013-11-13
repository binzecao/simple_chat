<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<pre>
如果报异常！！：No suitable driver found for jdbc
先要看服务器有没有加mysql的连接包，如果已经存在，那么试一试先调用这句
Class.forName("com.mysql.jdbc.Driver");
</pre>

<body>
	<%
		String MYSQL_USERNAME = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
		String MYSQL_PASSWORD = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
		String MYSQL_DATABASE_HOST = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
		String MYSQL_DATABASE_PORT = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
		String MYSQL_DATABASE_NAME = "simple_chat";
		String MYSQL_DATABASE_DRIVER = "com.mysql.jdbc.Driver";
		String characterEncoding = "UTF-8";
		String url = "jdbc:mysql://" + MYSQL_DATABASE_HOST + ":" + MYSQL_DATABASE_PORT + "/" + MYSQL_DATABASE_NAME + "?autoReconnect=true&useUnicode=true&characterEncoding=" + characterEncoding;
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Connection conn = DriverManager.getConnection(url, MYSQL_USERNAME, MYSQL_PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from dialog");
			while (rs.next()) {
				out.println(rs.getInt(1));
				out.println(rs.getString(2));
			}
			rs.close();
			stmt.close();
			conn.close();
			out.println("ok");
		} catch (SQLException e) {
			e.printStackTrace();
			out.println(e.getMessage());
		}
	%>
</body>
</html>