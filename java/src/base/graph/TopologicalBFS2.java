package base.graph;

import java.io.*;
import java.util.ArrayList;

/**
 * 拓扑排序的数组优化版本
 * 使用数组和邻接表实现，提高性能，适用于大规模图
 * 题目链接：https://www.nowcoder.com/questionTerminal/88f7e156ca7d43a1a535f619cd3f495c
 */
public class TopologicalBFS2 {
    public static int MAXN = 200001;
    public static int[] queue = new int[MAXN];      // 模拟队列的数组
    public static int[] indegree = new int[MAXN];   // 存储每个节点的入度
    public static int[] ans = new int[MAXN];        // 存储拓扑排序的结果
    public static int n, m, from, to;               // n:节点数, m:边数, from/to:边的起点/终点

    /**
     * 拓扑排序的核心算法
     * @param graph 邻接表表示的有向图
     * @return 如果图中有环返回false，否则返回true
     */
    public static boolean topoSort(ArrayList<ArrayList<Integer>> graph) {
        // 计算每个节点的入度
        for (ArrayList<Integer> nexts : graph) {
            for (int next : nexts) {
                indegree[next]++;
            }
        }
        
        // 将入度为0的节点加入队列
        int l = 0;  // 队列头指针
        int r = 0;  // 队列尾指针
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 0) {
                queue[r++] = i;
            }
        }
        
        int cnt = 0;  // 已处理的节点数量
        while (l < r) {
            int cur = queue[l++];  // 从队列头取出节点
            ans[cnt++] = cur;      // 加入结果数组
            
            // 处理当前节点的所有邻接节点
            for (int next : graph.get(cur)) {
                if (--indegree[next] == 0) {  // 邻接节点入度减1，如果变为0
                    queue[r++] = next;        // 加入队列
                }
            }
        }
        return cnt == n;  // 如果处理的节点数等于总节点数，说明无环
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            n = (int) in.nval;
            in.nextToken();
            m = (int) in.nval;
            ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int i = 0; i < m; i++) {
                in.nextToken();
                from = (int) in.nval;
                in.nextToken();
                to = (int) in.nval;
                graph.get(from).add(to);
            }
            if (!topoSort(graph)) {
                out.println(-1);
            } else {
                for (int i = 0; i < n - 1; i++) {
                    out.print(ans[i] + " ");
                }
                out.println(ans[n - 1]);
            }
            out.flush();
        }
    }
}
