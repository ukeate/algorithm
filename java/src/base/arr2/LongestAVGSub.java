package base.arr2;

import java.util.TreeMap;

/**
 * 最长平均值不超过v的子数组
 * 三种解法：暴力法、TreeMap优化、滑动窗口优化
 * 核心思想：将数组每个元素减去v，问题转化为求最长累加和<=0的子数组
 */
public class LongestAVGSub {
    /**
     * 暴力解法：枚举所有子数组，计算平均值
     * 时间复杂度：O(n³)
     * @param arr 原数组
     * @param v 平均值上限
     * @return 最长子数组长度
     */
    public static int ways1(int[] arr, int v) {
        int ans = 0;
        // 枚举所有可能的左边界
        for (int l = 0; l < arr.length; l++) {
            // 枚举所有可能的右边界
            for (int r = l; r < arr.length; r++) {
                int sum = 0;
                int k = r - l + 1; // 子数组长度
                // 计算子数组和
                for (int i = l; i <= r; i++) {
                    sum += arr[i];
                }
                // 计算平均值
                double avg = (double) sum / (double) k;
                if (avg <= v) {
                    ans = Math.max(ans, k);
                }
            }
        }
        return ans;
    }

    /**
     * TreeMap优化解法：转化为求最长累加和<=0的子数组
     * 时间复杂度：O(n log n)
     * 核心思想：对于每个位置i，找到最远的位置j使得sum[j] >= sum[i]
     * @param arr 原数组（会被修改）
     * @param v 平均值上限
     * @return 最长子数组长度
     */
    public static int ways2(int[] arr, int v) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 将每个元素减去v，转化问题
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= v;
        }
        // TreeMap存储前缀和及其最早出现位置
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(0, -1); // 前缀和为0的位置设为-1
        int sum = 0;
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            // 找到第一个>=sum的前缀和
            Integer ceiling = map.ceilingKey(sum);
            if (ceiling != null) {
                // 更新最长长度
                len = Math.max(len, i - map.get(ceiling));
            } else {
                // 如果没有找到，说明当前sum是最小的，记录位置
                map.put(sum, i);
            }
        }
        return len;
    }

    /**
     * 滑动窗口辅助方法：求累加和不超过k的最长子数组
     * 时间复杂度：O(n)
     * @param arr 数组
     * @param k 累加和上限
     * @return 最长子数组长度
     */
    private static int maxLength(int[] arr, int k) {
        int n = arr.length;
        // sums[i]表示从位置i开始向右的最小累加和
        int[] sums = new int[n];
        // ends[i]表示从位置i开始达到最小累加和的结束位置
        int[] ends = new int[n];
        
        // 从右向左预处理
        sums[n - 1] = arr[n - 1];
        ends[n - 1] = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            if (sums[i + 1] < 0) {
                // 如果右边的累加和为负，可以扩展
                sums[i] = arr[i] + sums[i + 1];
                ends[i] = ends[i + 1];
            } else {
                // 否则只取当前位置
                sums[i] = arr[i];
                ends[i] = i;
            }
        }
        
        // 滑动窗口求解
        int end = 0;
        int sum = 0;
        int res = 0;
        for (int i = 0; i < n; i++) {
            // 尽可能向右扩展end指针
            while (end < n && sum + sums[end] <= k) {
                sum += sums[end];
                end = ends[end] + 1;
            }
            res = Math.max(res, end - i);
            // 移动左指针i
            if (end > i) {
                sum -= arr[i];
            } else {
                end = i + 1;
            }
        }
        return res;
    }

    /**
     * 最优解法：滑动窗口
     * 时间复杂度：O(n)
     * @param arr 原数组（会被修改）
     * @param v 平均值上限
     * @return 最长子数组长度
     */
    public static int ways3(int[] arr, int v) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        // 将每个元素减去v，转化问题
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= v;
        }
        return maxLength(arr, 0);
    }

    /**
     * 生成随机数组用于测试
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int len = (int) ((maxLen + 1) * Math.random()) + 1;
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    /**
     * 复制数组
     * @param arr 原数组
     * @return 复制的数组
     */
    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    /**
     * 打印数组
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    /**
     * 测试方法，验证三种算法的正确性
     */
    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 20;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int val = (int) ((maxVal + 1) * Math.random());
            // 复制数组用于不同算法测试
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] arr3 = copy(arr);
            int ans1 = ways1(arr1, val);
            int ans2 = ways2(arr2, val);
            int ans3 = ways3(arr3, val);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
            }
        }
        System.out.println("test end");
    }
}