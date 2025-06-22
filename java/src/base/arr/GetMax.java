package base.arr;

/**
 * 使用分治算法找数组中的最大值
 * 分治思想：将大问题分解为小问题，递归求解
 * 时间复杂度：O(n)，空间复杂度：O(log n)（递归栈深度）
 */
public class GetMax {
    /**
     * 递归处理函数，使用分治思想找到指定范围内的最大值
     * @param arr 待查找的数组
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     * @return 指定范围内的最大值
     */
    private static int process(int[] arr, int l, int r) {
        // 基准情况：只有一个元素时，直接返回该元素
        if (l == r) {
            return arr[l];
        }
        // 计算中点位置，避免溢出的写法
        int mid = l + ((r - l) >> 1);
        // 递归求解左半部分的最大值
        int lMax = process(arr, l, mid);
        // 递归求解右半部分的最大值
        int rMax = process(arr, mid + 1, r);
        // 返回左右两部分中的较大值
        return Math.max(lMax, rMax);
    }

    /**
     * 获取数组中的最大值
     * @param arr 待查找的数组
     * @return 数组中的最大值
     */
    public static int getMax(int[] arr) {
        return process(arr, 0, arr.length - 1);
    }
}
