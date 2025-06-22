package base.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 单调栈算法实现
 * 
 * 单调栈是一种特殊的栈结构，栈中元素保持单调递增或单调递减的性质
 * 
 * 主要应用场景：
 * 1. 寻找每个元素左边和右边第一个比它大/小的元素
 * 2. 直方图中最大矩形面积问题
 * 3. 子数组的最小值之和等问题
 * 
 * 核心思想：
 * - 维护一个单调递增的栈（栈底到栈顶递增）
 * - 当遇到比栈顶小的元素时，依次弹出栈顶元素
 * - 被弹出的元素可以确定其左右边界的信息
 * 
 * 时间复杂度：O(n) - 每个元素最多入栈出栈一次
 * 空间复杂度：O(n) - 栈的空间
 */
public class MonotonousStack {
    
    /**
     * 单调栈核心算法 - 寻找每个位置左右两边最近的较小值
     * 
     * 对于数组中的每个位置i，找到：
     * - 左边离i最近且比arr[i]小的位置
     * - 右边离i最近且比arr[i]小的位置
     * 
     * 如果不存在这样的位置，则返回-1
     * 
     * 算法流程：
     * 1. 维护一个单调递增的栈（存储数组下标）
     * 2. 遍历数组，对于每个元素：
     *    - 如果栈为空或当前元素>=栈顶元素，直接入栈
     *    - 否则，弹出栈顶元素，此时可以确定被弹出元素的左右边界
     * 3. 处理完所有元素后，栈中剩余元素的右边界都是-1
     * 
     * @param arr 输入数组
     * @return 二维数组，res[i][0]表示位置i左边最近的较小值位置，res[i][1]表示右边
     */
    public static int[][] getNearLessNoRepeat(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0][0];
        }
        
        int[][] res = new int[arr.length][2];
        Stack<Integer> stack = new Stack<>();  // 单调递增栈，存储数组下标
        
        for (int i = 0; i < arr.length; i++) {
            // 当栈不为空且当前元素小于栈顶元素时，弹出栈顶
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                int popIndex = stack.pop();
                // 确定被弹出元素的左右边界
                int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
                res[popIndex][0] = leftLessIndex;  // 左边最近的较小值
                res[popIndex][1] = i;              // 右边最近的较小值
            }
            stack.push(i);  // 当前元素入栈
        }
        
        // 处理栈中剩余元素（右边没有更小的值）
        while (!stack.isEmpty()) {
            int popIndex = stack.pop();
            int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
            res[popIndex][0] = leftLessIndex;
            res[popIndex][1] = -1;  // 右边没有更小的值
        }
        
        return res;
    }

    /**
     * 处理有重复值的单调栈问题
     * 
     * 当数组中有重复元素时，需要使用链表来存储相同值的所有位置
     * 这样可以正确处理重复元素的左右边界问题
     * 
     * @param arr 可能包含重复元素的数组
     * @return 每个位置的左右最近较小值位置
     */
    public static int[][] getNearLess(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0][0];
        }
        
        int[][] res = new int[arr.length][2];
        // 栈中存储位置链表，处理重复值的情况
        Stack<List<Integer>> stack = new Stack<>();
        
        for (int i = 0; i < arr.length; i++) {
            // 弹出所有大于当前值的元素
            while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
                List<Integer> popIs = stack.pop();
                // 获取左边界：上一个元素列表的最后一个位置
                int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
                
                // 为被弹出的所有位置设置边界
                for (Integer popi : popIs) {
                    res[popi][0] = leftLessIndex;
                    res[popi][1] = i;
                }
            }
            
            // 处理当前元素：如果与栈顶相等，加入同一个列表；否则创建新列表
            if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
                stack.peek().add(i);
            } else {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                stack.push(list);
            }
        }
        
        // 处理栈中剩余元素
        while (!stack.isEmpty()) {
            List<Integer> popIs = stack.pop();
            int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
            
            for (Integer popi : popIs) {
                res[popi][0] = leftLessIndex;
                res[popi][1] = -1;
            }
        }
        
        return res;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param size 数组大小
     * @param max 最大元素值
     * @return 随机数组
     */
    public static int[] getRandomArrayNoRepeat(int size, int max) {
        int[] arr = new int[(int) (Math.random() * size) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * max) + 1;
        }
        return arr;
    }

    /**
     * 暴力解法 - 用于验证单调栈结果的正确性
     * 
     * 对每个位置都进行线性搜索，找到左右最近的较小值
     * 时间复杂度：O(n²)
     * 
     * @param arr 输入数组
     * @return 每个位置的左右最近较小值位置
     */
    public static int[][] rightWay(int[] arr) {
        int[][] res = new int[arr.length][2];
        for (int i = 0; i < arr.length; i++) {
            int leftLessIndex = -1;
            int rightLessIndex = -1;
            
            // 向左寻找最近的较小值
            for (int j = i - 1; j >= 0; j--) {
                if (arr[j] < arr[i]) {
                    leftLessIndex = j;
                    break;
                }
            }
            
            // 向右寻找最近的较小值
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[i]) {
                    rightLessIndex = j;
                    break;
                }
            }
            
            res[i][0] = leftLessIndex;
            res[i][1] = rightLessIndex;
        }
        return res;
    }

    /**
     * 判断两个二维数组是否相等
     * 
     * @param res1 数组1
     * @param res2 数组2
     * @return 相等返回true，否则返回false
     */
    public static boolean isEqual(int[][] res1, int[][] res2) {
        if (res1.length != res2.length) {
            return false;
        }
        for (int i = 0; i < res1.length; i++) {
            if (res1[i][0] != res2[i][0] || res1[i][1] != res2[i][1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印二维数组
     * 
     * @param res 要打印的二维数组
     */
    public static void printArray(int[][] res) {
        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i][0] + "," + res[i][1]);
        }
    }

    /**
     * 测试方法 - 验证单调栈算法的正确性
     */
    public static void main(String[] args) {
        int size = 10;
        int max = 20;
        int testTimes = 2000000;
        System.out.println("测试开始");
        
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = getRandomArrayNoRepeat(size, max);
            int[] arr2 = new int[arr1.length];
            System.arraycopy(arr1, 0, arr2, 0, arr1.length);
            
            // 比较单调栈算法和暴力解法的结果
            if (!isEqual(getNearLessNoRepeat(arr1), rightWay(arr2))) {
                System.out.println("出错了！");
                printArray(getNearLessNoRepeat(arr1));
                System.out.println("=======");
                printArray(rightWay(arr2));
                break;
            }
        }
        System.out.println("测试结束");
        
        // 测试包含重复元素的情况
        System.out.println("\n测试包含重复元素的单调栈:");
        int[] testArr = {3, 1, 2, 3};
        int[][] result = getNearLess(testArr);
        System.out.println("数组: [3, 1, 2, 3]");
        System.out.println("结果 (左边最近较小值, 右边最近较小值):");
        for (int i = 0; i < result.length; i++) {
            System.out.println("位置" + i + ": (" + result[i][0] + ", " + result[i][1] + ")");
        }
    }
}
