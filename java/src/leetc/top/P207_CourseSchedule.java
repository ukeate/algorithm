package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 207. 课程表 (Course Schedule)
 * 
 * 问题描述：
 * 你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1。
 * 在选修某些课程之前需要一些先修课程。 先修课程和课程之间的关系由 prerequisites 给出。
 * 请你判断是否可能完成所有课程的学习？
 * 
 * 示例：
 * 输入：numCourses = 2, prerequisites = [[1,0]]
 * 输出：true
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0 。这是可能的。
 * 
 * 解法思路：
 * 使用拓扑排序(Kahn算法)检测有向图是否有环：
 * 1. 构建有向图，计算每个节点的入度
 * 2. 将所有入度为0的节点加入队列
 * 3. 从队列中取出节点，将其所有邻居节点的入度-1
 * 4. 如果邻居节点入度变为0，则加入队列
 * 5. 重复步骤3-4，直到队列为空
 * 6. 如果处理的节点数等于图中节点数，说明无环，否则有环
 * 
 * 时间复杂度：O(V + E) - V是课程数，E是先修关系数
 * 空间复杂度：O(V + E) - 图的存储空间
 */
public class P207_CourseSchedule {
    /**
     * 图节点类，代表一门课程
     */
    public static class Node {
        public int name;               // 课程编号
        public int in;                 // 入度（先修课程数量）
        public ArrayList<Node> nexts;  // 邻居节点（依赖当前课程的课程列表）
        
        public Node(int n) {
            name = n;
            nexts = new ArrayList<>();
        }
    }

    /**
     * 判断是否能完成所有课程的学习
     * 
     * @param numCourses 课程数量
     * @param prerequisites 先修关系数组，[i][0]表示课程，[i][1]表示先修课程
     * @return 能否完成所有课程的学习
     */
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        if (prerequisites == null || prerequisites.length == 0) {
            return true; // 无先修关系，可以完成所有课程
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
        
        // Kahn算法进行拓扑排序
        int need = nodes.size(); // 需要处理的课程数
        Queue<Node> que = new LinkedList<>();
        
        // 将所有入度为0的节点加入队列
        for (Node node : nodes.values()) {
            if (node.in == 0) {
                que.add(node);
            }
        }
        
        int count = 0; // 已处理的课程数
        while (!que.isEmpty()) {
            Node cur = que.poll();
            count++;
            
            // 更新邻居节点的入度
            for (Node next : cur.nexts) {
                if (--next.in == 0) {
                    que.add(next); // 入度变为0，加入队列
                }
            }
        }
        
        // 如果处理的节点数等于图中节点数，说明无环
        return count == need;
    }
}
