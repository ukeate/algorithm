package base.binary;

import java.util.Arrays;

/**
 * 二分查找算法
 * 在有序数组中查找指定元素
 * 时间复杂度：O(log n)，空间复杂度：O(1)
 */
public class Find {

    /**
     * 二分查找：在有序数组中查找指定元素
     * @param arr 有序数组（升序）
     * @param num 目标元素
     * @return 如果找到返回true，否则返回false
     */
    // arr有序
    public static boolean binaryFind(int[] arr, int num) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        int l = 0; // 左边界
        int r = arr.length - 1; // 右边界
        while (l <= r) {
            // 计算中点，防止整数溢出的安全写法
            int mid = l + (r - l) / 2;
            if (arr[mid] == num) {
                return true; // 找到目标元素
            } else if (arr[mid] < num) {
                l = mid + 1; // 目标在右半部分
            } else {
                r = mid - 1; // 目标在左半部分
            }
        }
        return false; // 未找到
    }

    /**
     * 暴力查找：线性遍历数组查找元素
     * 时间复杂度：O(n)
     * @param arr 数组
     * @param num 目标元素
     * @return 如果找到返回true，否则返回false
     */
    public static boolean binaryFindSure(int[] arr, int num) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成随机数组用于测试
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组（包含正负数）
     */
    public static int[] randomArr(int maxLen, int maxVal) {
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
     * 测试方法，验证二分查找和暴力查找的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            Arrays.sort(arr); // 二分查找需要有序数组
            // 生成随机查找目标
            int val = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
            boolean ans1 = binaryFind(arr, val);
            boolean ans2 = binaryFindSure(arr, val);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(val + "|" + ans1 + "|" + ans2);
                print(arr);
                break;
            }
        }
        System.out.println("test end");
    }
}
