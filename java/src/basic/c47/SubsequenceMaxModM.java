package basic.c47;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * 子数组累加和模M的最大值问题
 * 
 * 问题描述：
 * 给定一个整数数组arr和一个正整数m，求所有子数组的累加和模m的最大值
 * 子数组是指数组中连续的一段元素
 * 
 * 例如：
 * arr = [1, 2, 3], m = 4
 * 所有子数组及其累加和：
 * [1] -> 1, [2] -> 2, [3] -> 3
 * [1,2] -> 3, [2,3] -> 5, [1,2,3] -> 6
 * 模4后的值：1, 2, 3, 3, 1, 2
 * 最大值为3
 * 
 * 解法思路：
 * 方法1：暴力枚举所有子数组 - O(2^n)
 * 方法2：动态规划记录所有可能的和 - O(n * sum)
 * 方法3：优化DP，只记录模m的结果 - O(n * m)
 * 方法4：折半搜索（分治） - O(n * 2^(n/2))
 * 
 * 适用场景：
 * - 当数组长度较小时，使用方法1或4
 * - 当m较小时，使用方法3
 * - 当数组元素和较小时，使用方法2
 * 
 * 时间复杂度：根据不同方法而异
 * 空间复杂度：根据不同方法而异
 * 
 * @author 算法学习
 */
public class SubsequenceMaxModM {
    
    /**
     * 递归生成所有子数组的累加和
     * 
     * @param arr 输入数组
     * @param idx 当前处理的位置
     * @param sum 当前累计和
     * @param set 存储所有可能的累加和
     */
    private static void process(int[] arr, int idx, int sum, HashSet<Integer> set) {
        if (idx == arr.length) {
            set.add(sum);  // 到达数组末尾，记录当前累加和
        } else {
            // 不选择当前元素
            process(arr, idx + 1, sum, set);
            // 选择当前元素
            process(arr, idx + 1, sum + arr[idx], set);
        }
    }

    /**
     * 方法1：暴力递归解法
     * 
     * @param arr 输入数组
     * @param m 模数
     * @return 所有子数组累加和模m的最大值
     * 
     * 算法思路：
     * 通过递归枚举所有可能的子数组（实际是子序列），计算每个的累加和
     * 然后取模m，找出最大值
     * 
     * 时间复杂度：O(2^n) - 每个元素有选和不选两种状态
     * 空间复杂度：O(2^n) - 存储所有可能的累加和
     * 
     * 注意：这里实际计算的是子序列而不是子数组
     */
    public static int max1(int[] arr, int m) {
        HashSet<Integer> set = new HashSet<>();
        process(arr, 0, 0, set);
        
        int max = 0;
        for (Integer sum : set) {
            max = Math.max(max, sum % m);
        }
        return max;
    }

    /**
     * 方法2：动态规划解法（完整版）
     * 
     * @param arr 输入数组
     * @param m 模数
     * @return 所有子数组累加和模m的最大值
     * 
     * 算法思路：
     * dp[i][j] 表示前i个元素能否组成累加和j
     * 状态转移：dp[i][j] = dp[i-1][j] | dp[i-1][j-arr[i]]
     * 
     * 时间复杂度：O(n * sum) - sum是所有元素的总和
     * 空间复杂度：O(n * sum)
     * 
     * 适用场景：当数组元素和相对较小时
     */
    public static int max2(int[] arr, int m) {
        int sum = 0;
        int n = arr.length;
        
        // 计算所有元素的总和
        for (int i = 0; i < n; i++) {
            sum += arr[i];
        }
        
        // dp[i][j] 表示前i个元素能否组成累加和j
        boolean[][] dp = new boolean[n][sum + 1];
        
        // 边界条件：任何前缀都可以组成和0（不选任何元素）
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        
        // 第一个元素只能组成它自己的值
        dp[0][arr[0]] = true;
        
        // 状态转移
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= sum; j++) {
                // 不选择当前元素
                dp[i][j] = dp[i - 1][j];
                
                // 选择当前元素（如果j >= arr[i]）
                if (j - arr[i] >= 0) {
                    dp[i][j] = dp[i][j] | dp[i - 1][j - arr[i]];
                }
            }
        }
        
        // 找出所有可能的累加和模m的最大值
        int ans = 0;
        for (int j = 0; j <= sum; j++) {
            if (dp[n - 1][j]) {
                ans = Math.max(ans, j % m);
            }
        }
        return ans;
    }

    /**
     * 方法3：优化的动态规划解法
     * 
     * @param arr 输入数组
     * @param m 模数
     * @return 所有子数组累加和模m的最大值
     * 
     * 算法思路：
     * 由于我们只关心模m的结果，所以状态空间可以压缩到[0, m-1]
     * dp[i][j] 表示前i个元素能否组成模m余数为j的累加和
     * 
     * 优化关键：
     * (a + b) % m = ((a % m) + (b % m)) % m
     * 
     * 时间复杂度：O(n * m)
     * 空间复杂度：O(n * m)
     * 
     * 适用场景：当模数m相对较小时
     */
    public static int max3(int[] arr, int m) {
        int n = arr.length;
        
        // dp[i][j] 表示前i个元素能否组成模m余数为j的累加和
        boolean[][] dp = new boolean[n][m];
        
        // 边界条件：任何前缀都可以组成余数0（不选任何元素）
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        
        // 第一个元素只能组成它自己模m的余数
        dp[0][arr[0] % m] = true;
        
        // 状态转移
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                // 不选择当前元素
                dp[i][j] = dp[i - 1][j];
                
                // 选择当前元素
                int cur = arr[i] % m;  // 当前元素的余数
                
                if (j - cur >= 0) {
                    // 余数相减不跨越边界
                    dp[i][j] = dp[i][j] | dp[i - 1][j - cur];
                }
                if (j - cur < 0) {
                    // 余数相减跨越边界，需要加上m
                    dp[i][j] = dp[i][j] | dp[i - 1][m + j - cur];
                }
            }
        }
        
        // 找出最大的可能余数
        int ans = 0;
        for (int i = 0; i < m; i++) {
            if (dp[n - 1][i]) {
                ans = i;  // 由于从0到m-1遍历，最后一个为true的就是最大值
            }
        }
        return ans;
    }

    /**
     * 递归生成数组一半元素的所有可能累加和（用于折半搜索）
     * 
     * @param arr 输入数组
     * @param idx 当前处理位置
     * @param sum 当前累计和
     * @param end 处理结束位置
     * @param m 模数
     * @param set 存储所有可能的累加和模m的结果
     */
    private static void process4(int[] arr, int idx, int sum, int end, int m, TreeSet<Integer> set) {
        if (idx == end + 1) {
            set.add(sum % m);  // 记录当前累加和模m的结果
        } else {
            // 不选择当前元素
            process4(arr, idx + 1, sum, end, m, set);
            // 选择当前元素
            process4(arr, idx + 1, sum + arr[idx], end, m, set);
        }
    }

    /**
     * 方法4：折半搜索（分治优化）
     * 
     * @param arr 输入数组
     * @param m 模数
     * @return 所有子数组累加和模m的最大值
     * 
     * 算法思路：
     * 将数组分成两半，分别计算所有可能的累加和模m的结果
     * 然后枚举左半部分的每个结果，在右半部分中找最优匹配
     * 
     * 核心优化：
     * 对于左半部分的余数leftMod，右半部分最优的余数是：
     * - 如果存在rightMod使得leftMod + rightMod < m，选择最大的这样的rightMod
     * - 否则选择任意rightMod，结果为(leftMod + rightMod) % m
     * 
     * 使用TreeSet的floor方法可以高效查找最大的不超过target的值
     * 
     * 时间复杂度：O(n * 2^(n/2)) - 相比O(2^n)有显著改进
     * 空间复杂度：O(2^(n/2))
     * 
     * 适用场景：当数组长度中等且m较大时
     */
    public static int max4(int[] arr, int m) {
        if (arr.length == 1) {
            return arr[0] % m;
        }
        
        int mid = (arr.length - 1) / 2;
        
        // 计算左半部分所有可能的累加和模m
        TreeSet<Integer> set1 = new TreeSet<>();
        process4(arr, 0, 0, mid, m, set1);
        
        // 计算右半部分所有可能的累加和模m
        TreeSet<Integer> set2 = new TreeSet<>();
        process4(arr, mid + 1, 0, arr.length - 1, m, set2);
        
        int ans = 0;
        
        // 枚举左半部分的每个余数，在右半部分找最优匹配
        for (Integer leftMod : set1) {
            // 理想情况：leftMod + rightMod = m - 1（最大可能值）
            // 实际情况：找最大的rightMod使得leftMod + rightMod < m
            Integer bestRight = set2.floor(m - 1 - leftMod);
            if (bestRight != null) {
                // 找到了不跨越m的最优匹配
                ans = Math.max(ans, leftMod + bestRight);
            } else {
                // 没有找到不跨越m的匹配，选择最大的rightMod
                // 结果会是(leftMod + maxRight) % m
                ans = Math.max(ans, (leftMod + set2.last()) % m);
            }
        }
        
        return ans;
    }

    /**
     * 生成随机测试数组
     */
    private static int[] randomArr(int len, int val) {
        int[] ans = new int[(int) (len * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (val * Math.random());
        }
        return ans;
    }

    /**
     * 测试方法：验证各种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 子数组累加和模M最大值测试 ===");
        
        // 小规模手工测试
        int[] testArr = {1, 2, 3};
        int testM = 4;
        
        System.out.println("手工测试：");
        System.out.println("数组: [1, 2, 3]");
        System.out.println("模数: " + testM);
        System.out.println("方法1结果: " + max1(testArr, testM));
        System.out.println("方法2结果: " + max2(testArr, testM));
        System.out.println("方法3结果: " + max3(testArr, testM));
        System.out.println("方法4结果: " + max4(testArr, testM));
        
        // 大规模随机测试
        System.out.println("\n=== 大规模随机测试 ===");
        int times = 500000;
        int len = 10;      // 数组长度上限
        int val = 100;     // 元素值上限
        int m = 76;        // 模数
        
        System.out.println("测试参数：");
        System.out.println("测试次数: " + times);
        System.out.println("数组长度: 1-" + len);
        System.out.println("元素范围: 0-" + val);
        System.out.println("模数: " + m);
        
        System.out.println("\n开始测试...");
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, val);
            int ans1 = max1(arr, m);
            int ans2 = max2(arr, m);
            int ans3 = max3(arr, m);
            int ans4 = max4(arr, m);
            
            if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
                System.out.println("测试失败！");
                System.out.println("数组: " + java.util.Arrays.toString(arr));
                System.out.println("结果: " + ans1 + ", " + ans2 + ", " + ans3 + ", " + ans4);
                return;
            }
            
            // 每完成10万次测试输出进度
            if ((i + 1) % 100000 == 0) {
                System.out.printf("已完成 %d 次测试\n", i + 1);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.printf("测试完成！耗时 %d ms\n", endTime - startTime);
        System.out.println("所有测试通过，算法实现正确！");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析与适用场景 ===");
        System.out.println("方法1 - 暴力递归：");
        System.out.println("  时间复杂度: O(2^n)");
        System.out.println("  空间复杂度: O(2^n)");
        System.out.println("  适用场景: 数组长度很小（n ≤ 20）");
        
        System.out.println("\n方法2 - 完整DP：");
        System.out.println("  时间复杂度: O(n * sum)");
        System.out.println("  空间复杂度: O(n * sum)");
        System.out.println("  适用场景: 数组元素和较小");
        
        System.out.println("\n方法3 - 优化DP：");
        System.out.println("  时间复杂度: O(n * m)");
        System.out.println("  空间复杂度: O(n * m)");
        System.out.println("  适用场景: 模数m较小（推荐方法）");
        
        System.out.println("\n方法4 - 折半搜索：");
        System.out.println("  时间复杂度: O(n * 2^(n/2))");
        System.out.println("  空间复杂度: O(2^(n/2))");
        System.out.println("  适用场景: 数组长度中等且模数较大");
    }
}
