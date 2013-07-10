<%@ page language="java" contentType="text/html; charset=gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>测试-获取上传文件的绝对路径</title>

<script>
function test(){
	var file = document.getElementById("file");
	
	/* firefox
    var reader = new FileReader();
    reader.readAsDataURL(file.files[0]);
    alert(reader.result)
	*/

	var str = "file.value:" + file.value+"\n<br/>";
	str += "file.files[0]全部属性:";
	for(var p in file.files[0]){
		str += p+";" ;
	}
	str +="\n<br/>各属性值：\n<br/>";
	for(var p in file.files[0]){
		str += "\t"+p+":"+file.files[0][p]+"\n<br/>"; 
	}
	document.getElementById("inf").innerHTML = str;
}
</script>

</head>
<body>
	<h2>测试-获取上传文件的绝对路径</h2>
	
	<input type="file" id="file" value="选择文件"/>
	<input type="button" value="上传" onclick="test();"/>
	
	<hr/>
	<div id="inf" style="width:100%"></div>
</body>
</html>