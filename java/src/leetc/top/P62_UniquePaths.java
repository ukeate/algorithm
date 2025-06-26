package leetc.top;

/**
 * LeetCode 62. 不同路径 (Unique Paths)
 * 
 * 问题描述：
 * 一个机器人位于一个 m x n 网格的左上角（起始点在下图中标记为 "Start" ）。
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角。
 * 问总共有多少条不同的路径？
 * 
 * 示例：
 * - 输入：m = 3, n = 7，输出：28
 * - 输入：m = 3, n = 2，输出：3
 * 
 * 解法思路：
 * 组合数学 - 这是一个经典的组合问题：
 * 1. 从左上角(0,0)到右下角(m-1,n-1)，总共需要移动(m-1+n-1)步
 * 2. 其中必须向右移动(m-1)步，向下移动(n-1)步
 * 3. 问题转化为：在总共(m+n-2)步中，选择(n-1)步向下，其余向右
 * 4. 答案为组合数：C(m+n-2, n-1) = C(m+n-2, m-1)
 * 
 * 组合数计算：
 * C(all, part) = all! / (part! * (all-part)!)
 * 为避免大数溢出，使用边乘边除的方式，并利用最大公约数化简
 * 
 * 数学推导：
 * C(all, part) = (all * (all-1) * ... * (part+1)) / (1 * 2 * ... * (all-part))
 * 
 * 时间复杂度：O(min(m,n)) - 计算组合数的时间
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class P62_UniquePaths {
    
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
     * 计算从左上角到右下角的不同路径数
     * 
     * 算法思路：
     * 1. 问题转化为组合数：C(m+n-2, n-1)
     * 2. 使用边乘边除避免溢出：分子乘大数，分母乘小数
     * 3. 每次计算后使用最大公约数化简
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 不同路径的数量
     */
    public static int uniquePaths(int m, int n) {
        // 计算组合数 C(all, part)，其中：
        int part = n - 1;        // 需要向下移动的步数
        int all = m + n - 2;     // 总移动步数
        
        long o1 = 1, o2 = 1;     // o1是分子累乘，o2是分母累乘
        
        // 计算 C(all, part) = (part+1 * part+2 * ... * all) / (1 * 2 * ... * (all-part))
        for (int i = part + 1, j = 1; i <= all || j <= all - part; i++, j++) {
            o1 *= i;    // 分子：累乘 (part+1) 到 all
            o2 *= j;    // 分母：累乘 1 到 (all-part)
            
            // 使用最大公约数化简，避免溢出
            long gcd = gcd(o1, o2);
            o1 /= gcd;
            o2 /= gcd;
        }
        
        return (int) o1;  // 此时o2应该为1，结果就是o1
    }
}
