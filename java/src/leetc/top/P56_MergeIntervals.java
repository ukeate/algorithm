package leetc.top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class P56_MergeIntervals {
    public static class Range {
        public int s;
        public int e;

        public Range(int start, int end) {
            s = start;
            e = end;
        }
    }

    public static class Comp implements Comparator<Range> {
        @Override
        public int compare(Range o1, Range o2) {
            return o1.s - o2.s;
        }
    }

    private static int[][] transfer(ArrayList<Range> list) {
        int[][] matrix = new int[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            matrix[i] = new int[]{list.get(i).s, list.get(i).e};
        }
        return matrix;
    }

    public int[][] merge(int[][] intervals) {
        Range[] arr = new Range[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            arr[i] = new Range(intervals[i][0], intervals[i][1]);
        }
        Arrays.sort(arr, new Comp());
        ArrayList<Range> ans = new ArrayList<>();
        if (arr.length == 0) {
            return new int[0][0];
        }
        int s = arr[0].s, e = arr[0].e;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].s > e) {
                ans.add(new Range(s, e));
                s = arr[i].s;
                e = arr[i].e;
            } else {
                e = Math.max(e, arr[i].e);
            }
        }
        ans.add(new Range(s, e));
        return transfer(ans);
    }
}
