package bin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bin.Dialog;
import bin.DialogManager;

@WebServlet("/AjaxLoad")
public class AjaxLoad extends HttpServlet {
	private static final long serialVersionUID = 101L;


	public AjaxLoad() {
		super();
	}

	protected void doGet(HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {
		this.doPost(req, res);
	}

	protected void doPost(HttpServletRequest req,
			HttpServletResponse res) throws ServletException, IOException {
		//"{hasError:true,errorTxt:'error'}" // 错误
		//"{hasError:false,aDialogs:[{did:0,type:1,txt:'a'},{did:1,type:2,txt:'b'}]}"  // 对话
		//type:1 :普通对话    type:2 上传文件成功提示
		
		// System.out.println("Start AjaxLoad Servlet........");
		// 基本设置
		res.setContentType("text/xml;charset=gbk");
		res.setHeader("Cache-Control", "no-cache");
		OutputStream os = res.getOutputStream();

		// 得到客户端传来的当前对话Id
		String currentIdParam = (String) req.getParameter("currentId");

		// currentId转类型
		int currentId = -1;
		try {
			currentId = Integer.parseInt(currentIdParam);
		} catch (NumberFormatException ex) {
			os.write("{hasError:true,errorTxt:'currentId 不是整形'}".getBytes());
			if(os!=null) os.close();
			return; 
		}

		// 得到客户端传来的isLoadAll参数（全部读取的标识）
		String isLoadAllParam = (String) req.getParameter("isLoadAll");
		boolean isLoadAll = Boolean.parseBoolean(isLoadAllParam);

		// 得到dialogs列表		
		ArrayList<Dialog> list = DialogManager.getDialogsList(getServletContext());
		
		// 声明要返回的json字符串
		StringBuffer rtnText = new StringBuffer();
		rtnText.append("{hasError:false,aDialogs:[");
		
		// 根据是否全部读取情况返回相应字段
		if (isLoadAll) {
			// 返回全部
			for(int i=0;i<list.size();i++){
				rtnText.append("{");
				rtnText.append("did:"+list.get(i).getId()+",");
				rtnText.append("type:"+list.get(i).getType()+",");			
				rtnText.append("txt:'"+list.get(i).getTxt()+"'");
				rtnText.append("}");
				rtnText.append(",");
			}
			// 假如有，去掉最后一个","
			if(rtnText.lastIndexOf(",") == rtnText.length()-1){
				rtnText.deleteCharAt(rtnText.length()-1);
			}
		} else {
			// 只返回最新一条，没最新就不返回
			if (list.size()!=0 && currentId < list.get(list.size()-1).getId()) {
				rtnText.append("{");
				rtnText.append("did:"+list.get(currentId+1).getId()+",");
				rtnText.append("type:"+list.get(currentId + 1).getType()+",");			
				rtnText.append("txt:'"+list.get(currentId + 1).getTxt()+"'");
				rtnText.append("}");
			}
		}
		rtnText.append("]}");
			
		// 返回客户端
		//System.out.println(rtnText);
		os.write(rtnText.toString().getBytes("gbk"));		
		os.flush();
		if(os!=null) os.close();
	}
}
