package leetc.top;

public class P28_FindTheIndexOfTheFirstOccurrenceInString {
    public static int[] next(char[] m) {
        if (m.length == 1) {
            return new int[] {-1};
        }
        int[] next = new int[m.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2, cn = 0;
        while (i < next.length) {
            if (m[i - 1] == m[cn]) {
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }

    public static int strStr(String s, String m) {
        if (s == null || m == null || s.length() < m.length()) {
            return -1;
        }
        if (m.length() == 0) {
            return 0;
        }
        char[] str1 = s.toCharArray();
        char[] str2 = m.toCharArray();
        int x = 0, y = 0;
        int[] next = next(str2);
        while (x < str1.length && y < str2.length) {
            if (str1[x] == str2[y]) {
                x++;
                y++;
            } else if (next[y] == -1) {
                x++;
            } else {
                y = next[y];
            }
        }
        return y == str2.length ? x - y : -1;
    }
}
