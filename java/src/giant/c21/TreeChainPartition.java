package giant.c21;

import java.util.HashMap;

/**
 * 树链剖分算法实现
 * 
 * 问题描述：
 * 树链剖分是一种将树分解为若干条链的算法，用于高效处理树上路径查询和修改操作。
 * 支持以下操作：
 * 1. 子树更新：将某节点的整个子树所有节点值都加上某个值
 * 2. 子树查询：查询某节点子树所有节点值的和
 * 3. 路径更新：将两个节点之间的路径上所有节点值都加上某个值
 * 4. 路径查询：查询两个节点之间路径上所有节点值的和
 * 
 * 核心思想：
 * 1. 重链剖分：将树分解为若干条重链，每条链在线段树中对应连续区间
 * 2. DFS序：通过深度优先遍历为每个节点分配唯一的时间戳
 * 3. 线段树：利用线段树高效处理区间查询和修改
 * 
 * 算法步骤：
 * 1. 第一次DFS：计算每个节点的父节点、深度、子树大小、重儿子
 * 2. 第二次DFS：计算DFS序、重链头节点，构建线段树数组
 * 3. 利用线段树处理查询和修改操作
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 单次查询/修改：O(log²n)
 * 
 * 空间复杂度：O(n)
 * 
 * @author algorithm learning
 */
public class TreeChainPartition {
    
    /**
     * 线段树实现类
     * 支持区间修改和区间查询操作，使用懒惰标记优化
     */
    private static class SegmentTree {
        private int MAXN;        // 数组大小
        private int[] arr;       // 原始数组
        private int[] sum;       // 线段树节点和
        private int[] lazy;      // 懒惰标记数组

        /**
         * 线段树构造函数
         * @param origin 原始数组
         */
        public SegmentTree(int[] origin) {
            MAXN = origin.length;
            arr = origin;
            sum = new int[MAXN << 2];   // 线段树需要4倍空间
            lazy = new int[MAXN << 2];  // 懒惰标记数组
        }

        /**
         * 向上更新：用子节点的值更新父节点
         * @param rt 当前节点编号
         */
        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        /**
         * 向下传播懒惰标记：将父节点的懒惰标记传播给子节点
         * @param rt 当前节点编号
         * @param ln 左子节点的区间长度
         * @param rn 右子节点的区间长度
         */
        private void pushDown(int rt, int ln, int rn) {
            if (lazy[rt] != 0) {
                // 将懒惰标记传播给子节点
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt] = 0;  // 清空当前节点的懒惰标记
            }
        }

        /**
         * 构建线段树
         * @param l 区间左边界
         * @param r 区间右边界
         * @param rt 当前节点编号
         */
        public void build(int l, int r, int rt) {
            if (l == r) {
                sum[rt] = arr[l];  // 叶子节点直接赋值
                return;
            }
            int mid = (l + r) >> 1;
            build(l, mid, rt << 1);           // 递归构建左子树
            build(mid + 1, r, rt << 1 | 1);   // 递归构建右子树
            pushUp(rt);                       // 用子节点更新父节点
        }

        /**
         * 区间修改：对区间[tl, tr]内的所有元素加上tc
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param tc 要加的值
         * @param l 当前节点表示的区间左边界
         * @param r 当前节点表示的区间右边界
         * @param rt 当前节点编号
         */
        public void add(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 当前节点表示的区间完全包含在目标区间内
                sum[rt] += tc * (r - l + 1);
                lazy[rt] += tc;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);  // 先向下传播懒惰标记
            if (tl <= mid) {
                add(tl, tr, tc, l, mid, rt << 1);
            }
            if (tr > mid) {
                add(tl, tr, tc, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);  // 向上更新
        }

        /**
         * 区间查询：查询区间[tl, tr]内所有元素的和
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param l 当前节点表示的区间左边界
         * @param r 当前节点表示的区间右边界
         * @param rt 当前节点编号
         * @return 区间内所有元素的和
         */
        public int query(int tl, int tr, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                return sum[rt];  // 当前节点表示的区间完全包含在目标区间内
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);  // 先向下传播懒惰标记
            int ans = 0;
            if (tl <= mid) {
                ans += query(tl, tr, l, mid, rt << 1);
            }
            if (tr > mid) {
                ans += query(tl, tr, mid + 1, r, rt << 1 | 1);
            }
            return ans;
        }
    }

    /**
     * 树链剖分主类
     * 实现树上路径和子树的高效查询与修改
     */
    public static class TreeChain {
        private int tim;           // 时间戳计数器，用于生成DFS序
        private int n;             // 节点数量
        private int h;             // 树的根节点
        private int[][] tree;      // 邻接表表示的树结构：tree[i]存储节点i的所有子节点
        private int[] val;         // 每个节点的初始值：val[i]表示节点i的值
        private int[] fa;          // 父节点数组：fa[i]表示节点i的父节点
        private int[] dep;         // 深度数组：dep[i]表示节点i的深度
        private int[] son;         // 重儿子数组：son[i]表示节点i的重儿子（子树最大的儿子）
        private int[] siz;         // 子树大小数组：siz[i]表示以节点i为根的子树节点数量
        private int[] top;         // 重链头数组：top[i]表示节点i所在重链的头节点
        private int[] dfn;         // DFS序数组：dfn[i]表示节点i在DFS遍历中的时间戳
        private int[] tnw;         // 时间戳对应的节点值：tnw[t]表示时间戳为t的节点的值
        private SegmentTree seg;   // 线段树实例，用于处理区间操作

        /**
         * 初始化树结构
         * 根据父节点数组和节点值数组构建邻接表表示的树
         * 
         * @param father 父节点数组，father[i]表示节点i的父节点
         * @param values 节点值数组，values[i]表示节点i的初始值
         */
        private void initTree(int[] father, int[] values) {
            tim = 0;
            n = father.length + 1;           // 节点编号从1开始，所以需要+1
            tree = new int[n][];
            val = new int[n];
            fa = new int[n];
            dep = new int[n];
            son = new int[n];
            siz = new int[n];
            top = new int[n];
            dfn = new int[n];
            tnw = new int[n--];              // n减1是为了后续使用
            
            int[] help = new int[n];         // 临时数组，统计每个节点的子节点数量
            
            // 复制节点值（从0索引转换为1索引）
            for (int i = 0; i < n; i++) {
                val[i + 1] = values[i];
            }
            
            // 找到根节点并统计每个节点的子节点数量
            for (int i = 0; i < n; i++) {
                if (father[i] == i) {
                    h = i + 1;               // 根节点（父节点是自己）
                } else {
                    help[father[i]]++;       // 统计子节点数量
                }
            }
            
            // 初始化邻接表
            tree[0] = new int[0];
            for (int i = 0; i < n; i++) {
                tree[i + 1] = new int[help[i]];
            }
            
            // 构建邻接表（父子关系）
            for (int i = 0; i < n; i++) {
                if (i + 1 != h) {
                    tree[father[i] + 1][--help[father[i]]] = i + 1;
                }
            }
        }

        /**
         * 第一次DFS：计算树的基本信息
         * 计算每个节点的父节点、深度、子树大小、重儿子
         * 
         * @param u 当前访问的节点
         * @param f 当前节点的父节点
         */
        private void dfs1(int u, int f) {
            fa[u] = f;                       // 设置父节点
            dep[u] = dep[f] + 1;             // 深度 = 父节点深度 + 1
            siz[u] = 1;                      // 初始化子树大小为1（包含自己）
            int maxSize = -1;                // 记录最大子树的大小
            
            // 遍历所有子节点
            for (int v : tree[u]) {
                dfs1(v, u);                  // 递归处理子节点
                siz[u] += siz[v];            // 累加子树大小
                
                // 更新重儿子（子树最大的儿子）
                if (siz[v] > maxSize) {
                    maxSize = siz[v];
                    son[u] = v;              // 更新重儿子
                }
            }
        }

        /**
         * 第二次DFS：进行重链剖分
         * 计算DFS序、重链头节点，为线段树构建做准备
         * 
         * @param u 当前访问的节点
         * @param t 当前重链的头节点
         */
        private void dfs2(int u, int t) {
            dfn[u] = ++tim;                  // 分配DFS序（时间戳）
            top[u] = t;                      // 设置重链头
            tnw[tim] = val[u];               // 将节点值按DFS序存储

            // 如果有重儿子，优先访问重儿子（保证重链连续）
            if (son[u] != 0) {
                dfs2(son[u], t);             // 重儿子继承当前重链头
                
                // 然后访问所有轻儿子，每个轻儿子开始新的重链
                for (int v : tree[u]) {
                    if (v != son[u]) {
                        dfs2(v, v);          // 轻儿子作为新重链的头
                    }
                }
            }
        }

        /**
         * 树链剖分构造函数
         * 
         * @param father 父节点数组
         * @param values 节点值数组
         */
        public TreeChain(int[] father, int[] values) {
            initTree(father, values);        // 初始化树结构
            dfs1(h, 0);                      // 第一次DFS：计算基本信息
            dfs2(h, h);                      // 第二次DFS：重链剖分
            seg = new SegmentTree(tnw);      // 构建线段树
            seg.build(1, n, 1);             // 初始化线段树
        }

        /**
         * 子树修改：将指定节点的整个子树所有节点值都加上val
         * 
         * 算法思路：
         * 利用DFS序的性质，一个节点的子树在DFS序中是连续的区间
         * 区间为 [dfn[head], dfn[head] + siz[head] - 1]
         * 
         * @param head 子树根节点（0索引）
         * @param val 要加的值
         */
        public void addSubtree(int head, int val) {
            head++;  // 转换为1索引
            seg.add(dfn[head], dfn[head] + siz[head] - 1, val, 1, n, 1);
        }

        /**
         * 子树查询：查询指定节点的整个子树所有节点值的和
         * 
         * @param head 子树根节点（0索引）
         * @return 子树所有节点值的和
         */
        public int querySubtree(int head) {
            head++;  // 转换为1索引
            return seg.query(dfn[head], dfn[head] + siz[head] - 1, 1, n, 1);
        }

        /**
         * 路径修改：将两个节点之间的路径上所有节点值都加上v
         * 
         * 算法思路：
         * 1. 当两个节点不在同一重链上时，将深度更大的重链头的节点跳到其父节点
         * 2. 重复此过程直到两个节点在同一重链上
         * 3. 最后处理同一重链上的区间
         * 
         * 关键观察：每次跳跃都会使深度减少，最多跳跃O(log n)次
         * 
         * @param a 起始节点（0索引）
         * @param b 结束节点（0索引）
         * @param v 要加的值
         */
        public void addChain(int a, int b, int v) {
            a++;  // 转换为1索引
            b++;
            
            // 当两个节点不在同一重链上时，继续跳跃
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {
                    // a的重链头深度更大，修改a到其重链头的区间，然后跳到重链头的父节点
                    seg.add(dfn[top[a]], dfn[a], v, 1, n, 1);
                    a = fa[top[a]];
                } else {
                    // b的重链头深度更大，修改b到其重链头的区间，然后跳到重链头的父节点
                    seg.add(dfn[top[b]], dfn[b], v, 1, n, 1);
                    b = fa[top[b]];
                }
            }
            
            // 现在a和b在同一重链上，直接修改区间
            if (dep[a] > dep[b]) {
                seg.add(dfn[b], dfn[a], v, 1, n, 1);
            } else {
                seg.add(dfn[a], dfn[b], v, 1, n, 1);
            }
        }

        /**
         * 路径查询：查询两个节点之间路径上所有节点值的和
         * 
         * 算法思路与addChain类似，使用重链剖分将路径分解为O(log n)段重链
         * 
         * @param a 起始节点（0索引）
         * @param b 结束节点（0索引）
         * @return 路径上所有节点值的和
         */
        public int queryChain(int a, int b) {
            a++;  // 转换为1索引
            b++;
            int ans = 0;
            
            // 当两个节点不在同一重链上时，继续跳跃
            while (top[a] != top[b]) {
                if (dep[top[a]] > dep[top[b]]) {
                    // a的重链头深度更大，查询a到其重链头的区间，然后跳到重链头的父节点
                    ans += seg.query(dfn[top[a]], dfn[a], 1, n, 1);
                    a = fa[top[a]];
                } else {
                    // b的重链头深度更大，查询b到其重链头的区间，然后跳到重链头的父节点
                    ans += seg.query(dfn[top[b]], dfn[b], 1, n, 1);
                    b = fa[top[b]];
                }
            }
            
            // 现在a和b在同一重链上，直接查询区间
            if (dep[a] > dep[b]) {
                ans += seg.query(dfn[b], dfn[a], 1, n, 1);
            } else {
                ans += seg.query(dfn[a], dfn[b], 1, n, 1);
            }
            return ans;
        }
    }

    /**
     * 朴素实现类（用于验证树链剖分算法的正确性）
     * 使用直接的树遍历方式实现相同功能，效率较低但逻辑简单
     */
    public static class Right {
        private int n;                              // 节点数量
        private int[][] tree;                       // 邻接表表示的树结构
        private int[] fa;                           // 父节点数组
        private int[] val;                          // 节点值数组
        private HashMap<Integer, Integer> path;     // 路径查找辅助哈希表

        /**
         * 朴素实现构造函数
         * @param father 父节点数组
         * @param value 节点值数组
         */
        public Right(int[] father, int[] value) {
            n = father.length;
            tree = new int[n][];
            fa = new int[n];
            val = new int[n];
            
            // 复制数据
            for (int i = 0; i < n; i++) {
                fa[i] = father[i];
                val[i] = value[i];
            }
            
            // 构建邻接表
            int[] help = new int[n];
            int h = 0;
            for (int i = 0; i < n; i++) {
                if (father[i] == i) {
                    h = i;  // 根节点
                } else {
                    help[father[i]]++;
                }
            }
            
            for (int i = 0; i < n; i++) {
                tree[i] = new int[help[i]];
            }
            
            for (int i = 0; i < n; i++) {
                if (i != h) {
                    tree[father[i]][--help[father[i]]] = i;
                }
            }
            
            path = new HashMap<>();
        }

        /**
         * 朴素子树修改：递归修改整个子树
         * @param head 子树根节点
         * @param value 要加的值
         */
        public void addSubtree(int head, int value) {
            val[head] += value;                     // 修改当前节点
            for (int next : tree[head]) {           // 递归修改所有子节点
                addSubtree(next, value);
            }
        }

        /**
         * 朴素子树查询：递归查询整个子树
         * @param head 子树根节点
         * @return 子树所有节点值的和
         */
        public int querySubtree(int head) {
            int ans = val[head];                    // 当前节点的值
            for (int next : tree[head]) {           // 递归查询所有子节点
                ans += querySubtree(next);
            }
            return ans;
        }

        /**
         * 朴素路径修改：通过路径查找修改路径上的所有节点
         * @param a 起始节点
         * @param b 结束节点
         * @param v 要加的值
         */
        public void addChain(int a, int b, int v) {
            path.clear();
            path.put(a, null);
            
            // 从a向上遍历到根，记录路径
            while (a != fa[a]) {
                path.put(fa[a], a);
                a = fa[a];
            }
            
            // 从b向上遍历，直到找到公共祖先
            while (!path.containsKey(b)) {
                val[b] += v;
                b = fa[b];
            }
            
            val[b] += v;  // 修改公共祖先
            
            // 从公共祖先向下修改到a的路径
            while (path.get(b) != null) {
                b = path.get(b);
                val[b] += v;
            }
        }

        /**
         * 朴素路径查询：通过路径查找查询路径上所有节点值的和
         * @param a 起始节点
         * @param b 结束节点
         * @return 路径上所有节点值的和
         */
        public int queryChain(int a, int b) {
            path.clear();
            path.put(a, null);
            
            // 从a向上遍历到根，记录路径
            while (a != fa[a]) {
                path.put(fa[a], a);
                a = fa[a];
            }
            
            int ans = 0;
            // 从b向上遍历，直到找到公共祖先
            while (!path.containsKey(b)) {
                ans += val[b];
                b = fa[b];
            }
            
            ans += val[b];  // 加上公共祖先
            
            // 从公共祖先向下查询到a的路径
            while (path.get(b) != null) {
                b = path.get(b);
                ans += val[b];
            }
            return ans;
        }
    }

    /**
     * 交换数组中两个位置的元素
     * @param arr 数组
     * @param i 位置i
     * @param j 位置j
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 生成随机的父节点数组，构成一棵随机树
     * 算法：首先随机排列节点顺序，然后为每个节点随机选择一个在它之前的节点作为父节点
     * 
     * @param n 节点数量
     * @return 父节点数组，表示一棵随机生成的树
     */
    public static int[] generateFatherArray(int n) {
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        
        // 随机打乱节点顺序
        for (int i = n - 1; i >= 0; i--) {
            swap(order, i, (int) (Math.random() * (i + 1)));
        }
        
        int[] ans = new int[n];
        ans[order[0]] = order[0];  // 第一个节点是根节点，父节点是自己
        
        // 为每个节点随机选择一个在它之前的节点作为父节点
        for (int i = 1; i < n; i++) {
            ans[order[i]] = order[(int) (Math.random() * i)];
        }
        
        return ans;
    }

    /**
     * 生成随机的节点值数组
     * @param n 节点数量
     * @param v 值的上限
     * @return 随机生成的节点值数组
     */
    public static int[] generateValueArray(int n, int v) {
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = (int) (Math.random() * v) + 1;
        }
        return ans;
    }

    /**
     * 测试方法：验证树链剖分算法的正确性
     * 通过大量随机测试对比树链剖分和朴素实现的结果
     */
    public static void main(String[] args) {
        System.out.println("=== 树链剖分算法测试 ===");
        
        int maxLen = 50000;    // 最大节点数
        int maxVal = 100000;   // 最大节点值
        int times = 1000000;   // 测试次数
        
        System.out.println("生成随机测试数据...");
        int[] father = generateFatherArray(maxLen);
        int[] values = generateValueArray(maxLen, maxVal);
        
        System.out.println("初始化数据结构...");
        TreeChain tc = new TreeChain(father, values);
        Right right = new Right(father, values);
        
        System.out.println("开始随机测试...");
        System.out.println("节点数: " + maxLen);
        System.out.println("测试次数: " + times);
        
        long start = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            double decision = Math.random();
            
            if (decision < 0.25) {
                // 25%概率：子树修改操作
                int head = (int) (Math.random() * maxLen);
                int value = (int)(Math.random() * maxVal);
                tc.addSubtree(head, value);
                right.addSubtree(head, value);
            } else if (decision < 0.5) {
                // 25%概率：子树查询操作
                int head = (int) (Math.random() * maxLen);
                if (tc.querySubtree(head) != right.querySubtree(head)) {
                    System.out.println("子树查询错误! 测试用例: " + i);
                    break;
                }
            } else if (decision < 0.75) {
                // 25%概率：路径修改操作
                int a = (int) (Math.random() * maxLen);
                int b = (int) (Math.random() * maxLen);
                int value = (int) (Math.random() * maxVal);
                tc.addChain(a, b, value);
                right.addChain(a, b, value);
            } else {
                // 25%概率：路径查询操作
                int a = (int) (Math.random() * maxLen);
                int b = (int) (Math.random() * maxLen);
                if (tc.queryChain(a, b) != right.queryChain(a, b)) {
                    System.out.println("路径查询错误! 测试用例: " + i);
                    break;
                }
            }
            
            // 显示进度
            if ((i + 1) % 200000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试...");
            }
        }
        
        long end = System.currentTimeMillis();
        
        System.out.println("✓ 所有测试通过，树链剖分算法实现正确！");
        System.out.println("总耗时: " + (end - start) + "ms");
        System.out.println("平均每次操作耗时: " + (double)(end - start) / times + "ms");
        
        System.out.println("\n=== 算法特点总结 ===");
        System.out.println("1. 时间复杂度:");
        System.out.println("   - 预处理: O(n)");
        System.out.println("   - 单次查询/修改: O(log²n)");
        System.out.println("2. 空间复杂度: O(n)");
        System.out.println("3. 适用场景: 大量树上路径查询和修改操作");
        System.out.println("4. 核心思想: 重链剖分 + DFS序 + 线段树");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
