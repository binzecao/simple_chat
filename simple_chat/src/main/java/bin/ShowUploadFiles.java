package bin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFileChooser;

public class ShowUploadFiles extends HttpServlet {
	private static final long serialVersionUID = 101L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// 上传文件夹名字
		String folderName = (String) getServletContext().getAttribute(ServletContextParams.UPLOADFILES_FOLDER_NAME);

		// 上传在服务器文件夹的路径
		String uploadFolderPath = getServletContext().getRealPath("/" + folderName);
		// 找到上传文件夹，没有就创建一个
		File dir = new File(uploadFolderPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		// 获取文件夹全部文件
		File[] aFiles = dir.listFiles();

		// 按文件名排序
		qSort(aFiles);

		// 文件信息必备工具
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		JFileChooser jc = new JFileChooser();

		// 循环添加文件信息到list
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < aFiles.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", aFiles[i].getName());
			map.put("date", sdf.format(new Date(aFiles[i].lastModified())));
			map.put("type", jc.getTypeDescription(aFiles[i]));
			map.put("size", Utilities.getScale((float) aFiles[i].length() / 1024, 2, BigDecimal.ROUND_HALF_UP) + " KB");
			list.add(map);
		}

		// 跳转
		req.setAttribute("list", list);
		req.getRequestDispatcher("/WEB-INF/showUploadFiles.jsp").forward(req, res);
	}

	// 文件排序
	private void qSortInternal(File[] arr, int sPos, int ePos) {
		if (sPos < ePos) {
			File key = arr[sPos];
			int left = sPos;
			int right = ePos;
			while (left < right) {
				while (left < right && Utilities.compareString(arr[right].getName(), key.getName()) >= 0)
					right--;
				if (left < right)
					arr[left++] = arr[right];
				while (left < right && Utilities.compareString(arr[left].getName(), key.getName()) < 0)
					left++;
				if (left < right)
					arr[right--] = arr[left];
			}
			arr[left] = key;
			qSortInternal(arr, sPos, left - 1);
			qSortInternal(arr, right + 1, ePos);
		}
	}

	// 文件排序
	private void qSort(File[] arr) {
		qSortInternal(arr, 0, arr.length - 1);
	}
}
