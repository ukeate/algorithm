package basic.c34;

import java.util.Arrays;
import java.util.Comparator;

// 信封有长宽，返回最大嵌套层数
public class Envelopes {
    private static class Envelope {
        public int w;
        public int h;
        public Envelope(int width, int height) {
            w = width;
            h = height;
        }
    }

    public static class Comp implements Comparator<Envelope> {
        @Override
        public int compare(Envelope o1, Envelope o2) {
            return o1.w != o2.w ? o1.w - o2.w : o2.h - o1.h;
        }
    }

    private static Envelope[] sort(int[][] matrix) {
        Envelope[] res = new Envelope[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            res[i] = new Envelope(matrix[i][0], matrix[i][1]);
        }
        Arrays.sort(res, new Comp());
        return res;
    }

    public static int max(int[][] matrix) {
        Envelope[] es = sort(matrix);
        int[] ends = new int[matrix.length];
        ends[0] = es[0].h;
        int endR = 0;
        int l = 0, r = 0, m = 0;
        for (int i = 1; i < es.length; i++) {
            l = 0;
            r = endR;
            while (l <= r) {
                m = (l + r) / 2;
                if (es[i].h > ends[m]) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
            endR = Math.max(endR, l);
            ends[l] = es[i].h;
        }
        return endR + 1;
    }

    public static void main(String[] args) {
        int[][] m = {{3, 4}, {2, 3}, {4, 5}, {1, 3}, {2, 2}, {3, 6}, {1, 2}, {3, 2}, {2, 4}};
        System.out.println(max(m));
    }
}
