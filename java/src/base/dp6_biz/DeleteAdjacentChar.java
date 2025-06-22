package base.dp6_biz;

/**
 * 消除相邻相同字符问题
 * 给定字符串，可以消除任意长度的相邻相同字符，求消除后的最小剩余字符数
 * 这是一个区间DP问题，关键在于考虑是否将相邻区间合并消除
 */
public class DeleteAdjacentChar {
    /**
     * 检查字符串是否全部由相同字符组成（可以完全消除）
     */
    private static boolean canDelete(String s) {
        char[] str = s.toCharArray();
        for (int i = 1; i < str.length; i++) {
            if (str[i - 1] != str[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 方法1：暴力递归解法
     * 尝试删除所有可能的相邻相同字符子串
     */
    public static int min1(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() < 2) {
            return s.length();
        }
        int minLen = s.length();
        // 尝试删除每个可能的子串[l,r]
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

    /**
     * 方法2：区间DP递归解法
     * @param str 字符数组
     * @param l 左边界
     * @param r 右边界  
     * @param has 左边界字符是否有前置相同字符（用于合并）
     * @return 区间[l,r]的最小剩余字符数
     */
    private static int process2(char[] str, int l, int r, boolean has) {
        if (l > r) {
            return 0;
        }
        if (l == r) {
            return has ? 0 : 1; // 单个字符，有前置则可消除，否则剩余1个
        }
        
        // 统计从l开始的连续相同字符个数
        int idx = l;
        int k = has ? 1 : 0; // k表示包含前置字符的总个数
        while (idx <= r && str[idx] == str[l]) {
            k++;
            idx++;
        }
        
        // 策略1：当前连续字符独立处理
        int way1 = (k > 1 ? 0 : 1) + process2(str, idx, r, false);
        
        // 策略2：寻找后面相同字符进行合并
        int way2 = Integer.MAX_VALUE;
        for (int split = idx; split <= r; split++) {
            // 找到相同字符且不与前一个字符相同（确保是新的开始）
            if (str[split] == str[l] && str[split] != str[split - 1]) {
                // 中间部分必须能完全消除，才能进行合并
                if (process2(str, idx, split - 1, false) == 0) {
                    way2 = Math.min(way2, process2(str, split, r, k != 0));
                }
            }
        }
        return Math.min(way1, way2);
    }

    /**
     * 方法2的对外接口
     */
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

    /**
     * 方法3：带记忆化的区间DP
     * @param str 字符数组
     * @param l 左边界
     * @param r 右边界
     * @param has 是否有前置相同字符
     * @param dp 记忆化数组 dp[l][r][has]
     * @return 最小剩余字符数
     */
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
            // 统计连续相同字符
            while (idx <= r && str[idx] == str[l]){
                all++;
                idx++;
            }
            // 策略1：独立处理
            int way1 = (all > 1 ? 0 : 1) + process3(str, idx, r, false, dp);
            
            // 策略2：合并处理
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

    /**
     * 方法3：记忆化搜索版本
     */
    public static int min3(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() < 2) {
            return s.length();
        }
        char[] str = s.toCharArray();
        int n = str.length;
        // dp[l][r][has] -1表示未计算
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

    /**
     * 生成随机字符串用于测试
     */
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
