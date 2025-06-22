package base.dp4_quad;

/**
 * 范围分割问题
 * 对于数组的每个前缀范围[0..i]，找到最优的分割点，使得分割后较小部分的和最大
 * 返回每个范围的最优分割结果
 * 
 * 提供三种解法：
 * 1. 暴力枚举 O(n³)
 * 2. 前缀和优化 O(n²)  
 * 3. 利用最优分割点单调性优化 O(n)
 */
public class BestSplitRange {
    /**
     * 方法1：暴力枚举所有分割点
     * 对每个范围重新计算左右两部分的和
     * 时间复杂度O(n³)
     */
    public static int[] split1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        int[] ans = new int[n];
        ans[0] = 0; // 单个元素无法分割
        
        // 对每个范围[0..range]找最优分割点
        for (int range = 1; range < n; range++) {
            for (int s = 0; s < range; s++) {
                // 计算左半部分的和
                int sumL = 0;
                for (int l = 0; l <= s; l++) {
                    sumL += arr[l];
                }
                // 计算右半部分的和
                int sumR = 0;
                for (int r = s + 1; r <= range; r++) {
                    sumR += arr[r];
                }
                // 更新当前范围的最优值
                ans[range] = Math.max(ans[range], Math.min(sumL, sumR));
            }
        }
        return ans;
    }

    //

    /**
     * 辅助方法：计算前缀和数组中[l,r]的和
     */
    private static int sum(int[] sum, int l, int r) {
        return sum[r + 1] - sum[l];
    }

    /**
     * 方法2：使用前缀和优化
     * 预处理前缀和数组，避免重复计算区间和
     * 时间复杂度O(n²)
     */
    public static int[] split2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        int[] ans = new int[n];
        ans[0] = 0;
        
        // 构建前缀和数组
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + arr[i];
        }
        
        // 对每个范围找最优分割点
        for (int range = 1; range < n; range++) {
            for (int s = 0; s < range; s++) {
                int sumL = sum(sum, 0, s);       // 左半部分和
                int sumR = sum(sum, s + 1, range); // 右半部分和
                ans[range] = Math.max(ans[range], Math.min(sumL, sumR));
            }
        }
        return ans;
    }

    //

    /**
     * 方法3：利用最优分割点的单调性优化
     * 关键观察：随着范围的扩大，最优分割点具有单调性
     * 时间复杂度O(n)
     */
    public static int[] split3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        int[] ans = new int[n];
        ans[0] = 0;
        
        // 构建前缀和数组
        int[] sums = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + arr[i];
        }
        
        int best = 0; // 当前最优分割点
        
        for (int range = 1; range < n; range++) {
            // 利用单调性：检查是否需要右移分割点
            while (best + 1 < range) {
                // 比较当前分割点和下一个分割点的效果
                int before = Math.min(sum(sums, 0, best), sum(sums, best + 1, range));
                int after = Math.min(sum(sums, 0, best + 1), sum(sums, best + 2, range));
                
                if (before <= after) {
                    best++; // 分割点右移
                } else {
                    break; // 找到最优分割点
                }
            }
            // 记录当前范围的最优结果
            ans[range] = Math.min(sum(sums, 0, best), sum(sums, best + 1, range));
        }
        return ans;
    }

    /**
     * 生成随机数组用于测试
     */
    private static int[] randomArr(int len, int maxVal) {
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    /**
     * 比较两个数组是否相等
     */
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }
        int n = arr1.length;
        for (int i = 0 ; i < n; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测试方法，验证各个算法的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 20;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) ((maxLen + 1) * Math.random());
            int[] arr = randomArr(len, maxVal);
            int[] ans1 = split1(arr);
            int[] ans2 = split2(arr);
            int[] ans3 = split3(arr);
            if (!isEqual(ans1, ans2) || !isEqual(ans1, ans3)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
