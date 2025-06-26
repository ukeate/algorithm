package giant.c36;

import java.util.Arrays;

/**
 * 拿数加和最大分数问题
 * 
 * 问题描述：
 * 给定一个数组arr，当拿走某个数a的时候，其他所有的数都+a
 * 请返回最终所有数都拿走的最大分数
 * 
 * 示例：[2,3,1]
 * - 当拿走3时，获得3分，数组变成[5,4]
 * - 当拿走5时，获得5分，数组变成[9]  
 * - 当拿走9时，获得9分，数组变成[]
 * - 总分：3+5+9=17，这是最大的拿取方式
 * 
 * 解题思路：
 * 使用贪心算法 + 数学归纳法证明：
 * 1. 将数组排序，从大到小依次拿取
 * 2. 每次拿取一个数时，它会影响剩余所有数的值
 * 3. 通过数学推导发现：最优策略是按降序拿取，答案可以用位运算快速计算
 * 
 * 关键发现：
 * 如果数组排序后为[a1,a2,...,an]（降序），最优拿取顺序就是从a1到an
 * 最终得分 = a1*2^(n-1) + a2*2^(n-2) + ... + an*2^0
 * 
 * 算法优势：
 * - 时间复杂度：O(n log n)，主要是排序的时间
 * - 空间复杂度：O(1)，不考虑排序的额外空间
 * - 相比暴力递归O(n!)的复杂度，效率极大提升
 * 
 * 来源：腾讯面试题
 * 
 * @author Zhu Runqi
 */
public class PickAddMax {
    
    /**
     * 贪心算法求最大拿取分数
     * 
     * 核心思想：
     * 1. 数组排序后，按降序拿取能获得最大分数
     * 2. 每个数的贡献系数为2的幂次，越早拿取贡献越大
     * 3. 使用位运算快速计算：ans = ((ans << 1) + arr[i])
     * 
     * @param arr 输入数组
     * @return 最大拿取分数
     */
    public static int pick(int[] arr) {
        // 排序，准备按降序拿取
        Arrays.sort(arr);
        
        int ans = 0;
        // 从最大值开始拿取，每次左移一位相当于乘以2，然后加上当前数
        for (int i = arr.length - 1; i >= 0; i--) {
            ans = (ans << 1) + arr[i];
        }
        return ans;
    }

    /**
     * 辅助方法：移除数组中指定位置的元素，并将该元素的值加到其他所有元素上
     * 
     * @param arr 原数组
     * @param i 要移除的元素索引
     * @return 处理后的新数组
     */
    private static int[] removeAddOthers(int[] arr, int i) {
        int[] rest = new int[arr.length - 1];
        int ri = 0;
        
        // 将i位置前的元素加上arr[i]后放入新数组
        for (int j = 0; j < i; j++) {
            rest[ri++] = arr[j] + arr[i];
        }
        
        // 将i位置后的元素加上arr[i]后放入新数组
        for (int j = i + 1; j < arr.length; j++) {
            rest[ri++] = arr[j] + arr[i];
        }
        
        return rest;
    }

    /**
     * 暴力递归方法（用于验证正确性）
     * 枚举所有可能的拿取顺序，找出最大分数
     * 
     * @param arr 当前数组状态
     * @return 从当前状态开始能获得的最大分数
     */
    public static int sure(int[] arr) {
        if (arr.length == 1) {
            return arr[0];
        }
        
        int ans = 0;
        // 枚举拿取每个位置的数字
        for (int i = 0; i < arr.length; i++) {
            int[] rest = removeAddOthers(arr, i);
            ans = Math.max(ans, arr[i] + sure(rest));
        }
        return ans;
    }

    /**
     * 生成随机数组用于测试
     */
    private static int[] randomArr(int len, int val) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * val) + 1;
        }
        return arr;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 手动测试用例
        System.out.println("=== 手动测试用例 ===");
        
        // 测试用例1：题目示例
        int[] test1 = {2, 3, 1};
        int result1 = pick(test1.clone());
        int expected1 = sure(test1.clone());
        System.out.println("测试用例1: " + Arrays.toString(test1));
        System.out.println("贪心算法结果: " + result1);
        System.out.println("暴力算法结果: " + expected1);
        System.out.println("拿取过程分析:");
        System.out.println("  排序后: [3,2,1]");
        System.out.println("  拿3: 得3分, 数组变为[5,4]");
        System.out.println("  拿5: 得5分, 数组变为[9]");
        System.out.println("  拿9: 得9分, 数组变为[]");
        System.out.println("  总分: 3+5+9=17");
        System.out.println();
        
        // 测试用例2：递增序列
        int[] test2 = {1, 2, 3, 4};
        int result2 = pick(test2.clone());
        int expected2 = sure(test2.clone());
        System.out.println("测试用例2: " + Arrays.toString(test2));
        System.out.println("贪心算法结果: " + result2);
        System.out.println("暴力算法结果: " + expected2);
        System.out.println("数学公式验证: 4*8 + 3*4 + 2*2 + 1*1 = 32+12+4+1 = " + (4*8+3*4+2*2+1*1));
        System.out.println();
        
        // 测试用例3：相同元素
        int[] test3 = {5, 5, 5};
        int result3 = pick(test3.clone());
        int expected3 = sure(test3.clone());
        System.out.println("测试用例3: " + Arrays.toString(test3));
        System.out.println("贪心算法结果: " + result3);
        System.out.println("暴力算法结果: " + expected3);
        System.out.println("数学公式验证: 5*4 + 5*2 + 5*1 = 20+10+5 = " + (5*4+5*2+5*1));
        System.out.println();
        
        // 测试用例4：单个元素
        int[] test4 = {10};
        int result4 = pick(test4.clone());
        int expected4 = sure(test4.clone());
        System.out.println("测试用例4: " + Arrays.toString(test4));
        System.out.println("贪心算法结果: " + result4);
        System.out.println("暴力算法结果: " + expected4);
        System.out.println();
        
        // 随机测试验证正确性
        System.out.println("=== 随机测试验证 ===");
        int times = 10000;
        int n = 7, v = 10;
        System.out.println("开始随机测试...");
        boolean passed = true;
        
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * n) + 1;
            int[] arr = randomArr(len, v);
            int ans1 = pick(arr.clone());
            int ans2 = sure(arr.clone());
            if (ans1 != ans2) {
                System.out.println("测试失败！");
                System.out.println("数组: " + Arrays.toString(arr));
                System.out.println("贪心算法结果: " + ans1);
                System.out.println("暴力算法结果: " + ans2);
                passed = false;
                break;
            }
        }
        
        if (passed) {
            System.out.println("所有 " + times + " 个随机测试用例通过！");
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 贪心策略证明：");
        System.out.println("   - 设数组为[a1,a2,...,an]，按任意顺序拿取");
        System.out.println("   - 每个数ai的最终贡献为 ai * 2^(它后面拿取的数的个数)");
        System.out.println("   - 要最大化总和，应该让大的数有更大的系数");
        System.out.println("   - 因此按降序拿取是最优策略");
        System.out.println();
        System.out.println("2. 数学公式推导：");
        System.out.println("   - 排序后数组[a1,a2,...,an]（a1≥a2≥...≥an）");
        System.out.println("   - 最优得分 = a1*2^(n-1) + a2*2^(n-2) + ... + an*2^0");
        System.out.println("   - 可以用位运算快速计算：((ans << 1) + ai)");
        System.out.println();
        System.out.println("3. 复杂度分析：");
        System.out.println("   - 贪心算法：时间O(n log n)，空间O(1)");
        System.out.println("   - 暴力算法：时间O(n!)，空间O(n)");
        System.out.println("   - 优化效果：从指数级降低到多项式级");
    }
}
