package giant.c4;

// https://leetcode-cn.com/problems/max-submatrix-lcci/
public class SubMatrixMaxSum {

    public static int[] getMaxMatrix(int[][] m) {
        int ln = m.length;
        int lm = m[0].length;
        int max = Integer.MIN_VALUE;
        int a = 0,b = 0, c = 0, d = 0;
        for (int i = 0; i < ln; i++) {
            int[] s = new int[lm];
            for (int j = i; j < ln; j++) {
                int cur = 0, begin = 0;
                for (int k = 0; k < lm; k++) {
                    s[k] += m[j][k];
                    cur += s[k];
                    if (max < cur) {
                        max = cur;
                        a = i;
                        b = begin;
                        c = j;
                        d = k;
                    }
                    if (cur < 0) {
                        cur = 0;
                        begin = k + 1;
                    }
                }
            }
        }
        return new int[] {a, b, c, d};
    }

}
