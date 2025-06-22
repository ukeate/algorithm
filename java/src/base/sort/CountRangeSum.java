package base.sort;

/**
 * 区间和的个数问题 - 归并排序的高级应用
 * 
 * 问题描述：
 * 给定一个整数数组nums，返回区间和在[lower, upper]之间的个数，包含lower和upper
 * 区间和S(i, j)表示在nums中，位置从i到j的元素之和，包含i和j(i ≤ j)
 * 
 * 例如：nums = [-2,5,-1], lower = -2, upper = 2
 * 区间和有：
 * - S(0,0) = -2 (在范围内)
 * - S(0,1) = 3 (不在范围内)
 * - S(0,2) = 2 (在范围内)
 * - S(1,1) = 5 (不在范围内)
 * - S(1,2) = 4 (不在范围内)
 * - S(2,2) = -1 (在范围内)
 * 结果：3个区间和在范围内
 * 
 * 解决思路：
 * 1. 计算前缀和数组，将区间和问题转化为前缀和差值问题
 * 2. 对于区间[i,j]，其和等于preSum[j+1] - preSum[i]
 * 3. 问题转化为：对于每个j，有多少个i满足 lower ≤ preSum[j+1] - preSum[i] ≤ upper
 * 4. 即：preSum[j+1] - upper ≤ preSum[i] ≤ preSum[j+1] - lower
 * 5. 使用归并排序在合并过程中统计满足条件的区间数量
 * 
 * 算法原理：
 * 在归并排序的合并过程中，利用左右两部分已经有序的特性
 * 对于右半部分的每个元素，在左半部分中找出满足条件的元素个数
 * 
 * 时间复杂度：O(N*logN) - 归并排序的时间复杂度
 * 空间复杂度：O(N) - 前缀和数组和归并排序的辅助空间
 * 
 * 应用场景：
 * - LeetCode 327. Count of Range Sum
 * - 区间统计问题
 * - 分治算法的高级应用
 */
public class CountRangeSum {
    
    /**
     * 归并过程中统计满足条件的区间和个数
     * 
     * 算法详解：
     * 1. 对于右半部分的每个元素preSum[j]，在左半部分找满足条件的preSum[i]
     * 2. 条件：preSum[j] - upper ≤ preSum[i] ≤ preSum[j] - lower
     * 3. 由于左半部分已经有序，可以用双指针快速找到满足条件的区间
     * 
     * 举例说明：
     * 左半部分：[-2, 1, 3]，右半部分：[0, 2, 4]
     * lower = -1, upper = 1
     * 对于右半部分的0：需要在左半部分找 -1 ≤ preSum[i] ≤ 1，找到[-2, 1]，贡献2
     * 对于右半部分的2：需要在左半部分找 1 ≤ preSum[i] ≤ 3，找到[1, 3]，贡献2
     * 对于右半部分的4：需要在左半部分找 3 ≤ preSum[i] ≤ 5，找到[3]，贡献1
     * 
     * @param sums 前缀和数组
     * @param l 左边界
     * @param m 中点
     * @param r 右边界
     * @param lower 下界
     * @param upper 上界
     * @return 跨越左右两部分的满足条件的区间和个数
     */
    public static int merge(long[] sums, int l, int m, int r, int lower, int upper) {
        int ans = 0;
        int windowL = l;  // 左窗口指针
        int windowR = l;  // 右窗口指针
        
        // 对于右半部分的每个元素，统计左半部分中满足条件的元素个数
        for (int i = m + 1; i <= r; i++) {
            long min = sums[i] - upper;  // 左半部分元素的最小值
            long max = sums[i] - lower;  // 左半部分元素的最大值
            
            // 找到第一个 >= min 的位置
            while (windowL <= m && sums[windowL] < min) {
                windowL++;
            }
            
            // 找到第一个 > max 的位置
            while (windowR <= m && sums[windowR] <= max) {
                windowR++;
            }
            
            // [windowL, windowR) 区间内的元素都满足条件
            ans += windowR - windowL;
        }
        
        // 标准的归并排序合并过程
        long[] help = new long[r - l + 1];
        int i = 0;
        int p1 = l;      // 左半部分指针
        int p2 = m + 1;  // 右半部分指针
        
        // 合并两个有序部分
        while (p1 <= m && p2 <= r) {
            help[i++] = sums[p1] <= sums[p2] ? sums[p1++] : sums[p2++];
        }
        
        // 处理剩余元素
        while (p1 <= m) {
            help[i++] = sums[p1++];
        }
        while (p2 <= r) {
            help[i++] = sums[p2++];
        }
        
        // 将合并结果复制回原数组
        for (i = 0; i < help.length; i++) {
            sums[l + i] = help[i];
        }
        
        return ans;
    }

    /**
     * 递归处理函数 - 分治求解满足条件的区间和个数
     * 
     * 分治思想：
     * 1. 递归边界：只有一个元素时，检查该元素是否在范围内
     * 2. 分解：将数组分为左右两部分
     * 3. 解决：递归计算左半部分和右半部分的满足条件的区间和个数
     * 4. 合并：计算跨越左右两部分的满足条件的区间和个数
     * 
     * @param sums 前缀和数组
     * @param l 左边界
     * @param r 右边界
     * @param lower 下界
     * @param upper 上界
     * @return 区间[l,r]内满足条件的区间和个数
     */
    public static int process(long[] sums, int l, int r, int lower, int upper) {
        if (l == r) {
            // 只有一个元素，检查该元素是否在范围内
            // 注意：sums[0] = 0，所以sums[i]表示原数组前i个元素的和
            return sums[l] >= lower && sums[l] <= upper ? 1 : 0;
        }
        
        // 计算中点，避免整数溢出
        int mid = l + ((r - l) >> 1);
        
        // 分治：左半部分 + 右半部分 + 跨越部分
        return process(sums, l, mid, lower, upper) +
               process(sums, mid + 1, r, lower, upper) +
               merge(sums, l, mid, r, lower, upper);
    }

    /**
     * 区间和个数主函数
     * 
     * 算法步骤：
     * 1. 构建前缀和数组，sums[i]表示原数组前i个元素的和
     * 2. sums[0] = 0，便于计算区间和
     * 3. 区间[i,j]的和 = sums[j+1] - sums[i]
     * 4. 使用归并排序统计满足条件的区间和个数
     * 
     * @param nums 原数组
     * @param lower 下界
     * @param upper 上界
     * @return 满足条件的区间和个数
     */
    public static int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 构建前缀和数组
        long[] sums = new long[nums.length + 1];
        sums[0] = 0;  // 前0个元素的和为0
        for (int i = 0; i < nums.length; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        
        // 使用归并排序统计满足条件的区间和个数
        return process(sums, 0, sums.length - 1, lower, upper);
    }

    /**
     * 暴力解法 - 用于验证算法正确性
     * 
     * 直接枚举所有可能的区间，检查区间和是否在范围内
     * 时间复杂度：O(N²)
     * 
     * @param nums 数组
     * @param lower 下界
     * @param upper 上界
     * @return 满足条件的区间和个数
     */
    private static int countRangeSumSure(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int ans = 0;
        // 枚举所有可能的区间[i,j]
        for (int i = 0; i < nums.length; i++) {
            long sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                // 检查区间[i,j]的和是否在范围内
                if (sum >= lower && sum <= upper) {
                    ans++;
                }
            }
        }
        return ans;
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大数组长度
     * @param maxVal 数组元素的最大绝对值
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
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 区间和个数问题测试 ===");
        
        // 手工测试用例
        int[] testArr = {-2, 5, -1};
        int lower = -2;
        int upper = 2;
        System.out.println("测试数组：" + java.util.Arrays.toString(testArr));
        System.out.println("范围：[" + lower + ", " + upper + "]");
        
        int result1 = countRangeSum(testArr, lower, upper);
        int result2 = countRangeSumSure(testArr, lower, upper);
        
        System.out.println("归并排序解法：" + result1);
        System.out.println("暴力解法：" + result2);
        System.out.println("结果正确：" + (result1 == result2));
        
        // 详细计算过程展示
        System.out.println("\n详细计算过程：");
        System.out.println("数组：[-2, 5, -1]，范围：[-2, 2]");
        System.out.println("所有区间和：");
        System.out.println("S(0,0) = -2 (在范围内)");
        System.out.println("S(0,1) = -2+5 = 3 (不在范围内)");
        System.out.println("S(0,2) = -2+5-1 = 2 (在范围内)");
        System.out.println("S(1,1) = 5 (不在范围内)");
        System.out.println("S(1,2) = 5-1 = 4 (不在范围内)");
        System.out.println("S(2,2) = -1 (在范围内)");
        System.out.println("总计：3个区间和在范围内");
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试 ===");
        int times = 10000;  // 减少测试次数，因为暴力解法较慢
        int maxLen = 50;
        int maxVal = 100;
        System.out.println("测试次数：" + times);
        System.out.println("最大数组长度：" + maxLen);
        System.out.println("最大元素绝对值：" + maxVal);
        
        boolean success = true;
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int testLower = (int) (Math.random() * 200) - 100;
            int testUpper = testLower + (int) (Math.random() * 100);
            
            int ans1 = countRangeSum(arr, testLower, testUpper);
            int ans2 = countRangeSumSure(arr, testLower, testUpper);
            
            if (ans1 != ans2) {
                System.out.println("第" + (i + 1) + "次测试失败！");
                System.out.println("范围：[" + testLower + ", " + testUpper + "]");
                System.out.println("归并排序结果：" + ans1);
                System.out.println("暴力解法结果：" + ans2);
                System.out.print("测试数组：");
                print(arr);
                success = false;
                break;
            }
        }
        
        if (success) {
            System.out.println("所有测试通过！区间和个数算法实现正确。");
        } else {
            System.out.println("测试失败！请检查算法实现。");
        }
        
        System.out.println("=== 测试结束 ===");
    }
}
