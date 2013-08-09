<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta HTTP-EQUIV="pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<meta HTTP-EQUIV="expires" CONTENT="0">
<title>Admin</title>
<style>
ul.hints li{color:red;font-size:12px;margin: 0 0 4px 20px;}
p.summary{color: red;font-size: 14px;margin: 10px 0;}
</style>
</head>
<body>
<h2>System Params Setting</h2>
<p class="summary">
	<c:choose>
		<c:when test="${requestScope.setParams_isSuccess==null}"></c:when>
		<c:when test="${requestScope.setParams_isSuccess=='true'}">操作成功！</c:when>
		<c:otherwise>操作失败:</c:otherwise>
	</c:choose>
</p>
<ul class="hints">
	<c:forEach items="${requestScope.setParams_hints}" var="item">
		<li><c:out value="${item}"/></li>
	</c:forEach>
</ul>
<form action="Admin" method="post">
	UPLOADFILES_FOLDER_NAME:
	<input type="text" name="UPLOADFILES_FOLDER_NAME" value="${applicationScope.UPLOADFILES_FOLDER_NAME }"/>
	<br/>
	UPLOADFILRS_MAX_SIZE:
	<input type="text" name="UPLOADFILRS_MAX_SIZE" value="${applicationScope.UPLOADFILRS_MAX_SIZE}"/>
	<br/>
	<input type="hidden" name="operation" value="setParams" />
	<input type="hidden" name="SET_PARAMS_TOKEN" value="${requestScope.SET_PARAMS_TOKEN}" />
	<input type="submit" value="Change it" />
	<input type="reset" value="reset" />
</form>
<hr/>

<h2>Clear the dialogsList</h2>
<p class="summary">
	<c:choose>
		<c:when test="${requestScope.clearDialogs_isSuccess == null}"></c:when>
		<c:when test="${requestScope.clearDialogs_isSuccess =='true'}">清除对话成功!</c:when>
		<c:otherwise>清除对话失败:</c:otherwise>
	</c:choose>
</p>
<ul class="hints">
	<c:if test="${requestScope.clearDialogs_hint != null}">
		<li><c:out value="${requestScope.clearDialogs_hint}" /></li>
	</c:if>
</ul>

<form action="Admin" method="post">
	<input type="hidden" name="operation" value="clearDialogs" />
	<input type="hidden" name="CLEAR_DIALOGS_TOKEN" value="${requestScope.CLEAR_DIALOGS_TOKEN}" />
	<input type="submit" value="Clear the dialogs" />
</form>

<hr/>
<h2>Links</h2>
<a href="<%=request.getContextPath() %>/">Home Page</a><br/>
<a href="<%=request.getContextPath() %>/ShowUploadFiles">Show Upload Files</a><br/>
<a href="<%=request.getContextPath() %>/ShowServerFiles">Show Server's Files</a><br/>

</body>
</html>