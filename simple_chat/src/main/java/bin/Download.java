package bin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * 支持下载断点续传
 */
public class Download extends HttpServlet {
	private static final long serialVersionUID = 101L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, UnsupportedEncodingException {
		// 获取文件名且解码
		String fileName = req.getParameter("name");
		fileName = fileName == null ? "" : new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
		
		// 获取文件所在目录
		String dir = req.getParameter("dir");
		String folderName = (String) getServletContext().getAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME);
		dir = dir == null ? getServletContext().getRealPath("/" + folderName) : new String(dir.getBytes("ISO-8859-1"), "UTF-8");
		
		// 输入输出流
		OutputStream os = null;
		InputStream is = null;
		
		try {
			// 参数为空情况就输出提示
			if (dir.equals("") || fileName.equals("")) {
				res.setContentType("text/html;charset=UTF-8");
				os = res.getOutputStream();
				os.write("参数不能为空<script>alert('参数不能为空')</script>".getBytes("UTF-8"));
				return;
			}

			File file = new File(dir,fileName);

			// 文件不存在就输出提示
			if (!file.exists()) {
				res.setContentType("text/html;charset=UTF-8");
				os = res.getOutputStream();
				os.write("文件不存在<script>alert('文件不存在')</script>".getBytes("UTF-8"));
				// res.sendError(404);
				return;
			}
			
			// 文件大小
			long fileLength = file.length();

			// 分析request header参数Range,获取要下载的文件片段是始末位置
			// --assuming an entity-body of length 10000:
			// The first 500 bytes (byte offsets 0-499, inclusive): bytes=0-499
		    // The second 500 bytes (byte offsets 500-999, inclusive): bytes=500-999
		    // The final 500 bytes (byte offsets 9500-9999, inclusive): bytes=-500, Or bytes=9500-
		    // The first and last bytes only (bytes 0 and 9999): bytes=0-0,-1
		    // --Several legal but not canonical specifications of the second 500:
			// bytes (byte offsets 500-999, inclusive):
		    // bytes=500-600,601-999
		    // bytes=500-700,601-999
			String sRange = req.getHeader("range");
			long startPos = 0;
			long endPos = -1L;
			if (sRange != null && !"".equals(sRange.trim())) {
//				System.out.println("接收的Range: " + sRange);
				final Pattern pattern = Pattern.compile("(?i)bytes\\=(\\d+)?(?:\\-)?(\\d+)?(?:,(\\d+)?(?:\\-(\\d+)+))?");
				Matcher mr = pattern.matcher(sRange);
				if (mr.find()) {
					// 其实这三句就基本满足一般的下载器
					//startPos = (mr.group(1) == null ? 0 : Long.parseLong(mr.group(1), 10));
					//endPos = (mr.group(2) == null ? -1L : Long.parseLong(mr.group(2), 10));
					//endPos = (endPos == -1L || endPos >= fileLength) ? endPos = fileLength - 1 : endPos;
					
					// TODO 还要处理一些格式不正确的Range
					if(mr.group(1).equals("0") && mr.group(2).equals("0") && mr.group(3) == null && mr.group(4).equals("1")){
						// TODO 
						// bytes=0-0,-1
						throw new RuntimeException("Range:\"bytes=0-0,-1\" does not support yet");
					}else if(mr.group(1)!=null && mr.group(2)!=null && mr.group(3)==null && mr.group(4)==null){
						// bytes=0-499 (UC浏览器下载、迅雷下载)
						startPos = Long.parseLong(mr.group(1), 10);
						endPos = Long.parseLong(mr.group(2), 10);
						if(endPos >= fileLength){
							// UC会返回的endPos会固定增长，固定增长值为:262143，即首次0-262143,第二次262144-524287 ...
							// 所以当文件大小小于等于这个给定endPos时，要处理
							endPos = fileLength - 1;
						}
					}else if(mr.group(1)!=null && mr.group(2) == null){
						// bytes=9500-
						startPos = Long.parseLong(mr.group(1), 10);
						endPos = fileLength - 1;
					}else if(mr.group(1) == null && mr.group(2) != null){
						// bytes=-500
						startPos = fileLength - Long.parseLong(mr.group(1), 10);
						endPos = fileLength - 1;
					}else if(mr.group(1) != null && mr.group(2) !=null && mr.group(3) !=null && mr.group(4) !=null){
						// TODO
						// bytes=500-600,601-999,bytes=500-700,601-999
						throw new RuntimeException("Range:"+ sRange +" does not support yet");
					}else{
						// 还有?
						throw new RuntimeException("Range:"+ sRange +" does not support yet XXXXXXX");
					}
				}
				res.setStatus(206);
			} else {
				// 普通浏览器下载如：ie、firefox、chrome
				endPos = (endPos == -1L || endPos >= fileLength) ? endPos = fileLength - 1 : endPos;// 修正endPos
				res.setStatus(200);
			}
			
			// 设置响应头
			// 断点续传的标识
			res.setHeader("Accept-Ranges", "bytes");
			
			// 断点续传重要参数
			res.addHeader("Content-Range", "bytes " + startPos + "-" + endPos + "/" + fileLength);
			
			// 消息体大小,可以理解这次响应的传输字节大小(有Content-Range，好像就没什么用了。。。这个，就开头标识一下)
			long contentLength = endPos + 1 - startPos;
			res.addHeader("Content-Length", "" + contentLength);
			
			res.setContentType(ContentTypeUtil.getContentTypeByFileName(fileName)+",charset=UTF-8");
			res.addHeader("Content-Disposition", "attachment; filename=\"" + Utilities.encodeFileName(req, fileName) +"\"");
			
			// 推送数据到客户端
			is = new FileInputStream(file);
			is.skip(startPos);
			os = res.getOutputStream();
			byte[] bytes = new byte[1024];
			int len = -1;
			int total = 0;// 本次请求is.read(bytes)总字节数
			while ((len = is.read(bytes)) > 0) {
				total += len;
				if(total > contentLength){ // 对付这种情况: endPos < fileLength - 1
					os.write(bytes, 0, (int) (total - contentLength));// 输出未超出限制的大小的部分
					os.flush();
					break;
				}else{
					os.write(bytes, 0, len);
					os.flush();
				}
			}
		} catch (IOException ex) {
			// 某些浏览器(如UC)下载时会报 ClientAbortException: java.net.SocketException:
			// Connection reset by peer: socket write error
			// 客户端断开,这个Servlet还在write的错，好像是不可避免的，这里先捕获,使得两个流都能顺利关闭
			if (!"org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getName())){
				ex.printStackTrace();
				Utilities.outputText(res, ex.getMessage());
			}
		} finally {
			Utilities.closeOutputStream(os);
			Utilities.closeInputStream(is);
		}
	}
}
