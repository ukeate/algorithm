package base.dp4_quad;

// 划分数组，使小的部分最大
/**
 * 数组划分问题
 * 将数组划分为两部分，使得较小部分的和最大
 * 也就是让两部分的和尽可能接近，返回较小部分的和
 */
public class BestSplit {
    /**
     * 方法1：暴力枚举所有分割点
     * 时间复杂度O(n²)
     */
    public static int split1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int ans = 0;
        // 枚举所有可能的分割点
        for (int s = 0; s < n - 1; s++) {
            // 计算左半部分的和
            int sumL = 0;
            for (int l = 0; l <= s; l++) {
                sumL += arr[l];
            }
            // 计算右半部分的和
            int sumR = 0;
            for (int r = s + 1; r < n; r++) {
                sumR += arr[r];
            }
            // 更新答案为较小部分的最大值
            ans = Math.max(ans, Math.min(sumL, sumR));
        }
        return ans;
    }

    /**
     * 方法2：优化版本，使用前缀和避免重复计算
     * 时间复杂度O(n)
     */
    public static int split2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        // 计算数组总和
        int sumAll = 0;
        for (int num : arr) {
            sumAll += num;
        }
        int ans = 0;
        int sumL = 0;
        // 枚举所有分割点
        for (int s = 0; s < n - 1; s++) {
            sumL += arr[s];           // 左半部分累计和
            int sumR = sumAll - sumL; // 右半部分和 = 总和 - 左半部分和
            ans = Math.max(ans, Math.min(sumL, sumR));
        }
        return ans;
    }

    /**
     * 生成随机数组用于测试
     */
    public static int[] randomArr(int len, int maxVal) {
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    /**
     * 测试方法，验证两种算法的正确性
     */
    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 20;
        int maxVal =  30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
           int len = (int) (Math.random() * (maxLen + 1));
           int[] arr = randomArr(len, maxVal);
           int ans1 = split1(arr);
           int ans2 = split2(arr);
           if (ans1 != ans2) {
               System.out.println("Wrong");
               break;
           }
        }
        System.out.println("test end");
    }

}
