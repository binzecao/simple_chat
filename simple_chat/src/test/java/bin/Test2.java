package bin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;

import org.junit.Ignore;
import org.junit.Test;

public class Test2 {
	@Test
	@Ignore
	public void t1(){
		long s1 = System.currentTimeMillis();
		String filesPath = "D:\\";
		System.out.println(filesPath);
		File dir = new File(filesPath);
		if(!dir.exists()){
			
		}
				
		/* json obj example
		{folders:[name:'迅雷下载',date:'2013/06/09 05:42:53',type:'文件夹',},
		files:{name:'aa',date:'1999/12/30',type:'mp4',size:'19990 kb',{...},...]}
		*/
		JFileChooser jc= new JFileChooser();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		StringBuffer folderStr = new StringBuffer();
		StringBuffer filesStr = new StringBuffer();
		
		// 
		File[] aFiles = dir.listFiles();
		System.out.println(aFiles.length);
		for(int i=0;i<aFiles.length;i++){
			if(aFiles[i].isDirectory()){
				// 文件夹
				folderStr.append("{");
				folderStr.append("name:'"+aFiles[i].getName()+"',");
				folderStr.append("date:'"+sdf.format(new Date(aFiles[i].lastModified()))+"',");
				folderStr.append("type:'"+jc.getTypeDescription(aFiles[i])+"'");
				folderStr.append("},");
			}else{
				// 文件
				filesStr.append("{");
				filesStr.append("name:'"+aFiles[i].getName()+"',");
				filesStr.append("date:'"+sdf.format(new Date(aFiles[i].lastModified()))+"',");
				filesStr.append("type:'"+jc.getTypeDescription(aFiles[i])+"',");
				filesStr.append("size:'"+(aFiles[i].length()/1024 + 1)+" KB'");
				filesStr.append("},");
			}
		}

		// 假如有，去掉最后一个","
		if(folderStr.lastIndexOf(",") == folderStr.length()-1){
			folderStr.deleteCharAt(folderStr.length()-1);
		}
		if(filesStr.lastIndexOf(",") == filesStr.length()-1){
			filesStr.deleteCharAt(filesStr.length()-1);
		}
		
		// 声明要返回的json字符串
		StringBuffer rtnText = new StringBuffer();
		rtnText.append("{folders:["+folderStr+"],");
		rtnText.append("files:["+filesStr+"]}");
		
		long e1 = System.currentTimeMillis();
		System.out.println(rtnText);
		System.out.println(e1-s1);
		
	}

	@Test
	@Ignore
	public void t2(){
		String str = "F:\\eclipse\\workspace\\";
		System.out.println(str.replaceAll("\\\\", "\\\\\\\\"));
	}
	
	@Test
	@Ignore
	public void t3() throws IOException{
		// 工程根目录
		String str = "\\";
		File file = new File(str);
		System.out.println(file.exists());
		
		//JFileChooser jc= new JFileChooser();
		
		// 遍历盘符
		File[] arr =  File.listRoots();
		System.out.println(arr.length);
		for(int i = 0;i<arr.length;i++){
			float size = (float)arr[i].getFreeSpace()/(1024*1024*1024);
			BigDecimal bd = new BigDecimal(size);
			bd = bd.setScale(2,BigDecimal.ROUND_DOWN);
			System.out.println(bd.floatValue());

		}
			
		String str2 = "f:\\";
		File file2 = new File(str2);
		System.out.println(file2.exists());
		System.out.println(file2.getParent());
		
		String str3 = "f:\\a\\\\ac\\\\\\\\\\\\\\";
		System.out.println(str3.replaceAll("\\\\+$", ""));
		
		// 假如文件夹 或 文件存在，getCanonicalFile()就返回真实的大小写，假如部分文件夹或文件不存在，那么该部分钱半段真实，后半段就都是根据String的那部分来定了
		String str4 = "f:\\\\\\sAve\\FifA 13";
		str4 = "f:\\";
		File file4 = new File(str4);
		System.out.println(file4.getCanonicalFile());
		
		// 去掉"\\" 或者 "/"，只留最后一部分，以此获取文件名
		String str5 = "f:\\aa\\cc\\ee.ext";
		str5 = "http://192.168.1.100/simple_chat/uploadFiles/22.mp3";
		System.out.println(str5.replaceAll("^([\\s\\S]*(\\\\|/))", ""));
		
		System.out.println("-----------");	
		// 中文url的queryString 编码解码过程 (Chrome测试)
		String str6 = "陈绮贞 - 80%完美的日子.mp3";
		
		// 浏览器编码 '<a href="' + encodeURIComponent("陈绮贞 - 80%完美的日子.mp3") + '" />'
		str6 = java.net.URLEncoder.encode(str6,"utf-8");//%E9%99%88%E7%BB%AE%E8%B4%9E+-+80%25%E5%AE%8C%E7%BE%8E%E7%9A%84%E6%97%A5%E5%AD%90.mp3
		System.out.println(str6);
		
		// Tomcat 编码
		str6 = java.net.URLDecoder.decode(str6,"utf-8");//陈绮贞 - 80%完美的日子.mp3
		System.out.println(str6);
		str6 = new String(str6.getBytes("utf-8"),"iso-8859-1");//éç»®è´ - 80%å®ç¾çæ¥å­.mp3
		System.out.println(str6);
		
		// 服务器解码
		str6 = new String(str6.getBytes("iso-8859-1"),"utf-8");//陈绮贞 - 80%完美的日子.mp3
		System.out.println(str6);
		
		
		
		System.out.println("-----------");
		String aa = "";
		String str7 = "éç»®è´ - 80%å®ç¾çæ¥å­.mp3";
		aa = java.net.URLEncoder.encode(str7, "Unicode");
		System.out.println(aa);
		aa = java.net.URLEncoder.encode(str7, "iso-8859-1");
		System.out.println(aa);
		aa = java.net.URLEncoder.encode(str7, "utf-8");
		System.out.println(aa);
		aa = java.net.URLEncoder.encode(str7, "gbk");
		System.out.println(aa);
		
		
		File file7 = new File("c:\\éç»®è´ - 80%å®ç¾çæ¥å­.mp3");
		System.out.println(file7.exists());
		
	}	
	
	@Test
	@Ignore
	public void t4(){
		
		String[] str = new String[8000];
		for(int i=0;i<str.length;i++){
			str[i] = i +"";
		}
		
		String tt1 = null;
		
		tt1 = "";
		long s2 = System.currentTimeMillis();	
		for(int i=0;i<str.length;i++){	
			String temp = str[i];
			tt1 +=temp;
			tt1 +=temp;
			tt1 +=temp;
			tt1 +=temp.length();
			tt1 +=temp;
			tt1 +=temp.isEmpty();
			tt1 +=temp;
		}
		long e2 = System.currentTimeMillis();	
		tt1 = null;
		
		tt1 = "";		
		long s1 = System.currentTimeMillis();	
		for(int i=0;i<str.length;i++){
			tt1 +=str[i];
			tt1 +=str[i];
			tt1 +=str[i];
			tt1 +=str[i].length();
			tt1 +=str[i];
			tt1 +=str[i].isEmpty();
			tt1 +=str[i];
		}
		long e1 = System.currentTimeMillis();			
		tt1 = null;
		
		System.out.println(e1-s1);
		System.out.println(e2-s2);
	}
	
	@Test
	public void t5(){	
		String str = "hibernate3%E5%B1%9E%E6%80%A7%E5%BB%B6%E6%97%B6%E5%8A%A0%E8%BD%BD%E7%9A%84%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6%E5%92%8C%E8%83%BD%E7%94%A8%E7%9A%84jar%E5%8C%85";
	try {
			str = java.net.URLDecoder.decode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(str);
	}
	
}
