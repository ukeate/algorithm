package leetc.top;

import java.util.Arrays;

public class P204_CountPrimes {
    public static int countPrimes(int n) {
        if (n < 3) {
            return 0;
        }
        boolean[] f = new boolean[n];
        int count = n / 2;
        for (int i = 3; i * i < n; i += 2) {
            if (f[i]) {
                continue;
            }
            // +i为w
            for (int j = i * i; j < n; j += 2 * i) {
                if (!f[j]) {
                    --count;
                    f[j] = true;
                }
            }
        }
        return count;
    }

    //

    public static int countPrimes2(int n) {
        if (n < 3) {
            return 0;
        }
        boolean[] f = new boolean[n];
        int[] prime = new int[n];
        int cnt = 0;
        int ans = n - 2;
        for (int i = 2; i < n; i++) {
            if (!f[i]) {
                prime[cnt++] = i;
            }
            for (int j = 0; j < cnt && prime[j] * i < n; j++) {
                f[prime[j] * i] = true;
                ans--;
                if (i % prime[j] == 0) {
                    break;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(countPrimes(20));
        System.out.println(countPrimes2(20));
    }
}
