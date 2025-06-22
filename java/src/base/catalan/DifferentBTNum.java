package base.catalan;

/**
 * 计算n个节点能组成多少种不同的二叉树
 * 这是经典的卡塔兰数(Catalan Number)问题
 * 
 * 卡塔兰数的递推公式：C(n) = Σ(C(i) * C(n-1-i))，其中i从0到n-1
 * 卡塔兰数的通项公式：C(n) = C(2n,n) / (n+1)
 */
// n个节点，有多少个不同的二叉树
public class DifferentBTNum {
    /**
     * 动态规划解法：基于卡塔兰数递推公式
     * 时间复杂度：O(n²)，空间复杂度：O(n)
     * 
     * 思路：对于n个节点的二叉树，枚举左子树节点数i（0到n-1）
     * 左子树有i个节点，右子树有n-1-i个节点
     * 方案数 = C(i) * C(n-1-i)
     * 
     * @param n 节点数
     * @return n个节点能组成的不同二叉树数量
     */
    public static long num1(int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 2) {
            return 1;   // 0个或1个节点只有1种形态
        }
        long[] dp = new long[n + 1];
        dp[0] = 1;  // 空树
        dp[1] = 1;  // 单节点树
        
        for (int i = 2; i <= n; i++) {
            // 枚举左子树的节点数
            for (int leftSize = 0; leftSize < i; leftSize++) {
                // 右子树节点数 = i - 1 - leftSize（减1是因为根节点）
                dp[i] += dp[leftSize] * dp[i - 1 - leftSize];
            }
        }
        return dp[n];
    }

    /**
     * 求最大公约数（辗转相除法）
     * @param m 数1
     * @param n 数2
     * @return 最大公约数
     */
    private static long gcd(long m, long n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    /**
     * 数学公式解法：基于卡塔兰数通项公式
     * 时间复杂度：O(n)，空间复杂度：O(1)
     * 
     * 卡塔兰数公式：C(n) = C(2n,n) / (n+1) = (2n)! / ((n+1)! * n!)
     * 可以化简为：C(n) = (n+1) * (n+2) * ... * 2n / (1 * 2 * ... * n) / (n+1)
     * 
     * @param n 节点数
     * @return n个节点能组成的不同二叉树数量
     */
    public static long num2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 2) {
            return 1;
        }
        long a = 1; // 分母
        long b = 1; // 分子
        
        // 计算 C(2n,n) = (n+1)(n+2)...(2n) / (1)(2)...(n)
        for (int i = 1, j = n + 1; i <= n; i++, j++) {
            a *= i;     // 分母累乘：1 * 2 * ... * n
            b *= j;     // 分子累乘：(n+1) * (n+2) * ... * 2n
            // 及时约分防止溢出
            long gcd = gcd(a, b);
            a /= gcd;
            b /= gcd;
        }
        // 最后除以(n+1)得到卡塔兰数
        return (b / a) / (n + 1);
    }

    /**
     * 测试方法：验证两种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 15; i++) {
            long ans1 = num1(i);
            long ans2 = num2(i);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
