package base.graph;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

// https://lightoj.com/problem/internet-bandwidth
public class SimpleDinic {

    private static class Edge {
        public int from;
        public int to;
        public int available;
        public Edge(int a, int b, int c) {
            from = a;
            to = b;
            available = c;
        }
    }

    // 加反向边
    // 优化：高度数组, 记用过的边
    public static class Dinic {
        private int N;
        private ArrayList<ArrayList<Integer>> nexts;
        private ArrayList<Edge> edges;
        private int[] depth;
        // i边下层从几边开始
        private int[] cur;
        public Dinic(int nums) {
            // 从1开始
            N = nums + 1;
            nexts = new ArrayList<>();
            for (int i = 0; i <= N; i++) {
                nexts.add(new ArrayList<>());
            }
            edges = new ArrayList<>();
            depth = new int[N];
            cur = new int[N];
        }

        public void addEdge(int u, int v, int r) {
            int m = edges.size();
            edges.add(new Edge(u, v, r));
            nexts.get(u).add(m);
            edges.add(new Edge(v, u, 0));
            nexts.get(v).add(m + 1);
        }

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
                    if (!visited[v] && e.available > 0) {
                        visited[v] = true;
                        depth[v] = depth[u] + 1;
                        if (v == t) {
                            break;
                        }
                        que.addFirst(v);
                    }
                }
            }
            return visited[t];
        }

        private int dfs(int s, int t, int r) {
            if (s == t || r == 0) {
                return r;
            }
            int flow = 0;
            int f = 0;
            for (; cur[s] < nexts.get(s).size(); cur[s]++) {
                int ei = nexts.get(s).get(cur[s]);
                Edge e = edges.get(ei);
                // e对应的回边
                Edge o = edges.get(ei ^ 1);
                if (depth[e.to] == depth[s] + 1 && (f = dfs(e.to, t, Math.min(e.available, r))) != 0) {
                    e.available -= f;
                    o.available += f;
                    flow += f;
                    r -= f;
                    if (r <= 0) {
                        break;
                    }
                }
            }
            return flow;
        }

        public int maxFlow(int s, int t) {
            int flow = 0;
            // 反向边会产生新通路
            while(bfs(s, t)) {
                Arrays.fill(cur, 0);
                flow += dfs(s, t, Integer.MAX_VALUE);
                Arrays.fill(depth, 0);
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
