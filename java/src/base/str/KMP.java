package base.str;

public class KMP {
    private static int[] nextArr(char[] str2) {
        if (str2.length == 1) {
            return new int[]{-1};
        }
        int[] next = new int[str2.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2;
        int cn = 0;
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

    public static int match(String s1, String s2) {
        if (s1 == null || s2 == null || s2.length() < 1 || s1.length() < s2.length()) {
            return -1;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int x = 0;
        int y = 0;
        int[] next = nextArr(str2);
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

    //

    private static String randomStr(int maxLen, int charKind) {
        char[] ans = new char[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (char) ((int) (charKind * Math.random()) + 'a');
        }
        return String.valueOf(ans);
    }

    public static void main(String[] args) {
        int times = 10000000;
        int charKind = 5;
        int strLen = 20;
        int matchLen = 5;
        System.out.println("test begin");
        for (int i = 0; i <times; i++) {
            String str = randomStr(charKind, strLen);
            String match = randomStr(charKind, matchLen);
            if (match(str, match) != str.indexOf(match)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
