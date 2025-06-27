package base.catalan;

import java.util.LinkedList;

/**
 * 卡塔兰数问题：0和1各n个，1前面必须有匹配的0
 * 
 * 问题描述：
 * 给定n个0和n个1，要求排列成一个序列，使得任意前缀中0的个数>=1的个数
 * 这等价于合法的括号序列问题（0看作左括号，1看作右括号）
 * 
 * 数学本质：这是经典的卡塔兰数问题
 * 答案是第n个卡塔兰数：C(n) = C(2n,n) / (n+1)
 * 
 * 实际应用：
 * - 括号匹配问题
 * - 出栈入栈序列
 * - 二叉树计数
 * - 路径计数（不超过对角线）
 */
// 1和0各n个, 1前要有匹配的0
public class Ways10 {
    
    /**
     * 暴力递归解法：枚举所有可能的序列并验证
     * 时间复杂度：O(2^(2n))，用于验证正确性
     * 
     * 递归过程：
     * 1. 如果还有0，可以选择放0
     * 2. 如果还有1，可以选择放1
     * 3. 递归构建完整序列
     * 
     * @param zero 剩余0的个数
     * @param one 剩余1的个数
     * @param path 当前构建的路径
     * @param ans 所有可能的序列集合
     */
    private static void process1(int zero, int one, LinkedList<Integer> path, LinkedList<LinkedList<Integer>> ans) {
        if (zero == 0 && one == 0) {
            // 基准情况：找到一个完整序列，加入结果集
            LinkedList<Integer> cur = new LinkedList<>();
            for (Integer num : path) {
                cur.add(num);
            }
            ans.add(cur);
        } else {
            if (zero == 0) {
                // 只剩1，直接添加所有剩余的1
                path.addLast(1);
                process1(zero, one - 1, path, ans);
                path.removeLast();
            } else if (one == 0) {
                // 只剩0，直接添加所有剩余的0
                path.addLast(0);
                process1(zero - 1, one, path, ans);
                path.removeLast();
            } else {
                // 两种选择：先尝试放1，再尝试放0
                path.addLast(1);
                process1(zero, one - 1, path, ans);
                path.removeLast();
                
                path.addLast(0);
                process1(zero - 1, one, path, ans);
                path.removeLast();
            }
        }
    }

    /**
     * 暴力解法：生成所有序列并统计合法的数量
     * 用于验证卡塔兰数公式的正确性
     * 
     * 合法性检查：
     * 1. 遍历序列，维护0和1的差值计数器
     * 2. 遇到0时计数器+1，遇到1时计数器-1
     * 3. 过程中计数器不能为负（保证1不超过0）
     * 4. 最终计数器为0（保证0和1数量相等）
     * 
     * @param n 0和1的个数
     * @return 合法序列的数量
     */
    public static long ways1(int n) {
        int zero = n;
        int one = n;
        LinkedList<Integer> path = new LinkedList<>();
        LinkedList<LinkedList<Integer>> ans = new LinkedList<>();
        process1(zero, one, path, ans);
        
        long count = 0;
        // 验证每个序列是否合法
        for (LinkedList<Integer> cur : ans) {
            int status = 0; // 当前0-1的差值（0的个数-1的个数）
            boolean valid = true;
            for (Integer num : cur) {
                if (num == 0) {
                    status++; // 遇到0，差值+1
                } else {
                    status--; // 遇到1，差值-1
                }
                if (status < 0) {
                    // 如果1的数量超过0，序列不合法
                    valid = false;
                    break;
                }
            }
            if (valid && status == 0) {
                // 最终0和1数量相等且过程中1从未超过0
                count++;
            }
        }
        return count;
    }

    /**
     * 求最大公约数（辗转相除法）
     * 用于化简分数，防止计算过程中的溢出
     * @param m 数1
     * @param n 数2
     * @return 最大公约数
     */
    private static long gcd(long m, long n) {
        return n == 0 ? m : gcd(n, m % n);
    }

    /**
     * 数学公式解法：直接计算卡塔兰数
     * 时间复杂度：O(n)，空间复杂度：O(1)
     * 
     * 卡塔兰数公式：C(n) = C(2n,n) / (n+1)
     * 其中C(2n,n)表示从2n个位置中选n个位置的组合数
     * 
     * 实现细节：
     * 1. 计算C(2n,n) = (n+1)(n+2)...(2n) / (1)(2)...(n)
     * 2. 在计算过程中及时约分防止溢出
     * 3. 最后除以(n+1)得到卡塔兰数
     * 
     * @param n 0和1的个数
     * @return 合法序列的数量（第n个卡塔兰数）
     */
    public static long ways2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n < 2) {
            return 1; // C(0)=1, C(1)=1
        }
        
        long a = 1; // 分母：1 * 2 * ... * n
        long b = 1; // 分子：(n+1) * (n+2) * ... * 2n
        
        // 计算C(2n,n) = (n+1)(n+2)...(2n) / (1)(2)...(n)
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
     * 测试方法：验证暴力解法和数学解法的一致性
     * 
     * 前几个卡塔兰数：
     * C(0)=1, C(1)=1, C(2)=2, C(3)=5, C(4)=14, C(5)=42...
     */
    public static void main(String[] args) {
        System.out.println("test begin");
        System.out.println("前10个卡塔兰数：");
        for (int i = 0; i < 10; i++) {
            long ans1 = ways1(i);
            long ans2 = ways2(i);
            System.out.println("C(" + i + ") = " + ans1 + " (暴力) = " + ans2 + " (公式)");
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
