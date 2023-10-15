package leetc.top;

public class P483_SmallestGoodBase {
    public static String smallestGoodBase(String n) {
        long num = Long.valueOf(n);
        // 尝试m位
        for (int m = (int) (Math.log(num + 1) / Math.log(2)); m > 2; m--) {
            // k进制, sqrt(num, m - 1) < k <= sqrt(num, m)
            long l = (long) (Math.pow(num, 1.0 / m));
            long r = (long) (Math.pow(num, 1.0 / (m - 1))) + 1L;
            // 二分查找k
            while (l <= r) {
                long k = l + ((r - l) >> 1);
                long sum = 0L;
                long base = 1L;
                for (int i = 0; i < m && sum <= num; i++) {
                    sum += base;
                    base *= k;
                }
                if (sum < num) {
                    l = k + 1;
                } else if (sum > num) {
                    r = k - 1;
                } else {
                    return String.valueOf(k);
                }
            }
        }
        return String.valueOf(num - 1);
    }
}
