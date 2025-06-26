package leetc.top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * LeetCode 56. 合并区间 (Merge Intervals)
 * 
 * 问题描述：
 * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi]。
 * 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
 * 
 * 示例：
 * - 输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
 * - 输出：[[1,6],[8,10],[15,18]]
 * - 解释：区间[1,3]和[2,6]重叠，将它们合并为[1,6]
 * 
 * 解法思路：
 * 贪心算法 + 排序：
 * 1. 按照区间的起始位置对所有区间进行排序
 * 2. 初始化当前合并区间为第一个区间
 * 3. 遍历剩余区间：
 *    - 如果当前区间与合并区间重叠，扩展合并区间的结束位置
 *    - 如果不重叠，将当前合并区间加入结果，开始新的合并区间
 * 4. 添加最后一个合并区间到结果中
 * 
 * 重叠判断：
 * 两个区间[a,b]和[c,d]重叠 ⟺ max(a,c) <= min(b,d)
 * 等价于：c <= b（因为已按起始位置排序，所以a <= c）
 * 
 * 时间复杂度：O(n log n) - 排序的时间复杂度
 * 空间复杂度：O(n) - 存储结果和临时数组
 */
public class P56_MergeIntervals {
    
    /**
     * 区间类：表示一个区间[start, end]
     */
    public static class Range {
        public int s;  // 区间起始位置
        public int e;  // 区间结束位置

        public Range(int start, int end) {
            s = start;
            e = end;
        }
        
        @Override
        public String toString() {
            return "[" + s + "," + e + "]";
        }
    }

    /**
     * 比较器：按区间起始位置升序排序
     */
    public static class Comp implements Comparator<Range> {
        @Override
        public int compare(Range o1, Range o2) {
            return o1.s - o2.s;  // 按起始位置升序
        }
    }

    /**
     * 将Range列表转换为二维数组格式
     * 
     * @param list Range对象的列表
     * @return 二维数组表示的区间
     */
    private static int[][] transfer(ArrayList<Range> list) {
        int[][] matrix = new int[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            matrix[i] = new int[]{list.get(i).s, list.get(i).e};
        }
        return matrix;
    }

    /**
     * 合并重叠区间
     * 
     * 算法步骤：
     * 1. 将二维数组转换为Range对象数组，便于操作
     * 2. 按区间起始位置对数组进行排序
     * 3. 初始化当前合并区间为第一个区间
     * 4. 遍历剩余区间：
     *    - 如果当前区间的起始位置 > 合并区间的结束位置：不重叠
     *      → 将合并区间加入结果，开始新的合并区间
     *    - 否则：重叠，扩展合并区间的结束位置为两者的最大值
     * 5. 将最后一个合并区间加入结果
     * 6. 转换回二维数组格式返回
     * 
     * @param intervals 输入的区间数组
     * @return 合并后的区间数组
     */
    public int[][] merge(int[][] intervals) {
        // 边界检查
        if (intervals == null || intervals.length == 0) {
            return new int[0][0];
        }
        
        // 转换为Range对象数组
        Range[] arr = new Range[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            arr[i] = new Range(intervals[i][0], intervals[i][1]);
        }
        
        // 按起始位置排序
        Arrays.sort(arr, new Comp());
        
        // 合并重叠区间
        ArrayList<Range> ans = new ArrayList<>();
        
        // 初始化当前合并区间
        int s = arr[0].s;  // 当前合并区间的起始位置
        int e = arr[0].e;  // 当前合并区间的结束位置
        
        // 遍历剩余区间
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].s > e) {
                // 当前区间与合并区间不重叠
                ans.add(new Range(s, e));  // 保存当前合并区间
                s = arr[i].s;              // 开始新的合并区间
                e = arr[i].e;
            } else {
                // 当前区间与合并区间重叠，扩展结束位置
                e = Math.max(e, arr[i].e);
            }
        }
        
        // 添加最后一个合并区间
        ans.add(new Range(s, e));
        
        // 转换回二维数组格式
        return transfer(ans);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P56_MergeIntervals solution = new P56_MergeIntervals();
        
        // 测试用例1：[[1,3],[2,6],[8,10],[15,18]]
        int[][] intervals1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] result1 = solution.merge(intervals1);
        System.out.println("输入: " + Arrays.deepToString(intervals1));
        System.out.println("输出: " + Arrays.deepToString(result1));
        System.out.println("期望: [[1,6],[8,10],[15,18]]");
        System.out.println();
        
        // 测试用例2：[[1,4],[4,5]]
        int[][] intervals2 = {{1, 4}, {4, 5}};
        int[][] result2 = solution.merge(intervals2);
        System.out.println("输入: " + Arrays.deepToString(intervals2));
        System.out.println("输出: " + Arrays.deepToString(result2));
        System.out.println("期望: [[1,5]]");
        System.out.println();
        
        // 测试用例3：[[1,4],[2,3]]
        int[][] intervals3 = {{1, 4}, {2, 3}};
        int[][] result3 = solution.merge(intervals3);
        System.out.println("输入: " + Arrays.deepToString(intervals3));
        System.out.println("输出: " + Arrays.deepToString(result3));
        System.out.println("期望: [[1,4]]");
        System.out.println();
        
        // 测试用例4：无序输入 [[1,4],[0,4]]
        int[][] intervals4 = {{1, 4}, {0, 4}};
        int[][] result4 = solution.merge(intervals4);
        System.out.println("输入: " + Arrays.deepToString(intervals4));
        System.out.println("输出: " + Arrays.deepToString(result4));
        System.out.println("期望: [[0,4]]");
        System.out.println();
        
        // 测试用例5：不重叠区间 [[1,2],[3,4],[5,6]]
        int[][] intervals5 = {{1, 2}, {3, 4}, {5, 6}};
        int[][] result5 = solution.merge(intervals5);
        System.out.println("输入: " + Arrays.deepToString(intervals5));
        System.out.println("输出: " + Arrays.deepToString(result5));
        System.out.println("期望: [[1,2],[3,4],[5,6]]");
    }
}
