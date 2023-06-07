package base.graph;

import java.io.*;
import java.util.Arrays;

// https://www.nowcoder.com/questionTerminal/c23eab7bb39748b6b224a8a3afbe396b
public class Kruskal2 {
    public static int MAXM = 100001;
    public static int[][] edges = new int[MAXM][3];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int m = (int) in.nval;
            for (int i = 0; i < m; i++) {
                in.nextToken();
                edges[i][0] = (int) in.nval;
                in.nextToken();
                edges[i][1] = (int) in.nval;
                in.nextToken();
                edges[i][2] = (int) in.nval;
            }
            Arrays.sort(edges, 0, m, (a, b) -> a[2] - b[2]);
            build(n);
            int ans = 0;
            for (int[] edge : edges) {
                if (union(edge[0], edge[1])) {
                    ans += edge[2];
                }
            }
            out.println(ans);
            out.flush();
        }
    }

    public static int MAXN = 10001;
    public static int[] father = new int[MAXN];
    public static int[] size = new int[MAXN];
    public static int[] help = new int[MAXN];

    public static void build(int n) {
        for (int i = 1; i <= n; i++) {
            father[i] = i;
            size[i] = 1;
        }
    }

    private static int find(int i) {
        int hi = 0;
        while (i != father[i]) {
            help[hi++] = i;
            i = father[i];
        }
        while (hi > 0) {
            father[help[--hi]] = i;
        }
        return i;
    }

    private static boolean union(int a, int b) {
        int fa = find(a);
        int fb = find(b);
        if (fa == fb) {
            return false;
        }
        if (size[fa] >= size[fb]) {
            father[fb] = fa;
            size[fa] += size[fb];
        } else {
            father[fa] = fb;
            size[fb] += size[fa];
        }
        return true;
    }
}
