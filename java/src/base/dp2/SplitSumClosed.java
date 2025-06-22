package base.dp2;

/**
 * 数组分割成两个子集问题（最接近的和）
 * 
 * 问题描述：
 * 给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，
 * 使得两个子集的元素和尽可能接近。返回较小的那个子集的和。
 * 
 * 这个问题是经典的"分割等和子集"问题的变种，目标是让两个子集的和差值最小。
 * 
 * 解法分析：
 * 1. 思路：在不超过sum/2的前提下，尽可能选择更多的数字
 * 2. 状态定义：process(arr, i, rest)表示从arr[i...]中选择数字，不超过rest的最大和
 * 3. 动态规划：dp[i][rest]表示从位置i开始，剩余容量为rest时能达到的最大和
 * 
 * 时间复杂度：O(n * sum)
 * 空间复杂度：O(n * sum)
 */
public class SplitSumClosed {
    
    /**
     * 暴力递归解法
     * 
     * @param arr 数组
     * @param i 当前考虑的位置
     * @param rest 剩余的容量限制
     * @return 在不超过rest的前提下，从arr[i...]能取到的最大和
     */
    private static int process(int[] arr, int i, int rest) {
        // base case：没有更多元素可以选择
        if (i == arr.length) {
            return 0;
        }
        
        // 不选择当前元素
        int p1 = process(arr, i + 1, rest);
        
        // 选择当前元素（如果容量足够）
        int p2 = 0;
        if (arr[i] <= rest) {
            p2 = arr[i] + process(arr, i + 1, rest - arr[i]);
        }
        
        return Math.max(p1, p2);
    }

    /**
     * 分割数组使两个子集的和尽可能接近
     * 
     * 策略：计算总和的一半，在这个限制下尽可能选择更多数字。
     * 如果能取到的最大和是x，那么两个子集的和分别是x和sum-x，
     * 返回较小的那个，即min(x, sum-x) = x。
     * 
     * @param arr 输入数组
     * @return 较小子集的和
     */
    public static int closedSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        
        // 在不超过sum/2的前提下，尽可能选择更多数字
        return process(arr, 0, sum / 2);
    }

    /**
     * 动态规划解法
     * 
     * dp[i][rest]表示从位置i开始，在剩余容量为rest的情况下能取到的最大和
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
        sum /= 2;  // 容量限制
        
        int n = arr.length;
        // dp[i][rest]表示从位置i开始，容量为rest时的最大和
        int[][] dp = new int[n + 1][sum + 1];
        
        // base case：没有元素时，能取到的和为0（dp数组默认初始化为0）
        
        // 从后往前填充DP表
        for (int i = n - 1; i >= 0; i--) {
            for (int rest = 0; rest <= sum; rest++) {
                // 不选择当前元素
                int p1 = dp[i + 1][rest];
                
                // 选择当前元素（如果容量足够）
                int p2 = 0;
                if (arr[i] <= rest) {
                    p2 = arr[i] + dp[i + 1][rest - arr[i]];
                }
                
                dp[i][rest] = Math.max(p1, p2);
            }
        }
        
        return dp[0][sum];
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    public static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 20;
        int maxVal = 50;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = closedSum(arr);
            int ans2 = dp(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
