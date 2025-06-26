package giant.c6;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 最多异或为0的子数组段数问题
 * 
 * 问题描述：
 * 给定一个整数数组，你可以把数组分成若干段，求最多有多少段的异或和为0。
 * 
 * 例如：
 * [3, 2, 1, 9, 0, 6, 3] 可以分成：
 * [3,2,1] (异或和为0), [9], [0] (异或和为0), [6,3]
 * 其中有2段异或和为0，这是最优解。
 * 
 * 解决方案：
 * 1. 方法1：暴力递归法 - 尝试所有可能的分割方案，时间复杂度O(2^N)
 * 2. 方法2：动态规划法 - 使用前缀异或和和哈希表优化，时间复杂度O(N^2)
 * 
 * 核心思想：
 * 如果前缀异或和在位置i和位置j相等，那么[i+1, j]这段的异或和为0
 * 利用这个性质，可以快速找到所有异或和为0的子数组段
 * 
 * 时间复杂度：O(N) - 方法2优化版本
 * 空间复杂度：O(N)
 */
public class MostXorZero {
    
    /**
     * 计算给定分割方案下异或和为0的段数
     * 
     * @param eor 前缀异或和数组
     * @param parts 分割点列表，表示每段的结束位置
     * @return 异或和为0的段数
     */
    private static int eorZeros(int[] eor, ArrayList<Integer> parts) {
        int l = 0;  // 当前段的起始位置
        int ans = 0;  // 异或和为0的段数
        
        // 遍历每个分割段
        for (Integer end : parts) {
            // 计算当前段[l, end-1]的异或和
            // 段异或和 = eor[end-1] ^ eor[l-1] (l>0时)
            int segmentXor = eor[end - 1] ^ (l == 0 ? 0 : eor[l - 1]);
            if (segmentXor == 0) {
                ans++;
            }
            l = end;  // 更新下一段的起始位置
        }
        return ans;
    }

    /**
     * 暴力递归方法：尝试所有可能的分割方案
     * 
     * 算法思路：
     * 对于每个位置，都有两种选择：
     * 1. 在此处分割
     * 2. 不在此处分割
     * 递归尝试所有可能，返回最优解
     * 
     * @param eor 前缀异或和数组
     * @param idx 当前考虑的位置
     * @param parts 当前的分割方案
     * @return 当前方案下的最大异或和为0的段数
     */
    private static int process(int[] eor, int idx, ArrayList<Integer> parts) {
        int ans = 0;
        
        if (idx == eor.length) {
            // 到达数组末尾，添加最后一个分割点并计算结果
            parts.add(eor.length);
            ans = eorZeros(eor, parts);
            parts.remove(parts.size() - 1);  // 回溯
        } else {
            // 选择1：不在idx位置分割
            int p1 = process(eor, idx + 1, parts);
            
            // 选择2：在idx位置分割
            parts.add(idx);
            int p2 = process(eor, idx + 1, parts);
            parts.remove(parts.size() - 1);  // 回溯
            
            ans = Math.max(p1, p2);
        }
        return ans;
    }

    /**
     * 方法1：暴力递归法
     * 
     * 时间复杂度：O(2^N)，每个位置都有分割/不分割两种选择
     * 空间复杂度：O(N)，递归调用栈和前缀异或和数组
     * 
     * 适用场景：数组长度很小（N <= 20）的情况下使用
     * 
     * @param arr 输入数组
     * @return 最多异或和为0的段数
     */
    public static int sure(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        // 构建前缀异或和数组
        int[] eor = new int[n];
        eor[0] = arr[0];
        for (int i = 1; i < n; i++) {
            eor[i] = eor[i - 1] ^ arr[i];
        }
        
        // 从位置1开始递归（位置0必须包含在第一段中）
        return process(eor, 1, new ArrayList<>());
    }

    /**
     * 方法2：动态规划优化法
     * 
     * 算法思路：
     * 1. 使用前缀异或和快速计算任意段的异或值
     * 2. 利用哈希表记录每个前缀异或和最后出现的位置
     * 3. dp[i]表示前i个元素最多能分出多少个异或和为0的段
     * 4. 状态转移：如果存在j使得xor[i] = xor[j]，则(j+1,i]异或和为0
     * 
     * 关键观察：
     * - 如果前缀异或和xor[i] = xor[j]，那么(j+1, i]的异或和为0
     * - 贪心策略：一旦能形成异或和为0的段，就立即分割
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     * 
     * @param arr 输入数组
     * @return 最多异或和为0的段数
     */
    public static int most(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        // dp[i]：考虑前i+1个元素时，最多能分出多少个异或和为0的段
        int[] dp = new int[n];
        
        // map：记录每个前缀异或和最后出现的位置
        // key: 前缀异或和值, value: 最后出现的位置
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);  // 前缀异或和为0对应位置-1（便于处理边界）
        
        int xor = 0;  // 当前前缀异或和
        
        for (int i = 0; i < n; i++) {
            xor ^= arr[i];  // 更新前缀异或和
            
            // 检查当前前缀异或和是否之前出现过
            if (map.containsKey(xor)) {
                int pre = map.get(xor);  // 上次出现的位置
                
                // 如果在位置pre之后到位置i，异或和为0
                // 状态转移：dp[i] = dp[pre] + 1（新增一个异或和为0的段）
                dp[i] = pre == -1 ? 1 : (dp[pre] + 1);
            }
            
            // 继承之前的最优解（不分割的情况）
            if (i > 0) {
                dp[i] = Math.max(dp[i - 1], dp[i]);
            }
            
            // 更新当前前缀异或和的最后出现位置
            map.put(xor, i);
        }
        
        return dp[n - 1];
    }
    
    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最多异或为0的子数组段数测试 ===");
        
        // 测试用例1：简单情况
        int[] test1 = {3, 2, 1, 9, 0, 6, 3};
        System.out.println("测试数组1: [3, 2, 1, 9, 0, 6, 3]");
        System.out.println("方法1结果: " + sure(test1));
        System.out.println("方法2结果: " + most(test1));
        System.out.println();
        
        // 测试用例2：全零数组
        int[] test2 = {0, 0, 0, 0};
        System.out.println("测试数组2: [0, 0, 0, 0]");
        System.out.println("方法1结果: " + sure(test2));
        System.out.println("方法2结果: " + most(test2));
        System.out.println();
        
        // 测试用例3：无法分割的情况
        int[] test3 = {1, 2, 4, 8};
        System.out.println("测试数组3: [1, 2, 4, 8]");
        System.out.println("方法1结果: " + sure(test3));
        System.out.println("方法2结果: " + most(test3));
        System.out.println();
        
        // 随机测试验证
        System.out.println("随机测试验证算法正确性...");
        int times = 10000;
        int maxLen = 10;  // 限制长度以确保方法1能运行
        int maxVal = 20;
        
        boolean allCorrect = true;
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * maxLen) + 1;
            int[] arr = new int[len];
            for (int j = 0; j < len; j++) {
                arr[j] = (int) (Math.random() * maxVal);
            }
            
            int ans1 = sure(arr);
            int ans2 = most(arr);
            
            if (ans1 != ans2) {
                System.out.println("发现错误！测试用例: " + i);
                System.out.println("数组: " + java.util.Arrays.toString(arr));
                System.out.println("方法1: " + ans1 + ", 方法2: " + ans2);
                allCorrect = false;
                break;
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过！");
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
}
