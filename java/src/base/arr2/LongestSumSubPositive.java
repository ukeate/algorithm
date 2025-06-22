package base.arr2;

/**
 * 最长和为sum的子数组（数组元素均为正数）
 * 由于数组元素均为正数，具有单调性，可以使用滑动窗口
 * 时间复杂度：O(n)，空间复杂度：O(1)
 */
// 最长和为sum的子串，值为正数有单调性
public class LongestSumSubPositive {

    /**
     * 滑动窗口解法（最优）
     * 利用正数数组的单调性质：窗口扩大时和增大，窗口缩小时和减小
     * @param arr 正数数组
     * @param k 目标和
     * @return 最长子数组长度
     */
    public static int max(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k <= 0) {
            return 0;
        }
        int l = 0, r = 0; // 滑动窗口的左右边界
        int sum = arr[0]; // 当前窗口的和
        int len = 0; // 最长子数组长度
        
        while (r < arr.length) {
            if (sum == k) {
                // 找到目标和，更新最长长度
                len = Math.max(len, r - l + 1);
                sum -= arr[l++]; // 缩小窗口
            } else if (sum < k) {
                // 当前和小于目标，扩大窗口
                if (++r == arr.length) {
                    break; // 右边界越界，退出
                }
                sum += arr[r];
            } else {
                // 当前和大于目标，缩小窗口
                sum -= arr[l++];
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
     * 生成随机正数数组用于测试
     * @param len 数组长度
     * @param maxVal 最大值
     * @return 随机正数数组
     */
    private static int[] randomPositiveArr(int len, int maxVal) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            // 生成[1, maxVal+1)范围内的正数
            arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return arr;
    }

    /**
     * 测试方法，验证滑动窗口解法和暴力解法的正确性
     */
    public static void main(String[] args) {
        int times = 500000;
        int len = 50;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomPositiveArr(len, maxVal);
            // 生成随机正数目标和
            int k = (int) ((maxVal + 1) * Math.random()) + 1;
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
