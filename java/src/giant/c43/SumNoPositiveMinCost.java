package giant.c43;

import java.util.Arrays;

/**
 * 数组和变非正数最小代价问题
 * 
 * 问题描述：
 * 给定一个正数数组arr长度为n、正数x、正数y
 * 你的目标是让arr整体的累加和<=0
 * 
 * 操作规则：
 * 你可以对数组中的数num执行以下三种操作中的一种，且每个数最多能执行一次操作：
 * 1）不变：代价为0
 * 2）可以选择让num变成0，承担x的代价
 * 3）可以选择让num变成-num，承担y的代价
 * 
 * 求解目标：
 * 返回你达到目标的最小代价
 * 
 * 解题思路：
 * 方法1：递归+回溯（暴力解法）
 * - 对每个元素尝试三种操作
 * - 时间复杂度为O(3^n)，仅用于验证
 * 
 * 方法2-3：贪心算法（最优解法）
 * - 关键洞察：如果x >= y，总是优先选择变负数
 * - 如果x < y，需要在变0和变负数之间寻找平衡
 * - 排序+双指针优化搜索过程
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(1)
 * 
 * 来源：微软面试题
 * 
 * @author Zhu Runqi
 */
public class SumNoPositiveMinCost {

    /**
     * 递归尝试所有可能的操作组合（方法1：暴力解法）
     * 
     * 算法思路：
     * 1. 对每个位置的元素尝试三种操作：不变、变0、变负数
     * 2. 递归计算所有可能的操作组合
     * 3. 当累加和<=0时返回代价，否则返回无穷大
     * 
     * 注意：时间复杂度为O(3^n)，仅用于小规模验证
     * 
     * @param arr 数组
     * @param x 变0的代价
     * @param y 变负数的代价
     * @param i 当前处理的位置
     * @param sum 当前累加和
     * @return 最小代价
     */
    private static int process1(int[] arr, int x, int y, int i, int sum) {
        if (sum <= 0) {
            return 0;  // 已达到目标
        }
        if (i == arr.length) {
            return Integer.MAX_VALUE;  // 无法达到目标
        }
        
        // 操作1：不变
        int p1 = process1(arr, x, y, i + 1, sum);
        
        // 操作2：变成0（减少sum中的arr[i]）
        int p2 = Integer.MAX_VALUE;
        int next2 = process1(arr, x, y, i + 1, sum - arr[i]);
        if (next2 != Integer.MAX_VALUE) {
            p2 = x + next2;
        }
        
        // 操作3：变成负数（减少sum中的2*arr[i]）
        int p3 = Integer.MAX_VALUE;
        int next3 = process1(arr, x, y, i + 1, sum - (arr[i] << 1));
        if (next3 != Integer.MAX_VALUE) {
            p3 = y + next3;
        }
        
        return Math.min(p1, Math.min(p2, p3));
    }

    /**
     * 暴力递归方法的入口函数
     * 
     * @param arr 输入数组
     * @param x 变0的代价
     * @param y 变负数的代价
     * @return 最小代价
     */
    public static int min1(int[] arr, int x, int y) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return process1(arr, x, y, 0, sum);
    }

    /**
     * 二分查找最左边的位置
     * 
     * @param arr 已排序数组
     * @param l 左边界
     * @param v 目标值
     * @return 第一个<=v的位置
     */
    public static int mostLeft(int[] arr, int l, int v) {
        int r = arr.length - 1;
        int m = 0;
        int ans = arr.length;
        while (l <= r) {
            m = (l + r) / 2;
            if (arr[m] <= v) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }

    /**
     * 贪心算法求解（方法2：优化解法）
     * 
     * 算法思路：
     * 1. 数组按降序排列（从大到小）
     * 2. 如果x >= y，优先选择变负数操作
     * 3. 如果x < y，需要在变0和变负数之间找平衡：
     *    - 尝试不同数量的变负数操作
     *    - 对剩余元素使用变0操作
     *    - 使用二分查找优化搜索
     * 
     * @param arr 输入数组
     * @param x 变0的代价
     * @param y 变负数的代价
     * @return 最小代价
     */
    public static int min2(int[] arr, int x, int y) {
        Arrays.sort(arr);
        int n = arr.length;
        
        // 将数组反转为降序
        for (int l = 0, r = n - 1; l <= r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        
        if (x >= y) {
            // 如果变0的代价不低于变负数，优先变负数
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }
            int cost = 0;
            for (int i = 0; i < n && sum > 0; i++) {
                sum -= arr[i] << 1;  // 变负数减少2*arr[i]
                cost += y;
            }
            return cost;
        } else {
            // x < y的情况，需要找平衡点
            // 构建后缀和数组以便快速计算剩余和
            for (int i = n - 2; i >= 0; i--) {
                arr[i] += arr[i + 1];
            }
            
            // 尝试不同数量的变负数操作
            int benefit = 0;  // 当前变负数操作的总收益
            int left = mostLeft(arr, 0, benefit);  // 需要变0的元素个数
            int cost = left * x;  // 初始代价（全部变0）
            
            for (int i = 0; i < n - 1; i++) {
                benefit += arr[i] - arr[i + 1];  // 增加变负数的收益
                left = mostLeft(arr, i + 1, benefit);  // 更新需要变0的元素个数
                cost = Math.min(cost, (i + 1) * y + (left - i - 1) * x);
            }
            
            return cost;
        }
    }

    /**
     * 优化的贪心算法（方法3：最优解法）
     * 
     * 算法思路：
     * 1. 在方法2基础上进一步优化
     * 2. 使用双指针技术替代二分查找
     * 3. 减少不必要的计算，提高实际性能
     * 
     * @param arr 输入数组
     * @param x 变0的代价
     * @param y 变负数的代价
     * @return 最小代价
     */
    public static int min3(int[] arr, int x, int y) {
        Arrays.sort(arr);
        int n = arr.length;
        
        // 将数组反转为降序
        for (int l = 0, r = n - 1; l <= r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        
        if (x >= y) {
            // 如果变0的代价不低于变负数，优先变负数
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }
            int cost = 0;
            for (int i = 0; i < n && sum > 0; i++) {
                sum -= arr[i] << 1;  // 变负数减少2*arr[i]
                cost += y;
            }
            return cost;
        } else {
            // x < y的情况，使用双指针优化
            int benefit = 0;  // 变负数的总收益
            int cost = arr.length * x;  // 初始代价（全部变0）
            int benefitCut = 0;  // 被切掉的收益
            
            // 双指针：i表示变负数的个数，left表示需要变0的边界
            for (int i = 0, left = n; i < left - 1; i++) {
                benefit += arr[i];  // 增加变负数的收益
                
                // 移动左指针，找到最优的变0边界
                while (left - 1 > i && benefitCut + arr[left - 1] <= benefit) {
                    benefitCut += arr[left - 1];
                    left--;
                }
                
                // 更新最小代价
                cost = Math.min(cost, (i + 1) * y + (left - i - 1) * x);
            }
            
            return cost;
        }
    }

    /**
     * 生成随机数组
     * 
     * @param len 数组长度
     * @param v 数值上界
     * @return 随机数组
     */
    private static int[] randomArr(int len, int v) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * v) + 1;
        }
        return arr;
    }

    /**
     * 复制数组
     * 
     * @param arr 原数组
     * @return 复制的数组
     */
    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 数组和变非正数最小代价问题 ===\n");
        
        // 测试用例1：简单示例
        System.out.println("测试用例1：简单示例");
        int[] arr1 = {10, 5, 3};
        int x1 = 2, y1 = 3;
        int result1_1 = min1(copy(arr1), x1, y1);
        int result1_2 = min2(copy(arr1), x1, y1);
        int result1_3 = min3(copy(arr1), x1, y1);
        System.out.print("数组: [");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + (i == arr1.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("x=" + x1 + ", y=" + y1);
        System.out.println("最小代价 (方法1): " + result1_1);
        System.out.println("最小代价 (方法2): " + result1_2);
        System.out.println("最小代价 (方法3): " + result1_3);
        System.out.println("分析: 总和18，变负数10得到-10，收益20，总和变为-2");
        System.out.println();
        
        // 测试用例2：变0代价高的情况
        System.out.println("测试用例2：变0代价高的情况");
        int[] arr2 = {8, 6, 4, 2};
        int x2 = 10, y2 = 3;
        int result2_1 = min1(copy(arr2), x2, y2);
        int result2_2 = min2(copy(arr2), x2, y2);
        int result2_3 = min3(copy(arr2), x2, y2);
        System.out.print("数组: [");
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i] + (i == arr2.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("x=" + x2 + ", y=" + y2);
        System.out.println("最小代价 (方法1): " + result2_1);
        System.out.println("最小代价 (方法2): " + result2_2);
        System.out.println("最小代价 (方法3): " + result2_3);
        System.out.println("分析: x>=y，优先使用变负数操作");
        System.out.println();
        
        // 测试用例3：变负数代价高的情况
        System.out.println("测试用例3：变负数代价高的情况");
        int[] arr3 = {15, 10, 5};
        int x3 = 2, y3 = 8;
        int result3_1 = min1(copy(arr3), x3, y3);
        int result3_2 = min2(copy(arr3), x3, y3);
        int result3_3 = min3(copy(arr3), x3, y3);
        System.out.print("数组: [");
        for (int i = 0; i < arr3.length; i++) {
            System.out.print(arr3[i] + (i == arr3.length - 1 ? "" : ", "));
        }
        System.out.println("]");
        System.out.println("x=" + x3 + ", y=" + y3);
        System.out.println("最小代价 (方法1): " + result3_1);
        System.out.println("最小代价 (方法2): " + result3_2);
        System.out.println("最小代价 (方法3): " + result3_3);
        System.out.println("分析: x<y，需要在变0和变负数之间找平衡");
        System.out.println();
        
        // 性能测试和正确性验证
        System.out.println("=== 性能测试和正确性验证 ===");
        int times = 10000;
        int maxLen = 12;
        int maxVal = 20;
        int maxCost = 10;
        boolean allPassed = true;
        
        System.out.println("开始随机测试...");
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * maxLen);
            int[] arr = randomArr(len, maxVal);
            int[] testArr1 = copy(arr);
            int[] testArr2 = copy(arr);
            int[] testArr3 = copy(arr);
            int x = (int) (Math.random() * maxCost);
            int y = (int) (Math.random() * maxCost);
            
            int ans1 = min1(testArr1, x, y);
            int ans2 = min2(testArr2, x, y);
            int ans3 = min3(testArr3, x, y);
            
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("测试失败!");
                System.out.print("数组: [");
                for (int j = 0; j < arr.length; j++) {
                    System.out.print(arr[j] + (j == arr.length - 1 ? "" : ", "));
                }
                System.out.println("]");
                System.out.println("x=" + x + ", y=" + y);
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                System.out.println("方法3结果: " + ans3);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有随机测试通过！");
        }
        
        // 大规模性能测试
        System.out.println("\n=== 大规模性能测试 ===");
        int[] largeArr = randomArr(1000, 1000);
        int largeX = 50, largeY = 30;
        
        long start2 = System.currentTimeMillis();
        int largeResult2 = min2(copy(largeArr), largeX, largeY);
        long end2 = System.currentTimeMillis();
        
        long start3 = System.currentTimeMillis();
        int largeResult3 = min3(copy(largeArr), largeX, largeY);
        long end3 = System.currentTimeMillis();
        
        System.out.println("数组长度: 1000");
        System.out.println("方法2结果: " + largeResult2 + ", 时间: " + (end2 - start2) + "ms");
        System.out.println("方法3结果: " + largeResult3 + ", 时间: " + (end3 - start3) + "ms");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 目标：数组总和 <= 0");
        System.out.println("   - 操作：不变(0)、变0(x)、变负数(y)");
        System.out.println("   - 约束：每个元素最多操作一次");
        System.out.println();
        System.out.println("2. 关键洞察：");
        System.out.println("   - 变负数操作的收益是变0操作的2倍");
        System.out.println("   - 应该优先对大数进行操作");
        System.out.println("   - x和y的相对大小决定策略");
        System.out.println();
        System.out.println("3. 策略分析：");
        System.out.println("   - x >= y：变负数总是更优，优先使用");
        System.out.println("   - x < y：需要在收益和代价之间平衡");
        System.out.println("   - 排序：按降序处理，优先处理大数");
        System.out.println();
        System.out.println("4. 算法优化：");
        System.out.println("   - 方法2：使用二分查找定位边界");
        System.out.println("   - 方法3：使用双指针技术替代二分");
        System.out.println("   - 后缀和：快速计算剩余元素和");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 方法1：O(3^n)，指数级，仅供验证");
        System.out.println("   - 方法2-3：O(n log n)，主要是排序开销");
        System.out.println("   - 空间复杂度：O(1)，原地算法");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 资源调度优化");
        System.out.println("   - 成本控制问题");
        System.out.println("   - 组合优化问题");
    }
}
