package bin;

import java.io.Serializable;

/** 对话模型
 * 加这个中文注释是因为中文能让java文件的编码从ANSI变为UTF-8(无BOM)，
 * 不然每次Maven package后，对于纯英文的java文件，eclipse都会将之变为ANSI，然后无端端报错*/
public class Dialog implements Serializable{
	private static final long serialVersionUID = 101L;
	
	private int id;
	
	private int type;
	
	private String txt;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
