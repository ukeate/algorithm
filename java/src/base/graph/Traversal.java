package base.graph;

import base.graph.o.Graph;
import base.graph.o.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 图的遍历算法实现
 * 包含广度优先搜索（BFS）和深度优先搜索（DFS）两种基本图遍历算法
 * 
 * BFS特点：
 * - 按层级遍历，先访问距离起点近的节点
 * - 使用队列实现
 * - 时间复杂度：O(V+E)，空间复杂度：O(V)
 * 
 * DFS特点：
 * - 尽可能深入遍历，直到无法继续才回溯
 * - 使用栈实现
 * - 时间复杂度：O(V+E)，空间复杂度：O(V)
 */
public class Traversal {
    /**
     * 广度优先搜索（BFS）
     * 从起始节点开始，按层级遍历图中的所有可达节点
     * @param start 起始节点
     */
    public static void bfs(Node start) {
        if (start == null) {
            return;
        }
        Queue<Node> queue = new LinkedList<>();  // 用于BFS的队列
        HashSet<Node> set = new HashSet<>();     // 记录已访问的节点，避免重复访问
        
        queue.add(start);
        set.add(start);
        
        while (!queue.isEmpty()) {
            Node cur = queue.poll();  // 从队列头取出节点
            System.out.print(cur.val + ",");  // 访问当前节点
            
            // 将当前节点的所有未访问邻接节点加入队列
            for (Node next : cur.nexts) {
                if (!set.contains(next)) {
                    set.add(next);
                    queue.add(next);
                }
            }
        }
    }

    //

    /**
     * 深度优先搜索（DFS）
     * 从起始节点开始，尽可能深入地遍历图，直到无法继续才回溯
     * @param node 起始节点
     */
    public static void dfs(Node node) {
        if (node == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();      // 用于DFS的栈
        HashSet<Node> set = new HashSet<>();    // 记录已访问的节点，避免重复访问
        
        stack.add(node);
        set.add(node);
        System.out.print(node.val + ",");  // 访问起始节点
        
        while (!stack.isEmpty()) {
            Node cur = stack.pop();  // 从栈顶取出节点
            
            // 寻找当前节点的第一个未访问邻接节点
            for (Node next : cur.nexts) {
                if (!set.contains(next)) {
                    stack.push(cur);   // 将当前节点重新入栈（用于回溯）
                    stack.push(next);  // 将邻接节点入栈
                    set.add(next);
                    System.out.print(next.val + ",");  // 访问邻接节点
                    break;  // 找到一个未访问节点后立即处理，体现深度优先的特性
                }
            }
        }
    }

    //

    /**
     * 测试方法，演示图遍历算法的使用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建测试图：边的格式为 [权重, 起点, 终点]
        Graph g = Graph.createGraph(new int[][]{
                {1,0,1},  // 节点0到节点1，权重1
                {1,0,3},  // 节点0到节点3，权重1
                {1,1,2},  // 节点1到节点2，权重1
                {1,2,1},  // 节点2到节点1，权重1
                {1,1,0}   // 节点1到节点0，权重1
        });
        dfs(g.nodes.get(0));  // 从节点0开始进行深度优先搜索
    }
}
