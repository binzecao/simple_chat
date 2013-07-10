package bin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** 公共类   
 * 加这个中文注释是因为中文能让java文件的编码从ANSI变为UTF-8(无BOM)，
 * 不然每次Maven package后，对于纯英文的java文件，eclipse都会将之变为ANSI，然后无端端报错*/
public class Utilities {
	public static void closeInputStream(InputStream stream){
		if(stream!=null){
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void closeOutputStream(OutputStream stream){
		if(stream!=null){
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
}
