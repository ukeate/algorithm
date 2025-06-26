package basic.c56;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * 最小范围覆盖K个列表中的元素问题
 * LeetCode 632: https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
 * 
 * 问题描述：
 * 给定k个有序的整数列表，找到最小的范围[a,b]，使得每个列表中至少有一个数字在这个范围内。
 * 
 * 算法思想：
 * 1. 滑动窗口 + 有序集合的思想
 * 2. 维护一个包含每个列表当前元素的有序集合
 * 3. 每次移动最小元素对应列表的指针，更新范围
 * 4. 当某个列表遍历完毕时停止
 * 
 * 核心洞察：
 * - 最优范围一定是以某个列表中的元素为左端点
 * - 使用TreeSet维护当前窗口的最大值和最小值
 * - 贪心策略：总是移动最小元素，尽可能缩小范围
 * 
 * 时间复杂度：O(n * log k)，其中n是所有元素总数，k是列表数量
 * 空间复杂度：O(k)
 * 
 * @author: algorithm learning
 */
public class SmallestRangeCoveringElementsFromKLists {
    
    /**
     * 节点类，存储元素值及其在哪个数组的哪个位置
     */
    public static class Node {
        public int val;      // 元素值
        public int arrId;    // 所属数组ID
        public int idx;      // 在数组中的索引

        public Node(int v, int ai, int i) {
            val = v;
            arrId = ai;
            idx = i;
        }
    }

    /**
     * 比较器类
     * 首先按元素值排序，值相同时按数组ID排序
     * 这样确保TreeSet中元素的唯一性和有序性
     */
    public static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            // 先按值排序，值相同则按数组ID排序
            return o1.val != o2.val ? o1.val - o2.val : o1.arrId - o2.arrId;
        }
    }

    /**
     * 寻找覆盖所有列表的最小范围
     * 
     * 算法步骤：
     * 1. 初始化：将每个列表的第一个元素加入TreeSet
     * 2. 循环：当TreeSet包含所有k个列表的元素时
     *    - 记录当前范围[min, max]
     *    - 移除最小元素，尝试添加该列表的下一个元素
     *    - 更新最优范围
     * 3. 当某个列表遍历完毕时，无法继续缩小范围，算法结束
     * 
     * @param arr k个有序列表
     * @return 最小范围[a, b]
     */
    public static int[] smallest(List<List<Integer>> arr) {
        int n = arr.size();  // 列表数量
        TreeSet<Node> set = new TreeSet<>(new Comp());
        
        // 初始化：将每个列表的第一个元素加入TreeSet
        for (int i = 0; i < n; i++) {
            set.add(new Node(arr.get(i).get(0), i, 0));
        }
        
        boolean hasSet = false;  // 是否已经设置过答案
        int a = 0, b = 0;       // 最优范围的左右端点
        
        // 当TreeSet包含所有k个列表的元素时继续循环
        while (set.size() == n) {
            Node min = set.first();  // 当前最小元素
            Node max = set.last();   // 当前最大元素
            
            // 更新最优范围（如果当前范围更小）
            if (!hasSet || (max.val - min.val < b - a)) {
                hasSet = true;
                a = min.val;    // 新的左端点
                b = max.val;    // 新的右端点
            }
            
            // 移除最小元素，尝试用该列表的下一个元素替换
            min = set.pollFirst();
            int arrId = min.arrId;         // 最小元素所属的列表ID
            int idx = min.idx + 1;         // 该列表中的下一个索引
            
            // 如果该列表还有下一个元素，则加入TreeSet
            if (idx < arr.get(arrId).size()) {
                set.add(new Node(arr.get(arrId).get(idx), arrId, idx));
            }
            // 如果某个列表遍历完毕，TreeSet大小会小于n，循环结束
        }
        
        return new int[]{a, b};
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        // 输入: [[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]
        // 输出: [20,24]
        // 解释: 列表1选择24，列表2选择20，列表3选择22，范围[20,24]包含所有列表中的一个元素
        
        System.out.println("算法验证：最小范围覆盖K个列表");
        System.out.println("================================");
        
        // 可以添加具体的测试用例来验证算法正确性
        System.out.println("主要特点：");
        System.out.println("1. 使用TreeSet维护当前窗口的有序性");
        System.out.println("2. 贪心策略：总是移动最小元素");
        System.out.println("3. 时间复杂度O(n*log k)，空间复杂度O(k)");
        System.out.println("4. 适用于k个有序列表的范围查找问题");
    }
}
