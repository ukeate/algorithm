package leetc.top;

public class P279_PerfectSquares {
    public static int numSquares1(int n) {
        int res = n, num = 2;
        while (num * num <= n) {
            int a = n / (num * num), b = n % (num * num);
            res = Math.min(res, a + numSquares1(b));
            num++;
        }
        return res;
    }

    //

    /**
     * 规律:
     * 个数不超过4
     * 1: 1, 4, 9, 16, 25 ...
     * 4: n % 8 == 7, 或 n消去4因子后剩下rest, rest % 8 == 7
     */
    public static int numSquares2(int n) {
        int rest = n;
        while (rest % 4 == 0) {
            rest /= 4;
        }
        if (rest % 8 == 7) {
            return 4;
        }
        int f = (int) Math.sqrt(n);
        if (f * f == n) {
            return 1;
        }
        for (int first = 1; first * first <= n; first++) {
            int second = (int) Math.sqrt(n - first * first);
            if (first * first + second * second == n) {
                return 2;
            }
        }
        return 3;
    }

    public static void main(String[] args) {
        for (int i = 1; i < 1000; i++) {
            System.out.println(i + "," + numSquares1(i));
        }
    }
}
