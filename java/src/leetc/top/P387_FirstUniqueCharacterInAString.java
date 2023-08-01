package leetc.top;

public class P387_FirstUniqueCharacterInAString {
    public int firstUniqChar(String s) {
        char[] str = s.toCharArray();
        int n = str.length;
        int count[] = new int[26];
        for (int i = 0; i < n; i++) {
            count[str[i] - 'a']++;
        }
        for (int i = 0; i < n; i++) {
            if (count[str[i] - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }
}
