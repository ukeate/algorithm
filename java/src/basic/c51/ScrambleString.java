package basic.c51;

// https://leetcode.com/problems/scramble-string/
// 递归选位置交换左右部分，问s1能否变s2
public class ScrambleString {
    private static boolean sameCount(char[] str1, char[] str2) {
        if (str1.length != str2.length) {
            return false;
        }
        int[] map = new int[256];
        for (int i = 0; i < str1.length; i++) {
            map[str1[i]]++;
        }
        for (int i = 0; i < str2.length; i++) {
            if (--map[str2[i]] < 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean process(char[] str1, char[] str2, int l1, int l2, int size) {
        if (size == 1) {
            return str1[l1] == str2[l2];
        }
        for (int leftPart = 1; leftPart < size; leftPart++) {
            if ((process(str1, str2, l1, l2, leftPart) && process(str1, str2, l1 + leftPart, l2 + leftPart, size - leftPart))
                    || (process(str1, str2, l1, l2 + size - leftPart, leftPart) && process(str1, str2, l1 + leftPart, l2, size - leftPart))) {
                return true;
            }
        }
        return false;
    }

    public static boolean can1(String s1, String s2) {
        if (s1 == null ^ s2 == null) {
            return false;
        }
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1.equals(s2)) {
            return true;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        if (!sameCount(str1, str2)) {
            return false;
        }
        int n = s1.length();
        return process(str1, str2, 0, 0, n);
    }

    //

    public static boolean can2(String s1, String s2) {
        if (s1 == null ^ s2 == null) {
            return false;
        }
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1.equals(s2)) {
            return true;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        if (!sameCount(str1, str2)) {
            return false;
        }
        int n = s1.length();
        boolean[][][] dp = new boolean[n][n][n + 1];
        for (int l1 = 0; l1 < n; l1++) {
            for (int l2 = 0; l2 < n; l2++) {
                dp[l1][l2][1] = str1[l1] == str2[l2];
            }
        }
        for (int size = 2; size <= n; size++) {
            for (int l1 = 0; l1 <= n - size; l1++) {
                for (int l2 = 0; l2 <= n - size; l2++) {
                    for (int leftPart = 1; leftPart < size; leftPart++) {
                        if ((dp[l1][l2][leftPart] && dp[l1 + leftPart][l2 + leftPart][size - leftPart])
                                || (dp[l1][l2 + size - leftPart][leftPart] && dp[l1 + leftPart][l2][size - leftPart])) {
                            dp[l1][l2][size] = true;
                            break;
                        }
                    }
                }
            }
        }
        return dp[0][0][n];
    }

    public static void main(String[] args) {
        String s1 = "abcd";
        String s2 = "cdab";
        System.out.println(can1(s1, s2));
        System.out.println(can2(s1, s2));
        s2 = "cadb";
        System.out.println(can1(s1, s2));
        System.out.println(can2(s1, s2));

        s1 = "bcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcde";
        s2 = "ebcdeebcdebebcdebcdebcdecdebcbcdcdebcddebcbdebbbcdcdebcdeebcdebcdeebcddeebccdebcdbcdebcd";
        System.out.println(can2(s1, s2));
    }
}
