package bin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 公共类 */
public class Utilities {
	/** 关闭输入流 */
	public static void closeInputStream(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 关闭输出流 */
	public static void closeOutputStream(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Servlet响应输出文字 */
	public static void outputText(HttpServletResponse res, String text) {
		res.reset();
		res.setContentType("text/html;charset=UTF-8");
		OutputStream os = null;
		try {
			os = res.getOutputStream();
			os.write(text.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** 返回只保留scale位小数的float类型数字 */
	public static float getScale(float num, int scale, int mode) {
		return new BigDecimal(num).setScale(scale, mode).floatValue();
	}

	/** Response的Header存放文件名时转码 */
	public static String encodeFileName(HttpServletRequest req, String fileName) throws UnsupportedEncodingException {
		// 文件名转码
		String agent = req.getHeader("USER-AGENT");
		if (null != agent && -1 != agent.toUpperCase().indexOf("MSIE")) { // IE内核浏览器
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			fileName = fileName.replace("+", "%20");// 处理IE文件名中有空格会变成+"的问题;
		} else {// 非IE
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		return fileName;
	}

	/** 比较字符串大小 */
	public static int compareString(String s1, String s2) {
		int i = 0;
		int result = 0;
		while ((result = s1.codePointAt(i) - s2.codePointAt(i)) == 0) {
			boolean outOfBounds1 = i >= s1.length() - 1;
			boolean outOfBounds2 = i >= s2.length() - 1;
			// 其中一字符串越界的三种情况
			if (outOfBounds1 && outOfBounds2)
				return 0;
			if (!outOfBounds1 && outOfBounds2)
				return 1;
			if (outOfBounds1 && !outOfBounds2)
				return -1;
			// 两字符串都没越界就继续循环比较
			i++;
		}
		return result;
	}

	/** 对String数组进行快速排序(内部方法) */
	private static void qSortInternal(String[] arr, int sPos, int ePos) {
		if (sPos < ePos) {
			String key = arr[sPos];
			int left = sPos;
			int right = ePos;
			while (left < right) {
				while (left < right && compareString(arr[right], key) >= 0)
					right--;
				if (left < right)
					arr[left++] = arr[right];
				while (left < right && compareString(arr[left], key) < 0)
					left++;
				if (left < right)
					arr[right--] = arr[left];
			}
			arr[left] = key;
			qSortInternal(arr, sPos, left - 1);
			qSortInternal(arr, right + 1, ePos);
		}
	}

	/** 对String数组进行快速排序 */
	public static void qSort(String[] arr) {
		qSortInternal(arr, 0, arr.length - 1);
	}
}
