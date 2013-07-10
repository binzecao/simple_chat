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
		$("#inf-panel").html('<span style="color:red">'+fileName+" ��ѡ��</span>");
	}
	function upload(elem){
		var file = $("#file")[0];
		var path = file.value;
		var fileName = path.substring(path.lastIndexOf("\\")+1);
		if(path==""){
			alert("����ѡ�ļ�");
			return;
		}
		$("#inf-panel").html('<span style="color:red">'+fileName+" �����ϴ�...</span>");
		$("#file")[0].form.submit();
		// ����������ť�Ľ���
		setBtnDisalbeState(false);
	}
	function cancelUpload(){
		$("iframe[name='iframe1']").contents()[0].location.reload();
		var file = $("#file")[0];
		var path = file.value;
		var fileName = path.substring(path.lastIndexOf("\\")+1);
		$("#inf-panel").html('<span style="color:red">'+fileName+" �ϴ���ȡ��</span>");
		// ����������ť�Ľ���
		setBtnDisalbeState(true);
	}
	function uploadCallBack(isSuccess,msg){
		if(isSuccess){
			// ��ʾ��Ϣ
			$("#inf-panel").html('<span style="color:red">'+msg+' �ϴ��ɹ�!</span>');
		}else{
			// ��ʾ��Ϣ
			$("#inf-panel").html('<span style="color:red">'+msg+'</span>');
		}
		// ����������ť�Ľ���
		setBtnDisalbeState(true);
	}
	function setBtnDisalbeState(canUpload){
		// ����������ť�Ľ���״̬
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
		<input id="file" type="file" name="file" value="ѡ���ļ�" onchange="selectFile();"/>
		<input id="upload-btn" type="button" value="�ϴ�" onclick="upload(this)"/>
		<input id="cancelUpload-btn" disabled="true" type="button" value="ȡ���ϴ�" onclick="cancelUpload()"/>
	</form>
	<div id="inf-panel"></div>
	<iframe id="iframe1" name="iframe1"></iframe>
</body>
</html>