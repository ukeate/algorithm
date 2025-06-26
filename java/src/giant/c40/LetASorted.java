package giant.c40;

/**
 * 数组A与B交换使A有序问题
 * 
 * 问题描述：
 * 给定两个长度为N的数组A和B
 * A[i]不可以在A中和其他数交换，只可以选择和B[i]交换 (0≤i<n)
 * 目标是让数组A变成有序（非递减），判断是否能够做到
 * 
 * 解题思路：
 * 使用递归+回溯：
 * 对于每个位置i，有两种选择：
 * 1. 不交换：使用A[i]
 * 2. 交换：使用B[i]
 * 需要保证选择后的值不小于前一个位置的值
 * 
 * 优化思路：
 * 方法1：记录前一个位置的实际值
 * 方法2：记录前一个位置的值是来自A还是B（减少参数）
 * 
 * 算法核心：
 * - 状态定义：当前位置、前一个位置的值（或来源）
 * - 决策：选择A[i]或B[i]，保证非递减性
 * - 剪枝：如果无法保证有序性，提前返回false
 * 
 * 时间复杂度：O(2^n)（最坏情况），实际有剪枝优化
 * 空间复杂度：O(n)，递归栈深度
 * 
 * @author Zhu Runqi
 */
public class LetASorted {

    /**
     * 递归判断是否能让A有序（方法1）
     * 
     * 算法思路：
     * 1. 对每个位置，尝试不交换和交换两种选择
     * 2. 检查选择后的值是否不小于前一个值
     * 3. 如果满足条件，继续递归下一个位置
     * 4. 任意一种选择能成功就返回true
     * 
     * @param a 数组A
     * @param b 数组B
     * @param i 当前考虑的位置
     * @param lastA 前一个位置实际选择的值
     * @return 是否能让A有序
     */
    private static boolean process1(int[] a, int[] b, int i, int lastA) {
        // 递归边界：所有位置都已处理完
        if (i == a.length) {
            return true;
        }
        
        // 选择1：不交换，使用A[i]
        if (a[i] >= lastA && process1(a, b, i + 1, a[i])) {
            return true;
        }
        
        // 选择2：交换，使用B[i]
        if (b[i] >= lastA && process1(a, b, i + 1, b[i])) {
            return true;
        }
        
        return false;
    }

    /**
     * 判断是否能让A有序（方法1入口）
     * 
     * @param a 数组A
     * @param b 数组B
     * @return 是否能通过交换让A有序
     */
    public static boolean can1(int[] a, int[] b) {
        return process1(a, b, 0, Integer.MIN_VALUE);
    }

    /**
     * 递归判断是否能让A有序（方法2，优化版）
     * 
     * 算法思路：
     * 通过boolean参数fromA记录前一个位置是否来自数组A
     * 这样可以直接通过a[i-1]或b[i-1]获取前一个值，减少参数传递
     * 
     * 优势：
     * - 参数更少，状态空间更小
     * - 便于记忆化搜索（如果需要）
     * - 时间复杂度可以优化到O(N)
     * 
     * @param a 数组A
     * @param b 数组B
     * @param i 当前位置
     * @param fromA 前一个位置的值是否来自A（true：来自A，false：来自B）
     * @return 是否能让A有序
     */
    private static boolean process2(int[] a, int[] b, int i, boolean fromA) {
        // 递归边界：所有位置都已处理完
        if (i == a.length) {
            return true;
        }
        
        // 获取前一个位置的实际值
        int lastVal = (i == 0) ? Integer.MIN_VALUE : (fromA ? a[i - 1] : b[i - 1]);
        
        // 选择1：不交换，使用A[i]
        if (a[i] >= lastVal && process2(a, b, i + 1, true)) {
            return true;
        }
        
        // 选择2：交换，使用B[i]
        if (b[i] >= lastVal && process2(a, b, i + 1, false)) {
            return true;
        }
        
        return false;
    }

    /**
     * 判断是否能让A有序（方法2入口）
     * 
     * @param a 数组A
     * @param b 数组B
     * @return 是否能通过交换让A有序
     */
    public static boolean can2(int[] a, int[] b) {
        return process2(a, b, 0, true);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 数组A与B交换使A有序问题 ===\n");
        
        // 测试用例1：能够通过交换使A有序
        System.out.println("测试用例1：能够通过交换使A有序");
        int[] a1 = {1, 3, 2, 4};
        int[] b1 = {2, 1, 4, 3};
        boolean result1 = can1(a1, b1);
        System.out.println("数组A: [1, 3, 2, 4]");
        System.out.println("数组B: [2, 1, 4, 3]");
        System.out.println("能否使A有序: " + result1);
        System.out.println("分析: 选择[1, 1, 4, 4]，A变为有序");
        System.out.println();
        
        // 测试用例2：无法通过交换使A有序
        System.out.println("测试用例2：无法通过交换使A有序");
        int[] a2 = {3, 1, 4, 2};
        int[] b2 = {1, 4, 2, 5};
        boolean result2 = can1(a2, b2);
        System.out.println("数组A: [3, 1, 4, 2]");
        System.out.println("数组B: [1, 4, 2, 5]");
        System.out.println("能否使A有序: " + result2);
        System.out.println("分析: 无论如何选择都无法使A严格有序");
        System.out.println();
        
        // 测试用例3：A已经有序
        System.out.println("测试用例3：A已经有序");
        int[] a3 = {1, 2, 3, 4};
        int[] b3 = {4, 3, 2, 1};
        boolean result3 = can1(a3, b3);
        System.out.println("数组A: [1, 2, 3, 4]");
        System.out.println("数组B: [4, 3, 2, 1]");
        System.out.println("能否使A有序: " + result3);
        System.out.println("分析: A本身就有序，选择不交换即可");
        System.out.println();
        
        // 测试用例4：需要全部交换
        System.out.println("测试用例4：需要全部交换");
        int[] a4 = {4, 3, 2, 1};
        int[] b4 = {1, 2, 3, 4};
        boolean result4 = can1(a4, b4);
        System.out.println("数组A: [4, 3, 2, 1]");
        System.out.println("数组B: [1, 2, 3, 4]");
        System.out.println("能否使A有序: " + result4);
        System.out.println("分析: 全部交换后A变为[1, 2, 3, 4]");
        System.out.println();
        
        // 测试用例5：相同元素的处理
        System.out.println("测试用例5：相同元素的处理");
        int[] a5 = {1, 2, 2, 3};
        int[] b5 = {2, 1, 3, 2};
        boolean result5 = can1(a5, b5);
        System.out.println("数组A: [1, 2, 2, 3]");
        System.out.println("数组B: [2, 1, 3, 2]");
        System.out.println("能否使A有序: " + result5);
        System.out.println("分析: 包含相同元素，非严格递增");
        System.out.println();
        
        // 方法对比测试
        System.out.println("=== 方法对比测试 ===");
        int[][] testCasesA = {
            {1, 3, 2, 4},
            {3, 1, 4, 2},
            {1, 2, 3, 4},
            {4, 3, 2, 1},
            {1, 2, 2, 3}
        };
        int[][] testCasesB = {
            {2, 1, 4, 3},
            {1, 4, 2, 5},
            {4, 3, 2, 1},
            {1, 2, 3, 4},
            {2, 1, 3, 2}
        };
        
        boolean allMatch = true;
        for (int i = 0; i < testCasesA.length; i++) {
            boolean result_1 = can1(testCasesA[i], testCasesB[i]);
            boolean result_2 = can2(testCasesA[i], testCasesB[i]);
            
            if (result_1 != result_2) {
                System.out.println("测试用例" + (i + 1) + "结果不一致!");
                allMatch = false;
            }
        }
        
        if (allMatch) {
            System.out.println("所有测试用例两种方法结果一致！");
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 位置限制：A[i]只能与B[i]交换");
        System.out.println("   - 目标约束：最终A必须非递减有序");
        System.out.println("   - 决策二元：每个位置只有交换或不交换两种选择");
        System.out.println();
        System.out.println("2. 递归策略：");
        System.out.println("   - 状态定义：当前位置 + 前一个位置的值");
        System.out.println("   - 状态转移：选择A[i]或B[i]，保证有序性");
        System.out.println("   - 剪枝优化：违反有序性时提前返回");
        System.out.println();
        System.out.println("3. 两种实现：");
        System.out.println("   - 方法1：直接传递前一个值");
        System.out.println("   - 方法2：传递前一个值的来源标记");
        System.out.println("   - 方法2更适合记忆化搜索优化");
        System.out.println();
        System.out.println("4. 关键思想：");
        System.out.println("   - 贪心判断：每个位置的选择必须满足有序性");
        System.out.println("   - 回溯搜索：尝试所有可能的选择组合");
        System.out.println("   - 提前终止：一旦找到可行解就返回true");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(2^n)最坏情况，有剪枝优化");
        System.out.println("   - 空间复杂度：O(n)，递归栈深度");
        System.out.println("   - 优化潜力：可用动态规划优化到O(n)");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 数组重排问题");
        System.out.println("   - 约束优化问题");
        System.out.println("   - 递归决策树搜索");
    }
}
