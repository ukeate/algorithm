package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 210. 课程表 II (Course Schedule II)
 * 
 * 问题描述：
 * 现在你总共有 numCourses 门课需要选，记为 0 到 numCourses - 1。
 * 给你一个数组 prerequisites ，其中 prerequisites[i] = [ai, bi] ，表示在选修课程 ai 之前必须先选修 bi。
 * 返回你为了学完所有课程所安排的学习顺序。可能会有多个正确的顺序，你只要返回其中一种就可以了。
 * 如果不可能完成所有课程，返回一个空数组。
 * 
 * 示例：
 * 输入：numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * 输出：[0,2,1,3]
 * 解释：总共有 4 门课程。要学习课程 3，你应该先完成课程 1 和课程 2。并且课程 1 和课程 2 都应该排在课程 0 之后。
 * 因此，一个正确的课程顺序是 [0,2,1,3]。另一个正确的排序是 [0,1,2,3]。
 * 
 * 解法思路：
 * 拓扑排序（Kahn算法）：
 * 1. 构建有向图，统计每个节点的入度
 * 2. 将所有入度为0的节点加入队列和结果数组
 * 3. 从队列中取出节点，将其邻居节点的入度-1
 * 4. 如果邻居节点入度变为0，则加入队列
 * 5. 重复步骤3-4，直到队列为空
 * 6. 如果处理的节点数等于总课程数，说明无环，返回拓扑序列；否则返回空数组
 * 
 * 与Course Schedule I的区别：
 * - 需要返回具体的拓扑排序序列，而不仅仅是判断是否可能
 * - 需要处理没有先修关系的独立课程
 * 
 * 核心思想：
 * - 拓扑排序：对有向无环图(DAG)进行线性排序
 * - 入度为0的节点可以立即学习
 * - 学完一门课程后，依赖它的课程入度减1
 * 
 * 时间复杂度：O(V + E) - V是课程数，E是先修关系数
 * 空间复杂度：O(V + E) - 图的存储空间
 * 
 * LeetCode链接：https://leetcode.com/problems/course-schedule-ii/
 */
public class P210_CourseScheduleII {
    
    /**
     * 图节点类，代表一门课程
     */
    public static class Node {
        public int name;               // 课程编号
        public int in;                 // 入度（先修课程数量）
        public ArrayList<Node> nexts;  // 邻居节点（依赖当前课程的课程列表）
        
        public Node(int n) {
            name = n;
            in = 0;                    // 初始入度为0
            nexts = new ArrayList<>();
        }
    }

    /**
     * 返回课程的学习顺序
     * 
     * @param numCourses 课程总数
     * @param prerequisites 先修关系数组，[i][0]表示课程，[i][1]表示先修课程
     * @return 课程学习顺序数组，如果无法完成所有课程则返回空数组
     */
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        // 初始化结果数组，先按顺序填入所有课程
        int[] ans = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            ans[i] = i;
        }
        
        // 如果没有先修关系，直接返回默认顺序
        if (prerequisites == null || prerequisites.length == 0) {
            return ans;
        }
        
        // 构建有向图
        HashMap<Integer, Node> nodes = new HashMap<>();
        for (int[] arr : prerequisites) {
            int to = arr[0];   // 课程
            int from = arr[1]; // 先修课程
            
            // 确保节点存在
            if (!nodes.containsKey(to)) {
                nodes.put(to, new Node(to));
            }
            if (!nodes.containsKey(from)) {
                nodes.put(from, new Node(from));
            }
            
            // 建立有向边：from -> to
            Node t = nodes.get(to);
            Node f = nodes.get(from);
            f.nexts.add(t);
            t.in++; // to课程的入度+1
        }
        
        int idx = 0; // 结果数组的填充索引
        Queue<Node> que = new LinkedList<>();
        
        // 处理所有课程，分为两类：
        // 1. 没有先修关系的独立课程：直接加入结果
        // 2. 有先修关系的课程：入度为0的加入队列
        for (int i = 0; i < numCourses; i++) {
            if (!nodes.containsKey(i)) {
                // 独立课程，没有先修关系，可以随时学习
                ans[idx++] = i;
            } else {
                // 有先修关系的课程，检查入度
                if (nodes.get(i).in == 0) {
                    que.add(nodes.get(i));
                }
            }
        }
        
        int needPrerequisiteNums = nodes.size(); // 需要处理先修关系的课程数
        int count = 0; // 已处理的有先修关系的课程数
        
        // Kahn算法进行拓扑排序
        while (!que.isEmpty()) {
            Node cur = que.poll();
            ans[idx++] = cur.name; // 将课程加入结果序列
            count++;
            
            // 更新邻居节点的入度
            for (Node next : cur.nexts) {
                if (--next.in == 0) {
                    que.add(next); // 入度变为0，可以学习了
                }
            }
        }
        
        // 检查是否所有有先修关系的课程都被处理了
        // 如果count != needPrerequisiteNums，说明存在环，无法完成所有课程
        return count == needPrerequisiteNums ? ans : new int[0];
    }
}
