package base.sort;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * 快速排序算法实现
 * 
 * 算法原理：
 * 1. 选择一个基准元素(pivot)
 * 2. 将数组分为三部分：小于基准、等于基准、大于基准
 * 3. 递归地对小于和大于基准的部分进行排序
 * 
 * 核心思想：分治法 (Divide and Conquer)
 * 
 * 时间复杂度：
 * - 平均情况：O(N*logN)
 * - 最坏情况：O(N²) - 当每次选择的基准都是最小或最大值时
 * - 最好情况：O(N*logN)
 * 
 * 空间复杂度：
 * - 递归版本：O(logN) - 递归调用栈
 * - 迭代版本：O(logN) - 显式栈
 * 
 * 优化策略：
 * 1. 随机选择基准元素，避免最坏情况
 * 2. 三路快排，处理大量重复元素的情况
 */
public class QuickSort {

    /**
     * 交换数组中两个位置的元素
     * 
     * @param arr 数组
     * @param i 位置i
     * @param j 位置j
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 荷兰国旗问题 - 三路划分
     * 
     * 将数组分为三部分：
     * [l, less] - 小于基准的元素
     * [less+1, more-1] - 等于基准的元素  
     * [more, r-1] - 大于基准的元素
     * arr[r] - 基准元素
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界（基准元素位置）
     * @return 等于基准的区域边界 [less+1, more-1]
     */
    public static int[] partition(int[] arr, int l, int r) {
        int less = l - 1;    // 小于区域的右边界
        int more = r;        // 大于区域的左边界
        int i = l;           // 当前遍历位置
        
        while (i < more) {
            if (arr[i] < arr[r]) {
                // 当前元素小于基准，放入小于区域
                swap(arr, ++less, i++);
            } else if (arr[i] > arr[r]) {
                // 当前元素大于基准，放入大于区域
                swap(arr, --more, i);
                // 注意：这里i不递增，因为交换过来的元素还没有判断
            } else {
                // 当前元素等于基准，直接移动到下一个位置
                i++;
            }
        }
        // 将基准元素放到正确位置
        swap(arr, more, r);
        return new int[]{less + 1, more};
    }

    /**
     * 快速排序的递归实现
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     */
    private static void process(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        
        // 随机选择基准元素，避免最坏情况
        swap(arr, l + (int) ((r - l + 1) * Math.random()), r);
        
        // 三路划分
        int[] eq = partition(arr, l, r);
        
        // 递归排序小于基准的部分
        process(arr, l, eq[0] - 1);
        // 递归排序大于基准的部分
        process(arr, eq[1] + 1, r);
        // 等于基准的部分已经在正确位置，不需要排序
    }

    /**
     * 快速排序主方法 - 递归版本
     * 
     * @param arr 待排序数组
     */
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    /**
     * 排序任务类 - 用于迭代版本的快速排序
     */
    private static class Job {
        public int l;    // 左边界
        public int r;    // 右边界

        public Job(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }

    /**
     * 快速排序的迭代实现 - 使用栈模拟递归
     * 
     * 优点：避免递归调用栈溢出的问题
     * 
     * @param arr 待排序数组
     */
    public static void quickSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        
        Deque<Job> stack = new ArrayDeque<>();
        stack.push(new Job(0, arr.length - 1));
        
        while (!stack.isEmpty()) {
            Job op = stack.pop();
            if (op.l < op.r) {
                // 随机选择基准元素
                swap(arr, op.l + (int) ((op.r - op.l + 1) * Math.random()), op.r);
                
                // 三路划分
                int[] eq = partition(arr, op.l, op.r);
                
                // 将子任务压入栈中
                stack.push(new Job(op.l, eq[0] - 1));      // 小于基准的部分
                stack.push(new Job(eq[1] + 1, op.r));      // 大于基准的部分
            }
        }
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 最大元素值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
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
     * 判断两个数组是否相等
     * 
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 相等返回true，否则返回false
     */
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1 == null || arr2 == null) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法 - 验证两种快速排序实现的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(maxLen, maxVal);
            int[] arr2 = copy(arr1);
            quickSort(arr1);    // 递归版本
            quickSort2(arr2);   // 迭代版本
            if (!isEqual(arr1, arr2)) {
                System.out.println("Wrong");
                print(arr1);
                print(arr2);
                break;
            }
        }
        System.out.println("test end");
    }
}
