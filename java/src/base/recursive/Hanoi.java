package base.recursive;

import java.util.HashSet;
import java.util.Stack;

/**
 * 汉诺塔问题 - 经典递归算法
 * 
 * 问题描述：
 * 有三根柱子A、B、C，其中A柱上有N个不同大小的圆盘，大盘在下，小盘在上。
 * 现在要把所有圆盘从A柱移动到C柱，但是有以下规则：
 * 1. 每次只能移动一个圆盘
 * 2. 每次移动都是把某根柱子最上面的圆盘移动到另一根柱子的最上面
 * 3. 在移动过程中，大盘不能放在小盘上面
 * 
 * 递归思路：
 * 要将N个圆盘从A移动到C（借助B），可以分解为：
 * 1. 将上面N-1个圆盘从A移动到B（借助C作为辅助）
 * 2. 将最下面的圆盘从A移动到C
 * 3. 将N-1个圆盘从B移动到C（借助A作为辅助）
 * 
 * 递归边界：
 * 当N=1时，直接将圆盘从起始柱移动到目标柱
 * 
 * 算法特点：
 * - 时间复杂度：O(2^n)，因为每增加一个圆盘，移动次数翻倍
 * - 空间复杂度：O(n)，递归调用栈的深度
 * - 最优解：移动次数为 2^n - 1
 * 
 * 数学证明：
 * 设T(n)为移动n个圆盘的最少步数
 * - T(1) = 1
 * - T(n) = 2*T(n-1) + 1
 * 解得：T(n) = 2^n - 1
 * 
 * 应用场景：
 * - 递归算法的经典教学案例
 * - 分治思想的完美体现
 * - 数学归纳法的实际应用
 */
public class Hanoi {
    /**
     * 汉诺塔递归求解 - 打印移动步骤
     * 
     * 递归三要素：
     * 1. 递归定义：hanoi(n, from, to, other) 表示将n个圆盘从from移动到to，借助other
     * 2. 递归边界：n == 1时，直接移动
     * 3. 递归关系：
     *    - 先移动上面n-1个盘子到辅助柱
     *    - 再移动最大的盘子到目标柱
     *    - 最后移动n-1个盘子到目标柱
     * 
     * @param n 圆盘数量
     * @param from 起始柱子标识
     * @param to 目标柱子标识
     * @param other 辅助柱子标识
     */
    public static void hanoi(int n, String from, String to, String other) {
        if (n == 1) {
            // 递归边界：只有一个圆盘时，直接移动
            System.out.println("Move 1 from " + from + " to " + to);
        } else {
            // 递归步骤1：将上面n-1个圆盘从起始柱移动到辅助柱
            // 此时目标柱作为临时辅助
            hanoi(n - 1, from, other, to);
            
            // 步骤2：将最大的圆盘（第n个）从起始柱移动到目标柱
            System.out.println("Move " + n + " from " + from + " to " + to);
            
            // 递归步骤3：将n-1个圆盘从辅助柱移动到目标柱
            // 此时起始柱作为临时辅助
            hanoi(n - 1, other, to, from);
        }
    }

    /**
     * 计算汉诺塔最少移动步数
     * 
     * 使用数学公式：T(n) = 2^n - 1
     * 也可以用递归方式计算：T(n) = 2*T(n-1) + 1，T(1) = 1
     * 
     * @param n 圆盘数量
     * @return 最少移动步数
     */
    public static long minSteps(int n) {
        if (n <= 0) return 0;
        return (1L << n) - 1;  // 2^n - 1，使用位运算提高效率
    }

    /**
     * 递归方式计算移动步数（用于验证公式正确性）
     * 
     * @param n 圆盘数量
     * @return 移动步数
     */
    public static long minStepsRecursive(int n) {
        if (n == 1) return 1;
        return 2 * minStepsRecursive(n - 1) + 1;
    }

    /**
     * 测试方法 - 演示汉诺塔问题的求解过程
     */
    public static void main(String[] args) {
        System.out.println("=== 汉诺塔问题演示 ===");
        
        // 测试小规模的汉诺塔
        for (int n = 1; n <= 4; n++) {
            System.out.println("\n" + n + "个圆盘的汉诺塔解法：");
            System.out.println("最少步数：" + minSteps(n));
            System.out.println("移动步骤：");
            hanoi(n, "A", "C", "B");
            
            // 验证公式和递归计算的一致性
            long formula = minSteps(n);
            long recursive = minStepsRecursive(n);
            System.out.println("公式计算：" + formula + "，递归计算：" + recursive + 
                             "，结果" + (formula == recursive ? "一致" : "不一致"));
        }
        
        // 展示更大规模的步数（不打印具体步骤）
        System.out.println("\n=== 大规模汉诺塔步数统计 ===");
        System.out.println("圆盘数\t最少步数\t\t时间复杂度概念");
        for (int n = 5; n <= 20; n++) {
            long steps = minSteps(n);
            System.out.printf("%d\t%,d\t\t%s\n", n, steps, 
                n <= 10 ? "可接受" : (n <= 15 ? "较大" : "非常大"));
        }
        
        // 趣味事实
        System.out.println("\n=== 趣味数学事实 ===");
        System.out.println("传说中的汉诺塔有64个圆盘");
        System.out.println("如果每秒移动一个圆盘，需要时间：");
        long steps64 = minSteps(64);
        long seconds = steps64;
        long years = seconds / (365L * 24 * 3600);
        System.out.printf("约 %,d 年\n", years);
        System.out.println("这比宇宙的年龄还要长得多！");
        
        // 算法复杂度分析
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度：O(2^n)");
        System.out.println("空间复杂度：O(n) - 递归调用栈");
        System.out.println("这是一个指数级复杂度的算法");
        System.out.println("展现了某些看似简单的问题实际上非常复杂");
    }

    //

    private static class Record {
        public int level;
        public String from;
        public String to;
        public String other;

        public Record(int l, String f, String t, String o) {
            level = l;
            from = f;
            to = t;
            other = o;
        }
    }

    public static void hanoi3(int n) {
        if (n < 1) {
            return;
        }
        Stack<Record> stack = new Stack<>();
        HashSet<Record> finishLeft = new HashSet<>();
        stack.add(new Record(n, "left", "right", "mid"));
        while (!stack.isEmpty()) {
            Record cur = stack.pop();
            if (cur.level == 1) {
                System.out.println("Move 1 from " + cur.from + " to " + cur.to);
            } else {
                if (!finishLeft.contains(cur)) {
                    finishLeft.add(cur);
                    stack.push(cur);
                    stack.push(new Record(cur.level - 1, cur.from, cur.other, cur.to));
                } else {
                    System.out.println("Move " + cur.level + " from " + cur.from + " to " + cur.to);
                    stack.push(new Record(cur.level - 1, cur.other, cur.to, cur.from));
                }
            }
        }
    }
}
