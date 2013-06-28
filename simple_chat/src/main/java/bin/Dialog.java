﻿package bin;

import java.io.Serializable;

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