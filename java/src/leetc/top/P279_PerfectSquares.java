package leetc.top;

/**
 * LeetCode 279. 完全平方数 (Perfect Squares)
 * 
 * 问题描述：
 * 给定正整数 n，找到若干个完全平方数（比如 1, 4, 9, 16, ...）使得它们的和等于 n。
 * 你需要让组成和的完全平方数的个数最少。
 * 
 * 示例：
 * 输入：n = 12
 * 输出：3 
 * 解释：12 = 4 + 4 + 4
 * 
 * 解法思路：
 * 提供两种方法：
 * 1. 递归暴力搜索（numSquares1）
 * 2. 数学定理优化（numSquares2）
 * 
 * 数学定理（四平方和定理）：
 * - 任何正整数都可以表示为至多4个完全平方数的和
 * - 需要4个的充要条件：n % 8 == 7 或 n消去4因子后 % 8 == 7
 * - 需要1个的条件：n本身是完全平方数
 * - 需要2个的条件：n = a² + b²
 * - 其他情况需要3个
 * 
 * LeetCode链接：https://leetcode.com/problems/perfect-squares/
 */
public class P279_PerfectSquares {
    
    /**
     * 方法1：递归暴力搜索
     * 
     * 基本思路：
     * 1. 尝试每一个可能的完全平方数
     * 2. 递归求解剩余部分的最少个数
     * 3. 取所有可能方案的最小值
     * 
     * 时间复杂度：O(n^(n/2))，指数级复杂度
     * 空间复杂度：O(√n)，递归栈深度
     * 
     * @param n 目标数
     * @return 组成n所需的最少完全平方数个数
     */
    public static int numSquares1(int n) {
        int res = n;  // 最坏情况：n个1的平方
        int num = 2;  // 从2开始尝试
        
        // 尝试所有可能的完全平方数
        while (num * num <= n) {
            int a = n / (num * num);      // 使用多少个num²
            int b = n % (num * num);      // 剩余部分
            res = Math.min(res, a + numSquares1(b));  // 递归求解剩余部分
            num++;
        }
        return res;
    }

    /**
     * 方法2：基于数学定理的优化解法
     * 
     * 四平方和定理（拉格朗日定理）：
     * 任何正整数都可以表示为至多4个完全平方数的和
     * 
     * 判断规律：
     * - 个数=1：n本身是完全平方数
     * - 个数=4：n % 8 == 7，或者n消去所有4因子后 % 8 == 7
     * - 个数=2：存在a²+b²=n的解
     * - 个数=3：其他情况
     * 
     * 时间复杂度：O(√n)
     * 空间复杂度：O(1)
     * 
     * @param n 目标数
     * @return 组成n所需的最少完全平方数个数
     */
    public static int numSquares2(int n) {
        // 第一步：消除4因子（4的任何次幂都不影响结果）
        int rest = n;
        while (rest % 4 == 0) {
            rest /= 4;
        }
        
        // 第二步：判断是否需要4个完全平方数
        if (rest % 8 == 7) {
            return 4;
        }
        
        // 第三步：判断是否需要1个完全平方数
        int f = (int) Math.sqrt(n);
        if (f * f == n) {
            return 1;
        }
        
        // 第四步：判断是否需要2个完全平方数
        for (int first = 1; first * first <= n; first++) {
            int second = (int) Math.sqrt(n - first * first);
            if (first * first + second * second == n) {
                return 2;
            }
        }
        
        // 第五步：其他情况需要3个完全平方数
        return 3;
    }

    public static void main(String[] args) {
        for (int i = 1; i < 1000; i++) {
            System.out.println(i + "," + numSquares1(i));
        }
    }
}
