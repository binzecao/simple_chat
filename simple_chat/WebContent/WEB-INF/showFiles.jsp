<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Show Server's Files</title>
<link rel="stylesheet" type="text/css" href="resources/css/fileIcon.css" />
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
<script src="resources/js/jquery-1.9.1.js"></script>
<script src="resources/js/ExtTool.js"></script>
<script>
function refreshTable(dir){
	$.ajax({
		url: "AjaxLoadServerFiles",
		type:"post",
		dataType:"text",
		data:{dir:dir},
		timeout:20000,
		success:function(rtnText){
			/* 接收的currentDir 尾部在服务端已经设置了带"\\",除了为空情况
			{
				currentDir:"D:\\a\\",
				folders:[name:'迅雷下载',date:'2013/06/09 05:42:53',type:'文件夹',},
				files:{name:'aa',date:'1999/12/30',type:'mp4',size:'19990 kb',{...},...]
			}
			*/
			
			// 解释返回字符串
			var rtnObj = eval("("+rtnText+")");
	
			// 有错就报
			if(rtnObj.error){
				alert(rtnObj.error);
				return;
			}
			
			// 当前路径、文件夹数组、文件数组
			var currentDir = rtnObj.currentDir;
			var aFolders = rtnObj.folders;
			var aFiles = rtnObj.files;
			
			// 拼写html
			var html = "<tr><th>名称</th><th>修改日期</th><th>类型</th><th>大小</th></tr>";
			
			// 不是盘符目录就显示上一层的链接
			if(currentDir != ""){
				// 上层目录，为盘符目录就为空
				var parentDir = currentDir.replace(/(([^\\])+\\+)$/,"");
				html += '<tr><td><span class="return"></span><a value="'+parentDir+'" onclick="openFolder(event,this)">..(上层目录)</a></td><td></td><td></td><td></td></tr>';
			}
			
			// 处理文件夹 <a value="c:\ss\a\" .../>
			for(var prop in aFolders){
				var file = aFolders[prop];
				var path = (currentDir+file.name).replace(/\\+$/,"") + "\\";
				var className = path.split("\\").length-1>1 ? "folder" : "disk";
				path = encodeURIComponent(path);
				html += '<tr><td><span class="'+className+'"></span><a value="'+path+'" onclick="openFolder(event,this);">'+
					file.name+'</a></td><td>'+file.date+'</td><td>'+file.type+'</td><td>'+(file.size?file.size:"")+'</td></tr>';
			}
			// 处理文件  <a value="c:\a.txt" .../>
			for(var prop in aFiles){
				var file = aFiles[prop];
				var className = ExtTool.getClassName(file.name);
				html += '<tr><td><span class="'+className+'"></span>'+
					'<a href="Download?dir='+encodeURIComponent(currentDir)+
					'&name='+encodeURIComponent(file.name)+'">'+
					file.name+'</a></td><td>'+file.date+'</td><td>'+file.type+'</td><td>'+file.size+'</td></tr>';
			}
			
			// 设置标题的路径显示
			$("#title h2").html("当前路径:&nbsp;"+currentDir);
			// 填充表格
			$("#main table tbody").html(html);
		},
		error:function(evt){
			alert("error obj:"+evt);
		}
	});
}
function openFolder(evt,elem){
	// 去掉a标签默认的跳转动作
	if(evt.preventDefault){
		evt.preventDefault();
	}else{
		event.returnValue = false;
	}
	// 发送
	refreshTable($(elem).attr("value"));
}
function openFile(evt,elem){
	// 去掉a标签默认的跳转动作
	if(evt.preventDefault){
		evt.preventDefault();
	}else{
		event.returnValue = false;
	}
	// 打开新窗口
	//window.open("Download2?name=" + h);
}
function submit(){
	// 提交地址刷新目录表格
	refreshTable(encodeURIComponent($("#inputDir").val()));
}
</script>
</head>
<body>
	<div id="title" class="title">
		<h2>当前路径:&nbsp;</h2>
		<p>输入目录：<input id="inputDir" type="text" style="width:600px;"/></p>
		<p><input type="button" value="提交" onclick="submit();"/></p>
	</div>
	<div id="main" class="main">
		<table cellspacing="0">
			<tbody>
			</tbody>
		</table>
	</div>
</body>
</html>