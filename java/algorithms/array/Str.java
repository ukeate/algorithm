package array;


import org.junit.Test;

public class Str {

    public boolean isLetter(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }

    public int lastWorldLen(String s) {
        int len = s.length();
        if (len < 1) {
            return 1;
        }
        int pos = len - 1;
        while (pos >= 0) {
            if (isLetter(s.charAt(pos))) {
                break;
            } else {
                pos--;
            }
        }
        int retLen = 0;
        while (pos >= 0) {
            if (!isLetter(s.charAt(pos))) {
                break;
            } else {
                pos--;
                retLen++;
            }
        }
        return retLen;
    }

    @Test
    public void testLastWorldLen() {
        System.out.println(lastWorldLen("abc def "));
    }

    /*
     * hash表算法
     * 时间复杂度n, 空间复杂度1
     *
     */
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        } else {
            int TWENTY_SIX = 26;
            int len = s.length();
            int[] sTable = new int[TWENTY_SIX];
            int[] tTable = new int[TWENTY_SIX];
            for (int i = 0; i < len; i++) {
                sTable[s.charAt(i) - 'a']++;
                tTable[t.charAt(i) - 'a']++;
            }
            for (int i = 0; i < TWENTY_SIX; i++) {
                if (sTable[i] != tTable[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    @Test
    public void testIsAnagram() {
        System.out.println(isAnagram("anagram", "nagaram"));
    }

    /**
     * 时间复杂度n^3, 空间复杂度n^2
     *
     * @param n
     */
    public String countAndSay(int n) {
        if (n <= 0) {
            return "";
        } else if (n == 1) {
            return "1";
        } else if (n == 2) {
            return "11";
        } else {
            String str = "11";
            StringBuilder sb = new StringBuilder();
            for (int i = 3; i <= n; i++) {
                char temp = str.charAt(0);
                int count = 1;
                for (int j = 1; j < str.length(); j++) {    // 这里str.length()的时间复杂度在for中越来越接近n^2
                    if (str.charAt(j) == temp) {
                        count++;
                    } else {
                        sb.append(count).append(temp);  // 这里sb的长度越来越接近n^2
                        count = 1;
                        temp = str.charAt(j);
                    }
                }
                sb.append(count).append(temp);
                str = sb.toString();
                sb.delete(0, sb.length());
            }
            return str;
        }
    }

    @Test
    public void testCountAndSay() {
        System.out.println(countAndSay(20));
    }

    /**
     * 模式匹配
     * 时间复杂度为 m*n, 空间复杂度为1
     * kmp 算法时间复杂度为 m+n
     */
    public int bruteForce(String str, String matcher) {
        int m = str.length();
        int n = matcher.length();
        for (int i = 0; i < m; i++) {
            int count = 0;
            for (int j = 0; j < n && i + j < m; j++) {
                if (str.charAt(i + j) != matcher.charAt(j)) {
                    break;
                } else {
                    count++;
                }
            }
            if (count == n) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void testBruteForce () {
        String s = "aabbccagdbbccdec";
        String p = "bbccd";

        System.out.println(bruteForce(s, p));
    }
}
