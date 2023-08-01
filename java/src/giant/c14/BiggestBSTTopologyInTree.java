package giant.c14;

import java.io.*;

// https://www.nowcoder.com/practice/e13bceaca5b14860b83cbcc4912c5d4a
public class BiggestBSTTopologyInTree {
    // m[i]是i节点最大拓扑值
    private static int max(int h, int[][] t, int[] m) {
        if (h == 0) {
            return 0;
        }
        int l = t[h][1], r = t[h][2], c = 0;
        int p1 = max(l, t, m);
        int p2 = max(r, t, m);

        while (l < h && m[l] != 0) {
            l = t[l][2];
        }
        if (m[l] != 0) {
            // l > h
            c = m[l];
            while (l != h) {
                m[l] -= c;
                l = t[l][0];
            }
        }
        while (r > h && m[r] != 0) {
            r = t[r][1];
        }
        if (m[r] != 0) {
            // r < h
            c = m[r];
            while (r != h) {
                m[r] -= c;
                r = t[r][0];
            }
        }
        m[h] = m[t[h][1]] + m[t[h][2]] + 1;
        return Math.max(Math.max(p1, p2), m[h]);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int h = (int) in.nval;
            // [节点][父，左，右]
            int[][] tree = new int[n + 1][3];
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                int c = (int) in.nval;
                in.nextToken();
                int l = (int) in.nval;
                in.nextToken();
                int r = (int) in.nval;
                tree[l][0] = c;
                tree[r][0] = c;
                tree[c][1] = l;
                tree[c][2] = r;
            }
            out.print(max(h, tree, new int[n + 1]));
            out.flush();
        }
    }
}
