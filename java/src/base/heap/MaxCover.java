package base.heap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 线段最大重叠高度问题
 * 
 * 问题描述：
 * 给定多个线段（区间），求所有线段在某个位置的最大重叠数量
 * 
 * 例如：线段[1,3], [2,4], [3,5]
 * 在位置2.5处有2个线段重叠，在位置3处有3个线段重叠
 * 
 * 解题思路：
 * 1. 将所有线段按起始位置排序
 * 2. 使用小根堆记录当前重叠线段的结束位置
 * 3. 遍历每个线段时：
 *    - 移除所有在当前线段开始前就结束的线段
 *    - 将当前线段的结束位置加入堆中
 *    - 更新最大重叠数量
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
public class MaxCover {
    /**
     * 线段类 - 表示一个区间
     */
    private static class Line {
        public int start;   // 线段起始位置
        public int end;     // 线段结束位置

        public Line(int s, int e) {
            start = s;
            end = e;
        }
    }

    /**
     * 线段起始位置比较器
     * 用于将线段按起始位置从小到大排序
     */
    public static class StartComparator implements Comparator<Line> {
        @Override
        public int compare(Line o1, Line o2) {
            return o1.start - o2.start;
        }
    }

    /**
     * 计算线段最大重叠高度（优化算法）
     * 
     * 算法核心思想：
     * 1. 按线段起始位置排序，确保处理顺序
     * 2. 用小根堆维护当前重叠线段的结束位置
     * 3. 遍历线段时动态维护重叠状态
     * 
     * 关键理解：
     * - 堆中存储的是当前重叠线段的结束位置
     * - 堆的大小就是当前的重叠数量
     * - 当新线段开始时，移除所有已结束的线段
     * 
     * @param m 二维数组，每一行表示一个线段[start, end]
     * @return 最大重叠数量
     */
    public static int maxCover(int[][] m) {
        // 转换为Line对象数组
        Line[] lines = new Line[m.length];
        for (int i = 0; i < m.length; i++) {
            lines[i] = new Line(m[i][0], m[i][1]);
        }
        
        // 按起始位置排序
        Arrays.sort(lines, new StartComparator());
        
        // 小根堆：存储当前重叠线段的结束位置
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int max = 0;    // 记录最大重叠数量
        
        for (int i = 0; i < lines.length; i++) {
            // 移除所有在当前线段开始前就结束的线段
            // 堆顶是最早结束的线段，如果它都没结束，其他线段也没结束
            while (!heap.isEmpty() && heap.peek() <= lines[i].start) {
                heap.poll();
            }
            
            // 将当前线段的结束位置加入堆中
            heap.add(lines[i].end);
            
            // 更新最大重叠数量（堆的大小就是当前重叠数量）
            max = Math.max(max, heap.size());
        }
        return max;
    }

    /**
     * 暴力验证算法 - 用于验证优化算法的正确性
     * 
     * 思路：
     * 1. 找到所有线段的最小和最大位置
     * 2. 在每个整数位置+0.5的点检查重叠数量
     * 3. 返回最大重叠数量
     * 
     * 为什么检查x+0.5的位置：
     * - 避免端点重叠的边界问题
     * - 简化判断逻辑（start < p < end）
     * 
     * @param lines 线段数组
     * @return 最大重叠数量
     */
    public static int maxCoverSure(int[][] lines) {
        // 找到所有线段的范围
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < lines.length; i++) {
            min = Math.min(min, lines[i][0]);
            max = Math.max(max, lines[i][1]);
        }
        
        int cover = 0;
        // 在每个0.5位置检查重叠数量
        for (double p = min + 0.5; p < max; p += 1) {
            int cur = 0;
            // 统计包含当前位置的线段数量
            for (int i = 0; i < lines.length; i++) {
                if (lines[i][0] < p && lines[i][1] > p) {
                    cur++;
                }
            }
            cover = Math.max(cover, cur);
        }
        return cover;
    }

    /**
     * 生成随机线段数组用于测试
     * 
     * @param n 最大线段数量
     * @param l 位置范围左边界
     * @param r 位置范围右边界
     * @return 随机线段数组
     */
    private static int[][] randomLines(int n, int l, int r) {
        int size = (int) ((n + 1) * Math.random()) + 1;
        int[][] res = new int[size][2];
        for (int i = 0; i < size; i++) {
            int a = l + (int) ((r - l + 1) * Math.random());
            int b = l + (int) ((r - l + 1) * Math.random());
            // 确保线段长度至少为1
            if (a == b) {
                b = a + 1;
            }
            res[i][0] = Math.min(a, b);     // 起始位置
            res[i][1] = Math.max(a, b);     // 结束位置
        }
        return res;
    }

    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        int times = 100000;     // 测试次数
        int maxLen = 100;       // 最大线段数量
        int l = 0;              // 位置范围左边界
        int r = 200;            // 位置范围右边界
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[][] lines = randomLines(maxLen, l, r);
            int ans1 = maxCover(lines);         // 优化算法结果
            int ans2 = maxCoverSure(lines);     // 暴力算法结果
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
