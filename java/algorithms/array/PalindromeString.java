package array;

import org.junit.Test;

public class PalindromeString {

	public boolean isPalindrome(String str) {
		for (int i = 0, j = str.length() - 1; i < j; i++, j--) {
			if (str.charAt(i) != str.charAt(j))
				return false;
		}
		return true;
	}

	@Test
	public void testIsPalindrome() {
		System.out.println(isPalindrome("abcdAdcba"));
	}

	public boolean isAlpha(char c) {
		if (c >= '0' && c <= '9' || c >= 'a' && c <= 'z')
			return true;
		else
			return false;
	}

	public boolean isPalindrome2(String s) {
		s = s.toLowerCase();
		for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
			while (i < j && !isAlpha(s.charAt(i)))
				i++;
			while (i < j && !isAlpha(s.charAt(j)))
				j--;
			if (s.charAt(i) != s.charAt(j))
				return false;
		}
		return true;
	}

	@Test
	public void testIsPalindrome2() {
		System.out.println(isPalindrome2("a.,+bXA2c2axBa"));
	}

	public int wei(int num) {
		int retCount = 0;
		while (num > 0) {
			num /= 10;
			retCount++;
		}
		return retCount;
	}

	public boolean palindromeNum(int num) {
		if (num < 10)
			return true;
		// if (num < 0)
		// return false;

		int wei = wei(num);
		int t = (int) Math.pow(10, wei - 1);
		int half = wei / 2;
		int n = num;
		for (int i = 0; i < half; i++) {
			if ((num / t) % 10 == n % 10) {
				t /= 10;
				n /= 10;
			} else
				return false;
		}
		return true;
	}

	@Test
	public void testPalindromeNum() {
		System.out.println(palindromeNum(1245421));
	}

	public boolean isPalindromeStr(String s, int start, int end) {
		while (start < end) {
			if (s.charAt(start) != s.charAt(end))
				return false;
			start++;
			end--;
		}
		return true;
	}

	public String longestPalindrome(String s){
		int len = s.length();
		int from = 0, to = 0, max = 0;
		for(int i = 0; i < len; i++){
			for(int j = i; j < len; j++){
				if(isPalindromeStr(s, i, j) && (j - i) > (max)){
					from = i;
					to = j;
					max = to - from;
				};
			}
		}
		return s.substring(from, to + 1);
	}

	@Test
	public void testLongestPalindrome() {
		System.out.println(longestPalindrome("sdbsdaswoabcbaowe"));
	}
	
	/**
	 * 中心扩展法
	 * 时间复杂度n^2, 空间复杂度1
	 * @param s
	 * @return
	 */
	public String longestPalindrome2(String s) {
		int maxLeft = 0;
		int maxRight = 0;
		int max = 1;
		int n = s.length();
		for(int i = 0; i < n; i++) {
			int start = i;
			int end = i + 1;
			int len = 0;
			int left = start;
			int right = end;
			while(start >= 0 && end < n) {
				if(s.charAt(start) == s.charAt(end)) {
					len = len + 2;
					left = start;
					right = end;
					start--;
					end++;
				}else{
					break;
				}
			}
			if(len > max){
				maxLeft = left;
				maxRight = right;
				max = len;
			}
			start = i - 1;
			end = i + 1;
			len = 1;
			left = start;
			right = end;
			while(start >= 0 && end < n) {
				if(s.charAt(start) == s.charAt(end)) {
					len = len + 2;
					left = start;
					right = end;
					start--;
					end++;
				}else{
					break;
				}
			}
			if(len > max){
				maxLeft = left;
				maxRight = right;
				max = len;
			}
		}
		return s.substring(maxLeft, maxRight + 1);
	}
	@Test
	public void testLongestPalindrome2() {
		System.out.println(longestPalindrome2("sdbsdaswoabcbaowe"));
	}
}
