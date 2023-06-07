package base.greed;

import java.util.Arrays;
import java.util.Comparator;

// 安排最多的会议
public class BestArrange {
    private static class Meeting {
        public int start;
        public int end;

        public Meeting(int s, int e) {
            start = s;
            end = e;
        }
    }

    private static Meeting[] copyExcept(Meeting[] meetings, int except) {
        Meeting[] ans = new Meeting[meetings.length - 1];
        int ansIdx = 0;
        for (int i = 0; i < meetings.length; i++) {
            if (i != except) {
                ans[ansIdx++] = meetings[i];
            }
        }
        return ans;
    }

    private static int process1(Meeting[] meetings, int done, int timeline) {
        if (meetings.length == 0) {
            return done;
        }
        int max = done;
        for (int i = 0; i < meetings.length; i++) {
            if (meetings[i].start >= timeline) {
                Meeting[] next = copyExcept(meetings, i);
                max = Math.max(max, process1(next, done + 1, meetings[i].end));
            }
        }
        return max;
    }

    // 全排列
    public static int bestArrange1(Meeting[] meetings) {
        if (meetings == null || meetings.length == 0) {
            return 0;
        }
        return process1(meetings, 0, 0);
    }

    //

    private static class Comp implements Comparator<Meeting> {
        @Override
        public int compare(Meeting o1, Meeting o2) {
            return o1.end - o2.end;
        }
    }

    public static int bestArrange2(Meeting[] programs) {
        Arrays.sort(programs, new Comp());
        int timeline = 0;
        int res = 0;
        for (int i = 0; i < programs.length; i++) {
            if (timeline <= programs[i].start) {
                res++;
                timeline = programs[i].end;
            }
        }
        return res;
    }

    //

    private static Meeting[] randomMeetings(int maxLen, int maxTime) {
        Meeting[] ans = new Meeting[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < ans.length; i++) {
            int r1 = (int) ((maxTime + 1) * Math.random());
            int r2 = (int) ((maxTime + 1) * Math.random());
            if (r1 == r2) {
                ans[i] = new Meeting(r1, r1 + 1);
            } else {
                ans[i] = new Meeting(Math.min(r1, r2), Math.max(r1, r2));
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 12;
        int maxTime = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Meeting[] meetings = randomMeetings(maxLen, maxTime);
            if (bestArrange1(meetings) != bestArrange2(meetings)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
