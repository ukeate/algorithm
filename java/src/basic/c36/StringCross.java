package basic.c36;

// 判断aim字符来自s1, s2也不缺少, 并保留原有顺序
public class StringCross {
    public static boolean isCross1(String s1, String s2, String ai) {
        if (s1 == null || s2 == null || ai == null) {
            return false;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        char[] aim = ai.toCharArray();
        if (aim.length != str1.length + str2.length) {
            return false;
        }
        boolean[][] dp = new boolean[str1.length + 1][str2.length + 1];
        dp[0][0] = true;
        for (int i = 1; i <= str1.length; i++) {
            if (str1[i - 1] != aim[i - 1]) {
                break;
            }
            dp[i][0] = true;
        }
        for (int j = 1; j <= str2.length; j++) {
            if (str2[j - 1] != aim[j - 1]) {
                break;
            }
            dp[0][j] = true;
        }
        for (int i = 1; i <= str1.length; i++) {
            for (int j = 1; j <= str2.length; j++) {
                if ((str1[i - 1] == aim[i + j - 1] && dp[i - 1][j])
                        || (str2[j - 1] == aim[i + j - 1] && dp[i][j - 1])) {
                    dp[i][j] = true;
                }
            }
        }
        return dp[str1.length][str2.length];
    }

    //

    public static boolean isCross2(String s1, String s2, String ai) {
        if (s1 == null || s2 == null || ai == null) {
            return false;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        char[] aim = ai.toCharArray();
        if (aim.length != str1.length + str2.length) {
            return false;
        }
        char[] longs = str1.length >= str2.length ? str1 : str2;
        char[] shorts = str1.length < str2.length ? str1 : str2;
        boolean[] dp = new boolean[shorts.length + 1];
        dp[0] = true;
        for (int i = 1; i <= shorts.length; i++) {
            if (shorts[i - 1] != aim[i - 1]) {
                break;
            }
            dp[i] = true;
        }
        for (int i = 1; i <= longs.length; i++) {
            dp[0] = dp[0] && longs[i - 1] == aim[i - 1];
            for (int j = 1; j <= shorts.length; j++) {
                if ((longs[i - 1] == aim[i + j - 1] && dp[j])
                        || (shorts[j - 1] == aim[i + j - 1] && dp[j - 1])) {
                    dp[j] = true;
                } else {
                    dp[j] = false;
                }
            }
        }
        return dp[shorts.length];
    }

    //

    public static void main(String[] args) {
        String s1 = "1234";
        String s2 = "abcd";
        String aim = "1a23bcd4";
        System.out.println(isCross1(s1, s2, aim));
        System.out.println(isCross2(s1, s2, aim));
    }
}
