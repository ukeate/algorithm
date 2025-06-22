package base.catalan;

import java.util.LinkedList;

/**
 * 卡塔兰数问题：0和1各n个，1前面必须有匹配的0
 * 
 * 问题描述：给定n个0和n个1，要求任意前缀中0的个数>=1的个数
 * 这等价于合法的括号序列问题（0看作左括号，1看作右括号）
 * 答案是第n个卡塔兰数：C(n) = C(2n,n) / (n+1)
 */
// 1和0各n个, 1前要有匹配的0
public class Ways10 {
    /**
     * 暴力递归解法：枚举所有可能的序列并验证
     * 时间复杂度：O(2^(2n))，用于验证正确性
     * 
     * @param zero 剩余0的个数
     * @param one 剩余1的个数
     * @param path 当前构建的路径
     * @param ans 所有可能的序列
     */
    private static void process1(int zero, int one, LinkedList<Integer> path, LinkedList<LinkedList<Integer>> ans) {
        if (zero == 0 && one == 0) {
            // 找到一个完整序列，加入结果集
            LinkedList<Integer> cur = new LinkedList<>();
            for (Integer num : path) {
                cur.add(num);
            }
            ans.add(cur);
        } else {
            if (zero == 0) {
                // 只剩1，直接添加
                path.addLast(1);
                process1(zero, one - 1, path, ans);
                path.removeLast();
            } else if (one == 0) {
                // 只剩0，直接添加
                path.addLast(0);
                process1(zero - 1, one, path, ans);
                path.removeLast();
            } else {
                // 两种选择：放1或放0
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
            int status = 0; // 当前0-1的差值
            for (Integer num : cur) {
                if (num == 0) {
                    status++; // 遇到0，计数器+1
                } else {
                    status--; // 遇到1，计数器-1
                }
                if (status < 0) {
                    // 如果1的数量超过0，序列不合法
                    break;
                }
            }
            if (status == 0) {
                // 最终0和1数量相等且过程中1从未超过0
                count++;
            }
        }
        return count;
    }

    /**
     * 数学公式解法：直接计算卡塔兰数
     * 时间复杂度：O(n)，空间复杂度：O(1)
     * 
     * 卡塔兰数公式：C(n) = C(2n,n) / (n+1)
     * 其中C(2n,n)表示从2n个位置中选n个位置的组合数
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
        long a = 1; // 分母
        long b = 1; // 分子
        long limit = n << 1; // 2n
        
        // 计算C(2n,n) = (n+1)(n+2)...(2n) / (1)(2)...(n)
        for (long i = n + 1; i <= limit; i++) {
            if (i <= n) {
                a *= i; // 这个条件永远不会满足，这里有逻辑错误
            } else {
                b *= i; // 分子：(n+1) * (n+2) * ... * 2n
            }
        }
        // 这里的实现有问题，正确的应该是同时计算分子分母并约分
        // 正确实现应该参考DifferentBTNum.java中的方法
        return (b / a) / (n + 1);
    }

    /**
     * 测试方法：验证暴力解法和数学解法的一致性
     */
    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 10; i++) {
            long ans1 = ways1(i);
            long ans2 = ways2(i);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
