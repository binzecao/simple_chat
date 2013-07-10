package bin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Ignore;
import org.junit.Test;


public class Test1 {

	@Test
	//@Ignore
	public void t1(){
		Boolean isLoadAll = Boolean.parseBoolean("fffffff");
		System.out.println(isLoadAll);
	}
	
	@Test
	@Ignore
	public void t2(){
		String str = "  ddd  ";
		System.out.print(str.trim());
	}
	
	@Test
	@Ignore
	public void t3(){
		String a1 = "aa.txt";
		String a2 = "aa.bb.exe";
		String a3 = "cc";
		showName(a1);
		showName(a2);
		showName(a3);
	}
	
	public void showName(String str){
		String name="";
		String extention ="";
		
		int index = str.lastIndexOf(".");
		if(index==-1){
			name = str;
		}else{
			name = str.substring(0, index);
			extention = str.substring(index+1);
		}
		
		System.out.println(str +", �ļ��� "+ name + ",��չ��: "+extention+" ");
	}
	
	@Test
	@Ignore
	public void t4(){
		String fileName = "aa.txt";
		String newFileName = fileName;
		
		// �����ļ������չ��
		String name="";
		String extention ="";
		int index = fileName.lastIndexOf(".");
		if(index==-1){
			name = fileName;
		}else{
			name = fileName.substring(0, index);
			extention = fileName.substring(index+1);
		}
		
		String path = "f:\\tt\\";
		
		// �����µ��ļ���
		boolean isFileNameExist = new File(path+fileName).exists();
		for(int i=1;isFileNameExist==true;i++){
			newFileName = name + "("+i+")." + extention;
			isFileNameExist = new File(path+newFileName).exists();
		}
		System.out.println(newFileName);
		
		try {
			new File(path+newFileName).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void t5(){
		TreeMap<Integer, String> list = new TreeMap<Integer, String>();
		list.put(4, "01");
		list.put(1, "Asss");
		list.put(2,"b");
		list.put(3, "0");
		
		System.out.println(list.floorEntry(2).getValue());
		
		for(int i =0 ;i<list.size();i++){
			//System.out.println(list.get(i));
			//list.floorEntry(key)
		}
		
		
		for(Entry<Integer, String> entry : list.entrySet()){
			System.out.println(entry.getKey()+","+entry.getValue());
		}
		
		
	}

	@Test
	@Ignore
	public void t6() throws UnsupportedEncodingException{
		String s1 = "%E4%B8%AD%E5%9B%BD";
		System.out.println(s1);
		System.out.println(URLEncoder.encode(s1,"utf-8"));
		System.out.println(URLEncoder.encode(s1, "gbk"));		
		System.out.println(URLDecoder.decode(s1,"utf-8"));//����ʱ�����
		System.out.println(URLDecoder.decode(s1, "gbk"));
		System.out.println(URLEncoder.encode(s1, "iso-8859-1"));
		System.out.println(URLDecoder.decode(s1, "iso-8859-1"));
		
		System.out.println();
		
		String s2 = "中国";
		System.out.println(s2);
		System.out.println(URLEncoder.encode(s2,"utf-8"));//����ʱ�����
		System.out.println(URLEncoder.encode(s2, "gbk"));		
		System.out.println(URLDecoder.decode(s2,"utf-8"));
		System.out.println(URLDecoder.decode(s2, "gbk"));
		System.out.println();
		
		String s3 = "%B3%D4%B4%F3%B2%CD.txt";
		System.out.println(URLDecoder.decode(s3,"utf-8"));//����ʱ�����
		System.out.println(URLDecoder.decode(s3, "gbk"));
		System.out.println(URLDecoder.decode(s3, "iso-8859-1"));
		
		
		System.out.println(new String(s2.getBytes("iso8859-1"),"gbk"));
		
		
		
	}

	@Test
	@Ignore
	public void t7() throws ArithmeticException{
		//int ss = 1/0;
		try{
			int s = 1/0;
			System.out.println(s);
		}catch(ArithmeticException ex){
			System.err.println(ex.getMessage());
		}
	}
}


