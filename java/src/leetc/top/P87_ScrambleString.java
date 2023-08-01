package leetc.top;

public class P87_ScrambleString {
    private static boolean sameType(char[] str1, char[] str2) {
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

    private static boolean process1(char[] str1, char[] str2, int l1, int l2, int size, int[][][] dp) {
        if (dp[l1][l2][size] != 0) {
            return dp[l1][l2][size] == 1;
        }
        boolean ans = false;
        if (size == 1) {
            ans = str1[l1] == str2[l2];
        }
        for (int leftSize = 1; leftSize < size; leftSize++) {
            if (process1(str1, str2, l1, l2, leftSize, dp) && process1(str1, str2, l1 + leftSize, l2 + leftSize, size - leftSize, dp)
                    || (process1(str1, str2, l1, l2 + size - leftSize, leftSize, dp) && process1(str1, str2, l1 + leftSize, l2, size - leftSize, dp))) {
                ans = true;
                break;
            }
        }
        dp[l1][l2][size] = ans ? 1 : -1;
        return ans;
    }

    public static boolean isScramble1(String s1, String s2) {
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
        if (!sameType(str1, str2)) {
            return false;
        }
        int n = s1.length();
        int[][][] dp = new int[n][n][n + 1];
        return process1(str1, str2, 0, 0, n, dp);
    }

    //

    public static boolean isScramble2(String s1, String s2) {
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
        if (!sameType(str1, str2)) {
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
                    for (int leftSize = 1; leftSize < size; leftSize++) {
                        if ((dp[l1][l2][leftSize] && dp[l1 + leftSize][l2 + leftSize][size - leftSize])
                                || (dp[l1][l2 + size - leftSize][leftSize] && dp[l1 + leftSize][l2][size - leftSize])) {
                            dp[l1][l2][size] = true;
                            break;
                        }
                    }
                }
            }
        }
        return dp[0][0][n];
    }

}
