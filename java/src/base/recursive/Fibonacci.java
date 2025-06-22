package base.recursive;

/**
 * 斐波那契数列的多种实现方式
 * 
 * 斐波那契数列定义：F(n) = F(n-1) + F(n-2)，其中F(1) = F(2) = 1
 * 
 * 本文件包含以下实现：
 * 1. 递归实现 - 最直观但效率最低
 * 2. 迭代实现 - 线性时间复杂度
 * 3. 矩阵快速幂实现 - 对数时间复杂度
 * 
 * 以及相关的变形问题：
 * - 铺砖问题
 * - 母牛生小牛问题  
 * - 字符串构造问题
 * 
 * 矩阵快速幂的核心思想：
 * 利用矩阵乘法的性质，将递推关系转化为矩阵运算，
 * 然后使用快速幂算法降低时间复杂度
 */
public class Fibonacci {
    /**
     * 斐波那契数列 - 递归实现
     * 
     * 最直观的实现方式，严格按照数学定义
     * 
     * 时间复杂度：O(2^n) - 指数级，非常低效
     * 空间复杂度：O(n) - 递归调用栈
     * 
     * 缺点：存在大量重复计算
     * 
     * @param n 第n项
     * @return 斐波那契数列第n项的值
     */
    public static int f1(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        return f1(n - 1) + f1(n - 2);
    }

    /**
     * 斐波那契数列 - 迭代实现
     * 
     * 使用滚动数组的思想，只保存前两项的值
     * 避免了递归的重复计算
     * 
     * 时间复杂度：O(n) - 线性时间
     * 空间复杂度：O(1) - 常数空间
     * 
     * @param n 第n项
     * @return 斐波那契数列第n项的值
     */
    public static int f2(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        int res = 1;    // 当前项的值
        int pre = 1;    // 前一项的值
        int tmp = 0;    // 临时变量
        for (int i = 3; i <= n; i++) {
            tmp = res;
            res += pre;
            pre = tmp;
        }
        return res;
    }

    /**
     * 矩阵乘法 - 计算两个矩阵的乘积
     * 
     * @param a 矩阵a
     * @param b 矩阵b
     * @return 矩阵乘积 a × b
     */
    private static int[][] multi(int[][] a, int[][] b) {
        int n = a.length;
        int m = b[0].length;
        int k = a[0].length;
        int[][] ans = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int c = 0; c < k; c++) {
                    ans[i][j] += a[i][c] * b[c][j];
                }
            }
        }
        return ans;
    }

    /**
     * 矩阵快速幂 - 计算矩阵的p次幂
     * 
     * 使用快速幂算法，将时间复杂度从O(p)降低到O(logp)
     * 
     * 原理：
     * - 如果p是偶数：m^p = (m^(p/2))^2
     * - 如果p是奇数：m^p = m × m^(p-1)
     * 
     * @param m 基础矩阵
     * @param p 幂次
     * @return 矩阵m的p次幂
     */
    private static int[][] matrixPower(int[][] m, int p) {
        int[][] res = new int[m.length][m[0].length];
        // 初始化为单位矩阵
        for (int i = 0; i < res.length; i++) {
            res[i][i] = 1;
        }
        int[][] t = m;
        for (; p != 0; p >>= 1) {
            if ((p & 1) != 0) {
                res = multi(res, t);
            }
            t = multi(t, t);
        }
        return res;
    }

    /**
     * 斐波那契数列 - 矩阵快速幂实现
     * 
     * 核心思想：
     * [F(n), F(n-1)] = [F(2), F(1)] × [[1,1],[1,0]]^(n-2)
     * 
     * 状态转移矩阵：
     * [[1, 1],
     *  [1, 0]]
     * 
     * 时间复杂度：O(logn) - 对数时间
     * 空间复杂度：O(1) - 常数空间
     * 
     * @param n 第n项
     * @return 斐波那契数列第n项的值
     */
    public static int f3(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        int[][] base = {
                {1, 1},
                {1, 0}
        };
        int[][] res = matrixPower(base, n - 2);
        return res[0][0] + res[1][0];
    }

    /**
     * 铺砖问题 - 递归实现
     * 
     * 问题描述：2×n的地面，用1×2的砖铺满，有多少种方法？
     * 
     * 分析：
     * - 如果最后一列用一块竖砖，前面有s(n-1)种方法
     * - 如果最后两列用两块横砖，前面有s(n-2)种方法
     * - 递推关系：s(n) = s(n-1) + s(n-2)
     * 
     * 实际上就是斐波那契数列的变形
     * 
     * @param n 地面宽度
     * @return 铺砖方法数
     */
    public static int s1(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }
        return s1(n - 1) + s1(n - 2);
    }

    /**
     * 铺砖问题 - 迭代实现
     * 
     * @param n 地面宽度
     * @return 铺砖方法数
     */
    public static int s2(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }
        int res = 2;
        int pre = 1;
        int tmp = 0;
        for (int i = 3; i <= n; i++) {
            tmp = res;
            res += pre;
            pre = tmp;
        }
        return res;
    }

    /**
     * 铺砖问题 - 矩阵快速幂实现
     * 
     * @param n 地面宽度
     * @return 铺砖方法数
     */
    public static int s3(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }
        int[][] base = {{1, 1}, {1, 0}};
        int[][] res = matrixPower(base, n - 2);
        return 2 * res[0][0] + res[1][0];
    }

    /**
     * 母牛生小牛问题 - 递归实现
     * 
     * 问题描述：
     * 一开始有一头母牛，每头母牛从第4年开始每年生一头小牛，
     * 牛不会死，求第n年牛的总数
     * 
     * 分析：
     * - 第n年的牛数 = 第(n-1)年的牛数 + 第(n-3)年的牛数
     * - 第(n-1)年的牛都活着
     * - 第(n-3)年的牛在第n年都会生小牛
     * - 递推关系：c(n) = c(n-1) + c(n-3)
     * 
     * @param n 年数
     * @return 第n年牛的总数
     */
    public static int c1(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2 || n == 3) {
            return n;
        }
        return c1(n - 1) + c1(n - 3);
    }

    /**
     * 母牛生小牛问题 - 迭代实现
     * 
     * @param n 年数
     * @return 第n年牛的总数
     */
    public static int c2(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2 || n == 3) {
            return n;
        }
        int res = 3;        // 第3年的牛数
        int pre = 2;        // 第2年的牛数
        int prepre = 1;     // 第1年的牛数
        int tmp1 = 0;
        int tmp2 = 0;
        for (int i = 4; i <= n; i++) {
            tmp1 = res;
            tmp2 = pre;
            res += prepre;
            pre = tmp1;
            prepre = tmp2;
        }
        return res;
    }

    /**
     * 母牛生小牛问题 - 矩阵快速幂实现
     * 
     * 状态转移矩阵：
     * [[1, 1, 0],
     *  [0, 0, 1],
     *  [1, 0, 0]]
     * 
     * @param n 年数
     * @return 第n年牛的总数
     */
    public static int c3(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2 || n == 3) {
            return n;
        }
        int[][] base = {
                {1, 1, 0},
                {0, 0, 1},
                {1, 0, 0}
        };
        int[][] res = matrixPower(base, n - 3);
        return 3 * res[0][0] + 2 * res[1][0] + res[2][0];
    }

    /**
     * 字符串构造问题的辅助递归函数
     * 
     * @param i 当前位置
     * @param n 字符串长度
     * @return 从位置i开始的构造方法数
     */
    private static int process(int i, int n) {
        if (i == n - 1) {
            return 2;
        }
        if (i == n) {
            return 1;
        }
        return process(i + 1, n) + process(i + 2, n);
    }

    /**
     * 字符串构造问题 - 递归实现
     * 
     * 问题描述：
     * 用0和1组成长度为n的字符串，要求0不能在1的左边，求构造方法数
     * 
     * 分析：
     * 这实际上也是斐波那契数列的变形
     * 
     * @param n 字符串长度
     * @return 构造方法数
     */
    public static int zeroLeftWays(int n) {
        if (n < 1) {
            return 0;
        }
        return process(1, n);
    }

    /**
     * 字符串构造问题 - 迭代实现
     * 
     * @param n 字符串长度
     * @return 构造方法数
     */
    public static int zeroLeftWays2(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int pre = 1;
        int res = 1;
        int tmp = 0;
        for (int i = 2; i <= n; i++) {
            tmp = res;
            res += pre;
            pre = tmp;
        }
        return res;
    }

    /**
     * 字符串构造问题 - 矩阵快速幂实现
     * 
     * @param n 字符串长度
     * @return 构造方法数
     */
    public static int zeroLeftWays3(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }
        int[][] base = {{1, 1}, {1, 0}};
        int[][] res = matrixPower(base, n - 2);
        return 2 * res[0][0] + res[1][0];
    }

    /**
     * 测试方法 - 验证各种实现的正确性
     */
    public static void main(String[] args) {
        int n = 19;
        
        System.out.println("=== 斐波那契数列 ===");
        System.out.println("递归实现: " + f1(n));
        System.out.println("迭代实现: " + f2(n));
        System.out.println("矩阵快速幂: " + f3(n));
        
        System.out.println("\n=== 铺砖问题 ===");
        System.out.println("递归实现: " + s1(n));
        System.out.println("迭代实现: " + s2(n));
        System.out.println("矩阵快速幂: " + s3(n));
        
        System.out.println("\n=== 母牛生小牛问题 ===");
        System.out.println("递归实现: " + c1(n));
        System.out.println("迭代实现: " + c2(n));
        System.out.println("矩阵快速幂: " + c3(n));
        
        System.out.println("\n=== 字符串构造问题 ===");
        System.out.println("递归实现: " + zeroLeftWays(n));
        System.out.println("迭代实现: " + zeroLeftWays2(n));
        System.out.println("矩阵快速幂: " + zeroLeftWays3(n));
    }
}
