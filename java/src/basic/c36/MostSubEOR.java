package basic.c36;

import java.util.HashMap;

/**
 * 最多划分异或和为0的子数组数量问题
 * 
 * 问题描述：
 * 给定一个整数数组，将其划分成若干个连续的子数组，使得每个子数组的异或和都为0。
 * 求最多可以划分出多少个这样的子数组。
 * 
 * 核心思想：
 * 利用异或运算的性质：A ^ A = 0
 * 如果从位置0到位置i的前缀异或和 = 从位置0到位置j的前缀异或和，
 * 那么子数组[j+1, i]的异或和为0。
 * 
 * 算法策略：
 * 使用动态规划 + 前缀异或和 + 哈希表：
 * 1. dp[i]表示以位置i结尾的数组最多能划分成多少个异或和为0的子数组
 * 2. 对于每个位置i，如果前缀异或和曾经出现过，说明可以形成新的异或和为0的子数组
 * 3. 选择使划分数量最大的方案
 * 
 * 时间复杂度：O(N)
 * 空间复杂度：O(N)
 */
public class MostSubEOR {

    /**
     * 主算法：计算最多划分异或和为0的子数组数量
     * 
     * 算法思路：
     * 1. 使用HashMap记录每个前缀异或和最后出现的位置
     * 2. dp[i]表示0到i位置最多能划分成多少个异或和为0的子数组
     * 3. 状态转移：如果当前前缀异或和之前出现过，可以形成新的异或和为0的子数组
     * 
     * 关键洞察：
     * - 初始时map中放入(0, -1)，表示空前缀的异或和为0
     * - 如果prefixXor[i] == prefixXor[j]，则子数组[j+1, i]的异或和为0
     * - 选择最优的划分方案：要么不划分（继承dp[i-1]），要么在当前位置划分
     * 
     * @param arr 输入数组
     * @return 最多能划分的异或和为0的子数组数量
     */
    public static int most(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        int[] dp = new int[n];  // dp[i]表示0到i位置的最优划分数量
        
        // 异或和 -> 最晚出现位置的映射
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);  // 初始化：空前缀的异或和为0，位置为-1
        
        int sum = 0;  // 前缀异或和
        
        for (int i = 0; i < arr.length; i++) {
            sum ^= arr[i];  // 更新前缀异或和
            
            if (map.containsKey(sum)) {
                // 当前前缀异或和之前出现过，可以形成异或和为0的子数组
                int pre = map.get(sum);  // 上次出现该异或和的位置
                
                // 子数组[pre+1, i]的异或和为0
                // 可以在位置pre后面划分，得到dp[pre] + 1个子数组
                dp[i] = pre == -1 ? 1 : (dp[pre] + 1);
            }
            
            // 状态转移：选择最优方案
            if (i > 0) {
                dp[i] = Math.max(dp[i - 1], dp[i]);
                // dp[i-1]表示不在当前位置划分的方案
                // dp[i]表示在当前位置划分的方案（如果可以的话）
            }
            
            // 更新当前异或和的最晚出现位置
            map.put(sum, i);
        }
        
        return dp[dp.length - 1];
    }

    /**
     * 暴力验证方法：用于测试正确性
     * 
     * 算法思路：
     * 使用前缀异或和数组，枚举所有可能的划分方案。
     * 时间复杂度：O(N³)，仅用于小规模数据验证
     * 
     * @param arr 输入数组
     * @return 最多能划分的异或和为0的子数组数量
     */
    public static int sure(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 计算前缀异或和数组
        int[] eors = new int[arr.length];
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
            eors[i] = eor;
        }
        
        // 动态规划：dp[i]表示0到i位置的最优划分数量
        int[] dp = new int[arr.length];
        
        // 初始化：检查第一个元素是否为0
        dp[0] = arr[0] == 0 ? 1 : 0;
        
        for (int i = 1; i < arr.length; i++) {
            // 检查0到i的整个前缀是否异或和为0
            dp[i] = eors[i] == 0 ? 1 : 0;
            
            // 枚举所有可能的划分点j
            for (int j = 0; j < i; j++) {
                // 检查子数组[j+1, i]是否异或和为0
                if ((eors[i] ^ eors[j]) == 0) {
                    // 如果是，则可以在j后面划分
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            
            // 选择最优方案：要么划分，要么不划分
            dp[i] = Math.max(dp[i], dp[i - 1]);
        }
        
        return dp[dp.length - 1];
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

    /**
     * 测试方法：验证两种算法的一致性
     */
    public static void main(String[] args) {
        int times = 500000;  // 测试次数
        int maxLen = 300;    // 最大数组长度
        int maxVal = 100;    // 最大元素值
        
        System.out.println("=== 最多异或和为0子数组划分测试开始 ===");
        
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = most(arr);     // 优化算法
            int ans2 = sure(arr);     // 暴力验证算法
            
            if (ans1 != ans2) {
                System.out.println("算法结果不一致！");
                System.out.println("优化算法结果: " + ans1);
                System.out.println("暴力算法结果: " + ans2);
                
                // 打印测试数组
                System.out.print("测试数组: [");
                for (int j = 0; j < arr.length; j++) {
                    System.out.print(arr[j]);
                    if (j < arr.length - 1) System.out.print(", ");
                }
                System.out.println("]");
                break;
            }
        }
        
        System.out.println("=== 测试完成 ===");
    }
}
