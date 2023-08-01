package base.dp6_biz;

// 消除相邻相同字符，返回最小剩余字符
public class DeleteAdjacentChar {
    private static boolean canDelete(String s) {
        char[] str = s.toCharArray();
        for (int i = 1; i < str.length; i++) {
            if (str[i - 1] != str[i]) {
                return false;
            }
        }
        return true;
    }

    public static int min1(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() < 2) {
            return s.length();
        }
        int minLen = s.length();
        for (int l = 0; l < s.length(); l++) {
            for (int r = l + 1; r < s.length(); r++) {
                if (canDelete(s.substring(l, r + 1))) {
                    minLen = Math.min(minLen, min1(s.substring(0, l) + s.substring(r + 1, s.length())));
                }
            }
        }
        return minLen;
    }

    //

    // has表示之前有[l]相同字符
    private static int process2(char[] str, int l, int r, boolean has) {
        if (l > r) {
            return 0;
        }
        if (l == r) {
            return has ? 0 : 1;
        }
        int idx = l;
        int k = has ? 1 : 0;
        while (idx <= r && str[idx] == str[l]) {
            k++;
            idx++;
        }
        // 当前消
        int way1 = (k > 1 ? 0 : 1) + process2(str, idx, r, false);
        // 当前和后边消
        int way2 = Integer.MAX_VALUE;
        for (int split = idx; split <= r; split++) {
            if (str[split] == str[l] && str[split] != str[split - 1]) {
                if (process2(str, idx, split - 1, false) == 0) {
                    way2 = Math.min(way2, process2(str, split, r, k != 0));
                }
            }
        }
        return Math.min(way1, way2);
    }

    public static int min2(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() < 2) {
            return s.length();
        }
        char[] str = s.toCharArray();
        return process2(str, 0, str.length - 1, false);
    }

    //

    private static int process3(char[] str, int l, int r, boolean has, int[][][] dp) {
        if (l > r) {
            return 0;
        }
        int k = has ? 1 : 0;
        if (dp[l][r][k] != -1) {
            return dp[l][r][k];
        }
        int ans = 0;
        if (l == r) {
            ans = (k == 0 ? 1 : 0);
        } else {
            int idx = l;
            int all = k;
            while (idx <= r && str[idx] == str[l]){
                all++;
                idx++;
            }
            int way1 = (all > 1 ? 0 : 1) + process3(str, idx, r, false, dp);
            int way2 = Integer.MAX_VALUE;
            for (int split = idx; split <= r; split++) {
                if (str[split] == str[l] && str[split] != str[split - 1]) {
                    if (process3(str, idx, split - 1, false, dp) == 0) {
                        way2 = Math.min(way2, process3(str, split, r, all > 0, dp));
                    }
                }
            }
            ans = Math.min(way1, way2);
        }
        dp[l][r][k] = ans;
        return ans;
    }

    public static int min3(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() < 2) {
            return s.length();
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[][][] dp = new int[n][n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        return process3(str, 0, n - 1, false, dp);
    }

    //

    private static String randomStr(int len ,int maxKind) {
         char[] str = new char[len];
         for (int i = 0; i < len; i++) {
             str[i] = (char) ((int) ((maxKind + 1) * Math.random()) + 'a');
         }
         return String.valueOf(str);
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 16;
        int maxKind = 3;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) ((maxLen + 1) * Math.random());
            String str = randomStr(len, maxKind);
            int ans1 = min1(str);
            int ans2 = min2(str);
            int ans3 = min3(str);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
