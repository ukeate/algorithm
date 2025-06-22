package base.dp3_knap;

import java.util.HashSet;
import java.util.TreeSet;

// 非负数组arr, 正数m，返回子序列累加和%m的最大值
/**
 * 子序列累加和模m的最大值问题
 * 给定非负数组arr和正数m，求所有子序列累加和%m的最大值
 * 提供多种解法：暴力枚举、DP优化、Meet in the Middle优化
 */
public class SubsequenceMaxMod {
    /**
     * 递归枚举所有子序列
     * @param arr 数组
     * @param idx 当前处理的索引
     * @param sum 当前累计和
     * @param set 存储所有可能的累计和
     */
    private static void process1(int[] arr, int idx, int sum, HashSet<Integer> set) {
        // 处理完所有元素，记录当前累计和
        if (idx == arr.length) {
            set.add(sum);
        } else {
            // 不选择当前元素
            process1(arr, idx + 1, sum, set);
            // 选择当前元素
            process1(arr, idx + 1, sum + arr[idx], set);
        }
    }
    
    /**
     * 方法1：暴力枚举所有子序列，时间复杂度O(2^n)
     */
    public static int max1(int[] arr, int m) {
        HashSet<Integer> set = new HashSet<>();
        process1(arr, 0, 0, set);
        int max = 0;
        // 找到所有累计和中模m的最大值
        for (Integer sum : set) {
            max = Math.max(max, sum % m);
        }
        return max;
    }

    //

    /**
     * 方法2：动态规划优化，当累计和不大时使用
     * dp[i][j]表示前i个数字能否组成累计和为j
     */
    public static int dp1(int[] arr, int m) {
        int sum = 0;
        int n = arr.length;
        // 计算所有数字的累计和
        for (int i = 0; i < n; i++) {
            sum += arr[i];
        }
        boolean[][] dp = new boolean[n][sum + 1];
        // 基础条件：每行的累计和为0都是可以达到的（什么都不选）
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        // 第一个数字的初始化
        dp[0][arr[0]] = true;
        
        // 填表
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= sum; j++) {
                // 不选择当前数字
                dp[i][j] = dp[i - 1][j];
                // 选择当前数字（如果可行）
                if (j - arr[i] >= 0) {
                    dp[i][j] |= dp[i - 1][j - arr[i]];
                }
            }
        }
        
        // 找到最大的累计和%m值
        int ans = 0;
        for (int j = 0; j <= sum; j++) {
            if (dp[n - 1][j]) {
                ans = Math.max(ans, j % m);
            }
        }
        return ans;
    }

    //

    /**
     * 方法3：基于模运算的动态规划优化
     * dp[i][j]表示前i个数字能否组成累计和模m等于j
     * 空间复杂度降低到O(n*m)
     */
    public static int dp2(int[] arr, int m) {
        int n = arr.length;
        boolean[][] dp = new boolean[n][m];
        // 基础条件：累计和模m为0都是可以达到的
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        // 第一个数字的初始化
        dp[0][arr[0] % m] = true;
        
        // 填表
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                // 不选择当前数字
                dp[i][j] = dp[i - 1][j];
                int cur = arr[i] % m;
                // 选择当前数字，考虑模运算的循环性质
                if (cur <= j) {
                    dp[i][j] |= dp[i - 1][j - cur];
                } else {
                    dp[i][j] |= dp[i - 1][m + j - cur];
                }
            }
        }
        
        // 找到最大的模m值
        int ans = 0;
        for (int i = 0; i < m; i++) {
            if (dp[n - 1][i]) {
                ans = i;
            }
        }
        return ans;
    }

    //

    /**
     * 递归生成指定范围内的所有累计和模m的结果
     * @param arr 数组
     * @param idx 当前索引
     * @param sum 当前累计和
     * @param end 结束索引
     * @param m 模数
     * @param set 存储所有可能的模m结果
     */
    private static void process2(int[] arr, int idx, int sum, int end, int m, TreeSet<Integer> set) {
        if (idx == end + 1) {
            set.add(sum % m);
        } else {
            // 不选择当前元素
            process2(arr, idx + 1, sum, end, m, set);
            // 选择当前元素
            process2(arr, idx + 1, sum + arr[idx], end, m, set);
        }
    }

    /**
     * 方法4：Meet in the Middle优化
     * 当累计和很大，m很大，但数组长度较小时使用
     * 时间复杂度从O(2^n)降低到O(2^(n/2))
     */
    public static int max2(int[] arr, int m) {
        if (arr.length == 1) {
            return arr[0] % m;
        }
        
        // 将数组分成两部分
        int mid = (arr.length - 1) / 2;
        
        // 处理左半部分
        TreeSet<Integer> set1 = new TreeSet<>();
        process2(arr, 0, 0, mid, m, set1);
        
        // 处理右半部分
        TreeSet<Integer> set2 = new TreeSet<>();
        process2(arr, mid + 1, 0, arr.length - 1, m, set2);
        
        int ans = 0;
        // 合并两部分的结果
        for (Integer leftMod : set1) {
            // 找到右半部分中不超过(m-1-leftMod)的最大值
            ans = Math.max(ans, leftMod + set2.floor(m - 1 - leftMod));
        }
        return ans;
    }

    //

    /**
     * 生成随机数组用于测试
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] ans = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    /**
     * 测试方法，验证各个算法的正确性
     */
    public static void main(String[] args) {
        int times = 500000;
        int maxLen = 10;
        int maxVal = 100;
        int m = 76;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = max1(arr, m);
            int ans2 = dp1(arr, m);
            int ans3 = dp2(arr, m);
            int ans4 = max2(arr, m);
            if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
