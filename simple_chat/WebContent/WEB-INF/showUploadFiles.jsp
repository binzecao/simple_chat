<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.File,java.util.*" %>
<%@ page import="java.net.URLEncoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Show Server's Upload Files</title>
<style>
.title{float:left;clear:both}
.main{float:left;clear:both;min-width: 60%;}
.main table{width:100%;border:1px solid #ccc}
.main table td,th{border-bottom:1px solid #ccc;padding: 5px 10px;font-size:14px}
.main table th{background-color:#ddd;text-align: left;}
.main table td span{;width:20px;height:20px;float:left;margin:0 10px 0 0}
.main table td a{float:left}
.main table td a:hover{text-decoration: underline;color:blue;cursor:pointer;}
</style>									 
<link rel="stylesheet" type="text/css" href="resources/css/fileIcon.css" />
</head>
<body>
	<div id="title" class="title">
		<h2>服务器上传文件夹</h2>
	</div>
	<h6 id="callBackText" style="color:red;clear:both;margin:5px;word-break:break-all;">
		${sessionScope.DeleteUploadFiles_hint }
		<% session.removeAttribute("DeleteUploadFiles_hint"); %>
	</h6>
	<div id="main" class="main">
		<form action="DeleteUploadFiles" method="post" id="form1">
			<input type="hidden" value="${requestScope.token}" id="token" name="token"/>
			<input type="submit" value="删除选中文件" />
			<table cellspacing="0">
				<tbody>
					<tr><th><input type="checkbox" onclick="selectAll()"/></th><th>名称</th><th>上传时间</th><th>类型</th><th>大小</th></tr>
					<%
						List<Map<String,String>> list = (List<Map<String,String>>) request.getAttribute("list");
						if(list !=null){
							for(int i=0;i<list.size();i++){
					%>
					<tr>
						<td><input type="checkbox" name="fileNames" value="<%=URLEncoder.encode(list.get(i).get("name"),"UTF-8") %>"/></td>
						<td><span></span><a href="Download?name=<%=URLEncoder.encode(list.get(i).get("name"),"UTF-8") %>"><%=list.get(i).get("name") %></a></td>
						<td><%=list.get(i).get("date") %></td>
						<td><%=list.get(i).get("type") %></td>
						<td><%=list.get(i).get("size") %></td>
					</tr>
					<% }}%>
				</tbody>
			</table>
		</form>
	</div>
	<script src="resources/js/ExtTool.js"></script>
	<script>
		var spans = document.getElementsByTagName("span");	
		for(var i=0;i<spans.length;i++){
			var a = spans[i].nextSibling;
			spans[i].className = ExtTool.getClassName(a.innerHTML);
		}
		function deleteCallback(txt){
			var h6 = document.getElementById("callBackText");
			h6.innerHTML = txt;
		}
		function selectAll(){
			var cbs = document.getElementsByName("fileNames");
			for(var i=0;i<cbs.length;i++){
				cbs[i].checked = !cbs[i].checked;
			}
		}
	</script>
</body>
</html>