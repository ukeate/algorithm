package base.stack;

import java.util.Stack;

/**
 * 子数组最大值乘以最小值的最大结果问题 - 单调栈的高级应用
 * 
 * 问题描述：
 * 给定一个数组，对于每个子数组，计算其最大值乘以最小值的结果
 * 求所有子数组中这个乘积的最大值
 * 
 * 例如：数组[2,3,1,4]
 * 所有子数组及其最大值*最小值：
 * [2]: 2*2 = 4
 * [2,3]: 3*2 = 6
 * [2,3,1]: 3*1 = 3
 * [2,3,1,4]: 4*1 = 4
 * [3]: 3*3 = 9
 * [3,1]: 3*1 = 3
 * [3,1,4]: 4*1 = 4
 * [1]: 1*1 = 1
 * [1,4]: 4*1 = 4
 * [4]: 4*4 = 16
 * 最大结果：16
 * 
 * 解决思路：
 * 使用单调栈优化暴力解法
 * 1. 对于每个位置，找出以该位置为最小值的所有子数组
 * 2. 在这些子数组中，找出最大值，计算最大值*最小值
 * 3. 使用单调栈快速找到每个元素作为最小值时的左右边界
 * 
 * 算法原理：
 * 1. 使用单调递增栈找到每个元素左边第一个比它小的元素
 * 2. 使用单调递增栈找到每个元素右边第一个比它小的元素
 * 3. 对于每个位置i，以arr[i]为最小值的子数组范围是(left[i], right[i])
 * 4. 在这个范围内使用RMQ（区间最大值查询）找到最大值
 * 5. 计算最大值*最小值，更新全局最大值
 * 
 * 时间复杂度：O(N²) - 虽然使用了单调栈，但RMQ查询仍需要O(N)
 * 空间复杂度：O(N) - 单调栈和辅助数组的空间
 * 
 * 应用场景：
 * - 子数组极值问题
 * - 单调栈的综合应用
 * - 区间查询优化
 */
public class LargestSubMultiMin {
    
    /**
     * 求子数组最大值乘以最小值的最大结果
     * 
     * 算法步骤：
     * 1. 使用单调栈找到每个元素作为最小值时的左右边界
     * 2. 对于每个位置，在其作为最小值的范围内找最大值
     * 3. 计算最大值*最小值，更新全局最大值
     * 
     * @param arr 输入数组
     * @return 最大的（子数组最大值*最小值）结果
     */
    public static int maxSubArrayMultiMin(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        
        // 找到每个元素作为最小值时的左右边界
        int[] leftBounds = getLeftBounds(arr);   // 左边第一个比当前元素小的位置
        int[] rightBounds = getRightBounds(arr); // 右边第一个比当前元素小的位置
        
        int maxResult = 0;
        
        // 对于每个位置，计算以该位置为最小值的子数组的最大值*最小值
        for (int i = 0; i < n; i++) {
            int left = leftBounds[i];   // 左边界（不包含）
            int right = rightBounds[i]; // 右边界（不包含）
            
            // 在区间(left, right)内找最大值
            int maxInRange = getMaxInRange(arr, left + 1, right - 1);
            
            // 计算最大值*最小值
            int result = maxInRange * arr[i];
            maxResult = Math.max(maxResult, result);
        }
        
        return maxResult;
    }

    /**
     * 使用单调栈找到每个元素左边第一个比它小的元素位置
     * 
     * 算法详解：
     * 1. 维护一个单调递增栈（存储数组下标）
     * 2. 对于每个元素，弹出栈中所有大于等于当前元素的下标
     * 3. 栈顶元素就是左边第一个比当前元素小的位置
     * 4. 将当前元素下标入栈
     * 
     * @param arr 输入数组
     * @return 每个位置左边第一个比它小的元素位置（如果不存在则为-1）
     */
    private static int[] getLeftBounds(int[] arr) {
        int n = arr.length;
        int[] leftBounds = new int[n];
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            // 弹出所有大于等于当前元素的下标
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }
            
            // 栈顶就是左边第一个比当前元素小的位置
            leftBounds[i] = stack.isEmpty() ? -1 : stack.peek();
            
            // 当前元素下标入栈
            stack.push(i);
        }
        
        return leftBounds;
    }

    /**
     * 使用单调栈找到每个元素右边第一个比它小的元素位置
     * 
     * 算法详解：
     * 1. 从右向左遍历数组
     * 2. 维护一个单调递增栈（存储数组下标）
     * 3. 对于每个元素，弹出栈中所有大于等于当前元素的下标
     * 4. 栈顶元素就是右边第一个比当前元素小的位置
     * 5. 将当前元素下标入栈
     * 
     * @param arr 输入数组
     * @return 每个位置右边第一个比它小的元素位置（如果不存在则为数组长度）
     */
    private static int[] getRightBounds(int[] arr) {
        int n = arr.length;
        int[] rightBounds = new int[n];
        Stack<Integer> stack = new Stack<>();
        
        // 从右向左遍历
        for (int i = n - 1; i >= 0; i--) {
            // 弹出所有大于等于当前元素的下标
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }
            
            // 栈顶就是右边第一个比当前元素小的位置
            rightBounds[i] = stack.isEmpty() ? n : stack.peek();
            
            // 当前元素下标入栈
            stack.push(i);
        }
        
        return rightBounds;
    }

    /**
     * 在指定区间内找到最大值
     * 
     * 简单的线性查找实现
     * 可以优化为使用稀疏表（Sparse Table）实现O(1)查询
     * 
     * @param arr 数组
     * @param left 左边界（包含）
     * @param right 右边界（包含）
     * @return 区间内的最大值
     */
    private static int getMaxInRange(int[] arr, int left, int right) {
        if (left > right) {
            return 0;  // 无效区间
        }
        
        int max = arr[left];
        for (int i = left + 1; i <= right; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }

    /**
     * 暴力解法 - 用于验证算法正确性
     * 
     * 枚举所有可能的子数组，计算每个子数组的最大值*最小值
     * 时间复杂度：O(N³)
     * 
     * @param arr 输入数组
     * @return 最大的（子数组最大值*最小值）结果
     */
    private static int maxSubArrayMultiMinSure(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int maxResult = 0;
        int n = arr.length;
        
        // 枚举所有子数组
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                // 找到子数组[i,j]的最大值和最小值
                int max = arr[i];
                int min = arr[i];
                
                for (int k = i; k <= j; k++) {
                    max = Math.max(max, arr[k]);
                    min = Math.min(min, arr[k]);
                }
                
                // 计算最大值*最小值
                int result = max * min;
                maxResult = Math.max(maxResult, result);
            }
        }
        
        return maxResult;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 数组元素的最大值（正数）
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;  // 保证为正数
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
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
            System.out.println("null");
            return;
        }
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 子数组最大值乘以最小值的最大结果问题测试 ===");
        
        // 手工测试用例1
        int[] testArr1 = {2, 3, 1, 4};
        System.out.println("\n测试用例1：");
        System.out.print("数组：");
        print(testArr1);
        
        int result1 = maxSubArrayMultiMin(testArr1);
        int result2 = maxSubArrayMultiMinSure(testArr1);
        
        System.out.println("单调栈解法：" + result1);
        System.out.println("暴力解法：" + result2);
        System.out.println("结果正确：" + (result1 == result2));
        
        // 详细分析过程
        System.out.println("\n详细分析：");
        System.out.println("所有子数组的最大值*最小值：");
        int n = testArr1.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int max = testArr1[i];
                int min = testArr1[i];
                System.out.print("[");
                for (int k = i; k <= j; k++) {
                    System.out.print(testArr1[k]);
                    if (k < j) System.out.print(",");
                    max = Math.max(max, testArr1[k]);
                    min = Math.min(min, testArr1[k]);
                }
                System.out.println("]: " + max + "*" + min + " = " + (max * min));
            }
        }
        
        // 手工测试用例2 - 单元素
        int[] testArr2 = {5};
        System.out.println("\n测试用例2（单元素）：");
        System.out.print("数组：");
        print(testArr2);
        
        int result3 = maxSubArrayMultiMin(testArr2);
        int result4 = maxSubArrayMultiMinSure(testArr2);
        
        System.out.println("单调栈解法：" + result3);
        System.out.println("暴力解法：" + result4);
        System.out.println("结果正确：" + (result3 == result4));
        
        // 手工测试用例3 - 递增序列
        int[] testArr3 = {1, 2, 3, 4, 5};
        System.out.println("\n测试用例3（递增序列）：");
        System.out.print("数组：");
        print(testArr3);
        
        int result5 = maxSubArrayMultiMin(testArr3);
        int result6 = maxSubArrayMultiMinSure(testArr3);
        
        System.out.println("单调栈解法：" + result5);
        System.out.println("暴力解法：" + result6);
        System.out.println("结果正确：" + (result5 == result6));
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试 ===");
        int times = 10000;
        int maxLen = 50;  // 减小测试规模，因为暴力解法是O(N³)
        int maxVal = 100;
        System.out.println("测试次数：" + times);
        System.out.println("最大数组长度：" + maxLen);
        System.out.println("最大元素值：" + maxVal);
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            if (arr.length == 0) continue;  // 跳过空数组
            
            int ans1 = maxSubArrayMultiMin(arr);
            int ans2 = maxSubArrayMultiMinSure(arr);
            
            if (ans1 != ans2) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.println("单调栈解法：" + ans1);
                System.out.println("暴力解法：" + ans2);
                System.out.print("测试数组：");
                print(arr);
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！子数组最大值乘以最小值算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
