<%@ page language="java" contentType="text/html; charset=gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>����-��ȡ�ϴ��ļ��ľ���·��</title>

<script>
function test(){
	var file = document.getElementById("file");
	
	/* firefox
    var reader = new FileReader();
    reader.readAsDataURL(file.files[0]);
    alert(reader.result)
	*/

	var str = "file.value:" + file.value+"\n<br/>";
	str += "file.files[0]ȫ������:";
	for(var p in file.files[0]){
		str += p+";" ;
	}
	str +="\n<br/>������ֵ��\n<br/>";
	for(var p in file.files[0]){
		str += "\t"+p+":"+file.files[0][p]+"\n<br/>"; 
	}
	document.getElementById("inf").innerHTML = str;
}
</script>

</head>
<body>
	<h2>����-��ȡ�ϴ��ļ��ľ���·��</h2>
	
	<input type="file" id="file" value="ѡ���ļ�"/>
	<input type="button" value="�ϴ�" onclick="test();"/>
	
	<hr/>
	<div id="inf" style="width:100%"></div>
</body>
</html>