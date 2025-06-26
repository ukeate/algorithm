package leetc.top;

import java.util.Arrays;

/**
 * LeetCode 781. 森林中的兔子 (Rabbits in Forest)
 * 
 * 问题描述：
 * 森林中，每个兔子都有颜色。其中一些兔子（可能是全部）告诉你还有多少其他的兔子和自己有相同的颜色。
 * 我们将这些回答放在 answers 数组里。
 * 
 * 返回森林中兔子的最少数量。
 * 
 * 示例：
 * - 输入: answers = [1, 1, 2]
 * - 输出: 5
 * - 解释:
 *   两只回答了 "1" 的兔子可能有相同的颜色，设为红色。
 *   之后回答了 "2" 的兔子不会是红色，否则他们的回答会是 "3"。
 *   设回答了 "2" 的兔子为蓝色。
 *   此外，森林中还应有另外 2 只蓝色兔子。
 *   因此森林中兔子的最少数量是 5: 3 只红色, 2 只蓝色。
 * 
 * - 输入: answers = [10, 10, 10]
 * - 输出: 11
 * - 解释: 第三只回答 "10" 的兔子可能与前两只颜色不同。
 * 
 * 解法思路：
 * 贪心算法 + 数学计算：
 * 1. 如果兔子回答x，说明有x+1只相同颜色的兔子（包括自己）
 * 2. 相同回答的兔子尽可能分组为同一颜色，以最小化总数
 * 3. 对于c只回答x的兔子，需要 ⌈c/(x+1)⌉ 组，每组有(x+1)只兔子
 * 4. 总数 = ⌈c/(x+1)⌉ * (x+1)
 * 
 * 核心思想：
 * - 贪心策略：相同回答的兔子尽量归为同一颜色
 * - 数学公式：ceiling除法的技巧 (c+x)/(x+1)
 * - 分组最优：每种颜色最多有(x+1)只兔子回答x
 * 
 * 时间复杂度：O(nlogn) - 排序的时间复杂度
 * 空间复杂度：O(1) - 除排序外只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/rabbits-in-forest/
 */
public class P781_RabbitsInForest {
    
    /**
     * 计算森林中兔子的最少数量
     * 
     * 算法步骤：
     * 1. 对答案数组进行排序，使相同回答的兔子聚集在一起
     * 2. 遍历数组，统计每种回答的数量
     * 3. 对于每种回答x，有c只兔子：
     *    - 每组最多有(x+1)只兔子
     *    - 需要⌈c/(x+1)⌉组
     *    - 总兔子数 = ⌈c/(x+1)⌉ * (x+1)
     * 4. 累加所有颜色的兔子总数
     * 
     * 数学推导：
     * - 如果兔子回答x，说明该颜色总共有x+1只兔子
     * - 如果有c只兔子都回答x，它们最多可以分成⌈c/(x+1)⌉组
     * - 每组代表一种颜色，有(x+1)只兔子
     * 
     * @param arr 兔子的回答数组
     * @return 森林中兔子的最少数量
     */
    public static int numRabbits(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        // 排序：使相同回答的兔子聚集在一起
        Arrays.sort(arr);
        
        int x = arr[0];     // 当前处理的回答值
        int c = 1;          // 当前回答值的兔子数量
        int ans = 0;        // 总兔子数量
        
        // 遍历处理每个回答
        for (int i = 1; i < arr.length; i++) {
            if (x != arr[i]) {
                // 遇到新的回答值，结算之前的回答
                // 计算公式：⌈c/(x+1)⌉ * (x+1) = ((c+x)/(x+1)) * (x+1)
                ans += ((c + x) / (x + 1)) * (x + 1);
                
                // 开始处理新的回答值
                x = arr[i];
                c = 1;
            } else {
                // 相同回答，增加计数
                c++;
            }
        }
        
        // 处理最后一组回答
        return ans + ((c + x) / (x + 1)) * (x + 1);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] answers1 = {1, 1, 2};
        System.out.println("测试用例1:");
        System.out.println("输入: " + java.util.Arrays.toString(answers1));
        System.out.println("输出: " + numRabbits(answers1));
        System.out.println("期望: 5");
        System.out.println("解释: 2只回答1的兔子为一组(2只)，1只回答2的兔子需要一组(3只)，总计5只");
        System.out.println();
        
        // 测试用例2：重复回答
        int[] answers2 = {10, 10, 10};
        System.out.println("测试用例2:");
        System.out.println("输入: " + java.util.Arrays.toString(answers2));
        System.out.println("输出: " + numRabbits(answers2));
        System.out.println("期望: 11");
        System.out.println("解释: 3只回答10的兔子最少需要1组(11只兔子)");
        System.out.println();
        
        // 测试用例3：都回答0
        int[] answers3 = {0, 0, 0, 0};
        System.out.println("测试用例3 (都回答0):");
        System.out.println("输入: " + java.util.Arrays.toString(answers3));
        System.out.println("输出: " + numRabbits(answers3));
        System.out.println("期望: 4");
        System.out.println("解释: 每只回答0的兔子都是不同颜色，各自一组");
        System.out.println();
        
        // 测试用例4：需要多组
        int[] answers4 = {1, 1, 1, 1, 1};
        System.out.println("测试用例4 (需要多组):");
        System.out.println("输入: " + java.util.Arrays.toString(answers4));
        System.out.println("输出: " + numRabbits(answers4));
        System.out.println("期望: 6");
        System.out.println("解释: 5只回答1的兔子需要⌈5/2⌉=3组，前2组各2只，最后1组2只，总计6只");
        System.out.println();
        
        // 测试用例5：单个兔子
        int[] answers5 = {5};
        System.out.println("测试用例5 (单个兔子):");
        System.out.println("输入: " + java.util.Arrays.toString(answers5));
        System.out.println("输出: " + numRabbits(answers5));
        System.out.println("期望: 6");
        System.out.println("解释: 1只回答5的兔子需要1组，该组有6只兔子");
        System.out.println();
        
        // 测试用例6：混合回答
        int[] answers6 = {1, 0, 1, 0, 0};
        System.out.println("测试用例6 (混合回答):");
        System.out.println("输入: " + java.util.Arrays.toString(answers6));
        System.out.println("输出: " + numRabbits(answers6));
        System.out.println("期望: 5");
        System.out.println("解释: 3只回答0各自一组(3只) + 2只回答1为一组(2只) = 5只");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 贪心算法，相同回答尽量归为同色");
        System.out.println("- 时间复杂度: O(nlogn) - 主要是排序时间");
        System.out.println("- 空间复杂度: O(1) - 只使用常数额外空间");
        System.out.println("- 数学技巧: ceiling除法公式 (c+x)/(x+1)");
        System.out.println("- 贪心策略: 最小化总兔子数量");
        System.out.println("- 分组原则: 每种颜色最多(x+1)只兔子回答x");
    }
}
