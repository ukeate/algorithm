package base.graph;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Prim算法的优化版本
 * 使用邻接表和优先队列实现，提高处理稀疏图的效率
 * 题目链接：https://www.nowcoder.com/questionTerminal/c23eab7bb39748b6b224a8a3afbe396b
 */
public class Prim2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            // 构建邻接表
            ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
            int n = (int) in.nval;  // 节点数
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            in.nextToken();
            int m = (int) in.nval;  // 边数
            
            // 读取边信息，构建无向图
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int a = (int) in.nval;  // 起点
                in.nextToken();
                int b = (int) in.nval;  // 终点
                in.nextToken();
                int cost = (int) in.nval;  // 权重
                graph.get(a).add(new int[] {b, cost});  // 添加边a->b
                graph.get(b).add(new int[] {a, cost});  // 添加边b->a（无向图）
            }
            
            // 使用Prim算法求最小生成树
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);  // 按权重排序的优先队列
            boolean[] visited = new boolean[n + 1];  // 标记节点是否已访问
            
            // 从节点1开始，将其所有邻边加入优先队列
            for (int[] edge : graph.get(1)) {
                heap.add(edge);
            }
            visited[1] = true;  // 标记节点1为已访问
            
            int ans = 0;  // 最小生成树的总权重
            while (!heap.isEmpty()) {
                int[] edge = heap.poll();  // 取出权重最小的边
                int next = edge[0];        // 目标节点
                int cost = edge[1];        // 边的权重
                
                if (!visited[next]) {      // 如果目标节点未被访问
                    visited[next] = true;  // 标记为已访问
                    ans += cost;           // 累加权重
                    // 将新节点的所有邻边加入优先队列
                    for (int[] e : graph.get(next)) {
                        heap.add(e);
                    }
                }
            }
            out.println(ans);
            out.flush();
        }
    }
}
