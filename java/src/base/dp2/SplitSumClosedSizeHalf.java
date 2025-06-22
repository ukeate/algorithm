package base.dp2;

/**
 * 等长度分割数组问题（和尽可能接近）
 * 
 * 问题描述：
 * 给定一个正整数数组，将其分割成两个子集，要求：
 * 1. 两个子集的大小相等（如果数组长度为偶数）或相差1（如果数组长度为奇数）
 * 2. 在满足大小约束的前提下，两个子集的和尽可能接近
 * 返回较小子集的和。
 * 
 * 这是在SplitSumClosed基础上增加了大小约束的版本。
 * 
 * 解法分析：
 * 1. 三维状态：process(arr, i, picks, rest)
 *    - i: 当前考虑的位置
 *    - picks: 还需要选择的元素个数
 *    - rest: 剩余的和的限制
 * 2. 分奇偶讨论：偶数长度时两个子集大小相等；奇数长度时一个子集比另一个多1个元素
 * 
 * 时间复杂度：O(n * (n/2) * sum)
 * 空间复杂度：O(n * (n/2) * sum)
 */
public class SplitSumClosedSizeHalf {
    
    /**
     * 暴力递归解法
     * 
     * @param arr 数组
     * @param i 当前考虑的位置
     * @param picks 还需要选择的元素个数
     * @param rest 剩余的和的限制
     * @return 在约束条件下能达到的最大和，无法满足返回-1
     */
    private static int process(int[] arr, int i, int picks, int rest) {
        // base case：所有元素都考虑完了
        if (i == arr.length) {
            return picks == 0 ? 0 : -1;  // 必须恰好选择picks个元素
        }
        
        // 如果剩余需要选择的元素个数为负，无效状态
        if (picks < 0) {
            return -1;
        }
        
        // 不选择当前元素
        int p1 = process(arr, i + 1, picks, rest);
        
        // 选择当前元素
        int p2 = -1;
        int next = -1;
        if (arr[i] <= rest) {
            next = process(arr, i + 1, picks - 1, rest - arr[i]);
        }
        if (next != -1) {
            p2 = arr[i] + next;
        }
        
        return Math.max(p1, p2);
    }

    /**
     * 等长度分割数组（和尽可能接近）
     * 
     * 策略：
     * - 如果数组长度为偶数，每个子集包含n/2个元素
     * - 如果数组长度为奇数，一个子集包含n/2个元素，另一个包含n/2+1个元素
     * 
     * @param arr 输入数组
     * @return 较小子集的和
     */
    public static int closed(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        
        if ((arr.length & 1) == 0) {
            // 偶数长度：每个子集n/2个元素
            return process(arr, 0, arr.length / 2, sum / 2);
        } else {
            // 奇数长度：尝试n/2个元素和n/2+1个元素，取较大值
            return Math.max(process(arr, 0, arr.length / 2, sum / 2), 
                           process(arr, 0, arr.length / 2 + 1, sum / 2));
        }
    }

    /**
     * 动态规划解法1
     * 
     * dp[i][picks][rest]表示从位置i开始，选择picks个元素，
     * 在不超过rest的限制下能达到的最大和
     * 
     * @param arr 输入数组
     * @return 较小子集的和
     */
    public static int dp(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        sum /= 2;
        
        int n = arr.length;
        int m = (n + 1) / 2;  // 最多选择的元素个数
        
        // dp[i][j][k]表示从位置i开始，选择j个元素，不超过k的最大和
        int[][][] dp = new int[n + 1][m + 1][sum + 1];
        
        // 初始化为-1，表示无效状态
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k <= sum; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        
        // base case：不需要选择元素时，和为0
        for (int rest = 0; rest <= sum; rest++) {
            dp[n][0][rest] = 0;
        }
        
        // 从后往前填充DP表
        for (int i = n - 1; i >= 0; i--) {
            for (int picks = 0; picks <= m; picks++) {
                for (int rest = 0; rest <= sum; rest++) {
                    // 不选择当前元素
                    int p1 = dp[i + 1][picks][rest];
                    
                    // 选择当前元素
                    int p2 = -1;
                    int next = -1;
                    if (picks - 1 >= 0 && arr[i] <= rest) {
                        next = dp[i + 1][picks - 1][rest - arr[i]];
                    }
                    if (next != -1) {
                        p2 = arr[i] + next;
                    }
                    
                    dp[i][picks][rest] = Math.max(p1, p2);
                }
            }
        }
        
        if ((arr.length & 1) == 0) {
            return dp[0][arr.length / 2][sum];
        } else {
            return Math.max(dp[0][arr.length / 2][sum], dp[0][(arr.length / 2) + 1][sum]);
        }
    }

    /**
     * 动态规划解法2（优化版本）
     * 
     * 使用不同的状态定义和初始化方式，避免使用-1标记
     * 
     * @param arr 输入数组
     * @return 较小子集的和
     */
    public static int dp2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        sum >>= 1;  // sum /= 2
        
        int n = arr.length;
        int m = (arr.length + 1) >> 1;  // 最多选择的元素个数
        
        // dp[i][j][k]表示使用前i个元素，选择j个，和为k是否可能
        int[][][] dp = new int[n][m + 1][sum + 1];
        
        // 初始化为负无穷，表示不可能达到
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k <= sum; k++) {
                    dp[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }
        
        // 初始化：选择0个元素时，和为0
        for (int i = 0; i < n; i++) {
            for (int k = 0; k <= sum; k++) {
                dp[i][0][k] = 0;
            }
        }
        
        // 初始化第一行：只考虑第一个元素
        for (int k = 0; k <= sum; k++) {
            dp[0][1][k] = arr[0] <= k ? arr[0] : Integer.MIN_VALUE;
        }
        
        // 填充DP表
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= Math.min(i + 1, m); j++) {
                for (int k = 0; k <= sum; k++) {
                    // 不选择当前元素
                    dp[i][j][k] = dp[i - 1][j][k];
                    
                    // 选择当前元素
                    if (k - arr[i] >= 0) {
                        dp[i][j][k] = Math.max(dp[i][j][k], dp[i - 1][j - 1][k - arr[i]] + arr[i]);
                    }
                }
            }
        }
        
        // 返回结果：比较两种可能的选择个数
        return Math.max(dp[n - 1][m][sum], dp[n - 1][n - m][sum]);
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 10;
        int maxVal = 50;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = closed(arr);
            int ans2 = dp(arr);
            int ans3 = dp2(arr);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
