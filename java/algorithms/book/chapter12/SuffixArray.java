package chapter12;

/**
 * Created by outrun on 6/12/16.
 */
public class SuffixArray {

    public static void makeLCPArray(int[] s, int[] sa, int[] LCP) {
        int N = sa.length;
        int[] rank = new int[N];

        s[N] = -1;
        for (int i = 0; i < N; i++) {
            rank[sa[i]] = i;
        }

        int h = 0;
        for (int i = 0; i < N; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];

                while (s[i + h] == s[j + h]) {
                    h++;
                }

                LCP[rank[i]] = h;
                if (h > 0) {
                    h--;
                }
            }
        }

    }

    public static void createSuffixArray(String str, int[] sa, int[] LCP) {
        int N = str.length();

        int[] s = new int[N + 3];
        int[] SA = new int[N + 3];

        for (int i = 0; i < N; i++) {
            s[i] = str.charAt(i);
        }

        makeSuffixArray(s, SA, N, 256);

        for (int i = 0; i < N; i++) {
            sa[i] = SA[i];
        }

        makeLCPArray(s, sa, LCP);
    }

    public static void makeSuffixArray(int[] s, int[] SA, int n, int K) {
        int n0 = (n + 2) / 3;
        int n1 = (n + 1) / 3;
        int n2 = n / 3;
        int t = n0 - n1;
        int n12 = n1 + n2 + t;

        int[] s12 = new int[n12 + 3];
        int[] SA12 = new int[n12 + 3];
        int[] s0 = new int[n0];
        int[] SA0 = new int[n0];

        for(int i = 0, j = 0; i < n + t; i++) {
            if (i % 3 != 0) {
                s12[j++] = i;
            }
        }

        int K12 = assignNames(s, s12, SA12, n0, n12, K);

        computeS12(s12, SA12, n12, K12);
        computeS0(s, s0, SA0, SA12, n0, n12, K);
        merge(s, s12, SA, SA0, SA12, n, n0, n12, t);
    }

    private static int assignNames(int[] s, int[] s12, int[] SA12, int n0, int n12, int K){
        return 0;
    }

    private static void merge(int[] s, int[] s12, int[] sa, int[] sa0, int[] sa12, int n, int n0, int n12, int t) {
    }

    private static void computeS0(int[] s, int[] s0, int[] sa0, int[] sa12, int n0, int n12, int k) {

    }

    private static void computeS12(int[] s12, int[] sa12, int n12, int k12) {

    }

}
