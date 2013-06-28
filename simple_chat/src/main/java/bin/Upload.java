package bin;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/Upload")
@MultipartConfig
public class Upload extends HttpServlet{
	private static final long serialVersionUID = 101L;
	
	public Upload(){
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		//System.out.println("进入 Upload Servlet....");
		
		/** 以后可去掉计时 */
		Part file = null;
		long startMili=System.currentTimeMillis();// 当前时间对应的毫秒数
		// 获取上传的文件
		try{
			file = req.getPart("file");
		}catch(IOException ex){
			System.err.println(ex.getMessage()+"\n可能是用户取消上传");
			return;
		}finally{
			long endMili=System.currentTimeMillis();
			String msg =  file!=null? "file.getSize():"+file.getSize() : "";
			System.out.println(msg+" ,req.getPart总耗时为："+(endMili-startMili)+"毫秒");
		}
		
		if(file == null){
			System.out.println("上传的文件为空");
			res.setCharacterEncoding("gbk");
			res.getWriter().println("<script>parent.uploadCallBack(false,'上传失败：上传的文件为空')</script>");
			return;
		}

		// 获取文件名
		//form-data; name="file"; filename="新建文本文档 (3).xml"
		String temp = file.getHeader("content-disposition").split(";")[2].trim();
		String fileName = temp.substring(10,temp.length()-1);
		
		// 上传文件夹文字
		String folderPath = "uploadFiles";
		
		// 找到上传文件夹，没有就创建一个
		File dir = new File(getServletContext().getRealPath("/"+folderPath));
		if(!dir.exists()){
			dir.mkdir();
		}
		
		// 分离文件名和扩展名
		String name="";
		String extention ="";
		int index = fileName.lastIndexOf(".");
		if(index==-1){
			name = fileName;
		}else{
			name = fileName.substring(0, index);
			extention = fileName.substring(index+1);
		}
		
		// 避免重名就设置新的文件名
		int i=1;
		while(new File(dir.getPath()+"//"+fileName).exists()){
			fileName = name + "("+i+")." + extention;
			i++;
		}
		
		/** 以后可去掉计时 */
		long startMili2=System.currentTimeMillis();// 当前时间对应的毫秒数
		// 将文件写入服务器上
		file.write(dir.getPath()+"//"+fileName);
		long endMili2=System.currentTimeMillis();
		System.out.println("file.write总耗时为："+(endMili2-startMili2)+"毫秒");
				
		// 文件路径
		String filePath = "http://"+InetAddress.getLocalHost().getHostAddress()+":"+req.getServerPort()+
				req.getContextPath()+"/"+folderPath + "/" + fileName;
		//URL url = getServletContext().getResource("/uploadFiles/"+fileName);
		//System.out.println(url.getPath());
		
		// 保存上传成功信息
		DialogManager.saveDialog(getServletContext(), "[\""+fileName+"\",\""+filePath+"\"]", 2);
		
		// 返回js回调函数
		res.setCharacterEncoding("gbk");
		res.setHeader("Cache-Control", "no-cache");
		res.getWriter().println("<script>parent.uploadCallBack(true,'"+fileName+"')</script>");
	}
	
}
