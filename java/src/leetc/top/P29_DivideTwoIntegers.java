package leetc.top;

public class P29_DivideTwoIntegers {
    private static int add(int a, int b) {
        int sum = a;
        while (b != 0) {
            sum = a ^ b;
            b = (a & b) << 1;
            a = sum;
        }
        return sum;
    }

    private static int negNum(int n) {
        return add(~n, 1);
    }

    private static int minus(int a, int b) {
        return add(a, negNum(b));
    }

    private static int multi(int a, int b) {
        int ans = 0;
        while (b != 0) {
            if ((b & 1) != 0) {
                ans = add(ans, a);
            }
            a <<= 1;
            b >>>= 1;
        }
        return ans;
    }

    private static boolean isNeg(int n) {
        return n < 0;
    }

    private static int div(int a, int b) {
        int x = isNeg(a) ? negNum(a) : a;
        int y = isNeg(b) ? negNum(b) : b;
        int res = 0;
        for (int i = 30; i >= 0; i = minus(i, 1)) {
            if ((x >> i) >= y) {
                res |= (1 << i);
                x = minus(x, y << i);
            }
        }
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }

    public static int divide(int dividend, int divisor) {
        if (divisor == Integer.MIN_VALUE) {
            return dividend == Integer.MIN_VALUE ? 1 : 0;
        }
        if (dividend == Integer.MIN_VALUE) {
            if (divisor == negNum(1)) {
                return Integer.MAX_VALUE;
            }
            int res = div(add(dividend, 1), divisor);
            return add(res, div(minus(dividend, multi(res, divisor)), divisor));
        }
        return div(dividend, divisor);
    }

    //

    private static String printNumBinary(int num) {
        StringBuilder builder = new StringBuilder();
        for (int i = 31; i >= 0; i--) {
            builder.append(((num >> i) & 1) == 0 ? '0' : '1');
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(divide(Integer.MIN_VALUE, -3));
    }
}
