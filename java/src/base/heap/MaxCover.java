package base.heap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

// 线段最大重叠高度
public class MaxCover {
    private static class Line {
        public int start;
        public int end;

        public Line(int s, int e) {
            start = s;
            end = e;
        }
    }

    public static class StartComparator implements Comparator<Line> {
        @Override
        public int compare(Line o1, Line o2) {
            return o1.start - o2.start;
        }

    }

    public static int maxCover(int[][] m) {
        Line[] lines = new Line[m.length];
        for (int i = 0; i < m.length; i++) {
            lines[i] = new Line(m[i][0], m[i][1]);
        }
        Arrays.sort(lines, new StartComparator());
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int max = 0;
        for (int i = 0; i < lines.length; i++) {
            while (!heap.isEmpty() && heap.peek() <= lines[i].start) {
                heap.poll();
            }
            heap.add(lines[i].end);
            max = Math.max(max, heap.size());
        }
        return max;
    }

    public static int maxCoverSure(int[][] lines) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < lines.length; i++) {
            min = Math.min(min, lines[i][0]);
            max = Math.max(max, lines[i][1]);
        }
        int cover = 0;
        for (double p = min + 0.5; p < max; p += 1) {
            int cur = 0;
            for (int i = 0; i < lines.length; i++) {
                if (lines[i][0] < p && lines[i][1] > p) {
                    cur++;
                }
            }
            cover = Math.max(cover, cur);
        }
        return cover;
    }

    private static int[][] randomLines(int n, int l, int r) {
        int size = (int) ((n + 1) * Math.random()) + 1;
        int[][] res = new int[size][2];
        for (int i = 0; i < size; i++) {
            int a = l + (int) ((r - l + 1) * Math.random());
            int b = l + (int) ((r - l + 1) * Math.random());
            if (a == b) {
                b = a + 1;
            }
            res[i][0] = Math.min(a, b);
            res[i][1] = Math.max(a, b);
        }
        return res;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int l = 0;
        int r = 200;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[][] lines = randomLines(maxLen, l, r);
            int ans1 = maxCover(lines);
            int ans2 = maxCoverSure(lines);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
