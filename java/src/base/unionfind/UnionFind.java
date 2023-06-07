package base.unionfind;

import java.io.*;

// https://www.nowcoder.com/questionTerminal/e7ed657974934a30b2010046536a5372
public class UnionFind {
    public int MAXN = 1000001;
    public int[] father = new int[MAXN];
    public int[] size = new int[MAXN];
    public int[] help = new int[MAXN];
    private int sets;

    public UnionFind(int n) {
        this.sets = n;
        for (int i = 0; i < n; i++) {
            father[i] = i;
            size[i] = 1;
        }
    }

    public int find(int i) {
        int hi = 0;
        while (i != father[i]) {
            help[hi++] = i;
            i = father[i];
        }
        for (hi--; hi >= 0; hi--) {
            father[help[hi]] = i;
        }
        return i;
    }

    public boolean isSameSet(int x, int y) {
        return find(x) == find(y);
    }

    public void union(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx != fy) {
            sets--;
            if (size[fx] >= size[fy]) {
                size[fx] += size[fy];
                father[fy] = fx;
            } else {
                size[fy] += size[fx];
                father[fx] = fy;
            }
        }
    }

    public int sets() {
        return sets;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            UnionFind unionFind = new UnionFind(n);
            in.nextToken();
            int m = (int) in.nval;
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int op = (int) in.nval;
                in.nextToken();
                int x = (int) in.nval;
                in.nextToken();
                int y = (int) in.nval;
                if (op == 1) {
                    out.println(unionFind.isSameSet(x, y) ? "Yes" : "No");
                    out.flush();
                } else {
                    unionFind.union(x, y);
                }
            }
        }
    }
}
