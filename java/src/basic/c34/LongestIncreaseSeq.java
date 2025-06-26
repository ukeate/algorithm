package basic.c34;

/**
 * 最长递增子序列问题（Longest Increasing Subsequence, LIS）
 * 
 * 问题描述：
 * 给定一个整数数组，找到其中最长的严格递增子序列。
 * 子序列不要求连续，但必须保持原数组中元素的相对顺序。
 * 
 * 例如：数组[2, 1, 5, 3, 6, 4, 8, 9, 7]
 * 最长递增子序列可能是[1, 3, 4, 8, 9]，长度为5
 * 
 * 解决方案：
 * 1. 方法1：动态规划基础版 - O(N²)
 * 2. 方法2：二分搜索优化版 - O(N*logN)
 * 
 * 算法核心思想：
 * 方法1：dp[i]表示以arr[i]结尾的最长递增子序列长度
 * 方法2：维护一个ends数组，ends[i]表示长度为i+1的递增子序列的最小结尾数字
 * 
 * 时间复杂度：方法1为O(N²)，方法2为O(N*logN)
 * 空间复杂度：O(N)
 */
public class LongestIncreaseSeq {
    
    /**
     * 方法1：基础动态规划求LIS的dp数组
     * 
     * 算法思路：
     * dp[i]表示以arr[i]结尾的最长递增子序列的长度
     * 对于每个位置i，检查所有前面的位置j（j<i）
     * 如果arr[j] < arr[i]，则可以将arr[i]接在以arr[j]结尾的子序列后面
     * 
     * 状态转移方程：
     * dp[i] = max(dp[j] + 1) for all j < i where arr[j] < arr[i]
     * 
     * @param arr 输入数组
     * @return dp数组，dp[i]表示以arr[i]结尾的LIS长度
     */
    private static int[] getDp1(int[] arr) {
        int[] dp = new int[arr.length];
        
        // 每个元素自己构成长度为1的子序列
        for (int i = 0; i < arr.length; i++) {
            dp[i] = 1;
            
            // 检查所有前面的位置
            for (int j = 0; j < i; j++) {  // 注意：j应该从0开始而不是1
                if (arr[i] > arr[j]) {
                    // 可以将arr[i]接在以arr[j]结尾的子序列后面
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return dp;
    }

    /**
     * 根据dp数组还原出实际的最长递增子序列
     * 
     * 算法思路：
     * 1. 找到dp数组中的最大值及其位置，这就是LIS的长度和结尾位置
     * 2. 从结尾位置开始向前回溯，寻找构成LIS的所有元素
     * 3. 回溯条件：arr[i] < arr[当前位置] 且 dp[i] = dp[当前位置] - 1
     * 
     * @param arr 原数组
     * @param dp dp数组，记录每个位置的LIS长度
     * @return 实际的最长递增子序列
     */
    private static int[] lisArr(int[] arr, int[] dp) {
        // 找到最大长度和对应的结束位置
        int len = 0;
        int idx = 0;
        for (int i = 0; i < dp.length; i++) {
            if (dp[i] > len) {
                len = dp[i];
                idx = i;
            }
        }
        
        // 构建LIS数组
        int[] lis = new int[len];
        lis[--len] = arr[idx];  // 放入最后一个元素，len递减作为下一个位置
        
        // 从后向前回溯构建LIS
        for (int i = idx - 1; i >= 0; i--) {
            // 回溯条件：元素值更小且dp值恰好少1
            if (arr[i] < arr[idx] && dp[i] == dp[idx] - 1) {
                lis[--len] = arr[i];
                idx = i;  // 更新当前回溯位置
            }
        }
        return lis;
    }

    /**
     * 方法1：使用基础动态规划的LIS算法
     * 时间复杂度：O(N²)
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 最长递增子序列
     */
    public static int[] lis1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        int[] dp = getDp1(arr);
        return lisArr(arr, dp);
    }

    /**
     * 方法2：二分搜索优化的LIS求dp数组
     * 
     * 算法思路：
     * 维护一个ends数组，ends[i]表示所有长度为i+1的递增子序列中，结尾元素的最小值
     * 对于每个新元素，使用二分搜索在ends数组中找到合适的位置进行更新
     * 
     * 核心观察：
     * 1. ends数组本身是严格递增的
     * 2. 对于新元素x，如果x比ends中所有元素都大，则可以扩展LIS长度
     * 3. 否则，找到第一个大于等于x的位置进行替换，保持ends的最优性
     * 
     * @param arr 输入数组
     * @return dp数组，记录每个位置的LIS长度
     */
    private static int[] getDp2(int[] arr) {
        int[] dp = new int[arr.length];
        
        // ends[i]表示长度为i+1的递增子序列的最小结尾元素
        int[] ends = new int[arr.length];
        ends[0] = arr[0];
        dp[0] = 1;
        int endR = 0;  // ends数组的有效右边界
        
        // 处理每个元素
        for (int i = 1; i < arr.length; i++) {
            // 二分搜索：找到第一个大于等于arr[i]的位置
            int l = 0, r = endR, m = 0;
            while (l <= r) {
                m = (l + r) / 2;
                if (arr[i] > ends[m]) {
                    l = m + 1;  // 在右半部分继续搜索
                } else {
                    r = m - 1;  // 在左半部分继续搜索
                }
            }
            
            // 更新ends数组和右边界
            endR = Math.max(endR, l);  // 如果l超出当前范围，扩展右边界
            ends[l] = arr[i];          // 更新位置l的最小结尾元素
            dp[i] = l + 1;             // 记录以arr[i]结尾的LIS长度
        }
        return dp;
    }

    /**
     * 方法2：使用二分搜索优化的LIS算法
     * 时间复杂度：O(N*logN)
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 最长递增子序列
     */
    public static int[] lis2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        int[] dp = getDp2(arr);
        return lisArr(arr, dp);
    }

    /**
     * 辅助方法：打印数组
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
            System.out.println("null");
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        // 测试用例
        int[] arr = {2, 1, 5, 3, 6, 4, 8, 9, 7};
        
        System.out.println("=== 最长递增子序列测试 ===");
        System.out.print("原数组: ");
        print(arr);
        
        System.out.print("方法1结果(O(N²)): ");
        print(lis1(arr));
        
        System.out.print("方法2结果(O(N*logN)): ");
        print(lis2(arr));
        
        // 测试边界情况
        System.out.println("\n=== 边界情况测试 ===");
        
        // 单元素数组
        int[] single = {42};
        System.out.print("单元素数组: ");
        print(single);
        System.out.print("LIS: ");
        print(lis2(single));
        
        // 严格递增数组
        int[] increasing = {1, 2, 3, 4, 5};
        System.out.print("严格递增数组: ");
        print(increasing);
        System.out.print("LIS: ");
        print(lis2(increasing));
        
        // 严格递减数组
        int[] decreasing = {5, 4, 3, 2, 1};
        System.out.print("严格递减数组: ");
        print(decreasing);
        System.out.print("LIS: ");
        print(lis2(decreasing));
        
        // 性能比较测试
        System.out.println("\n=== 性能比较 ===");
        int[] largeArr = new int[1000];
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = (int) (Math.random() * 1000);
        }
        
        long start1 = System.currentTimeMillis();
        lis1(largeArr);
        long end1 = System.currentTimeMillis();
        
        long start2 = System.currentTimeMillis();
        lis2(largeArr);
        long end2 = System.currentTimeMillis();
        
        System.out.println("方法1耗时: " + (end1 - start1) + "ms");
        System.out.println("方法2耗时: " + (end2 - start2) + "ms");
    }
}
