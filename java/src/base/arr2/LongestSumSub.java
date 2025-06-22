package base.arr2;

import java.util.HashMap;

/**
 * 最长和为k的子数组（数组包含正负数）
 * 使用前缀和+HashMap解决
 * 核心思想：对于位置i，如果存在位置j使得preSum[i] - preSum[j] = k，
 * 则子数组[j+1...i]的和为k
 */
// 数有正负，最长和为k的子串
public class LongestSumSub {
    /**
     * HashMap解法（最优）
     * 时间复杂度：O(n)，空间复杂度：O(n)
     * @param arr 原数组（包含正负数）
     * @param k 目标和
     * @return 最长子数组长度
     */
    public static int max(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // HashMap存储前缀和及其最早出现的位置
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1); // 前缀和为0的位置设为-1，表示空数组
        int len = 0;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i]; // 计算前缀和
            // 如果存在前缀和sum-k，说明存在子数组和为k
            if (map.containsKey(sum - k)) {
                len = Math.max(i - map.get(sum - k), len);
            }
            // 只记录每个前缀和第一次出现的位置（为了获得最长长度）
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }
        return len;
    }

    /**
     * 验证子数组[l, r]的和是否等于k
     * @param arr 原数组
     * @param l 左边界
     * @param r 右边界
     * @param k 目标和
     * @return 是否等于k
     */
    private static boolean valid(int[] arr, int l, int r, int k) {
        int sum = 0;
        for (int i = l; i <= r; i++) {
            sum += arr[i];
        }
        return sum == k;
    }

    /**
     * 暴力解法：枚举所有子数组
     * 时间复杂度：O(n³)
     * @param arr 原数组
     * @param k 目标和
     * @return 最长子数组长度
     */
    public static int sure(int[] arr, int k) {
        int max = 0;
        // 枚举所有可能的子数组
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (valid(arr, i, j, k)) {
                    max = Math.max(max, j - i + 1);
                }
            }
        }
        return max;
    }

    /**
     * 生成随机数组用于测试
     * @param len 最大长度
     * @param maxVal 最大值
     * @return 随机数组（包含正负数）
     */
    public static int[] randomArr(int len, int maxVal) {
        int[] arr = new int[(int) (len * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            // 生成正负随机数
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 测试方法，验证HashMap解法和暴力解法的正确性
     */
    public static void main(String[] args) {
        int times = 500000;
        int len = 50;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, maxVal);
            // 生成随机目标和
            int k = (int) (((maxVal << 1) + 1) * Math.random()) - maxVal;
            int ans1 = max(arr, k);
            int ans2 = sure(arr, k);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
