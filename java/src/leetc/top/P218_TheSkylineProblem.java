package leetc.top;

import java.util.*;

/**
 * LeetCode 218. 天际线问题 (The Skyline Problem)
 * 
 * 问题描述：
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
 * 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
 * 
 * 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
 * - lefti 是第 i 座建筑物左边缘的 x 坐标
 * - righti 是第 i 座建筑物右边缘的 x 坐标  
 * - heighti 是第 i 座建筑物的高度
 * 
 * 天际线应该表示为关键点的列表，格式 [[x1,y1],[x2,y2],...] ，并按 x 坐标进行排序。
 * 关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的右端点，高度总是 0。
 * 
 * 示例：
 * 输入：buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
 * 输出：[[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
 * 
 * 解法思路：
 * 扫描线 + 数据结构：
 * 1. 事件处理：将每个建筑物转换为两个事件（左边缘和右边缘）
 * 2. 排序：按x坐标排序，同x坐标时左边缘优先于右边缘
 * 3. 扫描：从左到右处理每个事件，维护当前活跃建筑物的高度集合
 * 4. 高度变化检测：当最大高度发生变化时，记录关键点
 * 5. 数据结构：使用TreeMap维护高度及其出现次数
 * 
 * 核心思想：
 * - 天际线的变化只在建筑物的左右边缘处发生
 * - 维护当前时刻所有活跃建筑物的高度，最大高度决定天际线
 * - 使用事件驱动的方式，避免处理不必要的x坐标
 * 
 * 算法步骤：
 * 1. 创建事件数组：每个建筑物产生开始和结束两个事件
 * 2. 排序事件：按x坐标排序，同坐标时开始事件优先
 * 3. 扫描事件：维护活跃高度集合，检测最大高度变化
 * 4. 输出优化：合并相同高度的连续段
 * 
 * 时间复杂度：O(n log n) - 排序和TreeMap操作
 * 空间复杂度：O(n) - 存储事件和高度信息
 * 
 * LeetCode链接：https://leetcode.com/problems/the-skyline-problem/
 */
public class P218_TheSkylineProblem {

    /**
     * 事件节点类：表示建筑物的开始或结束事件
     */
    private static class Node {
        public int x;       // 事件发生的x坐标
        public boolean isAdd; // true表示建筑物开始，false表示建筑物结束
        public int h;       // 建筑物高度
        
        /**
         * 构造函数
         * 
         * @param x 事件x坐标
         * @param isAdd 是否为开始事件
         * @param h 建筑物高度
         */
        public Node(int x, boolean isAdd, int h) {
            this.x = x;
            this.isAdd = isAdd;
            this.h = h;
        }
    }

    /**
     * 事件比较器：定义事件的排序规则
     * 
     * 排序规则：
     * 1. 首先按x坐标升序排列
     * 2. 相同x坐标时，开始事件（isAdd=true）优先于结束事件
     * 3. 这样确保在同一位置，先处理建筑物开始，再处理结束
     */
    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            // 优先按x坐标排序
            if (o1.x != o2.x) {
                return o1.x - o2.x;
            }
            
            // 相同x坐标时，开始事件优先（isAdd=true返回-1，排在前面）
            if (o1.isAdd != o2.isAdd) {
                return o1.isAdd ? -1 : 1;
            }
            
            // 相同类型事件，顺序不重要
            return 0;
        }
    }

    /**
     * 计算天际线的主函数
     * 
     * 算法流程：
     * 1. 事件转换：将建筑物转换为开始/结束事件
     * 2. 事件排序：按照自定义规则排序
     * 3. 扫描处理：依次处理每个事件，维护活跃高度集合
     * 4. 高度跟踪：记录每个x坐标处的最大高度变化
     * 5. 结果优化：去除连续相同高度的冗余点
     * 
     * @param matrix 建筑物数组，每个元素为[left, right, height]
     * @return 天际线关键点列表，格式为[[x1,y1],[x2,y2],...]
     */
    public List<List<Integer>> getSkyline(int[][] matrix) {
        // 步骤1：创建事件数组
        Node[] nodes = new Node[matrix.length * 2];
        for (int i = 0; i < matrix.length; i++) {
            // 每个建筑物产生两个事件：开始和结束
            nodes[i * 2] = new Node(matrix[i][0], true, matrix[i][2]);      // 开始事件
            nodes[i * 2 + 1] = new Node(matrix[i][1], false, matrix[i][2]); // 结束事件
        }
        
        // 步骤2：排序事件
        Arrays.sort(nodes, new Comp());
        
        // 步骤3：初始化数据结构
        // heightTimes: 维护当前活跃的高度及其出现次数
        TreeMap<Integer, Integer> heightTimes = new TreeMap<>();
        // xHeight: 记录每个x坐标对应的最大高度
        TreeMap<Integer, Integer> xHeight = new TreeMap<>();
        
        // 步骤4：扫描处理所有事件
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].isAdd) {
                // 处理开始事件：添加建筑物高度
                if (!heightTimes.containsKey(nodes[i].h)) {
                    heightTimes.put(nodes[i].h, 1);
                } else {
                    heightTimes.put(nodes[i].h, heightTimes.get(nodes[i].h) + 1);
                }
            } else {
                // 处理结束事件：移除建筑物高度
                if (heightTimes.get(nodes[i].h) == 1) {
                    // 如果是最后一个该高度的建筑物，完全移除
                    heightTimes.remove(nodes[i].h);
                } else {
                    // 否则减少计数
                    heightTimes.put(nodes[i].h, heightTimes.get(nodes[i].h) - 1);
                }
            }
            
            // 记录当前x坐标的最大高度
            if (heightTimes.isEmpty()) {
                // 没有活跃建筑物，高度为0
                xHeight.put(nodes[i].x, 0);
            } else {
                // 取当前活跃建筑物的最大高度（TreeMap的lastKey）
                xHeight.put(nodes[i].x, heightTimes.lastKey());
            }
        }
        
        // 步骤5：构建结果并优化
        List<List<Integer>> ans = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : xHeight.entrySet()) {
            int curX = entry.getKey();      // 当前x坐标
            int height = entry.getValue();  // 当前最大高度
            
            // 优化：只有高度发生变化时才添加关键点
            if (ans.isEmpty() || ans.get(ans.size() - 1).get(1) != height) {
                ans.add(new ArrayList<>(Arrays.asList(curX, height)));
            }
        }
        
        return ans;
    }
}
