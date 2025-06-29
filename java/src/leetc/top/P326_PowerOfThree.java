package leetc.top;

/**
 * LeetCode 326. 3的幂 (Power of Three)
 * 
 * 问题描述：
 * 给定一个整数，写一个函数来判断它是否是 3 的幂次方。
 * 如果是，返回 true ；否则，返回 false 。
 * 整数 n 是 3 的幂次方需满足：存在整数 x 使得 n == 3^x
 * 
 * 进阶：你能不使用循环或者递归来完成本题吗？
 * 
 * 示例：
 * 输入：n = 27
 * 输出：true
 * 解释：27 = 3^3
 * 
 * 输入：n = 0
 * 输出：false
 * 解释：不存在整数 x 使得 3^x = 0
 * 
 * 解法思路：
 * 数学方法 - 最大3的幂取模：
 * 1. 在32位整数范围内，最大的3的幂是3^19 = 1162261467
 * 2. 如果n是3的幂，那么最大3的幂必定能被n整除
 * 3. 这是因为如果n = 3^k，那么1162261467 = 3^19 = 3^k × 3^(19-k)
 * 4. 因此 1162261467 % n == 0 当且仅当 n 是3的幂
 * 
 * 核心思想：
 * - 利用3是质数的特性：最大3的幂只能被3的幂整除
 * - 避免循环和递归，实现O(1)时间复杂度
 * - 预计算最大值，利用数学性质巧妙求解
 * 
 * 数学证明：
 * - 如果n是3的幂，设n = 3^k，那么1162261467 / n = 3^(19-k)，是整数
 * - 如果n不是3的幂，由于3是质数，1162261467只包含因子3，无法被n整除
 * - 因此取模结果为0当且仅当n是3的幂
 * 
 * 时间复杂度：O(1) - 常数时间操作
 * 空间复杂度：O(1) - 只使用常数空间
 * 
 * LeetCode链接：https://leetcode.com/problems/power-of-three/
 */
public class P326_PowerOfThree {
    
    /**
     * 判断一个数是否是3的幂
     * 
     * 算法原理：
     * 1. 首先检查n是否为正数（3的幂必须为正）
     * 2. 使用32位整数范围内最大的3的幂：3^19 = 1162261467
     * 3. 如果n是3的幂，那么1162261467必定能被n整除
     * 4. 这利用了3是质数的特性：3^19只包含因子3
     * 
     * 为什么这样可行：
     * - 在32位整数范围内，最大的3的幂是3^19 = 1162261467
     * - 如果n = 3^k（k ≤ 19），那么3^19 = n × 3^(19-k)
     * - 因此3^19 % n == 0
     * - 如果n不是3的幂，由于3是质数，3^19不能被n整除
     * 
     * @param n 待检查的整数
     * @return 是否是3的幂
     */
    public static boolean isPowerOfThree(int n) {
        // n必须为正数，且最大3的幂能被n整除
        return n > 0 && 1162261467 % n == 0;
    }
}
