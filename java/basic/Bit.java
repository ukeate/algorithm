package basic;

import java.util.HashSet;
import java.util.Set;

public class Bit {

    //

    public static void print(int num) {
        for (int i = 31; i >= 0; i--) {
            System.out.print((num & (i << i)) == 0 ? "0" : "1");
        }
    }

    //

    public static class Bitmap {
        private long[] bits;

        public Bitmap(int max) {
            this.bits = new long[((max >> 6) + 1)];
        }

        public void add(int num) {
            bits[num >> 6] |= (1L << (num & 63));
        }

        public void delete(int num) {
            bits[num >> 6] &= ~(1L << (num & 63));
        }

        public boolean contains(int num) {
            return (bits[num >> 6] & (1L << (num & 63))) != 0;
        }
    }

    private static void testBitmap() {
        System.out.println("testing Bitmap");
        int max = 10000;
        Bitmap bitmap = new Bitmap(max);
        Set<Integer> set = new HashSet<>();
        int testTimes = (int) 1e8;
        for (int i = 0; i < testTimes; i++) {
            int num = (int) (Math.random() * (max + 1));
            double decide = Math.random();
            if (decide < 0.333) {
                bitmap.add(num);
                set.add(num);
            } else if (decide < 0.666) {
                bitmap.delete(num);
                set.remove(num);
            } else {
                if (bitmap.contains(num) != set.contains(num)) {
                    System.out.println("Oops");
                    break;
                }
            }
        }
        for (int num = 0; num <= max; num++) {
            if (bitmap.contains(num) != set.contains(num)) {
                System.out.println("Oops");
            }
        }
        System.out.println("testing Bitmap over");
    }

    //

    public static int add(int a, int b) {
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

    public static int minus(int a, int b) {
        return add(a, negNum(b));
    }

    public static int multi(int a, int b) {
        int res = 0;
        while (b != 0) {
            if ((b & 1) != 0) {
                res = add(res, a);
            }
            a <<= 1;
            b >>>= 1;
        }
        return res;
    }

    private static boolean isNeg(int n) {
        return n < 0;
    }

    public static int div(int a, int b) {
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

    public static int divide(int a, int b) {
        if (a == Integer.MIN_VALUE && b == Integer.MIN_VALUE) {
            return 1;
        } else if (b == Integer.MIN_VALUE) {
            return 0;
        } else if (a == Integer.MIN_VALUE) {
            int c = div(add(a, 1), b);
            return add(c, div(minus(a, multi(c, b)), b));
        } else {
            return div(a, b);
        }
    }

    public static void main(String[] args) {
    }
}