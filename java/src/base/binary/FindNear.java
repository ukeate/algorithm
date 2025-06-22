package base.binary;

import java.util.Arrays;

/**
 * 二分查找的变种：查找边界位置
 * 包含两个核心函数：
 * 1. leftGe：查找第一个>=val的位置
 * 2. rightLe：查找最后一个<=val的位置
 */
public class FindNear {

    /**
     * 查找第一个大于等于val的位置
     * 在有序数组中找到第一个>=val的元素索引
     * @param arr 有序数组（升序）
     * @param val 目标值
     * @return 第一个>=val的位置索引，如果不存在返回-1
     */
    public static int leftGe(int[] arr, int val) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        int l = 0, r = arr.length - 1;
        int ans = -1; // 记录答案位置
        while (l <= r) {
            int mid = l + (r - l) / 2; // 防止溢出的中点计算
            if (arr[mid] >= val) {
                ans = mid; // 记录当前符合条件的位置
                r = mid - 1; // 继续向左寻找更早的位置
            } else {
                l = mid + 1; // 向右寻找
            }
        }
        return ans;
    }

    /**
     * 暴力方法：查找第一个大于等于val的位置
     * 时间复杂度：O(n)
     * @param arr 有序数组
     * @param val 目标值
     * @return 第一个>=val的位置索引
     */
    private static int leftGeSure(int[] arr, int val) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= val) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找最后一个小于等于val的位置
     * 在有序数组中找到最后一个<=val的元素索引
     * @param arr 有序数组（升序）
     * @param val 目标值
     * @return 最后一个<=val的位置索引，如果不存在返回-1
     */
    public static int rightLe(int[] arr, int val) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        int l = 0, r = arr.length - 1;
        int ans = -1; // 记录答案位置
        while (l <= r) {
            int mid = l + (r - l) / 2; // 防止溢出的中点计算
            if (arr[mid] <= val) {
                ans = mid; // 记录当前符合条件的位置
                l = mid + 1; // 继续向右寻找更晚的位置
            } else {
                r = mid - 1; // 向左寻找
            }
        }
        return ans;
    }

    /**
     * 暴力方法：查找最后一个小于等于val的位置
     * 时间复杂度：O(n)
     * @param arr 有序数组
     * @param val 目标值
     * @return 最后一个<=val的位置索引
     */
    private static int rightLeSure(int[] arr, int val) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        // 从后往前遍历找到第一个<=val的位置
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] <= val) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 生成随机数组用于测试
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组（包含正负数）
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            // 生成正负随机数
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 打印数组内容
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
     * 测试方法，验证二分查找边界算法的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            Arrays.sort(arr); // 二分查找需要有序数组
            // 生成随机查找目标
            int val = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
            int ans1 = rightLe(arr, val);
            int ans2 = rightLeSure(arr, val);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(val);
                break;
            }
        }
        System.out.println("test end");
    }
}
