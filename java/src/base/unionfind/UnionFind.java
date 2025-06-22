package base.unionfind;

import java.io.*;

/**
 * 并查集（Union-Find / Disjoint Set Union）数据结构实现
 * 用于处理动态连通性问题，支持合并集合和查询元素是否在同一集合中
 * 使用路径压缩和按大小合并优化，时间复杂度接近O(1)
 * https://www.nowcoder.com/questionTerminal/e7ed657974934a30b2010046536a5372
 */
public class UnionFind {
    public int MAXN = 1000001;      // 最大元素数量
    public int[] father = new int[MAXN];  // 父节点数组，father[i]表示i的父节点
    public int[] size = new int[MAXN];    // 集合大小数组，size[i]表示以i为根的集合大小
    public int[] help = new int[MAXN];    // 辅助数组，用于路径压缩时记录路径
    private int sets;                     // 当前集合数量

    /**
     * 构造函数：初始化并查集
     * @param n 元素数量，编号从0到n-1
     */
    public UnionFind(int n) {
        this.sets = n;  // 初始时每个元素都是一个独立的集合
        for (int i = 0; i < n; i++) {
            father[i] = i;  // 初始时每个元素的父节点是自己
            size[i] = 1;    // 初始时每个集合的大小是1
        }
    }

    /**
     * 查找元素i所在集合的根节点（代表元素）
     * 使用路径压缩优化：将查找路径上的所有节点直接连接到根节点
     * @param i 要查找的元素
     * @return 元素i所在集合的根节点
     */
    public int find(int i) {
        int hi = 0;  // help数组的索引
        // 第一阶段：找到根节点，同时记录路径
        while (i != father[i]) {
            help[hi++] = i;      // 记录路径上的节点
            i = father[i];       // 向上查找
        }
        // 第二阶段：路径压缩，将路径上的所有节点直接连接到根节点
        for (hi--; hi >= 0; hi--) {
            father[help[hi]] = i;
        }
        return i;  // 返回根节点
    }

    /**
     * 判断两个元素是否在同一个集合中
     * @param x 元素x
     * @param y 元素y
     * @return 是否在同一集合中
     */
    public boolean isSameSet(int x, int y) {
        return find(x) == find(y);
    }

    /**
     * 合并两个元素所在的集合
     * 使用按大小合并优化：将小集合合并到大集合中
     * @param x 元素x
     * @param y 元素y
     */
    public void union(int x, int y) {
        int fx = find(x);  // 找到x所在集合的根节点
        int fy = find(y);  // 找到y所在集合的根节点
        if (fx != fy) {    // 如果不在同一集合中才需要合并
            sets--;        // 合并后集合数量减1
            if (size[fx] >= size[fy]) {
                // 将小集合fy合并到大集合fx中
                size[fx] += size[fy];
                father[fy] = fx;
            } else {
                // 将小集合fx合并到大集合fy中
                size[fy] += size[fx];
                father[fx] = fy;
            }
        }
    }

    /**
     * 获取当前集合的数量
     * @return 集合数量
     */
    public int sets() {
        return sets;
    }

    /**
     * 主函数：处理输入输出，演示并查集的使用
     * 支持两种操作：
     * 1 x y：查询x和y是否在同一集合中
     * 其他 x y：将x和y所在的集合合并
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;  // 元素数量
            UnionFind unionFind = new UnionFind(n);
            in.nextToken();
            int m = (int) in.nval;  // 操作数量
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int op = (int) in.nval;  // 操作类型
                in.nextToken();
                int x = (int) in.nval;   // 操作的第一个元素
                in.nextToken();
                int y = (int) in.nval;   // 操作的第二个元素
                if (op == 1) {
                    // 查询操作：判断x和y是否在同一集合中
                    out.println(unionFind.isSameSet(x, y) ? "Yes" : "No");
                    out.flush();
                } else {
                    // 合并操作：将x和y所在的集合合并
                    unionFind.union(x, y);
                }
            }
        }
    }
}
