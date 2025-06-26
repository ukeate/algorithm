package leetc.top;

/**
 * LeetCode 96. 不同的二叉搜索树 (Unique Binary Search Trees)
 * 
 * 问题描述：
 * 给你一个整数 n，求恰由 n 个节点组成且节点值从 1 到 n 互不相同的二叉搜索树有多少种？
 * 
 * 示例：
 * - 输入：n = 3，输出：5
 * - 解释：有5种不同的二叉搜索树
 * 
 * 解法思路：
 * 这是一个经典的卡特兰数问题：
 * 1. 对于n个节点，以第i个节点为根，左子树有i-1个节点，右子树有n-i个节点
 * 2. 递推公式：G(n) = Σ(G(i-1) * G(n-i))，其中i从1到n
 * 3. 这个递推关系对应卡特兰数：Catalan(n) = C(2n,n) / (n+1)
 * 
 * 卡特兰数计算公式：
 * Catalan(n) = (2n)! / ((n+1)! * n!)
 *            = C(2n,n) / (n+1)
 *            = (2n * (2n-1) * ... * (n+1)) / (n * (n-1) * ... * 1) / (n+1)
 * 
 * 优化计算：
 * 使用最大公约数化简，避免大数溢出
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 */
public class P96_UniqueBinarySearchTrees {

    /**
     * 欧几里得算法求最大公约数
     * 
     * @param m 第一个数
     * @param n 第二个数
     * @return 最大公约数
     */
    private static long gcd(long m, long n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    /**
     * 计算卡特兰数 - 不同二叉搜索树的数量
     * 
     * 算法思路：
     * 1. 卡特兰数公式：C(2n,n) / (n+1)
     * 2. 等价于：(2n * (2n-1) * ... * (n+1)) / (n * (n-1) * ... * 1) / (n+1)
     * 3. 为避免溢出，计算过程中使用最大公约数化简
     * 
     * 数学原理：
     * 卡特兰数的应用场景包括：
     * - 二叉搜索树的数量
     * - 括号匹配的方案数
     * - 路径计数问题等
     * 
     * @param n 节点数量
     * @return 不同二叉搜索树的数量
     */
    public static int numTrees(int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 2) {
            return 1;  // 0个或1个节点只有1种BST
        }
        
        long a = 1, b = 1;  // a表示分母累乘，b表示分子累乘
        
        // 计算 C(2n,n) = (n+1) * (n+2) * ... * (2n) / (1 * 2 * ... * n)
        for (int i = 1, j = n + 1; i <= n; i++, j++) {
            a *= i;    // 分母：1 * 2 * ... * n
            b *= j;    // 分子：(n+1) * (n+2) * ... * (2n)
            
            // 使用最大公约数化简，避免溢出
            long gcd = gcd(a, b);
            a /= gcd;
            b /= gcd;
        }
        
        // 返回卡特兰数：C(2n,n) / (n+1)
        return (int) ((b / a) / (n + 1));
    }
}
