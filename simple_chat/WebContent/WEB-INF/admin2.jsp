<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
input[type=text]{width:900px;}
</style>
</head>
<body>
<h2>Reinitialize the Log4j</h2>
<form action="Admin2" method="post">
	<input type="hidden" name="action" value="reInitLog4j"/>
	<input type="hidden" name="Admin2_Token" value="${requestScope.Admin2_Token}"/>
	<input type="submit" value="Reinitialize the Log4j"/>
</form>
<hr/>

<h2>Reset Log4j config file's path</h2>
<form action="Admin2" method="post">
	<input name="path" type="text" value="${requestScope.configFilePath}"/><br>
	<input type="hidden" name="action" value="resetLog4jPath"/>
	<input type="hidden" name="Admin2_Token" value="${requestScope.Admin2_Token}"/>
	<input type="submit" value="submit"/>
</form>
<hr/>

<h2>Reset DataSource Connect String</h2>
<form action="Admin2" method="post">
	url:<input name="url" type="text" value="${requestScope.url}"/><br>
	username:<input name="username" type="text" value="${requestScope.username}"/><br>
	password:<input name="password" type="password" value="${requestScope.password}"/><br>
	<input type="hidden" name="action" value="resetDataSource"/>
	<input type="hidden" name="Admin2_Token" value="${requestScope.Admin2_Token}"/>
	<input type="submit" value="submit"/> 
</form>
<hr/>
<a href="<%=request.getContextPath() %>/Admin">Back</a>

</body>
</html>