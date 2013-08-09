package bin;

import org.junit.Test;

public class Test3 {
	@Test
	public void t1() {
		String s1 = "aa22";
		String s2 = "aa1分";
		String s3 = "bb3";
		String s4 = "A";
		String[] arr = {s1,s2,s3,s4};
		qSort(arr);
		System.out.println(getArrayString(arr));
	}
	
	String getArrayString(String[] arr){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<arr.length;i++){
			sb.append(arr[i]+",");
		}
		return sb.toString();
	}

	int compareString(String s1, String s2) {
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

	void qSortInternal(String[] arr, int sPos, int ePos) {
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
	
	void qSort(String[] arr){
		qSortInternal(arr,0,arr.length-1);
	}

}
