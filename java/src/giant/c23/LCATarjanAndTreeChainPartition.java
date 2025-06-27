package giant.c23;

import java.util.HashSet;

/**
 * 最近公共祖先(LCA)问题的三种解法
 * 
 * 问题描述：
 * 给定一棵树和多个查询，每个查询要求找到两个节点的最近公共祖先(Lowest Common Ancestor)
 * 
 * 本类实现了三种不同的LCA算法：
 * 1. 朴素算法：对每个查询单独处理，时间复杂度O(Q*N)
 * 2. Tarjan离线算法 + 并查集：批量处理所有查询，时间复杂度O(N+Q)
 * 3. 重链剖分算法：预处理后可快速回答查询，时间复杂度O(NlogN + QlogN)
 * 
 * 算法对比：
 * - 朴素算法：实现简单，但效率低，适合查询次数少的情况
 * - Tarjan算法：离线算法，需要提前知道所有查询，效率高但不支持在线查询
 * - 重链剖分：支持在线查询，预处理后查询效率高，适合查询次数多的情况
 */
public class LCATarjanAndTreeChainPartition {
    
    /**
     * 方法1：朴素LCA算法
     * 对每个查询，从两个节点分别向上找父节点，直到找到共同祖先
     * 
     * @param father 父节点数组，father[i]表示节点i的父节点
     * @param queries 查询数组，每个查询包含两个节点
     * @return 每个查询对应的LCA结果
     * 
     * 算法思路：
     * 1. 对于每个查询(a,b)，从a开始向上遍历到根，记录路径上所有节点
     * 2. 从b开始向上遍历，找到第一个在a的路径中出现的节点
     * 3. 该节点就是a和b的最近公共祖先
     * 
     * 时间复杂度：O(Q*N)，其中Q是查询数，N是节点数
     * 空间复杂度：O(N)，用于存储路径
     */
    public static int[] query1(int[] father, int[][] queries) {
        int m = queries.length;
        int[] ans = new int[m];
        HashSet<Integer> path = new HashSet<>(); // 存储从第一个节点到根的路径
        
        for (int i = 0; i < m; i++) {
            // 从第一个节点向上遍历到根，记录路径
            int jump = queries[i][0];
            while (father[jump] != jump) {
                path.add(jump);
                jump = father[jump];
            }
            path.add(jump); // 添加根节点
            
            // 从第二个节点向上遍历，找到第一个在路径中的节点
            jump = queries[i][1];
            while (!path.contains(jump)) {
                jump = father[jump];
            }
            ans[i] = jump; // 找到的节点就是LCA
            path.clear(); // 清空路径，准备下一次查询
        }
        return ans;
    }

    /**
     * 并查集数据结构
     * 支持合并操作和标记功能，用于Tarjan离线LCA算法
     */
    private static class UnionFind {
        // father：并查集父节点数组
        private int[] f;
        // size：集合大小数组  
        private int[] s;
        // tag：集合标记数组，用于标记LCA
        private int[] t;
        // help：路径压缩辅助数组
        private int[] h;

        /**
         * 初始化并查集
         * 
         * @param n 节点数量
         */
        public UnionFind(int n) {
            f = new int[n];
            s = new int[n];
            t = new int[n];
            h = new int[n];
            for (int i = 0; i < n; i++) {
                f[i] = i;     // 初始时每个节点的父节点是自己
                s[i] = 1;     // 初始时每个集合大小为1
                t[i] = -1;    // 初始时没有标记
            }
        }

        /**
         * 查找操作（带路径压缩）
         * 
         * @param i 要查找的节点
         * @return 该节点所在集合的代表元素
         */
        private int find(int i) {
            int j = 0;
            // 向上查找根节点，同时记录路径
            while (i != f[i]) {
                h[j++] = i;
                i = f[i];
            }
            // 路径压缩：将路径上所有节点直接指向根
            while (j > 0) {
                f[h[--j]] = i;
            }
            return i;
        }

        /**
         * 合并两个集合
         * 
         * @param i 第一个节点
         * @param j 第二个节点
         */
        public void union(int i, int j) {
            int fi = find(i);
            int fj = find(j);
            if (fi != fj) {
                int si = s[fi], sj = s[fj];
                // 按秩合并：将小集合合并到大集合
                int m = si >= sj ? fi : fj;
                int l = m == fi ? fj : fi;
                f[l] = m;
                s[m] += s[l];
            }
        }

        /**
         * 为集合设置标记
         * 
         * @param i 节点
         * @param tag 标记值
         */
        public void setTag(int i, int tag) {
            t[find(i)] = tag;
        }

        /**
         * 获取集合的标记
         * 
         * @param i 节点
         * @return 该节点所在集合的标记
         */
        public int getTag(int i) {
            return t[find(i)];
        }
    }

    /**
     * Tarjan离线LCA算法的核心递归过程
     * 
     * @param head 当前处理的节点
     * @param mt 邻接表：children of each node
     * @param mq 查询邻接表：与当前节点相关的查询的另一个节点
     * @param mi 查询索引表：与当前节点相关的查询在结果数组中的索引
     * @param uf 并查集
     * @param ans 结果数组
     * 
     * 算法核心思想：
     * 1. DFS遍历整棵树
     * 2. 当回溯到某个节点时，该节点的子树已全部处理完
     * 3. 此时可以回答所有涉及该节点且另一个节点已被访问过的查询
     * 4. 利用并查集维护已访问节点的祖先关系
     */
    private static void process(int head, int[][] mt, int[][] mq, int[][] mi, UnionFind uf, int[] ans) {
        // 递归处理所有子节点
        for (int next : mt[head]) {
            process(next, mt, mq, mi, uf, ans);
            uf.union(head, next);      // 合并当前节点和子节点
            uf.setTag(head, head);     // 设置合并后集合的标记为当前节点
        }
        
        // 处理与当前节点相关的查询
        int[] q = mq[head];  // 与当前节点相关的查询的另一个节点
        int[] i = mi[head];  // 对应查询在结果数组中的索引
        for (int k = 0; k < q.length; k++) {
            int tag = uf.getTag(q[k]);
            if (tag != -1) {
                // 如果查询的另一个节点已经被访问过，其LCA就是该节点所在集合的标记
                ans[i[k]] = tag;
            }
        }
    }

    /**
     * 方法2：Tarjan离线LCA算法 + 并查集
     * 
     * @param father 父节点数组
     * @param queries 查询数组
     * @return LCA结果数组
     * 
     * 算法优势：
     * 1. 只需要一次DFS遍历就能回答所有查询
     * 2. 时间复杂度优化到O(N + M)，其中N是节点数，M是查询数
     * 3. 特别适合查询数量很大的场景
     * 
     * 算法步骤：
     * 1. 构建树的邻接表表示
     * 2. 构建查询的邻接表表示
     * 3. 从根节点开始DFS遍历
     * 4. 利用并查集维护节点间的祖先关系
     * 5. 在回溯过程中回答查询
     * 
     * 时间复杂度：O(N + M * α(N))，其中α是反阿克曼函数，几乎为常数
     * 空间复杂度：O(N + M)
     */
    // 离线查询 Tarjan + 并查集，时间复杂度O(N + M)
    public static int[] query2(int[] father, int[][] queries) {
        int n = father.length;
        int m = queries.length;
        int[] help = new int[n];
        int h = 0;
        
        // 找根节点并统计每个节点的子节点数量
        for (int i = 0; i < n; i++) {
            if (father[i] == i) {
                h = i; // 找到根节点
            } else {
                help[father[i]]++; // 统计子节点数量
            }
        }
        
        // 构建树的邻接表
        int[][] mt = new int[n][];
        for (int i = 0; i < n; i++) {
            mt[i] = new int[help[i]];
        }
        for (int i = 0; i < n; i++) {
            if (i != h) {
                mt[father[i]][--help[father[i]]] = i;
            }
        }
        
        // 统计每个节点相关的查询数量
        for (int i = 0; i < m; i++) {
            if (queries[i][0] != queries[i][1]) {
                help[queries[i][0]]++;
                help[queries[i][1]]++;
            }
        }
        
        // 构建查询的邻接表
        int[][] mq = new int[n][]; // 存储与每个节点相关的查询的另一个节点
        int[][] mi = new int[n][]; // 存储对应查询的索引
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
        
        // 执行Tarjan算法
        int[] ans = new int[m];
        UnionFind uf = new UnionFind(n);
        process(h, mt, mq, mi, uf, ans);
        
        // 处理查询两个节点相同的情况
        for (int i = 0; i < m; i++) {
            if (queries[i][0] == queries[i][1]) {
                ans[i] = queries[i][0];
            }
        }
        return ans;
    }

    /**
     * 重链剖分数据结构
     * 将树分解为若干条重链，支持快速LCA查询
     */
    private static class TreeChain {
        private int n;       // 节点数量
        private int h;       // 根节点
        private int[][] tree; // 树的邻接表
        private int[] fa;    // 父节点数组
        private int[] dep;   // 深度数组
        private int[] son;   // 重儿子数组
        private int[] siz;   // 子树大小数组
        private int[] top;   // 重链顶端数组

        /**
         * 构造重链剖分
         * 
         * @param father 父节点数组
         */
        public TreeChain(int[] father) {
            initTree(father);
            dfs1(h, 0);    // 第一次DFS：计算深度、父节点、重儿子、子树大小
            dfs2(h, h);    // 第二次DFS：计算重链顶端
        }

        /**
         * 初始化树结构
         * 
         * @param father 父节点数组
         */
        private void initTree(int[] father) {
            n = father.length + 1;
            tree = new int[n][];
            fa = new int[n];
            dep = new int[n];
            son = new int[n];
            siz = new int[n];
            top = new int[n--];
            int[] help = new int[n];

            // 找根节点并统计子节点数量
            for (int i = 0; i < n; i++) {
                if (father[i] == i) {
                    h = i + 1; // 节点编号从1开始
                } else {
                    help[father[i]]++;
                }
            }
            
            // 构建邻接表
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

        /**
         * 第一次DFS：计算基本信息
         * 
         * @param u 当前节点
         * @param f 父节点
         */
        private void dfs1(int u, int f) {
            fa[u] = f;
            dep[u] = dep[f] + 1;
            siz[u] = 1;
            
            for (int v : tree[u]) {
                dfs1(v, u);
                siz[u] += siz[v];
                // 找重儿子（子树最大的儿子）
                if (siz[v] > siz[son[u]]) {
                    son[u] = v;
                }
            }
        }

        /**
         * 第二次DFS：计算重链顶端
         * 
         * @param u 当前节点
         * @param t 重链顶端
         */
        private void dfs2(int u, int t) {
            top[u] = t;
            
            // 优先处理重儿子
            if (son[u] != 0) {
                dfs2(son[u], t); // 重儿子继承同一个重链顶端
            }
            
            // 处理轻儿子
            for (int v : tree[u]) {
                if (v != son[u]) {
                    dfs2(v, v); // 轻儿子开始新的重链
                }
            }
        }

        /**
         * 查询两个节点的LCA
         * 
         * @param a 第一个节点
         * @param b 第二个节点
         * @return 最近公共祖先
         * 
         * 算法思路：
         * 1. 将两个节点分别向上跳到各自重链的顶端
         * 2. 比较两个重链顶端的深度，将深度大的向上跳一层
         * 3. 重复步骤1-2，直到两个节点在同一条重链上
         * 4. 返回深度较小的节点（即LCA）
         */
        public int lca(int a, int b) {
            while (top[a] != top[b]) {
                // 将深度较大的重链顶端向上跳
                if (dep[top[a]] > dep[top[b]]) {
                    a = fa[top[a]];
                } else {
                    b = fa[top[b]];
                }
            }
            // 两个节点在同一条重链上，返回深度较小的
            return dep[a] < dep[b] ? a : b;
        }
    }

    /**
     * 方法3：重链剖分LCA算法
     * 
     * @param father 父节点数组
     * @param queries 查询数组
     * @return LCA结果数组
     * 
     * 算法特点：
     * 1. 支持在线查询，无需提前知道所有查询
     * 2. 预处理时间复杂度O(N)，单次查询时间复杂度O(logN)
     * 3. 适合查询次数多且需要在线回答的场景
     * 
     * 重链剖分原理：
     * 1. 将树分解为若干条不相交的重链
     * 2. 重链：从某个节点开始，每次选择子树最大的儿子，形成的路径
     * 3. 任意两个节点的LCA查询可以转化为在重链上的跳跃
     * 4. 由于重链的性质，跳跃次数不超过O(logN)
     * 
     * 时间复杂度：预处理O(N) + 查询O(Q*logN)
     * 空间复杂度：O(N)
     */
    public static int[] query3(int[] father, int[][] queries) {
        TreeChain tc = new TreeChain(father);
        int m = queries.length;
        int[] ans = new int[m];
        
        for (int i = 0; i < m; i++) {
            // 节点编号需要+1，因为TreeChain内部从1开始编号
            ans[i] = tc.lca(queries[i][0] + 1, queries[i][1] + 1) - 1;
        }
        return ans;
    }

    /**
     * 生成随机父节点数组用于测试
     * 
     * @param n 节点数量
     * @return 随机生成的父节点数组
     */
    private static int[] generateFather(int n) {
        int[] father = new int[n];
        for (int i = 0; i < n; i++) {
            father[i] = i; // 初始时每个节点的父节点是自己
        }
        
        // 随机选择一些节点，为它们分配父节点
        for (int i = 1; i < n; i++) {
            // 为节点i随机选择一个编号小于i的父节点
            father[i] = (int) (Math.random() * i);
        }
        return father;
    }

    /**
     * 生成随机查询用于测试
     * 
     * @param m 查询数量
     * @param n 节点数量
     * @return 随机生成的查询数组
     */
    private static int[][] generateQueries(int m, int n) {
        int[][] queries = new int[m][2];
        for (int i = 0; i < m; i++) {
            queries[i][0] = (int) (Math.random() * n);
            queries[i][1] = (int) (Math.random() * n);
        }
        return queries;
    }

    /**
     * 交换数组中两个位置的元素
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 判断两个数组是否相等
     */
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

    /**
     * 测试方法：验证三种LCA算法的正确性
     */
    public static void main(String[] args) {
        int times = 1000;   // 测试次数
        int n = 50;         // 最大节点数
        int m = 500;        // 最大查询数
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int nodeNum = (int) (Math.random() * n) + 2;
            int queryNum = (int) (Math.random() * m) + 1;
            
            int[] father = generateFather(nodeNum);
            int[][] queries = generateQueries(queryNum, nodeNum);
            
            // 对比三种方法的结果
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
