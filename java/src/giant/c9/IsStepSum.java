package giant.c9;

import java.util.HashMap;

/**
 * 步进和判断问题
 * 
 * 问题描述：
 * 定义一个数的步进和StepSum为：该数加上去掉最后一位后的数，再加上去掉最后两位后的数...
 * 例如：StepSum(680) = 680 + 68 + 6 = 754
 * 
 * 给定一个数target，判断是否存在某个数x，使得StepSum(x) = target
 * 
 * 例如：
 * StepSum(123) = 123 + 12 + 1 = 136
 * 因此，136是某个数的步进和（x=123）
 * 
 * 解决方案：
 * 1. 方法1：二分查找法 - 时间复杂度O(logN * logN)
 * 2. 方法2：预处理哈希表法 - 时间复杂度O(1)查询
 * 
 * 核心思想：
 * 观察StepSum函数的单调性：如果x < y，则StepSum(x) < StepSum(y)
 * 利用这个单调性可以使用二分查找
 * 
 * 时间复杂度：O(logN * logN) - 二分查找
 * 空间复杂度：O(1)
 */
public class IsStepSum {
    
    /**
     * 计算一个数的步进和
     * 
     * 算法思路：
     * 不断将数字的最后一位去掉，同时累加每个阶段的数值
     * 例如：stepSum(680) = 680 + 68 + 6
     * 
     * 实现细节：
     * 1. 初始化sum为0
     * 2. 当num不为0时，将num加到sum中
     * 3. 将num除以10，去掉最后一位
     * 4. 重复步骤2-3直到num为0
     * 
     * 时间复杂度：O(logN)，N为输入数字的大小
     * 空间复杂度：O(1)
     * 
     * @param num 输入数字
     * @return 该数字的步进和
     */
    private static int stepSum(int num) {
        int sum = 0;
        while (num != 0) {
            sum += num;     // 累加当前数字
            num /= 10;      // 去掉最后一位
        }
        return sum;
    }

    /**
     * 判断给定数字是否为某个数的步进和
     * 
     * 算法思路：
     * 利用stepSum函数的单调性进行二分查找
     * 
     * 关键观察：
     * - 如果x < y，则stepSum(x) < stepSum(y)（单调递增）
     * - stepSum(x) >= x，因为stepSum包含原数字本身
     * - 因此搜索范围为[0, stepSum]
     * 
     * 二分查找过程：
     * 1. 设定搜索范围[l, r] = [0, stepSum]
     * 2. 计算中点m = l + (r-l)/2
     * 3. 计算stepSum(m)
     * 4. 如果stepSum(m) = stepSum，找到答案
     * 5. 如果stepSum(m) < stepSum，在右半部分搜索
     * 6. 如果stepSum(m) > stepSum，在左半部分搜索
     * 
     * 时间复杂度：O(logN * logN)
     * - 外层二分查找：O(logN)
     * - 内层stepSum计算：O(logN)
     * 
     * 空间复杂度：O(1)
     * 
     * @param stepSum 目标步进和
     * @return 是否存在某个数的步进和等于目标值
     */
    public static boolean isStepSum(int stepSum) {
        int l = 0, r = stepSum;  // 搜索范围[0, stepSum]
        int m = 0, cur = 0;
        
        while (l <= r) {
            m = l + ((r - l) >> 1);  // 防止溢出的中点计算
            cur = stepSum(m);        // 计算当前数字的步进和
            
            if (cur == stepSum) {
                return true;  // 找到匹配的数字
            } else if (cur < stepSum) {
                l = m + 1;    // 在右半部分搜索
            } else {
                r = m - 1;    // 在左半部分搜索
            }
        }
        return false;  // 未找到匹配的数字
    }

    /**
     * 构建步进和映射表（用于验证算法正确性）
     * 
     * 算法思路：
     * 预计算范围[0, max]内所有数字的步进和，构建哈希表
     * key: 步进和值，value: 对应的原数字
     * 
     * 优点：
     * - 查询时间复杂度O(1)
     * - 适合大量查询的场景
     * 
     * 缺点：
     * - 需要预处理时间O(N*logN)
     * - 需要额外空间O(N)
     * 
     * @param max 计算范围的上限
     * @return 步进和到原数字的映射表
     */
    private static HashMap<Integer, Integer> stepSumMap(int max) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i <= max; i++) {
            map.put(stepSum(i), i);  // 步进和 -> 原数字
        }
        return map;
    }

    /**
     * 测试方法：验证二分查找算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 步进和判断问题测试 ===");
        
        // 手动测试用例
        System.out.println("1. 手动测试用例:");
        int[] testCases = {680, 123, 456, 1, 10, 100};
        for (int num : testCases) {
            int stepSumValue = stepSum(num);
            boolean result = isStepSum(stepSumValue);
            System.out.println("StepSum(" + num + ") = " + stepSumValue + 
                             ", isStepSum(" + stepSumValue + ") = " + result);
        }
        System.out.println();
        
        // 测试一些不是步进和的数字
        System.out.println("2. 负面测试用例:");
        int[] negativeCases = {2, 4, 7, 15, 25};
        for (int target : negativeCases) {
            boolean result = isStepSum(target);
            System.out.println("isStepSum(" + target + ") = " + result);
        }
        System.out.println();
        
        // 大规模验证测试
        System.out.println("3. 大规模验证测试:");
        int max = 1000000;
        int maxStepSum = stepSum(max);
        
        System.out.println("测试范围: [0, " + max + "]");
        System.out.println("最大步进和: " + maxStepSum);
        System.out.println("构建验证映射表...");
        
        HashMap<Integer, Integer> ans = stepSumMap(max);
        
        System.out.println("开始验证算法正确性...");
        boolean allCorrect = true;
        long start = System.currentTimeMillis();
        
        for (int i = 0; i <= maxStepSum; i++) {
            boolean binarySearchResult = isStepSum(i);
            boolean hashMapResult = ans.containsKey(i);
            
            if (binarySearchResult != hashMapResult) {
                System.out.println("发现错误! 目标值: " + i);
                System.out.println("二分查找结果: " + binarySearchResult);
                System.out.println("哈希表结果: " + hashMapResult);
                allCorrect = false;
                break;
            }
            
            // 显示进度
            if ((i + 1) % 100000 == 0) {
                System.out.println("已验证 " + (i + 1) + " 个数值...");
            }
        }
        
        long end = System.currentTimeMillis();
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        System.out.println("验证耗时: " + (end - start) + "ms");
        System.out.println("有效步进和数量: " + ans.size());
        System.out.println("覆盖率: " + (ans.size() * 100.0 / maxStepSum) + "%");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
