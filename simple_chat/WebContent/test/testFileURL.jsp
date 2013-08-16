<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>测试-获取上传文件的绝对路径</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
span{color:coral;font-weight:bold;}
</style>
<script>
	function test() {
		var file = document.getElementById("file");

		/* firefox
		var reader = new FileReader();
		reader.readAsDataURL(file.files[0]);
		alert(reader.result)
		 */
		if (file.files) {
			var str = "file.value:" + "<span>" + file.value + "</span>" + "\n<br/>";
			str += "file.files[0]全部属性:";
			for ( var p in file.files[0]) {
				str += "<span>"+ p + "</span>" + ",";
			}
			str += "\n<br/>各属性值：\n<br/>";
			for ( var p in file.files[0]) {
				str += "\t" + p + ":" + "<span>" + file.files[0][p] + "</span>" + "\n<br/>";
			}
			document.getElementById("inf").innerHTML = str;
		} else {// ie8 或以下
			window.onerror = function(err) {
				if (err.indexOf('Automation') != -1) {
					alert('没有访问文件的权限');
					return true;
				} else {
					return false;
				}
			};
			var fso = new ActiveXObject('Scripting.FileSystemObject');
			var ieFile = fso.GetFile(fileName);
			window.onerror = window.oldOnError;
			document.getElementById("inf").innerHTML = "file 大小：" + ieFile.size;
		}
	}
</script>

</head>
<body>
	<h2>测试-获取上传文件的绝对路径</h2>

	<input type="file" id="file" value="选择文件" />
	<input type="button" value="test" onclick="test();" />

	<hr />
	<div id="inf" style="width: 100%"></div>
</body>
</html>