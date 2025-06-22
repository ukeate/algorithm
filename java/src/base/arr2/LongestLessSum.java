package base.arr2;

/**
 * 累加和<=k的最长子数组
 * 两种解法：滑动窗口和二分查找
 * 关键优化：利用最小累加和数组减少重复计算
 */
// 累加和<=k的最长子串
public class LongestLessSum {
    /**
     * 滑动窗口解法（最优）
     * 时间复杂度：O(n)，空间复杂度：O(n)
     * 核心思想：预处理每个位置向右的最小累加和，然后用滑动窗口
     * @param arr 原数组
     * @param k 累加和上限
     * @return 最长子数组长度
     */
    public static int max1(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        // minSums[i]：从位置i开始向右的最小累加和
        int[] minSums = new int[n];
        // minSumEnds[i]：从位置i开始达到最小累加和的结束位置
        int[] minSumEnds = new int[n];
        
        // 从右向左预处理最小累加和数组
        minSums[n - 1] = arr[n - 1];
        minSumEnds[n - 1] = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            if (minSums[i + 1] < 0) {
                // 如果右边的最小累加和为负，可以合并获得更小的和
                minSums[i] = arr[i] + minSums[i + 1];
                minSumEnds[i] = minSumEnds[i + 1];
            } else {
                // 否则只取当前元素
                minSums[i] = arr[i];
                minSumEnds[i] = i;
            }
        }
        
        // 滑动窗口求解
        int end = 0, sum = 0, ans = 0;
        for (int i = 0; i < n; i++) {
            // 尽可能向右扩展end指针
            while (end < n && sum + minSums[end] <= k) {
                sum += minSums[end];
                end = minSumEnds[end] + 1;
            }
            ans = Math.max(ans, end - i);
            // end不用回退的关键：最小累加和数组保证了单调性
            if (end > i) {
                sum -= arr[i];
            } else {
                end = i + 1;
            }
        }
        return ans;
    }

    /**
     * 二分查找辅助函数：在单调递增数组中查找第一个>=num的位置
     * @param arr 单调递增数组
     * @param num 目标值
     * @return 第一个>=num的位置索引，如果不存在返回-1
     */
    private static int getLessIdx(int[] arr, int num) {
        int l = 0, r = arr.length - 1;
        int res = -1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] >= num) {
                res = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return res;
    }

    /**
     * 二分查找解法
     * 时间复杂度：O(n log n)，空间复杂度：O(n)
     * 核心思想：维护单调递增的前缀和最大值数组，用二分查找
     * @param arr 原数组
     * @param k 累加和上限
     * @return 最长子数组长度
     */
    public static int max2(int[] arr, int k) {
        // h[i]：前缀和[0...i-1]的最大值
        int[] h = new int[arr.length + 1];
        int sum = 0;
        h[0] = sum;
        // 构建单调递增的前缀和最大值数组
        for (int i = 0; i != arr.length; i++) {
            sum += arr[i];
            h[i + 1] = Math.max(sum, h[i]);
        }
        
        sum = 0;
        int res = 0;
        int pre = 0;
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            // 找到第一个前缀和>=sum-k的位置
            pre = getLessIdx(h, sum - k);
            // 如果找到了，计算子数组长度
            len = pre == -1 ? 0 : i - pre + 1;
            res = Math.max(res, len);
        }
        return res;
    }

    /**
     * 生成随机数组用于测试
     * @param len 数组长度
     * @param maxVal 最大值
     * @return 随机数组（包含负数）
     */
    private static int[] randomArr(int len, int maxVal) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            // 生成范围在[-maxVal/3, maxVal]的随机数
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (maxVal / 3);
        }
        return arr;
    }

    /**
     * 测试方法，验证两种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 1000000; i++) {
            int[] arr = randomArr(10, 20);
            int k = (int) (21 * Math.random()) - 5;
            if (max1(arr, k) != max2(arr, k)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
