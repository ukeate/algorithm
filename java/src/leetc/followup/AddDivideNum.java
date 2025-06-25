package leetc.followup;

import java.util.HashMap;

/**
 * 带分数问题 (Mixed Fraction Problem)
 * 
 * 问题描述：
 * 将数字 1-9 排列成 p1 + p2 / p3 的形式，使得结果等于给定数字N
 * 例如：100 = 3 + 69258 / 714，100 = 82 + 3546 / 197
 * 
 * 约束条件：
 * 1. p1、p2、p3 必须使用数字 1-9 且每个数字只能用一次
 * 2. p2 必须能被 p3 整除
 * 3. 计算 p1 + p2/p3 的结果
 * 
 * 输入：一个整数 N (N < 10^8)
 * 输出：N 有多少种不同的带分数表示形式
 * 
 * 解法思路：
 * 全排列 + 数学计算：
 * 1. 生成数字 123456789 的所有排列
 * 2. 对每个排列，尝试所有可能的分割方式：
 *    - p1: 从1位到7位数字
 *    - p2: 剩余数字的前半部分
 *    - p3: 剩余数字的后半部分
 * 3. 检查 p2 % p3 == 0，如果成立则计算 p1 + p2/p3
 * 4. 使用 HashMap 统计每个结果的出现次数
 * 
 * 优化技巧：
 * - 预计算所有可能的排列和结果，避免重复计算
 * - 使用位运算优化除法运算（arr[dev] 实现 10^dev）
 * 
 * 时间复杂度：O(9! × 分割数) - 9! 种排列，每种排列多种分割方式
 * 空间复杂度：O(结果数量) - HashMap 存储不同的计算结果
 */
public class AddDivideNum {
    public static HashMap<Integer, Integer> map = new HashMap<>();  // 存储每个数字对应的带分数表示数量
    public static int[] arr = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000}; // 10的幂次数组

    /**
     * 交换数字 num 中第 l1+1 位和第 l2+1 位的数字
     * 
     * @param num 原始数字
     * @param l1 第一个位置（从右往左，0为个位）
     * @param l2 第二个位置（从右往左，0为个位）
     * @return 交换后的数字
     */
    private static int swap(int num, int l1, int l2) {
        int n1 = (num / arr[l1]) % 10;
        int n2 = (num / arr[l2]) % 10;
        return num + (n2 - n1) * arr[l1] - (n2 - n1) * arr[l2];
    }

    /**
     * 递归生成 [0,idx] 位置的全排列，并计算所有可能的带分数形式
     * 
     * @param num 当前的9位数字排列
     * @param idx 当前处理的位置索引
     */
    private static void process(int num, int idx) {
        if (idx == -1) {
            // 排列完成，尝试所有可能的分割方式
            for (int add = 8; add >= 2; add--) {
                int p1 = num / arr[add];        // p1: 整数部分
                int rest = num % arr[add];      // 剩余部分用于 p2/p3
                
                // 将剩余部分分为 p2 和 p3
                for (int dev = (add >> 1); dev >= 1; dev--) {
                    int p2 = rest / arr[dev];   // p2: 分子
                    int p3 = rest % arr[dev];   // p3: 分母
                    
                    // 检查是否能整除且分母不为0
                    if (p3 != 0 && p2 % p3 == 0) {
                        int ans = p1 + (p2 / p3);  // 计算带分数的值
                        // 统计该结果的出现次数
                        if (!map.containsKey(ans)) {
                            map.put(ans, 1);
                        } else {
                            map.put(ans, map.get(ans) + 1);
                        }
                    }
                }
            }
        } else {
            // 递归生成 [0,idx] 的全排列
            for (int i = idx; i >= 0; i--) {
                process(swap(num, idx, i), idx - 1);
            }
        }
    }

    /**
     * 查询给定数字 n 有多少种带分数表示形式
     * 
     * @param n 目标数字
     * @return n 的带分数表示形式数量
     */
    public static int ways(int n) {
        if (map.size() == 0) {
            // 第一次调用时，计算所有可能的带分数形式
            process(123456789, 8);
        }
        return map.containsKey(n) ? map.get(n) : 0;
    }

    public static void main(String[] args) {
        int n = 100;
        long start, end;
        start = System.currentTimeMillis();
        System.out.println(n + ":" + ways(n));
        end = System.currentTimeMillis();
        System.out.println("run time: " + (end - start));
        n = 10000;
        start = System.currentTimeMillis();
        System.out.println(n + ":" + ways(n));
        end = System.currentTimeMillis();
        System.out.println("run time: " + (end - start));
    }
}
