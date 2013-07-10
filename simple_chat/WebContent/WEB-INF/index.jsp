<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Simple Chat</title>
<style>
.dialog{width:100%;border:1px solid #ccc;padding:5px;float:left}
.dialog-content{float:left;word-break: break-all;}
.dialog-file{color: #4280DC;font-size: 12px;vertical-align: middle;}
.odd{background-color:#eee}
.copy-btn{float:right;clear:both;}
</style>
<meta http-equiv="Expires" CONTENT="0">
<meta http-equiv="Cache-Control" CONTENT="no-cache">
<meta http-equiv="Pragma" CONTENT="no-cache">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script>
$(function(){
	// 全局变量
	failedConnTime = 0;//定义连接服务器次数
	timeoutId = null;
	
	//初始化编辑器
	initInputer();
	
	// 设置三个按钮的禁用
	setBtnDisalbeState(true);
	
	// 异步加载对话
	ajaxLoad(true);
});

function ajaxLoad(isLoadAll){
	// 获取当前对话最大ID，没有则发送-1
	var currentId = $(".dialog:first").attr("dialog-id");
	if(currentId == null) currentId = -1;
	$.ajax({
		url: "AjaxLoad",
		type:"post",
		dataType:"text",
		data:{currentId:currentId,isLoadAll:isLoadAll},
		timeout:20000,
		success:function(rtnText){
			//"{hasError:true,errorTxt:'error'}" // 错误
			//"{hasError:false,aDialogs:[{did:0,type:1,txt:'a'},{did:1,type:2,txt:'b'}]}"  // 对话
			//type:1 :普通对话    type:2 上传文件成功提示
			
			// 转义字符"\"变为"\\",使eval不出错
			rtnText.replace(/\\/g,"\\\\");
			
			// 解析返回的json对象
			var rtnObj = eval("("+rtnText+")");
			
			// 出错提示
			if(rtnObj.hasError){
				alert(rtnObj.errorTxt);
				return
			}
			
			// 获取对话数组
			var aDialogs = rtnObj.aDialogs;		
			
			// 遍历对话数组添加到界面
			for(var i=0;i<aDialogs.length;i++){
				// 解释字符串
				var did = aDialogs[i].did;
				var txt = aDialogs[i].txt;
				var type = aDialogs[i].type;
				
				// 设置dialog样式
				var className = "dialog" + ($('.dialog:first').hasClass("odd") ? "" : " odd");
				
				var html = "";
				// 添加对话
				if(type==1){
					// 对话为文本
					html = '<div class="'+className+'" dialog-id="'+did+'" onclick="showCopyBtn(this)">'+
						'<span class="dialog-content">'+txt+'</span>'+
						'</div>';
				}else if(type==2){
					// 对话为上传提示
					var aFile = eval(txt);
					var name = encodeURI(aFile[0]);
					html = '<div class="'+className+'" dialog-id="'+did+'">'+
						'<span class="dialog-content dialog-file">文件：<i>'+aFile[0]+'</i> 已经成功上传到服务器！</span>'+
						'<a href="Download?name='+name+'" onclick="downloadFile(this,event)">下载</a>'+
						'<a href="'+encodeURI(aFile[1])+'" style="margin-left:30px">直接浏览</a>'+
						'</div>';
				}else{
					alert("marking another type");
				}
				if($(".dialog").size()==0){
					$(html).appendTo('#dialog-panel');
				}else{
					$(html).insertBefore('.dialog:first');
				}
			}
		},
		error:function(evt){
			if((evt.readyState==0 && evt.status==0) || (evt.readyState==4 && evt.status==404)){
				// 无法连接服务器
				failedConnTime++;
			}else{
				// 其他异常
				alert("error in ajaxLoad\n"+
						"readyState: "+evt.readyState+
						"\nstatus: "+evt.status+
						"\nstatusText: "+evt.statusText);
			}
		},
		complete:function(){
			// 无法连接服务器的操作
			if(failedConnTime>3){ //无法连接超过10次 
				alert("无法连接服务器,请刷新页面再试!");
				clearTimeout(timeoutId);
			}else{
				// 成功异步成功连接后处理
				// 这种方法保证只有一条异步连接
				timeoutId = setTimeout(function(){ajaxLoad(false);},1500);
			}
		}}
	);
}
function speak(){
	var content = $("#inputer").contents()[0].body.innerHTML;
	// chrome换行用div(从第二行开始加)，ie、opera用p, 火狐用<br>
	// 先将<div><br></div>变为<div></div>，再将<p>&nbsp;</p>变为<p></p>,然后<div>、</p>、<br>作为换行标识变为<br/>，再清除掉多余的<p>、<\div>、\r\n、\n，最后将后面的所有的换行抛弃，即将后面所有的<br/>清除掉
	content = content.replace(/<div>(<br>|<br\/>)<\/div>/gi,"<div></div>").replace(/<p>&nbsp;<\/p>/gi,"<p></p>").replace(/(<div>|<\/p>|<br>)/gi,"<br/>").replace(/(<p>|<\/div>|\r\n|\n)/gi,"").replace(/(<br\/>)*$/gi,"");
	// 除了空格回车其他什么都没按当作没输入，提示且返回
	if(content.replace(/(<br\/>|&nbsp;|\s)/gi,"")==""){
		alert("plz say something!");
		return;
	}
	$.ajax({
		url:"Speak",
		type:"post",
		data:{content:content},
		dataType:"text",
		success:function(resText){
			$("#inputer").contents()[0].body.innerHTML = "";
		},
		error:function(evt){
			alert("error in Speak\n"+
					"readyState: "+evt.readyState+
					"\nstatus: "+evt.status+
					"\nstatusText: "+evt.statusText);
		}
	});
}
function showCopyBtn(elem){
	// 将复制按钮加在点击的dialog里
	$("#copy-btn").appendTo(elem).end().show();
}
function copyAll(evt,elem){	
	var html = elem.previousSibling.innerHTML;
	var editorDoc = $("#inputer").contents()[0];
	editorDoc.body.innerHTML = html;
	// 取消冒泡
	evt = evt ? evt:window.event;
	if(evt.stopPropagation){
		evt.stopPropagation();
	}else{
		evt.cancelBubble = true;
	}
}
function reset(){
	var editorDoc = $("#inputer").contents()[0];
	editorDoc.body.innerHTML ="";
}
function selectFile(){
	// 提示信息
	var fileName = $("#file")[0].value.substring($("#file")[0].value.lastIndexOf("\\")+1);
	$("#inf-panel").html('<span style="color:red">'+fileName+" 已选择</span>");
}
function upload(){	
	// 检查是否选择了文件
	var fileName = $("#file")[0].value.substring($("#file")[0].value.lastIndexOf("\\")+1);
	if(fileName==""){alert("请先选文件");return;}
	// 提示信息
	$("#inf-panel").html('<span style="color:red">'+fileName+" 正在上传...</span>");
	// 移除再添加响应的iframe1
	$("iframe[name='iframe1']").remove();
	$('<iframe name="iframe1" style="display: none"></iframe>').appendTo(document.body);
	// 使用form 的 响应 Target指向 新建的 iframe1;
	$("#file")[0].form.target = "iframe1";
	// 提交上传文件的表单
	$("#file")[0].form.submit();
	// 设置三个按钮的禁用
	setBtnDisalbeState(false);
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
};
function cancelUpload(){
	// 移除iframe1中断，再使得上传的流中断
	$("iframe[name='iframe1']").remove();
	// 提示信息
	var fileName = $("#file")[0].value.substring($("#file")[0].value.lastIndexOf("\\")+1);
	$("#inf-panel").html('<span style="color:red">'+fileName+" 上传已取消</span>");
	// 设置三个按钮的禁用
	setBtnDisalbeState(true);
}
function downloadFile(elem,evt){
	// 去掉a标签默认的跳转动作
	if(evt.preventDefault){
		evt.preventDefault();
	}else{
		event.returnValue = false;
	}
	// 使流响应在iframe,使服务端的脚本可以响应在iframe中实现无页面跳转
	var name = "tempDown"+(new Date().getTime()+Math.random()+"").replace(/\./,"");
	$('<iframe name="'+name+'" style="display:none"></iframe>').appendTo(document.body);
	$("iframe[name='"+name+"']").contents()[0].write(
			'<script>window.location.href="'+elem.href+'";<'+'/script>');
	// 5秒后自动删除这iframe,避免过多iframe存在(但是5000过后ireFox就不能下载了,暂无解决方法)
	// setTimeout("$(\"iframe[name='"+name+"']\").remove();",5000);
}
function setBtnDisalbeState(canUpload){
	// 设置三个按钮的禁用状态
	$("#file").attr("disabled",!canUpload);
	$("#upload-btn").attr("disabled",!canUpload);
	$("#cancelUpload-btn").attr("disabled",canUpload);
}
function initInputer(){
	// 初始化编辑器
	setTimeout(function(){
		var editorDoc = $("#inputer").contents()[0];
		editorDoc.open();
		editorDoc.write("<html><head>");
		editorDoc.write("<html><head><style>");
		editorDoc.write("body{word-break:break-all;font-family: sans-serif;font-size: 12px;}");
		editorDoc.write("p,div{margin:1px 0}");
		editorDoc.write("</style></head><body></body></html>")
		editorDoc.close();
		editorDoc.designMode = "on";
		editorDoc.contentEditable = true;
	},10);
}
</script>
</head>
<body>

<div id="dialog-panel" style="width:100%;"></div>
<hr/>
<div id="operation-panel" style="width:100%">
	<iframe id="inputer" style="width:100%;height:300px;"></iframe>
	<input type="button" value="提交" onclick="speak();"/>
	<input type="button" value="重设" onclick="reset();"/>
	
	<hr/>
	<form action="Upload" enctype="multipart/form-data" method="post">
		<input id="file" type="file" name="file" value="选择文件" onchange="selectFile();"/>
		<input id="upload-btn" type="button" value="上传" onclick="upload(this)"/>
		<input id="cancelUpload-btn" disabled="true" type="button" value="取消上传" onclick="cancelUpload()"/>
	</form>
</div>

<div id="inf-panel" style="width:100%"></div>

<input type="button" value="copy" id="copy-btn" style="display:none;" onclick="copyAll(event,this)"/>

</body>
</html>