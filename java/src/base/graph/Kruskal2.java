package base.graph;

import java.io.*;
import java.util.Arrays;

/**
 * Kruskal算法的数组优化版本
 * 使用数组实现并查集，提高性能
 * 题目链接：https://www.nowcoder.com/questionTerminal/c23eab7bb39748b6b224a8a3afbe396b
 */
public class Kruskal2 {
    public static int MAXM = 100001;
    public static int[][] edges = new int[MAXM][3];  // 存储边信息：[起点, 终点, 权重]

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;  // 节点数
            in.nextToken();
            int m = (int) in.nval;  // 边数
            
            // 读取所有边的信息
            for (int i = 0; i < m; i++) {
                in.nextToken();
                edges[i][0] = (int) in.nval;  // 起点
                in.nextToken();
                edges[i][1] = (int) in.nval;  // 终点
                in.nextToken();
                edges[i][2] = (int) in.nval;  // 权重
            }
            
            // 按权重对边进行排序
            Arrays.sort(edges, 0, m, (a, b) -> a[2] - b[2]);
            
            // 初始化并查集
            build(n);
            int ans = 0;
            
            // 依次处理每条边，构建最小生成树
            for (int i = 0; i < m; i++) {
                if (union(edges[i][0], edges[i][1])) {
                    ans += edges[i][2];
                }
            }
            out.println(ans);
            out.flush();
        }
    }

    public static int MAXN = 10001;
    public static int[] father = new int[MAXN];  // 并查集父节点数组
    public static int[] size = new int[MAXN];    // 集合大小数组
    public static int[] help = new int[MAXN];    // 路径压缩辅助数组

    /**
     * 初始化并查集
     * @param n 节点数量
     */
    public static void build(int n) {
        for (int i = 1; i <= n; i++) {
            father[i] = i;  // 每个节点的父节点初始化为自己
            size[i] = 1;    // 每个集合的大小初始化为1
        }
    }

    /**
     * 查找节点的根节点，使用路径压缩优化
     * @param i 要查找的节点
     * @return 节点所在集合的根节点
     */
    private static int find(int i) {
        int hi = 0;
        // 沿着父节点链向上找到根节点
        while (i != father[i]) {
            help[hi++] = i;
            i = father[i];
        }
        // 路径压缩：将路径上的所有节点直接指向根节点
        while (hi > 0) {
            father[help[--hi]] = i;
        }
        return i;
    }

    /**
     * 合并两个节点所在的集合
     * @param a 节点a
     * @param b 节点b
     * @return 如果两个节点原本不在同一集合中则返回true，否则返回false
     */
    private static boolean union(int a, int b) {
        int fa = find(a);  // 找到a的根节点
        int fb = find(b);  // 找到b的根节点
        if (fa == fb) {    // 如果已经在同一集合中
            return false;
        }
        // 按秩合并：将小集合合并到大集合中
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
