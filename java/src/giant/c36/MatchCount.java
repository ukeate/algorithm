package giant.c36;

// 来自美团
// 给定两个字符串s1和s2
// 返回在s1中有多少个子串等于s2
public class MatchCount {

    // next数组多求一位
    private static int[] next(char[] str2) {
        if (str2.length == 1) {
            return new int[]{-1, 0};
        }
        int[] next = new int[str2.length + 1];
        next[0] = -1;
        next[1] = 0;
        int i = 2, cn = 0;
        while (i < next.length) {
            if (str2[i - 1] == str2[cn]) {
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }

    public static int count(char[] str1, char[] str2) {
        int x = 0, y = 0, count = 0;
        int[] next = next(str2);
        while (x < str1.length) {
            if (str1[x] == str2[y]) {
                x++;
                y++;
                if (y == str2.length) {
                    count++;
                    y = next[y];
                }
            } else if (next[y] == -1) {
                x++;
            } else {
                y = next[y];
            }
        }
        return count;
    }
}
