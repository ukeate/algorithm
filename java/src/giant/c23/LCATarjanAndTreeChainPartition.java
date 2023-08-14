package giant.c23;

import java.util.HashSet;

public class LCATarjanAndTreeChainPartition {
    public static int[] query1(int[] father, int[][] queries) {
        int m = queries.length;
        int[] ans = new int[m];
        HashSet<Integer> path = new HashSet<>();
        for (int i = 0; i < m; i++) {
            int jump = queries[i][0];
            while (father[jump] != jump) {
                path.add(jump);
                jump = father[jump];
            }
            path.add(jump);
            jump = queries[i][1];
            while (!path.contains(jump)) {
                jump = father[jump];
            }
            ans[i] = jump;
            path.clear();
        }
        return ans;
    }

    //

    private static class UnionFind {
        // father
        private int[] f;
        // size
        private int[] s;
        // tag
        private int[] t;
        // help
        private int[] h;

        public UnionFind(int n) {
            f = new int[n];
            s = new int[n];
            t = new int[n];
            h = new int[n];
            for (int i = 0; i < n; i++) {
                f[i] = i;
                s[i] = 1;
                t[i] = -1;
            }
        }

        private int find(int i) {
            int j = 0;
            while (i != f[i]) {
                h[j++] = i;
                i = f[i];
            }
            while (j > 0) {
                h[--j] = i;
            }
            return i;
        }

        public void union(int i, int j) {
            int fi = find(i);
            int fj = find(j);
            if (fi != fj) {
                int si = s[fi], sj = s[fj];
                int m = si >= sj ? fi : fj;
                int l = m == fi ? fj : fi;
                f[l] = m;
                s[m] += s[l];
            }
        }

        public void setTag(int i, int tag) {
            t[find(i)] = tag;
        }

        public int getTag(int i) {
            return t[find(i)];
        }
    }

    private static void process(int head, int[][] mt, int[][] mq, int[][] mi, UnionFind uf, int[] ans) {
        for (int next : mt[head]) {
            process(next, mt, mq, mi, uf, ans);
            uf.union(head, next);
            uf.setTag(head, head);
        }
        int[] q = mq[head];
        int[] i = mi[head];
        for (int k = 0; k < q.length; k++) {
            int tag = uf.getTag(q[k]);
            if (tag != -1) {
                ans[i[k]] = tag;
            }
        }
    }

    // 离线查询 Tarjan + 并查集，时间复杂度O(N + M)
    public static int[] query2(int[] father, int[][] queries) {
        int n = father.length;
        int m = queries.length;
        int[] help = new int[n];
        int h = 0;
        for (int i = 0; i < n; i++) {
            if (father[i] == i) {
                h = i;
            } else {
                help[father[i]]++;
            }
        }
        int[][] mt = new int[n][];
        for (int i = 0; i < n; i++) {
            mt[i] = new int[help[i]];
        }
        for (int i = 0; i < n; i++) {
            if (i != h) {
                mt[father[i]][--help[father[i]]] = i;
            }
        }
        for (int i = 0; i < m; i++) {
            if (queries[i][0] != queries[i][1]) {
                help[queries[i][0]]++;
                help[queries[i][1]]++;
            }
        }
        int[][] mq = new int[n][];
        int[][] mi = new int[n][];
        for (int i = 0; i < n; i++) {
            mq[i] = new int[help[i]];
            mi[i] = new int[help[i]];
        }
        for (int i = 0; i < m; i++) {
            if (queries[i][0] != queries[i][1]) {
                mq[queries[i][0]][--help[queries[i][0]]] = queries[i][1];
                mi[queries[i][0]][help[queries[i][0]]] = i;
                mq[queries[i][1]][--help[queries[i][1]]] = queries[i][0];
                mi[queries[i][1]][help[queries[i][1]]] = i;
            }
        }
        int[] ans = new int[m];
        UnionFind uf = new UnionFind(n);
        process(h, mt, mq, mi, uf, ans);
        for (int i = 0; i < m; i++) {
            if (queries[i][0] == queries[i][1]) {
                ans[i] = queries[i][0];
            }
        }
        return ans;
    }

    //

    private static class TreeChain {
        private int n;
        private int h;
        private int[][] tree;
        private int[] fa;
        private int[] dep;
        private int[] son;
        private int[] siz;
        private int[] top;

        public TreeChain(int[] father) {
            initTree(father);
            dfs1(h, 0);
            dfs2(h, h);
        }

        private void initTree(int[] father) {
            n = father.length + 1;
            tree = new int[n][];
            fa = new int[n];
            dep = new int[n];
            son = new int[n];
            siz = new int[n];
            top = new int[n--];
            int[] help = new int[n];

            for (int i = 0; i < n; i++) {
                if (father[i] == i) {
                    h = i + 1;
                } else {
                    help[father[i]]++;
                }
            }
            tree[0] = new int[0];
            for (int i = 0; i < n; i++) {
                tree[i + 1] = new int[help[i]];
            }
            for (int i = 0; i < n; i++) {
                if (i + 1 != h) {
                    tree[father[i] + 1][--help[father[i]]] = i + 1;
                }
            }
        }

        private void dfs1(int u, int f) {
            fa[u] = f;
            dep[u] = dep[f] + 1;
            siz[u] = 1;
            int maxSize = -1;
            for (int v : tree[u]) {
                dfs1(v, u);
                siz[u] += siz[v];
                if (siz[v] > maxSize) {
                    maxSize = siz[v];
                    son[u] = v;
                }
            }
        }

        private void dfs2(int u, int t) {
            top[u] = t;
            if (son[u] != 0) {
                dfs2(son[u], t);
                for (int v : tree[u]) {
                    if (v != son[u]) {
                        dfs2(v, v);
                    }
                }
            }
        }

        public int lca(int a, int b) {
            a++;
            b++;
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {
                    a = fa[top[a]];
                } else {
                    b = fa[top[b]];
                }
            }
            return (dep[a] < dep[b] ? a : b) - 1;
        }
    }

    // 在线查询，树链剖分，空间复杂度O(N), 时间复杂度O(N + M * logN);
    public static int[] query3(int[] father, int[][] queries) {
        TreeChain tc = new TreeChain(father);
        int m = queries.length;
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            if (queries[i][0] == queries[i][1]) {
                ans[i] = queries[i][0];
            } else {
                ans[i] = tc.lca(queries[i][0], queries[i][1]);
            }
        }
        return ans;
    }

    //

    private static int[] generateFather(int n) {
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        for (int i = n - 1; i >= 0; i--) {
            swap(order, i, (int) (Math.random() * (i + 1)));
        }
        int[] ans = new int[n];
        ans[order[0]] = order[0];
        for (int i = 1; i < n; i++) {
            ans[order[i]] = order[(int) (Math.random() * i)];
        }
        return ans;
    }

    private static int[][] generateQueries(int m, int n) {
        int[][] ans = new int[m][2];
        for (int i = 0; i < m; i++) {
            ans[i][0] = (int) (Math.random() * n);
            ans[i][1] = (int) (Math.random() * n);
        }
        return ans;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static boolean equal(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int n = 1000;
        int m = 200;
        int times = 50000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int size = (int) (Math.random() * n) + 1;
            int ques = (int) (Math.random() * m) + 1;
            int[] father = generateFather(size);
            int[][] queries = generateQueries(ques, size);
            int[] ans1 = query1(father, queries);
            int[] ans2 = query2(father, queries);
            int[] ans3 = query3(father, queries);
            if (!equal(ans1, ans2) || !equal(ans1, ans3)) {
                System.out.println("Wrong");
                break;

            }
        }
        System.out.println("test end");
    }
}
