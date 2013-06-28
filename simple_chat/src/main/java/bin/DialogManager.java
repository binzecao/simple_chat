package bin;

import java.util.ArrayList;

import javax.servlet.ServletContext;

public class DialogManager{
	static ArrayList<Dialog> getDialogsList(ServletContext sc){
		ArrayList<Dialog> list = (ArrayList<Dialog>) sc.getAttribute("dialogsList");
		if(list == null){			
			list = new ArrayList<Dialog>();
			setDialogsList(sc,list);
			System.out.println("build dialogs ArrayList");
		}
		return list;
	}
	
	private static void setDialogsList(ServletContext sc,ArrayList<Dialog> dialogsList){
		sc.setAttribute("dialogsList", dialogsList);
	}
	
	static synchronized void saveDialog(ServletContext sc,String txt,int type){
		// 获取对话列表
		ArrayList<Dialog> list = DialogManager.getDialogsList(sc);
		
		// 保存对话
		Dialog dialog = new Dialog();
		if(list.size()==0){
			dialog.setId(0);
		}else{
			dialog.setId(list.get(list.size() - 1).getId() + 1);
		}
		dialog.setTxt(txt);
		dialog.setType(type);
		list.add(dialog);
		setDialogsList(sc, list);
	}
}
