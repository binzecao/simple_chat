package bin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

public class Test4 {
	@Test
	@Ignore
	public void t1() throws IOException {
		String sRange = "bytes=222-262143";
		final Pattern pattern = Pattern.compile("(?i)bytes\\=(\\d+)(?:\\-(\\d+))?");
		Matcher mr = pattern.matcher(sRange);
		System.out.println(mr.groupCount());
		mr.find();
		System.out.print(mr.group(0)+",");
		System.out.print(mr.group(1)+",");
		System.out.print(mr.group(2)+",");
		System.out.print(mr.group());

		System.out.println("-----------");
		String s1 = "1x2";
		Matcher mr1 = Pattern.compile("(a)?\\w*").matcher(s1);
		System.out.println(mr1.groupCount());
		if(mr1.find()){
			System.out.print(mr1.group(0)+",");
			System.out.print(mr1.group(1)+",");	
		}
		//System.out.print(mr1.group(2)+",");	
	}
	
	@Test
	@Ignore
	public void t2() throws IOException{
		File file = new File("c:\\ab.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		OutputStream os = new FileOutputStream(file);
		os.write(new byte[262144]);
//		os.write(new byte[262145]);
		os.close();
	}
	
	@Test
	@Ignore
	public void t3(){
		String[] arr = { "\\", "/", ":","*","?","\"", "<",">","|"};
		Pattern p = Pattern.compile("\\\\|/|:|\\*|\\?|\"|<|>|\\|");
		for(String str : arr){
			if(p.matcher(str).matches()){
				//System.out.println(str);
			}
		}
		System.out.println(Pattern.compile("^[\\s\\S]*(\\\\|/|:|\\*|\\?|\"|<|>|\\|)+[\\s\\S]*$").matcher("/").matches());
		System.out.println(Pattern.compile("^(-1)|(\\d+)").matcher("10485760").matches());
		
	}
	
	@Test
	@Ignore
	public void t4() {
		Pattern pattern = Pattern.compile("(?i)bytes\\=(\\d+)?(?:\\-)?(\\d+)?(?:,(\\d+)?(?:\\-(\\d+)+))?");
		// --assuming an entity-body of length 10000:
		// The first 500 bytes (byte offsets 0-499, inclusive): bytes=0-499
	    // The second 500 bytes (byte offsets 500-999, inclusive): bytes=500-999
	    // The final 500 bytes (byte offsets 9500-9999, inclusive): bytes=-500, Or bytes=9500-
	    // The first and last bytes only (bytes 0 and 9999): bytes=0-0,-1
	    // --Several legal but not canonical specifications of the second 500:
		// bytes (byte offsets 500-999, inclusive):
	    // bytes=500-600,601-999
	    // bytes=500-700,601-999
		String s1 = "bytes=500-999";
		String s2 = "bytes=-500";
		String s3 = "bytes=9500-";
		String s4 = "bytes=0-0,-1";
		String s5 = "bytes=500-600,601-999";
		String s6 = "bytes=500-600601-999";// 错误
		Matcher mr = pattern.matcher(s2);
		if(mr.find()){
			System.out.println(mr.group(0));
			System.out.println(mr.group(1));
			System.out.println(mr.group(2));
			System.out.println(mr.group(3));
			System.out.println(mr.group(4));
		}		
	}
	
	@Test
	public void t5() throws UnsupportedEncodingException{
//		String str = "d f";
//		String str = "<a href='ff'>速度</a>";
		String str = " +";
		str = java.net.URLEncoder.encode(str,"utf-8");
		str = str.replaceAll("\\+", "a");
		System.out.println(str);
		str = java.net.URLDecoder.decode(str,"utf-8");
		System.out.println(str);
	}
}
