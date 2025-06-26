package basic.c48;

/**
 * 打爆气球获得最大得分问题
 * 
 * 问题描述：
 * 给定一个数组nums，每个元素代表气球上的数字
 * 打爆第i个气球可以获得nums[left] * nums[i] * nums[right]分数
 * 其中left和right是第i个气球左右相邻的未爆气球
 * 当打爆气球后，left和right就变成相邻的了
 * 边界气球的左边或右边被认为是1
 * 
 * 目标：求打爆所有气球能获得的最大得分
 * 
 * 例如：
 * nums = [3,1,5,8]
 * 边界条件：[1,3,1,5,8,1]
 * 可能的打爆顺序：1->5->3->8，总得分为：
 * 3*1*5 + 3*5*8 + 1*3*8 + 1*8*1 = 15+120+24+8 = 167
 * 
 * 算法思路：
 * 这是一个经典的区间动态规划问题
 * 
 * 关键洞察：
 * 不要考虑先打爆哪个气球，而是考虑最后打爆哪个气球
 * 如果最后打爆位置i的气球，那么在此之前，[l,i-1]和[i+1,r]区间的气球都已经被打爆
 * 此时打爆i的得分就是：help[l-1] * help[i] * help[r+1]
 * 
 * 状态定义：
 * dp[l][r] 表示打爆开区间(l,r)内所有气球的最大得分
 * 
 * 状态转移：
 * dp[l][r] = max{dp[l][i] + dp[i][r] + help[l]*help[i]*help[r]} (l < i < r)
 * 
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n²)
 * 
 * @author 算法学习
 * @see <a href="https://leetcode.com/problems/burst-balloons/">LeetCode 312</a>
 */
public class BurstBalloons {
    
    /**
     * 递归求解打爆气球的最大得分
     * 
     * @param arr 包含边界1的辅助数组
     * @param l 左边界（不会被打爆）
     * @param r 右边界（不会被打爆）
     * @return 打爆开区间(l,r)内所有气球的最大得分
     * 
     * 算法思路：
     * 考虑在区间(l,r)中最后打爆哪个气球
     * 如果最后打爆位置i的气球，则：
     * 1. 先打爆(l,i)内的所有气球，得分为process(arr, l, i)
     * 2. 再打爆(i,r)内的所有气球，得分为process(arr, i, r)
     * 3. 最后打爆i位置的气球，得分为arr[l] * arr[i] * arr[r]
     * 
     * 边界条件：
     * 当l和r相邻时（l+1==r），区间内没有气球，返回0
     */
    private static int process(int[] arr, int l, int r) {
        if (l >= r - 1) {
            return 0;  // 开区间内没有气球
        }
        
        // 只有一个气球的情况
        if (l == r - 2) {
            return arr[l] * arr[l + 1] * arr[r];
        }
        
        int max = 0;
        
        // 枚举最后打爆的气球位置i
        for (int i = l + 1; i < r; i++) {
            // 最后打爆i位置气球的得分 = 
            // 先打爆(l,i)的得分 + 先打爆(i,r)的得分 + 最后打爆i的得分
            int score = process(arr, l, i) + process(arr, i, r) + arr[l] * arr[i] * arr[r];
            max = Math.max(max, score);
        }
        
        return max;
    }

    /**
     * 方法1：递归解法
     * 
     * @param arr 原始气球数组
     * @return 最大得分
     * 
     * 算法步骤：
     * 1. 构造辅助数组，在原数组两端添加1作为边界
     * 2. 调用递归函数求解
     */
    public static int max1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];  // 只有一个气球，得分就是它本身
        }
        
        int n = arr.length;
        // 构造辅助数组：[1, arr[0], arr[1], ..., arr[n-1], 1]
        int[] help = new int[n + 2];
        help[0] = 1;        // 左边界
        help[n + 1] = 1;    // 右边界
        for (int i = 0; i < n; i++) {
            help[i + 1] = arr[i];
        }
        
        return process(help, 0, n + 1);
    }

    /**
     * 方法2：动态规划解法
     * 
     * @param arr 原始气球数组
     * @return 最大得分
     * 
     * 算法思路：
     * 将递归转换为动态规划，避免重复计算
     * dp[l][r] 表示打爆开区间(l,r)内所有气球的最大得分
     * 
     * 填表顺序：
     * 按区间长度从小到大填表，先填长度为3的区间，再填长度为4的区间...
     */
    public static int max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        
        int n = arr.length;
        // 构造辅助数组
        int[] help = new int[n + 2];
        help[0] = 1;
        help[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            help[i + 1] = arr[i];
        }
        
        // dp[i][j] 表示打爆开区间(i,j)内所有气球的最大得分
        int[][] dp = new int[n + 2][n + 2];
        
        // 初始化：长度为3的区间（只有一个气球）
        for (int i = 0; i < n; i++) {
            dp[i][i + 2] = help[i] * help[i + 1] * help[i + 2];
        }
        
        // 按区间长度填表：从长度4开始到长度n+2
        for (int len = 4; len <= n + 2; len++) {
            for (int l = 0; l <= n + 2 - len; l++) {
                int r = l + len - 1;
                
                // 枚举最后打爆的气球位置
                for (int i = l + 1; i < r; i++) {
                    int score = dp[l][i] + dp[i][r] + help[l] * help[i] * help[r];
                    dp[l][r] = Math.max(dp[l][r], score);
                }
            }
        }
        
        return dp[0][n + 1];
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 打爆气球最大得分测试 ===");
        
        // 测试用例1
        int[] test1 = {3, 1, 5, 8};
        System.out.println("测试用例1: [3, 1, 5, 8]");
        System.out.println("递归方法结果: " + max1(test1));
        System.out.println("动态规划结果: " + max2(test1));
        
        // 测试用例2
        int[] test2 = {1, 5};
        System.out.println("\n测试用例2: [1, 5]");
        System.out.println("递归方法结果: " + max1(test2));
        System.out.println("动态规划结果: " + max2(test2));
        
        // 边界测试
        int[] test3 = {5};
        System.out.println("\n测试用例3: [5]");
        System.out.println("递归方法结果: " + max1(test3));
        System.out.println("动态规划结果: " + max2(test3));
        
        // 空数组测试
        int[] test4 = {};
        System.out.println("\n测试用例4: []");
        System.out.println("递归方法结果: " + max1(test4));
        System.out.println("动态规划结果: " + max2(test4));
        
        // 性能对比测试
        System.out.println("\n=== 性能对比测试 ===");
        int[] largeTest = {4, 2, 3, 5, 1, 6, 7, 8};
        
        long start = System.currentTimeMillis();
        int result1 = max1(largeTest);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = max2(largeTest);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.printf("大数组测试: %s\n", java.util.Arrays.toString(largeTest));
        System.out.printf("递归方法: 结果=%d, 耗时=%dms\n", result1, time1);
        System.out.printf("动态规划: 结果=%d, 耗时=%dms\n", result2, time2);
        
        if (result1 == result2) {
            System.out.println("✓ 两种方法结果一致");
        } else {
            System.out.println("✗ 结果不一致，需要检查算法");
        }
        
        // 算法特点总结
        System.out.println("\n=== 算法特点总结 ===");
        System.out.println("问题特点：");
        System.out.println("1. 经典的区间动态规划问题");
        System.out.println("2. 关键是考虑最后打爆哪个气球，而不是先打爆哪个");
        System.out.println("3. 边界处理：在原数组两端添加虚拟气球1");
        
        System.out.println("\n复杂度分析：");
        System.out.println("时间复杂度: O(n³) - 三层循环");
        System.out.println("空间复杂度: O(n²) - 二维DP表");
        System.out.println("适用场景: 区间合并、区间最值问题");
    }
}
