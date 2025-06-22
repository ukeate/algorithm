package base.dp4_quad;

// https://leetcode.com/problems/split-array-largest-sum/
// 划分k部分，使部分最大和最小
// 画家问题：输入待画序列, 画家数量，只能画相邻画，求最小完成时间
/**
 * 数组分割为K部分问题 / 画家问题
 * 将数组分割为k个连续的子数组，使得这些子数组中的最大和尽可能小
 * 也可理解为画家问题：k个画家画n幅连续的画，每个画家只能画连续的画，求最小完成时间
 * 
 * 提供多种解法：
 * 1. 暴力递归
 * 2. 标准DP O(n²k)
 * 3. 四边形不等式优化DP O(nk)
 * 4. 二分搜索 + 贪心 O(n log(sum))
 * 5. 画家问题的特殊DP
 */
public class BestSplitK {
    /**
     * 辅助方法：找到数组中的最大值
     */
    private static int max(int[] arr){
        int max = 0;
        for (int num : arr) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    /**
     * 递归方法：暴力尝试所有分割方案
     * @param arr 数组
     * @param idx 当前起始位置
     * @param m 剩余分割数
     * @return 最小的最大子数组和
     */
    private static int process(int[] arr, int idx, int m) {
        // 处理完所有元素
        if (idx == arr.length) {
            return 0;
        }
        // 没有分割数了，方案不可行
        if (m == 0) {
            return -1;
        }
        // 分割数大于等于剩余元素数，每个元素单独一组
        if (m >= arr.length) {
            return max(arr);
        }
        
        int first = 0;
        int min = Integer.MAX_VALUE;
        // 尝试当前分组的所有可能结束位置
        for (int end = idx; arr.length - end >= m; end++) {
            first += arr[end];
            int rest = process(arr, end + 1, m - 1);
            if (rest != -1) {
                min = Math.min(min, Math.max(first, rest));
            }
        }
        return min;
    }

    /**
     * 方法0：递归解法
     */
    public static int split0(int[] arr, int m) {
        return process(arr, 0, m);
    }

    /**
     * 辅助方法：计算前缀和数组中[l,r]的和
     */
    private static int sum(int[] sums, int l, int r) {
        return sums[r + 1] - sums[l];
    }

    /**
     * 方法1：标准动态规划解法
     * dp[i][j]表示前i+1个元素分成j组的最小最大值
     * 时间复杂度O(n²k)
     */
    public static int split1(int[] nums, int k) {
        int n = nums.length;
        // 构建前缀和数组
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        
        int[][] dp = new int[n][k + 1];
        
        // 基础条件：第一个元素分成j组（j>=1）
        for (int j = 1; j <= k; j++) {
            dp[0][j] = nums[0];
        }
        
        // 基础条件：前i+1个元素分成1组
        for (int i = 1; i < n; i++) {
            dp[i][1] = sum(sum, 0, i);
        }
        
        // 填表
        for (int i = 1; i < n; i++) {
            for (int j = 2; j <= k; j++) {
                int ans = Integer.MAX_VALUE;
                // 枚举最后一组的起始位置
                for (int leftEnd = 0; leftEnd <= i; leftEnd++) {
                    int leftCost = leftEnd == -1 ? 0 : dp[leftEnd][j - 1];
                    int rightCost = leftEnd == -1 ? 0 : sum(sum, leftEnd + 1, i);
                    int cur = Math.max(leftCost, rightCost);
                    if (cur < ans) {
                        ans = cur;
                    }
                }
                dp[i][j] = ans;
            }
        }
        return dp[n - 1][k];
    }

    //

    /**
     * 方法2：四边形不等式优化的动态规划
     * 利用最优决策点的单调性，时间复杂度优化到O(nk)
     */
    public static int split2(int[] nums, int k) {
        int n = nums.length;
        int[] sums = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        
        int[][] dp = new int[n][k + 1];
        int[][] best = new int[n][k + 1]; // 记录最优决策点
        
        // 基础条件
        for (int j = 1; j <= k; j++) {
            dp[0][j] = nums[0];
            best[0][j] = -1;
        }
        for (int i = 1; i < n; i++) {
            dp[i][1] = sum(sums, 0, i);
            best[i][1] = -1;
        }
        
        // 利用四边形不等式性质优化
        for (int j = 2; j <= k; j++) {
            for (int i = n - 1; i >= 1; i--) {
                // 最优决策点的搜索范围
                int down = best[i][j - 1];
                int up = i == n - 1 ? n - 1 : best[i + 1][j];
                
                int ans = Integer.MAX_VALUE;
                int bestChoose = -1;
                
                // 在缩小的范围内搜索最优决策点
                for (int leftEnd = down; leftEnd <= up; leftEnd++) {
                    int leftCost = leftEnd == -1 ? 0 : dp[leftEnd][j - 1];
                    int rightCost = leftEnd == i ? 0 : sum(sums, leftEnd + 1, i);
                    int cur = Math.max(leftCost, rightCost);
                    if (cur < ans) {
                        ans = cur;
                        bestChoose = leftEnd;
                    }
                }
                dp[i][j] = ans;
                best[i][j] = bestChoose;
            }
        }
        return dp[n - 1][k];
    }

    //

    /**
     * 辅助方法：判断给定限制下需要多少个分组
     * @param arr 数组
     * @param aim 每组和的上限
     * @return 所需的分组数
     */
    public static int getNeedParts(int[] arr, long aim) {
        // 如果存在单个元素大于限制，无法分组
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > aim) {
                return Integer.MAX_VALUE;
            }
        }
        
        int parts = 1;
        int all = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (all + arr[i] > aim) {
                // 当前组装不下，开始新组
                parts++;
                all = arr[i];
            } else {
                all += arr[i];
            }
        }
        return parts;
    }

    /**
     * 方法3：二分搜索 + 贪心算法
     * 二分搜索答案，贪心验证可行性
     * 时间复杂度O(n log(sum))
     */
    public static int split3(int[] nums, int m) {
        long sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }
        
        long l = 0;
        long r = sum;
        long ans = 0;
        
        // 二分搜索最小的最大子数组和
        while (l <= r) {
            long mid = (l + r) / 2;
            long cur = getNeedParts(nums, mid);
            if (cur <= m) {
                // 分组数不超过m，可以尝试更小的值
                ans = mid;
                r = mid - 1;
            } else {
                // 分组数超过m，需要增大限制
                l = mid + 1;
            }
        }
        return (int) ans;
    }

    //

    /**
     * 方法4：画家问题的特殊DP解法
     * 专门针对画家问题优化的动态规划
     */
    public static int split4(int[] arr, int m) {
        if (m >= arr.length) {
            return max(arr);
        }
        
        int n = arr.length;
        // 构建前缀和数组
        int[] help = new int[arr.length + 1];
        for (int i = 0; i < n; i++) {
            help[i + 1] = help[i] + arr[i];
        }
        
        int[][] dp = new int[n][m + 1];
        
        // 基础条件：1个画家
        for (int i = 0; i < n; i++) {
            dp[i][1] = help[i + 1] - help[0];
        }
        
        // 特殊情况：每个画家画一幅画
        for (int i = 1; i < Math.min(n, m); i++) {
            dp[i][i + 1] = Math.max(dp[i - 1][i], arr[i]);
        }
        
        // 填表
        for (int i = 2; i < n; i++) {
            for (int j = 2; j <= Math.min(i, m); j++) {
                dp[i][j] = Integer.MAX_VALUE;
                // 枚举最后一个画家的起始位置
                for (int k = i; k >= j - 1; k--) {
                    dp[i][j] = Math.min(dp[i][j], 
                        Math.max(dp[k - 1][j - 1], help[i + 1] - help[k]));
                }
            }
        }
        return dp[n - 1][m];
    }

    /**
     * 方法5：画家问题的四边形不等式优化版本
     */
    public static int split5(int[] arr, int m) {
        if (m >= arr.length) {
            return max(arr);
        }
        
        int n = arr.length;
        int[] help = new int[arr.length + 1];
        for (int i = 0; i < n; i++) {
            help[i + 1] = help[i] + arr[i];
        }
        
        int[][] dp = new int[n][m + 1];
        int[][] best = new int[n][m + 1]; // 记录最优决策点
        
        // 基础条件
        for (int i = 0; i < n; i++) {
            dp[i][1] = help[i + 1] - help[0];
        }
        
        for (int i = 1; i < Math.min(n, m); i++) {
            dp[i][i + 1] = Math.max(dp[i - 1][i], arr[i]);
            best[i][i + 1] = i;
        }
        
        // 利用四边形不等式优化
        for (int i = 2; i < n; i++) {
            for (int j = Math.min(i, m); j >= 2; j--) {
                dp[i][j] = Integer.MAX_VALUE;
                int left = best[i - 1][j];
                int right = j + 1 > m ? i : best[i][j + 1];
                
                for (int k = left; k <= right; k++) {
                    int ans = Math.max(dp[k - 1][j - 1], help[i + 1] - help[k]);
                    if (dp[i][j] > ans) {
                        dp[i][j] = ans;
                        best[i][j] = k;
                    }
                }
            }
        }
        return dp[n - 1][m];
    }


    //

    /**
     * 打印数组的辅助方法
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 生成随机数组用于测试
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 测试方法，验证各个算法的正确性
     */
    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int m = (int) ((maxLen + 1) * Math.random()) + 1;
            int[] arr = randomArr(maxLen, maxVal);
//            int ans0 = split0(arr, m);
            int ans1 = split1(arr, m);
            int ans2 = split2(arr, m);
            int ans3 = split3(arr, m);
            int ans4 = split4(arr, m);
            int ans5 = split5(arr, m);
            if (ans1 != ans2 || ans3 != ans4 || ans4 != ans5 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(m);
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4 + "|" + ans5);
                break;
            }
        }
        System.out.println("test end");
    }
}
