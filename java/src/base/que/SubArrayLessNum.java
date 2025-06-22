package base.que;

import java.util.LinkedList;

/**
 * 子数组最大值与最小值差值小于等于指定值的子数组个数
 * 
 * 问题描述：
 * 给定一个整数数组arr和一个整数sum，
 * 求有多少个子数组满足：子数组中最大值减去最小值小于等于sum
 * 
 * 核心观察：
 * 1. 如果子数组[L,R]满足条件，那么其内部的任意子数组也满足条件
 * 2. 如果子数组[L,R]不满足条件，那么包含它的任意更大子数组也不满足条件
 * 
 * 算法思路：
 * 使用滑动窗口 + 单调双端队列：
 * - 维护一个最大值单调队列和最小值单调队列
 * - 对每个左边界L，找到最大的右边界R使得[L,R]满足条件
 * - 则以L为左边界的满足条件的子数组个数为 R-L+1
 * 
 * 时间复杂度：O(N) - 每个元素最多进出队列一次
 * 空间复杂度：O(N) - 两个双端队列的空间
 * 
 * 应用场景：
 * - 滑动窗口最值查询
 * - 单调队列优化
 * - 子数组统计问题
 */
public class SubArrayLessNum {
    /**
     * 高效算法：使用滑动窗口 + 单调双端队列
     * 
     * 算法核心思想：
     * 1. 维护两个单调双端队列：
     *    - maxWindow：维护窗口内的最大值（队头是最大值）
     *    - minWindow：维护窗口内的最小值（队头是最小值）
     * 2. 对每个左边界L，通过移动右边界R找到最大的满足条件的窗口
     * 3. 利用单调性：如果[L,R]满足条件，则[L,L], [L,L+1], ..., [L,R]都满足
     * 
     * 关键技巧：
     * - 右边界R只会向右移动，不会回退（单调性）
     * - 当max-min > sum时，停止扩展右边界
     * - 左边界向右移动时，维护队列的有效性
     * 
     * @param arr 输入数组
     * @param sum 最大允许的差值
     * @return 满足条件的子数组个数
     */
    public static int num(int[] arr, int sum) {
        if (arr == null || arr.length == 0 || sum < 0) {
            return 0;
        }
        
        int n = arr.length;
        int count = 0;
        
        // 最大值单调双端队列（单调递减，队头是最大值）
        LinkedList<Integer> maxWindow = new LinkedList<>();
        // 最小值单调双端队列（单调递增，队头是最小值）
        LinkedList<Integer> minWindow = new LinkedList<>();
        
        int r = 0;  // 右边界
        
        // 遍历每个可能的左边界
        for (int l = 0; l < n; l++) {
            // 扩展右边界，直到不满足条件为止
            while (r < n) {
                // 维护最大值单调队列
                // 移除队尾所有小于等于arr[r]的元素，保持单调递减
                while (!maxWindow.isEmpty() && arr[maxWindow.peekLast()] <= arr[r]) {
                    maxWindow.pollLast();
                }
                maxWindow.addLast(r);
                
                // 维护最小值单调队列
                // 移除队尾所有大于等于arr[r]的元素，保持单调递增
                while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[r]) {
                    minWindow.pollLast();
                }
                minWindow.addLast(r);
                
                // 检查当前窗口[l,r]是否满足条件
                if (arr[maxWindow.peekFirst()] - arr[minWindow.peekFirst()] > sum) {
                    // 不满足条件，停止扩展右边界
                    break;
                } else {
                    // 满足条件，继续扩展
                    r++;
                }
            }
            
            // 以l为左边界的满足条件的子数组个数为 r-l
            // 即：[l,l], [l,l+1], ..., [l,r-1]
            count += r - l;
            
            // 左边界右移，需要维护队列的有效性
            // 如果队头元素就是当前左边界，需要移除
            if (maxWindow.peekFirst() == l) {
                maxWindow.pollFirst();
            }
            if (minWindow.peekFirst() == l) {
                minWindow.pollFirst();
            }
        }
        
        return count;
    }

    /**
     * 暴力算法：用于验证结果正确性
     * 
     * 思路：枚举所有可能的子数组，逐一检查是否满足条件
     * 时间复杂度：O(N³) - 三层循环
     * 
     * @param arr 输入数组
     * @param sum 最大允许的差值
     * @return 满足条件的子数组个数
     */
    public static int numSure(int[] arr, int sum) {
        if (arr == null || arr.length == 0 || sum < 0) {
            return 0;
        }
        int n = arr.length;
        int count = 0;
        
        // 枚举所有可能的子数组[l,r]
        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {
                // 计算子数组[l,r]的最大值和最小值
                int max = arr[l];
                int min = arr[l];
                for (int i = l + 1; i <= r; i++) {
                    max = Math.max(max, arr[i]);
                    min = Math.min(min, arr[i]);
                }
                
                // 检查是否满足条件
                if (max - min <= sum) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 数组元素的最大绝对值
     * @return 随机数组
     */
    public static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 测试方法 - 验证算法正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 子数组最大最小值差值问题测试 ===");
        
        // 手工测试用例
        int[] testArr = {3, 1, 4, 1, 5, 9, 2, 6};
        int testSum = 3;
        
        System.out.println("测试数组：" + java.util.Arrays.toString(testArr));
        System.out.println("最大允许差值：" + testSum);
        
        int result1 = num(testArr, testSum);
        int result2 = numSure(testArr, testSum);
        
        System.out.println("高效算法结果：" + result1);
        System.out.println("暴力算法结果：" + result2);
        System.out.println("结果" + (result1 == result2 ? "一致" : "不一致"));
        
        // 详细分析一个小例子
        System.out.println("\n=== 详细分析示例 ===");
        int[] smallArr = {1, 3, 2};
        int smallSum = 1;
        System.out.println("数组：" + java.util.Arrays.toString(smallArr));
        System.out.println("最大差值：" + smallSum);
        
        System.out.println("所有子数组及其最大最小值差：");
        int detailCount = 0;
        for (int l = 0; l < smallArr.length; l++) {
            for (int r = l; r < smallArr.length; r++) {
                int max = smallArr[l], min = smallArr[l];
                for (int i = l + 1; i <= r; i++) {
                    max = Math.max(max, smallArr[i]);
                    min = Math.min(min, smallArr[i]);
                }
                int diff = max - min;
                boolean valid = diff <= smallSum;
                if (valid) detailCount++;
                
                System.out.printf("  [%d,%d]: %s, max=%d, min=%d, diff=%d, %s\n",
                    l, r, java.util.Arrays.toString(java.util.Arrays.copyOfRange(smallArr, l, r + 1)),
                    max, min, diff, valid ? "满足" : "不满足");
            }
        }
        System.out.println("满足条件的子数组个数：" + detailCount);
        
        // 大规模随机测试
        System.out.println("\n=== 大规模随机测试 ===");
        int times = 1000000;
        int maxLen = 100;
        int maxVal = 200;
        System.out.println("测试次数：" + times);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int sum = (int) ((maxVal + 1) * Math.random());
            int ans1 = num(arr, sum);
            int ans2 = numSure(arr, sum);
            if (ans1 != ans2) {
                System.out.println("测试失败！");
                System.out.println("数组：" + java.util.Arrays.toString(arr));
                System.out.println("sum：" + sum);
                System.out.println("高效算法：" + ans1);
                System.out.println("暴力算法：" + ans2);
                return;
            }
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("所有测试通过！");
        System.out.println("总耗时：" + (endTime - startTime) + "ms");
        
        // 算法复杂度说明
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("高效算法（滑动窗口 + 单调队列）：");
        System.out.println("- 时间复杂度：O(N)");
        System.out.println("- 空间复杂度：O(N)");
        System.out.println("- 每个元素最多进出队列一次");
        
        System.out.println("\n暴力算法：");
        System.out.println("- 时间复杂度：O(N³)");
        System.out.println("- 空间复杂度：O(1)");
        System.out.println("- 三层循环枚举所有子数组");
        
        System.out.println("\n核心技巧：");
        System.out.println("- 利用子数组的单调性质");
        System.out.println("- 单调队列维护滑动窗口最值");
        System.out.println("- 避免重复计算，实现线性时间复杂度");
    }
}
