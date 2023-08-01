package leetc.top;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class P253_MeetingRoomsII {
    private static class Line {
        public int start;
        public int end;
        public Line(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private static class StartComp implements Comparator<Line> {
        @Override
        public int compare(Line o1, Line o2) {
            return o1.start - o2.start;
        }
    }

    private static class EndComp implements Comparator<Line> {
        @Override
        public int compare(Line o1, Line o2) {
            return o1.end - o2.end;
        }
    }

    public static int minMeetingRooms(int[][] m) {
        Line[] lines = new Line[m.length];
        for (int i = 0; i < m.length; i++) {
            lines[i] = new Line(m[i][0], m[i][1]);
        }
        Arrays.sort(lines, new StartComp());
        PriorityQueue<Line> heap = new PriorityQueue<>(new EndComp());
        int max = 0;
        for (int i = 0; i < lines.length; i++) {
            while (!heap.isEmpty() && heap.peek().end <= lines[i].start) {
                heap.poll();
            }
            heap.add(lines[i]);
            max = Math.max(max, heap.size());
        }
        return max;
    }
}
