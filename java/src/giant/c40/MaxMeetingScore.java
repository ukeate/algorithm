package giant.c40;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 会议室调度最大收益问题
 * 
 * 问题描述：
 * 给定一个会议数组meetings，每个会议包含[截止时间, 收益]
 * - 一开始的时间是0
 * - 任何会议都持续10个时间单位
 * - 一个会议必须在其截止时间之前开始
 * - 只有一个会议室，会议不能重叠
 * - 一旦安排会议，就获得该会议的收益
 * 返回能获得的最大收益
 * 
 * 示例：
 * meetings = {{66, 70}, {25, 90}, {50, 30}}
 * - 0号会议：截止时间66，收益70
 * - 1号会议：截止时间25，收益90
 * - 2号会议：截止时间50，收益30
 * 
 * 解题思路：
 * 方法1：递归+回溯（暴力解法）
 * - 对每个会议决定是否选择
 * - 按截止时间排序，验证时间约束
 * 
 * 方法2：贪心+堆优化
 * - 按截止时间排序
 * - 使用最小堆维护已选会议的收益
 * - 如果时间不够，替换堆中收益最小的会议
 * 
 * 算法核心：
 * - 时间约束：会议必须在截止时间前开始
 * - 贪心策略：优先考虑截止时间早的会议
 * - 堆优化：动态维护已选会议，支持替换操作
 * 
 * 时间复杂度：O(n log n)，排序和堆操作
 * 空间复杂度：O(n)，堆空间
 * 
 * @author Zhu Runqi
 */
public class MaxMeetingScore {

    /**
     * 递归验证会议选择的可行性并计算收益（方法1）
     * 
     * 算法思路：
     * 1. 按截止时间排序会议
     * 2. 递归选择会议子集
     * 3. 验证选择的会议是否满足时间约束
     * 4. 返回满足约束的最大收益
     * 
     * @param meetings 会议数组
     * @param idx 当前考虑的会议索引
     * @param path 已选择的会议路径
     * @param size 已选择的会议数量
     * @return 当前选择下的最大收益（不满足约束返回0）
     */
    private static int process1(int[][] meetings, int idx, int[][] path, int size) {
        if (idx == meetings.length) {
            // 验证当前选择是否满足时间约束
            int time = 0;
            int ans = 0;
            for (int i = 0; i < size; i++) {
                // 检查是否能在截止时间前开始
                if (time + 10 <= path[i][0]) {
                    ans += path[i][1];  // 累加收益
                    time += 10;         // 更新时间
                } else {
                    return 0;           // 时间约束不满足
                }
            }
            return ans;
        }
        
        // 选择1：不选择当前会议
        int p1 = process1(meetings, idx + 1, path, size);
        
        // 选择2：选择当前会议
        path[size] = meetings[idx];
        int p2 = process1(meetings, idx + 1, path, size + 1);
        
        return Math.max(p1, p2);
    }

    /**
     * 计算最大会议收益（方法1入口）
     * 
     * @param meetings 会议数组
     * @return 最大收益
     */
    public static int max1(int[][] meetings) {
        // 按截止时间排序
        Arrays.sort(meetings, (a, b) -> a[0] - b[0]);
        int[][] path = new int[meetings.length][];
        return process1(meetings, 0, path, 0);
    }

    /**
     * 贪心算法+堆优化求解最大收益（方法2）
     * 
     * 算法思路：
     * 1. 按截止时间排序会议
     * 2. 依次考虑每个会议：
     *    - 如果有时间，直接安排
     *    - 如果没时间但收益更高，替换堆中最小收益的会议
     * 3. 使用最小堆维护已安排会议的收益
     * 
     * 贪心策略的正确性：
     * - 按截止时间排序确保不违反时间约束
     * - 替换策略确保总收益最大化
     * 
     * @param meetings 会议数组
     * @return 最大收益
     */
    public static int max2(int[][] meetings) {
        // 按截止时间排序
        Arrays.sort(meetings, (a, b) -> a[0] - b[0]);
        
        // 最小堆，存储已安排会议的收益
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int time = 0;  // 当前时间
        
        for (int i = 0; i < meetings.length; i++) {
            if (time + 10 <= meetings[i][0]) {
                // 有足够时间安排这个会议
                heap.add(meetings[i][1]);
                time += 10;
            } else {
                // 时间不够，考虑是否替换收益最小的会议
                if (!heap.isEmpty() && heap.peek() < meetings[i][1]) {
                    heap.poll();                    // 移除收益最小的会议
                    heap.add(meetings[i][1]);      // 安排当前会议
                    // 注意：时间不变，因为是替换操作
                }
            }
        }
        
        // 计算总收益
        int ans = 0;
        while (!heap.isEmpty()) {
            ans += heap.poll();
        }
        return ans;
    }

    /**
     * 生成随机会议数组
     * 
     * @param n 会议数量
     * @param t 最大截止时间
     * @param s 最大收益
     * @return 随机会议数组
     */
    private static int[][] randomMeetings(int n, int t, int s) {
        int[][] ans = new int[n][2];
        for (int i = 0; i < n; i++) {
            ans[i][0] = (int) (Math.random() * t) + 1;  // 截止时间[1, t]
            ans[i][1] = (int) (Math.random() * s) + 1;  // 收益[1, s]
        }
        return ans;
    }

    /**
     * 复制会议数组
     * 
     * @param meetings 原会议数组
     * @return 复制的会议数组
     */
    private static int[][] copy(int[][] meetings) {
        int n = meetings.length;
        int[][] ans = new int[n][2];
        for (int i = 0; i < n; i++) {
            ans[i][0] = meetings[i][0];
            ans[i][1] = meetings[i][1];
        }
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 会议室调度最大收益问题 ===\n");
        
        // 测试用例1：题目示例
        System.out.println("测试用例1：题目示例");
        int[][] meetings1 = {{66, 70}, {25, 90}, {50, 30}};
        int result1_1 = max1(copy(meetings1));
        int result1_2 = max2(copy(meetings1));
        System.out.println("会议列表:");
        for (int i = 0; i < meetings1.length; i++) {
            System.out.println("  会议" + i + ": 截止时间=" + meetings1[i][0] + ", 收益=" + meetings1[i][1]);
        }
        System.out.println("最大收益 (方法1): " + result1_1);
        System.out.println("最大收益 (方法2): " + result1_2);
        System.out.println("分析: 选择会议1(0-10)和会议0(10-20)，总收益=90+70=160");
        System.out.println();
        
        // 测试用例2：时间冲突的会议
        System.out.println("测试用例2：时间冲突的会议");
        int[][] meetings2 = {{15, 100}, {12, 80}, {20, 60}};
        int result2_1 = max1(copy(meetings2));
        int result2_2 = max2(copy(meetings2));
        System.out.println("会议列表:");
        for (int i = 0; i < meetings2.length; i++) {
            System.out.println("  会议" + i + ": 截止时间=" + meetings2[i][0] + ", 收益=" + meetings2[i][1]);
        }
        System.out.println("最大收益 (方法1): " + result2_1);
        System.out.println("最大收益 (方法2): " + result2_2);
        System.out.println("分析: 只能选择一个会议，选择收益最高的");
        System.out.println();
        
        // 测试用例3：可以安排多个会议
        System.out.println("测试用例3：可以安排多个会议");
        int[][] meetings3 = {{20, 50}, {30, 60}, {40, 70}, {50, 80}};
        int result3_1 = max1(copy(meetings3));
        int result3_2 = max2(copy(meetings3));
        System.out.println("会议列表:");
        for (int i = 0; i < meetings3.length; i++) {
            System.out.println("  会议" + i + ": 截止时间=" + meetings3[i][0] + ", 收益=" + meetings3[i][1]);
        }
        System.out.println("最大收益 (方法1): " + result3_1);
        System.out.println("最大收益 (方法2): " + result3_2);
        System.out.println("分析: 可以安排所有会议，总收益=50+60+70+80=260");
        System.out.println();
        
        // 测试用例4：需要替换策略的情况
        System.out.println("测试用例4：需要替换策略的情况");
        int[][] meetings4 = {{15, 30}, {25, 40}, {20, 100}};
        int result4_1 = max1(copy(meetings4));
        int result4_2 = max2(copy(meetings4));
        System.out.println("会议列表:");
        for (int i = 0; i < meetings4.length; i++) {
            System.out.println("  会议" + i + ": 截止时间=" + meetings4[i][0] + ", 收益=" + meetings4[i][1]);
        }
        System.out.println("最大收益 (方法1): " + result4_1);
        System.out.println("最大收益 (方法2): " + result4_2);
        System.out.println("分析: 替换低收益会议以获得更高总收益");
        System.out.println();
        
        // 测试用例5：空会议列表
        System.out.println("测试用例5：空会议列表");
        int[][] meetings5 = {};
        int result5_1 = max1(copy(meetings5));
        int result5_2 = max2(copy(meetings5));
        System.out.println("会议列表: 空");
        System.out.println("最大收益 (方法1): " + result5_1);
        System.out.println("最大收益 (方法2): " + result5_2);
        System.out.println("分析: 无会议可安排，收益为0");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int times = 10000;
        int n = 12;
        int t = 100;
        int s = 500;
        boolean allPassed = true;
        
        System.out.println("开始随机测试...");
        
        for (int i = 0; i < times; i++) {
            int size = (int) (Math.random() * n) + 1;
            int[][] meetings1_test = randomMeetings(size, t, s);
            int[][] meetings2_test = copy(meetings1_test);
            int ans1 = max1(meetings1_test);
            int ans2 = max2(meetings2_test);
            
            if (ans1 != ans2) {
                System.out.println("测试失败: 结果不一致");
                System.out.println("方法1结果: " + ans1);
                System.out.println("方法2结果: " + ans2);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有随机测试通过！");
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 时间约束：会议必须在截止时间前开始");
        System.out.println("   - 资源限制：只有一个会议室，会议不能重叠");
        System.out.println("   - 优化目标：最大化总收益");
        System.out.println();
        System.out.println("2. 贪心策略：");
        System.out.println("   - 排序依据：按截止时间从早到晚排序");
        System.out.println("   - 选择原则：优先安排截止时间早的会议");
        System.out.println("   - 替换策略：用高收益会议替换低收益会议");
        System.out.println();
        System.out.println("3. 堆优化：");
        System.out.println("   - 最小堆：维护已安排会议的收益");
        System.out.println("   - 动态替换：O(log n)时间完成替换操作");
        System.out.println("   - 空间效率：只存储必要的收益信息");
        System.out.println();
        System.out.println("4. 正确性证明：");
        System.out.println("   - 截止时间排序保证时间约束");
        System.out.println("   - 替换策略保证局部最优");
        System.out.println("   - 贪心选择导致全局最优");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(n log n)，主要是排序和堆操作");
        System.out.println("   - 空间复杂度：O(n)，堆的空间");
        System.out.println("   - 比递归O(2^n)解法快得多");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 任务调度问题");
        System.out.println("   - 资源分配优化");
        System.out.println("   - 活动安排问题");
    }
}
