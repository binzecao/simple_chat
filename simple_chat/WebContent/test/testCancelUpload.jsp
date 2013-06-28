<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script>
	function selectFile(){
		var file = $("#file")[0];
		var path = file.value;
		var fileName = path.substring(path.lastIndexOf("\\")+1);
		$("#inf-panel").html('<span style="color:red">'+fileName+" 已选择</span>");
	}
	function upload(elem){
		var file = $("#file")[0];
		var path = file.value;
		var fileName = path.substring(path.lastIndexOf("\\")+1);
		if(path==""){
			alert("请先选文件");
			return;
		}
		$("#inf-panel").html('<span style="color:red">'+fileName+" 正在上传...</span>");
		$("#file")[0].form.submit();
		// 设置三个按钮的禁用
		setBtnDisalbeState(false);
	}
	function cancelUpload(){
		$("iframe[name='iframe1']").contents()[0].location.reload();
		var file = $("#file")[0];
		var path = file.value;
		var fileName = path.substring(path.lastIndexOf("\\")+1);
		$("#inf-panel").html('<span style="color:red">'+fileName+" 上传已取消</span>");
		// 设置三个按钮的禁用
		setBtnDisalbeState(true);
	}
	function uploadCallBack(isSuccess,msg){
		if(isSuccess){
			// 提示信息
			$("#inf-panel").html('<span style="color:red">'+msg+' 上传成功!</span>');
		}else{
			// 提示信息
			$("#inf-panel").html('<span style="color:red">'+msg+'</span>');
		}
		// 设置三个按钮的禁用
		setBtnDisalbeState(true);
	}
	function setBtnDisalbeState(canUpload){
		// 设置三个按钮的禁用状态
		$("#file").attr("disabled",!canUpload);
		$("#upload-btn").attr("disabled",!canUpload);
		$("#cancelUpload-btn").attr("disabled",canUpload);
	}
	$(function(){
		setBtnDisalbeState(true);
	});
	
</script>
<body>	
	<form action="Upload" enctype="multipart/form-data" method="post" target="iframe1">
		<input id="file" type="file" name="file" value="选择文件" onchange="selectFile();"/>
		<input id="upload-btn" type="button" value="上传" onclick="upload(this)"/>
		<input id="cancelUpload-btn" disabled="true" type="button" value="取消上传" onclick="cancelUpload()"/>
	</form>
	<div id="inf-panel"></div>
	<iframe id="iframe1" name="iframe1"></iframe>
</body>
</html>