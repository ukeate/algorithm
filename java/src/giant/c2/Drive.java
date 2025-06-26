package giant.c2;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 司机分配两区域收入最大化问题
 * 
 * 问题描述：
 * 有N个司机（N为偶数），需要将他们平均分配到两个区域（每个区域N/2个司机）。
 * 每个司机在不同区域有不同的收入：income[i][0]表示第i个司机在区域0的收入，
 * income[i][1]表示第i个司机在区域1的收入。
 * 求如何分配司机使得总收入最大。
 * 
 * 解法对比：
 * 1. 方法1：暴力递归 - 时间O(2^n)，空间O(n)
 * 2. 方法2：动态规划 - 时间O(n²)，空间O(n²)
 * 3. 方法3：贪心算法 - 时间O(n*log n)，空间O(n)
 * 
 * 核心洞察：
 * 贪心策略最优 - 先假设所有司机都去区域0，然后选择收益差最大的一半司机转到区域1
 * 
 * @author algorithm learning
 */
public class Drive {

    /**
     * 方法1：暴力递归解法
     * 
     * 算法思路：
     * 对每个司机，尝试分配到区域0或区域1，递归计算所有可能的分配方案
     * 
     * 状态定义：
     * - idx: 当前正在分配第idx个司机
     * - rest: 区域0还需要分配多少个司机
     * 
     * 边界条件：
     * - 如果区域0已满（rest=0），剩余司机全部分配到区域1
     * - 如果区域1已满（剩余司机数=rest），剩余司机全部分配到区域0
     * 
     * 时间复杂度：O(2^n)，每个司机有两种选择
     * 空间复杂度：O(n)，递归栈深度
     * 
     * @param income 司机收入矩阵，income[i][0]为第i个司机在区域0的收入，income[i][1]为区域1的收入
     * @param idx 当前处理的司机索引
     * @param rest 区域0还需要分配的司机数量
     * @return 从当前状态开始的最大收入
     */
    private static int process1(int[][] income, int idx, int rest) {
        // 基础情况：所有司机都已分配完毕
        if (idx == income.length) {
            return 0;
        }
        
        // 边界情况1：区域1已满，剩余司机必须全部分配到区域0
        if (income.length - idx == rest) {
            return income[idx][0] + process1(income, idx + 1, rest - 1);
        }
        
        // 边界情况2：区域0已满，剩余司机必须全部分配到区域1
        if (rest == 0) {
            return income[idx][1] + process1(income, idx + 1, rest);
        }
        
        // 一般情况：两个区域都还有空位，选择收入更高的分配方案
        int p1 = income[idx][0] + process1(income, idx + 1, rest - 1); // 分配到区域0
        int p2 = income[idx][1] + process1(income, idx + 1, rest);     // 分配到区域1
        return Math.max(p1, p2);
    }

    /**
     * 方法1：暴力递归的接口方法
     * @param income 司机收入矩阵
     * @return 最大总收入
     */
    public static int max1(int[][] income) {
        // 边界条件检查
        if (income == null || income.length < 2 || (income.length & 1) != 0) {
            return 0; // 司机数量必须为偶数且至少为2
        }
        int n = income.length;
        int m = n >> 1; // 每个区域分配的司机数量
        return process1(income, 0, m);
    }

    /**
     * 方法2：动态规划解法
     * 
     * 算法思路：
     * 将递归改为动态规划，避免重复计算子问题
     * 
     * 状态定义：
     * dp[i][j] 表示从第i个司机开始分配，区域0还需要j个司机时的最大收入
     * 
     * 状态转移：
     * - 如果区域1已满：dp[i][j] = income[i][0] + dp[i+1][j-1]
     * - 如果区域0已满：dp[i][j] = income[i][1] + dp[i+1][j]
     * - 其他情况：dp[i][j] = max(income[i][0] + dp[i+1][j-1], income[i][1] + dp[i+1][j])
     * 
     * 时间复杂度：O(n²)，状态空间为n*m，每个状态O(1)计算
     * 空间复杂度：O(n²)，DP表大小
     * 
     * @param income 司机收入矩阵
     * @return 最大总收入
     */
    public static int max2(int[][] income) {
        int n = income.length;
        int m = n >> 1; // 每个区域分配的司机数量
        
        // dp[i][j] 表示从第i个司机开始，区域0还需要j个司机的最大收入
        int[][] dp = new int[n + 1][m + 1];
        
        // 从后往前填充DP表
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= m; j++) {
                // 区域1已满，剩余司机必须全部分配到区域0
                if (n - i == j) {
                    dp[i][j] = income[i][0] + dp[i + 1][j - 1];
                } 
                // 区域0已满，剩余司机必须全部分配到区域1
                else if (j == 0) {
                    dp[i][j] = income[i][1] + dp[i + 1][j];
                } 
                // 两个区域都有空位，选择最优分配
                else {
                    int p1 = income[i][0] + dp[i + 1][j - 1]; // 分配到区域0
                    int p2 = income[i][1] + dp[i + 1][j];     // 分配到区域1
                    dp[i][j] = Math.max(p1, p2);
                }
            }
        }
        
        return dp[0][m];
    }

    /**
     * 方法3：贪心算法解法（最优解）
     * 
     * 算法思路：
     * 1. 先假设所有司机都分配到区域0，计算总收入
     * 2. 对每个司机计算"转移收益" = 区域1收入 - 区域0收入
     * 3. 按转移收益降序排序，选择前N/2个司机转移到区域1
     * 
     * 贪心策略的正确性证明：
     * - 最终必须有N/2个司机在区域0，N/2个司机在区域1
     * - 对于司机i，如果他最终在区域1，贡献为income[i][1]
     * - 如果他最终在区域0，贡献为income[i][0]
     * - 相当于每个在区域1的司机比在区域0多贡献了diff[i] = income[i][1] - income[i][0]
     * - 要使总贡献最大，应该选择diff值最大的N/2个司机放在区域1
     * 
     * 时间复杂度：O(n*log n)，主要是排序的复杂度
     * 空间复杂度：O(n)，用于存储转移收益数组
     * 
     * @param income 司机收入矩阵
     * @return 最大总收入
     */
    public static int max3(int[][] income) {
        int n = income.length;
        int[] arr = new int[n]; // 存储每个司机的转移收益
        int sum = 0;            // 所有司机都在区域0的总收入
        
        // 计算每个司机的转移收益和基础收入
        for (int i = 0; i < n; i++) {
            arr[i] = income[i][1] - income[i][0]; // 转移到区域1的额外收益
            sum += income[i][0];                  // 基础收入（假设都在区域0）
        }
        
        // 按转移收益降序排序
        Arrays.sort(arr);
        
        // 选择转移收益最大的N/2个司机转移到区域1
        int m = n >> 1;
        for (int i = n - 1; i >= m; i--) {
            sum += arr[i]; // 加上转移收益
        }
        
        return sum;
    }

    /**
     * 生成随机收入矩阵用于测试
     * @param len 司机数量的一半
     * @param maxVal 收入的最大值
     * @return 随机生成的收入矩阵
     */
    private static int[][] randomMatrix(int len, int maxVal) {
        int[][] ans = new int[len << 1][2]; // len*2个司机，每个司机2个区域的收入
        for (int i = 0; i < ans.length; i++) {
            ans[i][0] = (int) ((maxVal + 1) * Math.random()); // 区域0的收入
            ans[i][1] = (int) ((maxVal + 1) * Math.random()); // 区域1的收入
        }
        return ans;
    }

    /**
     * 测试方法：对拍验证三种算法的正确性
     */
    public static void main(String[] args) {
        int times = 500;     // 测试次数
        int maxLen = 10;     // 最大司机数量（每个区域）
        int maxVal = 100;    // 最大收入值
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) (maxLen * Math.random()) + 1;
            int[][] matrix = randomMatrix(len, maxVal);
            
            int ans1 = max1(matrix);  // 暴力递归
            int ans2 = max2(matrix);  // 动态规划
            int ans3 = max3(matrix);  // 贪心算法
            
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                System.out.println("递归解法: " + ans1 + " | 动态规划: " + ans2 + " | 贪心算法: " + ans3);
                break;
            }
        }
        System.out.println("test end");
        
        System.out.println("\n=== 性能测试 ===");
        System.out.println("推荐使用方法3（贪心算法）:");
        System.out.println("- 时间复杂度最优: O(n*log n)");
        System.out.println("- 空间复杂度最优: O(n)");
        System.out.println("- 代码简洁，易于理解和实现");
    }
}
