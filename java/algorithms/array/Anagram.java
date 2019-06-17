package array;

import org.junit.Test;

public class Anagram {

	int ALPHABET_LEN = 26;

	public boolean isAnagram(String s, String t) {
		if (s.length() != t.length()) {
			return false;
		}
		int len = s.length();
		int[] sTable = new int[ALPHABET_LEN];
		int[] tTable = new int[ALPHABET_LEN];
		for (int i = 0; i < len; i++) {
			sTable[s.charAt(i) - 'a']++;
			tTable[t.charAt(i) - 'a']++;
		}
		for (int i = 0; i < ALPHABET_LEN; i++) {
			if (sTable[i] != tTable[i]) {
				return false;
			}
		}
		return true;
	}

	@Test
	public void testIsAnagram() {
		System.out.println(isAnagram("eel", "lee"));
	}
}
