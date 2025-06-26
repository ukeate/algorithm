package giant.c16;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 数组子集和判断问题
 * 
 * 问题描述：
 * 给定一个整数数组arr（可能包含正数、负数、零），自由选择数组中的数字，
 * 判断能否通过累加得到目标和sum。
 * 
 * 例如：
 * arr = [1, -2, 3, -1], sum = 2
 * 可以选择 [1, -2, 3] = 2，返回true
 * 
 * arr = [1, 2, 3], sum = 7
 * 无论怎么选择都无法得到7，返回false
 * 
 * 解决方案：
 * 1. 方法1：记忆化递归法 - 时间复杂度O(N*sum)
 * 2. 方法2：动态规划法 - 时间复杂度O(N*(max-min))
 * 3. 方法3：分治法 - 时间复杂度O(2^(N/2))，适用于N较小的情况
 * 
 * 核心思想：
 * 这是经典的子集和问题(Subset Sum Problem)的变种，支持负数
 * 
 * 时间复杂度：O(N*范围) 或 O(2^(N/2))
 * 空间复杂度：O(N*范围) 或 O(2^(N/2))
 */
public class IsSum {
    
    /**
     * 递归处理函数（方法1使用）
     * 
     * 算法思路：
     * 对于数组中的每个元素，有两种选择：
     * 1. 选择当前元素：递归处理剩余元素，目标和减去当前元素值
     * 2. 不选择当前元素：递归处理剩余元素，目标和不变
     * 
     * @param arr 输入数组
     * @param i 当前考虑的数组索引
     * @param sum 目标累加和
     * @param dp 记忆化缓存
     * @return 是否能通过选择arr[0...i]中的元素得到sum
     */
    private static boolean process1(int[] arr, int i, int sum, HashMap<Integer, HashMap<Integer, Boolean>> dp) {
        // 检查缓存
        if (dp.containsKey(i) && dp.get(i).containsKey(sum)) {
            return dp.get(i).get(sum);
        }
        
        boolean ans = false;
        
        if (sum == 0) {
            // 目标和为0，总是可以达到（选择空集）
            ans = true;
        } else if (i != -1) {
            // 还有元素可以选择
            // 选择1：不选择当前元素arr[i]
            boolean p1 = process1(arr, i - 1, sum, dp);
            // 选择2：选择当前元素arr[i]
            boolean p2 = process1(arr, i - 1, sum - arr[i], dp);
            ans = p1 || p2;
        }
        // 如果i == -1且sum != 0，则ans保持false
        
        // 缓存结果
        if (!dp.containsKey(i)) {
            dp.put(i, new HashMap<>());
        }
        dp.get(i).put(sum, ans);
        
        return ans;
    }

    /**
     * 方法1：记忆化递归法
     * 
     * 算法特点：
     * - 使用递归方式，直观易理解
     * - 通过记忆化避免重复计算
     * - 适用于目标和sum范围不太大的情况
     * 
     * 时间复杂度：O(N*sum_range)
     * 空间复杂度：O(N*sum_range)
     * 
     * @param arr 输入数组
     * @param sum 目标累加和
     * @return 是否能通过选择数组中的元素得到sum
     */
    public static boolean isSum1(int[] arr, int sum) {
        if (sum == 0) {
            return true;  // 目标和为0，选择空集即可
        }
        if (arr == null || arr.length == 0) {
            return false;  // 空数组无法得到非零和
        }
        
        return process1(arr, arr.length - 1, sum, new HashMap<>());
    }

    /**
     * 方法2：动态规划法
     * 
     * 算法思路：
     * 1. 计算数组所有元素的最小和min和最大和max
     * 2. 如果目标和超出[min, max]范围，直接返回false
     * 3. 使用dp[i][j]表示前i个元素能否组成和为j的子集
     * 4. 由于可能存在负数，需要对索引进行偏移处理
     * 
     * 状态转移方程：
     * dp[i][j] = dp[i-1][j] || dp[i-1][j-arr[i]]
     * 
     * 优化点：
     * - 通过计算理论最值范围，避免不必要的空间浪费
     * - 使用索引偏移技巧处理负数情况
     * 
     * 时间复杂度：O(N*(max-min))
     * 空间复杂度：O(N*(max-min))
     * 
     * @param arr 输入数组
     * @param sum 目标累加和
     * @return 是否能通过选择数组中的元素得到sum
     */
    public static boolean dp(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        
        // 计算理论上的最小和最大累加和
        int min = 0, max = 0;
        for (int num : arr) {
            min += num < 0 ? num : 0;  // 所有负数之和
            max += num > 0 ? num : 0;  // 所有正数之和
        }
        
        // 如果目标和超出理论范围，直接返回false
        if (sum < min || sum > max) {
            return false;
        }
        
        int n = arr.length;
        // dp[i][j-min]表示前i个元素能否组成和为j的子集
        // 使用偏移-min来处理负数索引
        boolean[][] dp = new boolean[n][max - min + 1];
        
        // 初始化：第一个元素的情况
        dp[0][-min] = true;              // 不选择第一个元素，和为0
        dp[0][arr[0] - min] = true;      // 选择第一个元素，和为arr[0]
        
        // 填表
        for (int i = 1; i < n; i++) {
            for (int j = min; j <= max; j++) {
                // 不选择当前元素arr[i]
                dp[i][j - min] = dp[i - 1][j - min];
                
                // 选择当前元素arr[i]
                int prev = j - min - arr[i];  // 之前需要的和对应的索引
                if (prev >= 0 && prev <= max - min) {
                    dp[i][j - min] |= dp[i - 1][prev];
                }
            }
        }
        
        return dp[n - 1][sum - min];
    }

    /**
     * 递归生成所有可能的子集和（方法3使用）
     * 
     * @param arr 输入数组
     * @param i 当前处理的索引
     * @param end 结束索引（不包含）
     * @param pre 当前累加和
     * @param ans 存储所有可能和的集合
     */
    private static void process2(int[] arr, int i, int end, int pre, HashSet<Integer> ans) {
       if (i == end) {
           ans.add(pre);  // 到达边界，添加当前累加和
       } else {
           // 不选择当前元素
           process2(arr, i + 1, end, pre, ans);
           // 选择当前元素
           process2(arr, i + 1, end, pre + arr[i], ans);
       }
    }

    /**
     * 方法3：分治法（Meet in the Middle）
     * 
     * 算法思路：
     * 1. 将数组分成两半
     * 2. 分别计算两半所有可能的子集和
     * 3. 对于左半部分的每个和l，检查右半部分是否存在(sum-l)
     * 
     * 优化思想：
     * - 时间复杂度从O(2^N)降低到O(2^(N/2))
     * - 适用于N较小（N <= 40）但sum范围很大的情况
     * - 空间换时间的经典算法
     * 
     * 时间复杂度：O(2^(N/2))
     * 空间复杂度：O(2^(N/2))
     * 
     * @param arr 输入数组
     * @param sum 目标累加和
     * @return 是否能通过选择数组中的元素得到sum
     */
    public static boolean isSum2(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        if (arr.length == 1) {
            return arr[0] == sum;
        }
        
        int n = arr.length;
        int mid = n >> 1;  // 中点位置
        
        // 生成左半部分所有可能的子集和
        HashSet<Integer> left = new HashSet<>();
        process2(arr, 0, mid, 0, left);
        
        // 生成右半部分所有可能的子集和
        HashSet<Integer> right = new HashSet<>();
        process2(arr, mid, n, 0, right);
        
        // 检查是否存在l + r = sum的组合
        for (int l : left) {
            if (right.contains(sum - l)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 生成随机数组用于测试
     * @param len 数组长度
     * @param max 数值范围[-max, max]
     * @return 随机数组
     */
    private static int[] randomArr(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * ((max << 1) + 1)) - max;
        }
        return arr;
    }

    /**
     * 测试方法：验证三种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 数组子集和判断问题测试 ===");
        
        // 手动测试用例
        System.out.println("1. 手动测试用例:");
        int[] test1 = {1, -2, 3, -1};
        int sum1 = 2;
        System.out.println("数组: [1, -2, 3, -1], 目标和: " + sum1);
        System.out.println("方法1: " + isSum1(test1, sum1));
        System.out.println("方法2: " + dp(test1, sum1));
        System.out.println("方法3: " + isSum2(test1, sum1));
        System.out.println("期望: true (1 + 3 - 2 = 2)");
        System.out.println();
        
        // 随机测试验证算法正确性
        int times = 100000;
        int maxLen = 20;
        int maxVal = 100;
        
        System.out.println("2. 随机测试验证:");
        System.out.println("测试次数: " + times);
        System.out.println("最大数组长度: " + maxLen);
        System.out.println("数值范围: [-" + maxVal + ", " + maxVal + "]");
        System.out.println("开始测试...");
        
        boolean allCorrect = true;
        for (int i = 0; i < times; i++) {
            int size = (int) (Math.random() * (maxLen + 1));
            int[] arr = randomArr(size, maxVal);
            int sum = (int) (Math.random() * ((maxVal << 1) + 1)) - maxVal;
            
            boolean ans1 = isSum1(arr, sum);
            boolean ans2 = dp(arr, sum);
            boolean ans3 = isSum2(arr, sum);
            
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("发现错误!");
                System.out.println("数组: " + java.util.Arrays.toString(arr));
                System.out.println("目标和: " + sum);
                System.out.println("方法1: " + ans1 + ", 方法2: " + ans2 + ", 方法3: " + ans3);
                allCorrect = false;
                break;
            }
            
            if ((i + 1) % 20000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试...");
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}
