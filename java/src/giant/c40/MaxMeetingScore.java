package giant.c40;

import java.util.Arrays;
import java.util.PriorityQueue;

// 给定int[][] meetings，比如
// {
//   {66, 70}   0号会议截止时间66，获得收益70
//   {25, 90}   1号会议截止时间25，获得收益90
//   {50, 30}   2号会议截止时间50，获得收益30
// }
// 一开始的时间是0，任何会议都持续10的时间，但是一个会议一定要在该会议截止时间之前开始
// 只有一个会议室，任何会议不能共用会议室，一旦一个会议被正确安排，将获得这个会议的收益
// 请返回最大的收益
public class MaxMeetingScore {
    private static int process1(int[][] meetings, int idx, int[][] path, int size) {
        if (idx == meetings.length) {
            int time = 0;
            int ans = 0;
            for (int i = 0; i < size; i++) {
                if (time + 10 <= path[i][0]) {
                    ans += path[i][1];
                    time += 10;
                } else {
                    return 0;
                }
            }
            return ans;
        }
        int p1 = process1(meetings, idx + 1, path, size);
        path[size] = meetings[idx];
        int p2 = process1(meetings, idx + 1, path, size + 1);
        return Math.max(p1, p2);
    }

    public static int max1(int[][] meetings) {
        Arrays.sort(meetings, (a, b) -> a[0] - b[0]);
        int[][] path = new int[meetings.length][];
        int size = 0;
        return process1(meetings, 0, path, size);
    }

    //

    public static int max2(int[][] meetings) {
        Arrays.sort(meetings, (a, b) -> a[0] - b[0]);
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int time = 0;
        for (int i = 0; i < meetings.length; i++) {
            if (time + 10 <= meetings[i][0]) {
                heap.add(meetings[i][1]);
                time += 10;
            } else {
                if (!heap.isEmpty() && heap.peek() < meetings[i][1]) {
                    heap.poll();
                    heap.add(meetings[i][1]);
                }
            }
        }
        int ans = 0;
        while (!heap.isEmpty()) {
            ans += heap.poll();
        }
        return ans;
    }

    //

    private static int[][] randomMeetings(int n, int t, int s) {
        int[][] ans = new int[n][2];
        for (int i = 0; i < n; i++) {
            ans[i][0] = (int) (Math.random() * t) + 1;
            ans[i][1] = (int) (Math.random() * s) + 1;
        }
        return ans;
    }

    private static int[][] copy(int[][] meetings) {
        int n = meetings.length;
        int[][] ans = new int[n][2];
        for (int i = 0; i < n; i++) {
            ans[i][0] = meetings[i][0];
            ans[i][1] = meetings[i][1];
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 100000;
        int n = 12;
        int t = 100;
        int s = 500;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int size = (int) (Math.random() * n) + 1;
            int[][] meetings1 = randomMeetings(size, t, s);
            int[][] meetings2 = copy(meetings1);
            int ans1 = max1(meetings1);
            int ans2 = max2(meetings2);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
