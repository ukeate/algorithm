package giant.c38;

// 来自字节
// 给定两个数a和b
// 第1轮，把1选择给a或者b
// 第2轮，把2选择给a或者b
// ...
// 第i轮，把i选择给a或者b
// 想让a和b的值一样大，请问至少需要多少轮？
public class FillGapMinStep {
    private static int process(int a, int b, int i, int n) {
        if (i > n) {
            return Integer.MAX_VALUE;
        }
        if (a + i == b || a == b + i) {
            return i;
        }
        return Math.min(process(a + i, b, i + 1, n), process(a, b + i, i + 1, n));
    }

    public static int minStep0(int a, int b) {
        if (a == b) {
            return 0;
        }
        int limit = 15;
        return process(a, b, 1, limit);
    }

    //

    // (sum - s) >= 0 的最小偶数
    public static int minStep1(int a, int b) {
        if (a == b) {
            return 0;
        }
        int s = Math.abs(a - b);
        int num = 1, sum = 0;
        for (; !(sum >= s && (sum - s) % 2 == 0); num++) {
            sum += num;
        }
        return num - 1;
    }

    //

    private static int best(int s2) {
        int l = 0, r = 1;
        for (; r * (r + 1) < s2;) {
            l = r;
            r *= 2;
        }
        int ans = 0;
        while (l <= r) {
            int m = (l + r) / 2;
            if (m * (m + 1) >= s2) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }

    public static int minStep2(int a, int b) {
        if (a == b) {
            return 0;
        }
        int s = Math.abs(a - b);
        int begin = best(s << 1);
        // 奇偶性最多3步
        for (; (begin * (begin + 1) / 2 - s) % 2 != 0;) {
            begin++;
        }
        return begin;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int a = 1; a < 100; a++) {
            for (int b = 1; b < 100; b++) {
                int ans1 = minStep0(a, b);
                int ans2 = minStep1(a, b);
                int ans3 = minStep2(a, b);
                if (ans1 != ans2 || ans1 != ans3) {
                    System.out.println("Wrong");
                    break;
                }
            }
        }
        System.out.println("test end");

        int a = 19019;
        int b = 8439284;
        int ans2 = minStep1(a, b);
        int ans3 = minStep2(a, b);
        System.out.println(ans2);
        System.out.println(ans3);
    }
}
