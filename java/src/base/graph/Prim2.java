package base.graph;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

// https://www.nowcoder.com/questionTerminal/c23eab7bb39748b6b224a8a3afbe396b
public class Prim2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
            int n = (int) in.nval;
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            in.nextToken();
            int m = (int) in.nval;
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int a = (int) in.nval;
                in.nextToken();
                int b = (int) in.nval;
                in.nextToken();
                int cost = (int) in.nval;
                graph.get(a).add(new int[] {b, cost});
                graph.get(b).add(new int[] {a, cost});
            }
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            boolean[] visited = new boolean[n + 1];
            for (int[] edge : graph.get(1)) {
                heap.add(edge);
            }
            visited[1] = true;
            int ans = 0;
            while (!heap.isEmpty()) {
                int[] edge = heap.poll();
                int next = edge[0];
                int cost = edge[1];
                if (!visited[next]) {
                    visited[next] = true;
                    ans += cost;
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
