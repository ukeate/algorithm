package leetc.top;

import java.util.Arrays;
import java.util.Comparator;

public class P354_RussianDollEnvelopes {
    private static class Envelope {
        public int w;
        public int h;
        public Envelope(int width, int height) {
            w = width;
            h = height;
        }
    }

    private static class Comp implements Comparator<Envelope> {
        @Override
        public int compare(Envelope o1, Envelope o2) {
            return o1.w != o2.w ? o1.w - o2.w : o2.h - o1.h;
        }
    }

    private static Envelope[] sort(int[][] matrix) {
        Envelope[] res = new Envelope[matrix.length];
        for (int i =0; i < matrix.length; i++) {
            res[i] = new Envelope(matrix[i][0], matrix[i][1]);
        }
        Arrays.sort(res, new Comp());
        return res;
    }

    public static int maxEnvelopes(int[][] matrix) {
        Envelope[] arr = sort(matrix);
        int[] ends = new int[matrix.length];
        ends[0] = arr[0].h;
        int right = 0, l = 0, r = 0, m = 0;
        for (int i = 1; i < arr.length; i++) {
            l = 0;
            r = right;
            while (l <= r) {
                m = (l + r) / 2;
                if (arr[i].h > ends[m]) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
            right = Math.max(right, l);
            ends[l] = arr[i].h;
        }
        return right + 1;
    }
}
