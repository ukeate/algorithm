package base.graph;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Dinic算法实现 - 最大流算法
 * 
 * 算法特点：
 * 1. 使用分层图的概念，通过BFS构建分层图
 * 2. 在分层图上使用DFS寻找增广路径
 * 3. 时间复杂度：O(V²E)
 * 4. 对于单位容量网络，时间复杂度可优化到O(min(V^(2/3), E^(1/2)) * E)
 * 
 * 题目链接：https://lightoj.com/problem/internet-bandwidth
 */
public class SimpleDinic {

    /**
     * 边的数据结构
     * 用于表示网络流图中的边
     */
    private static class Edge {
        public int from;       // 起点
        public int to;         // 终点
        public int available;  // 可用容量（剩余容量）
        
        public Edge(int a, int b, int c) {
            from = a;
            to = b;
            available = c;
        }
    }

    /**
     * Dinic算法核心类
     * 包含构建分层图、寻找增广路径等核心功能
     */
    public static class Dinic {
        private int N;                                      // 节点数量
        private ArrayList<ArrayList<Integer>> nexts;       // 邻接表，存储边的索引
        private ArrayList<Edge> edges;                     // 边列表
        private int[] depth;                               // 节点在分层图中的深度
        private int[] cur;                                 // 当前弧优化，记录每个节点当前考虑的边
        /**
         * 构造函数，初始化Dinic算法所需的数据结构
         * @param nums 节点数量
         */
        public Dinic(int nums) {
            N = nums + 1;  // 节点编号从1开始，所以需要nums+1个位置
            nexts = new ArrayList<>();
            for (int i = 0; i <= N; i++) {
                nexts.add(new ArrayList<>());
            }
            edges = new ArrayList<>();
            depth = new int[N];
            cur = new int[N];
        }

        /**
         * 添加边，同时添加正向边和反向边
         * @param u 起点
         * @param v 终点
         * @param r 容量
         */
        public void addEdge(int u, int v, int r) {
            int m = edges.size();
            edges.add(new Edge(u, v, r));      // 添加正向边
            nexts.get(u).add(m);
            edges.add(new Edge(v, u, 0));      // 添加反向边，初始容量为0
            nexts.get(v).add(m + 1);
        }

        /**
         * 使用BFS构建分层图
         * @param s 源点
         * @param t 汇点
         * @return 是否存在从源点到汇点的路径
         */
        private boolean bfs(int s, int t) {
            LinkedList<Integer> que = new LinkedList<>();
            que.addFirst(s);
            boolean[] visited = new boolean[N];
            visited[s] = true;
            
            while (!que.isEmpty()) {
                int u = que.pollLast();
                for (int i = 0; i < nexts.get(u).size(); i++) {
                    Edge e = edges.get(nexts.get(u).get(i));
                    int v = e.to;
                    // 只考虑有剩余容量且未访问的节点
                    if (!visited[v] && e.available > 0) {
                        visited[v] = true;
                        depth[v] = depth[u] + 1;  // 设置分层图的深度
                        if (v == t) {
                            break;  // 到达汇点即可停止
                        }
                        que.addFirst(v);
                    }
                }
            }
            return visited[t];  // 返回是否能到达汇点
        }

        /**
         * 在分层图上使用DFS寻找增广路径
         * @param s 当前节点
         * @param t 汇点
         * @param r 当前可推送的流量
         * @return 实际推送的流量
         */
        private int dfs(int s, int t, int r) {
            if (s == t || r == 0) {
                return r;  // 到达汇点或无流量可推送
            }
            int flow = 0;  // 总推送流量
            int f = 0;     // 当前边推送的流量
            
            // 当前弧优化：从上次停留的位置继续搜索
            for (; cur[s] < nexts.get(s).size(); cur[s]++) {
                int ei = nexts.get(s).get(cur[s]);
                Edge e = edges.get(ei);
                Edge o = edges.get(ei ^ 1);  // 对应的反向边
                
                // 只在分层图上搜索，且边有剩余容量
                if (depth[e.to] == depth[s] + 1 && (f = dfs(e.to, t, Math.min(e.available, r))) != 0) {
                    e.available -= f;  // 减少正向边容量
                    o.available += f;  // 增加反向边容量
                    flow += f;
                    r -= f;
                    if (r <= 0) {
                        break;  // 已无流量可推送
                    }
                }
            }
            return flow;
        }

        /**
         * 求最大流
         * @param s 源点
         * @param t 汇点
         * @return 最大流量
         */
        public int maxFlow(int s, int t) {
            int flow = 0;
            // 不断构建分层图并寻找增广路径，直到无法到达汇点
            while(bfs(s, t)) {
                Arrays.fill(cur, 0);  // 重置当前弧
                flow += dfs(s, t, Integer.MAX_VALUE);  // 在分层图上找增广路径
                Arrays.fill(depth, 0);  // 重置深度数组
            }
            return flow;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int cases = (int) in.nval;
            for (int i = 1; i <= cases; i++) {
                in.nextToken();
                int n = (int) in.nval;
                in.nextToken();
                int s = (int) in.nval;
                in.nextToken();
                int t = (int) in.nval;
                in.nextToken();
                int m = (int) in.nval;
                Dinic dinic = new Dinic(n);
                for (int j = 0; j < m; j++) {
                    in.nextToken();
                    int from = (int) in.nval;
                    in.nextToken();
                    int to = (int) in.nval;
                    in.nextToken();
                    int weight = (int) in.nval;
                    dinic.addEdge(from, to, weight);
                    dinic.addEdge(to, from, weight);
                }
                int ans = dinic.maxFlow(s, t);
                out.println("Case " + i + ": " + ans);
                out.flush();
            }
        }
    }
}
