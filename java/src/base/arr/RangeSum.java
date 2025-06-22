package base.arr;

/**
 * 数组区间和查询
 * 使用前缀和数组实现O(1)时间复杂度的区间和查询
 * 预处理时间复杂度：O(n)，查询时间复杂度：O(1)
 */
public class RangeSum {
    /**
     * 前缀和数据结构
     * 核心思想：preSum[i] = arr[0] + arr[1] + ... + arr[i]
     * 区间和[l,r] = preSum[r] - preSum[l-1]
     */
    public static class PreSum {
        // 前缀和数组，preSum[i]表示原数组[0...i]的累加和
        private int[] preSum;

        /**
         * 构造函数，根据原数组构建前缀和数组
         * @param arr 原数组
         */
        public PreSum(int[] arr) {
            if (arr == null || arr.length < 1) {
                return;
            }

            int n = arr.length;
            this.preSum = new int[n];
            // 第一个位置直接赋值
            preSum[0] = arr[0];
            // 从第二个位置开始，每个位置都是前一个位置的前缀和加上当前元素
            for (int i = 1; i < n; i++) {
                preSum[i] = preSum[i - 1] + arr[i];
            }
        }

        /**
         * 查询区间[l, r]的累加和
         * @param l 左边界（包含）
         * @param r 右边界（包含）
         * @return 区间[l, r]的累加和
         */
        public int rangeSum(int l, int r) {
            if (this.preSum == null) {
                return 0;
            }
            // 如果左边界为0，直接返回preSum[r]
            // 否则返回preSum[r] - preSum[l-1]
            return l == 0 ? preSum[r] : preSum[r] - preSum[l - 1];
        }
    }

    /**
     * 暴力方法计算区间和，用于对比验证
     * 时间复杂度：O(n)
     * @param arr 原数组
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l, r]的累加和
     */
    private static int rangeSumSure(int[] arr, int l, int r) {
        if (arr == null || l > r || arr.length - 1 < r) {
            return 0;
        }
        int sum = 0;
        // 直接遍历累加
        for (int i = l; i <= r; i++) {
            sum += arr[i];
        }
        return sum;
    }

    /**
     * 生成随机数组用于测试
     * @param ms 最大长度
     * @param mv 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int ms, int mv) {
        int[] arr = new int[(int) (Math.random() * (ms + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (mv + 1));
        }
        return arr;
    }

    /**
     * 判断两个数组是否相等
     * @param arr1 数组1
     * @param arr2 数组2
     * @return 是否相等
     */
    private static boolean equal(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if ((arr1 == null ^ arr2 == null)) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测试方法，验证前缀和实现的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            // 生成随机数组和随机查询区间
            int[] arr = randomArr(maxLen, maxVal);
            int i1 = (int) (Math.random() * (arr.length));
            int i2 = (int) (Math.random() * (arr.length));
            int l = Math.min(i1, i2);
            int r = Math.max(i1, i2);
            // 对比两种方法的结果
            int ans1 = rangeSumSure(arr, l, r);
            PreSum ps = new PreSum(arr);
            int ans2 = ps.rangeSum(l, r);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
