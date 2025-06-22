package base.greed;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 会议室安排问题 - 贪心算法经典问题
 * 
 * 问题描述：
 * 给定一系列会议，每个会议有开始时间和结束时间
 * 在同一时间只能进行一个会议，求最多能安排多少个会议
 * 
 * 核心思想：
 * 贪心策略：按照会议的结束时间排序，优先选择结束时间早的会议
 * 这样可以为后续会议留出更多的时间空间
 */
// 安排最多的会议
public class BestArrange {
    /**
     * 会议类 - 包含开始时间和结束时间
     */
    private static class Meeting {
        public int start;   // 会议开始时间
        public int end;     // 会议结束时间

        public Meeting(int s, int e) {
            start = s;
            end = e;
        }
    }

    /**
     * 复制数组并排除指定索引的元素
     * @param meetings 原会议数组
     * @param except 要排除的索引
     * @return 新的会议数组
     */
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

    /**
     * 递归暴力解法 - 尝试所有可能的会议组合
     * @param meetings 剩余可选择的会议
     * @param done 已经安排的会议数量
     * @param timeline 当前时间线（上一个会议的结束时间）
     * @return 最多能安排的会议数量
     */
    private static int process1(Meeting[] meetings, int done, int timeline) {
        // 基础情况：没有更多会议可选择
        if (meetings.length == 0) {
            return done;
        }
        int max = done;
        // 尝试每一个可能的会议
        for (int i = 0; i < meetings.length; i++) {
            // 如果会议的开始时间 >= 当前时间线，则可以安排
            if (meetings[i].start >= timeline) {
                Meeting[] next = copyExcept(meetings, i);
                // 递归计算选择这个会议后的最优解
                max = Math.max(max, process1(next, done + 1, meetings[i].end));
            }
        }
        return max;
    }

    /**
     * 暴力解法 - 全排列枚举所有可能
     * 时间复杂度：O(2^n) 指数级
     * @param meetings 会议数组
     * @return 最多能安排的会议数量
     */
    // 全排列
    public static int bestArrange1(Meeting[] meetings) {
        if (meetings == null || meetings.length == 0) {
            return 0;
        }
        return process1(meetings, 0, 0);
    }

    //

    /**
     * 比较器：按会议结束时间升序排列
     * 这是贪心策略的核心：优先选择结束时间早的会议
     */
    private static class Comp implements Comparator<Meeting> {
        @Override
        public int compare(Meeting o1, Meeting o2) {
            return o1.end - o2.end;
        }
    }

    /**
     * 贪心算法解法 - 按结束时间排序后选择
     * 
     * 算法步骤：
     * 1. 按结束时间对所有会议进行升序排序
     * 2. 依次遍历排序后的会议
     * 3. 如果当前会议的开始时间 >= 上一个选中会议的结束时间，则选择该会议
     * 
     * 时间复杂度：O(n log n) - 主要是排序的复杂度
     * 空间复杂度：O(1)
     * 
     * @param programs 会议数组
     * @return 最多能安排的会议数量
     */
    public static int bestArrange2(Meeting[] programs) {
        Arrays.sort(programs, new Comp());
        int timeline = 0;   // 当前时间线
        int res = 0;        // 已安排的会议数量
        for (int i = 0; i < programs.length; i++) {
            // 如果当前会议可以安排（开始时间 >= 上一个会议的结束时间）
            if (timeline <= programs[i].start) {
                res++;
                timeline = programs[i].end;  // 更新时间线为当前会议的结束时间
            }
        }
        return res;
    }

    //

    /**
     * 生成随机会议数组用于测试
     * @param maxLen 最大会议数量
     * @param maxTime 最大时间值
     * @return 随机会议数组
     */
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

    /**
     * 测试方法 - 验证两种算法的正确性
     */
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
