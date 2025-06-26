package giant.c41;

import java.util.PriorityQueue;

/**
 * 双世界最短路径问题
 * 
 * 问题描述：
 * N个结点之间，表世界存在双向通行的道路，里世界存在双向通行的传送门
 * - 若走表世界的道路，花费一分钟
 * - 若走里世界的传送门，不花费时间，但是接下来一分钟不能走传送门
 * 
 * 输入格式：
 * - T为测试用例的组数，对于每组数据:
 * - 第一行：N M1 M2（N代表结点的个数1到N）
 * - 接下来M1行：每行两个数u和v，表示表世界u和v之间存在道路
 * - 接下来M2行：每行两个数u和v，表示里世界u和v之间存在传送门
 * 
 * 求解目标：
 * 现在处于1号结点，最终要到达N号结点，求最小的到达时间
 * 
 * 解题思路：
 * 使用Dijkstra算法的变种：
 * 1. 状态定义：(上一步操作类型, 当前城市, 累计时间)
 * 2. 上一步操作类型：0表示之前走路，1表示之前传送
 * 3. 状态转移：
 *    - 任何时候都可以走路，花费1分钟
 *    - 只有上一步是走路时才能传送，传送不花费时间
 * 
 * 算法核心：
 * - 状态空间：2 * N（每个城市有两种到达方式）
 * - 优先队列：按时间最短优先
 * - 避免重复访问：visited数组记录状态
 * 
 * 时间复杂度：O((M1 + M2) * log(N))
 * 空间复杂度：O(N)
 * 
 * 来源：网易互娱面试题
 * 
 * @author Zhu Runqi
 */
public class MagicGoToAim {

    /**
     * 节点类，表示搜索过程中的状态
     */
    private static class Node {
        // 0表示之前走路，1表示之前传送
        public int preTransfer;
        // 当前所在城市
        public int city;
        // 累计花费时间
        public int cost;

        /**
         * 构造函数
         * 
         * @param a 上一步操作类型
         * @param b 当前城市
         * @param c 累计时间
         */
        public Node(int a, int b, int c) {
            preTransfer = a;
            city = b;
            cost = c;
        }
    }

    /**
     * 计算从1号城市到N号城市的最短时间
     * 
     * 算法思路：
     * 1. 使用Dijkstra算法的变种
     * 2. 状态定义为(上一步操作, 当前位置)
     * 3. 根据约束条件进行状态转移：
     *    - 走路：任何时候都可以，花费1分钟
     *    - 传送：只有上一步是走路时才可以，不花费时间但限制下一步
     * 
     * @param n 城市数量（1到n）
     * @param roads 表世界道路连接图
     * @param gates 里世界传送门连接图
     * @return 从城市1到城市n的最短时间
     */
    public static int fast(int n, int[][] roads, int[][] gates) {
        // distance[方法][到达地点] = 最小时间
        // 方法0：上一步走路到达，方法1：上一步传送到达
        int[][] distance = new int[2][n];
        
        // 初始化距离为无穷大（除了起点）
        for (int i = 1; i < n; i++) {
            distance[0][i] = Integer.MAX_VALUE;
            distance[1][i] = Integer.MAX_VALUE;
        }
        
        // 优先队列，按时间从小到大排序
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        heap.add(new Node(0, 0, 0));  // 从城市1开始，假设上一步是走路
        
        // 访问标记数组
        boolean[][] visited = new boolean[2][n];
        
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            
            // 如果已经访问过这个状态，跳过
            if (visited[cur.preTransfer][cur.city]) {
                continue;
            }
            visited[cur.preTransfer][cur.city] = true;
            
            // 尝试走路到相邻城市（任何时候都可以）
            for (int next : roads[cur.city]) {
                if (distance[0][next] > cur.cost + 1) {
                    distance[0][next] = cur.cost + 1;
                    heap.add(new Node(0, next, distance[0][next]));
                }
            }
            
            // 尝试传送到相邻城市（只有上一步是走路时才可以）
            if (cur.preTransfer == 0) {
                for (int next : gates[cur.city]) {
                    if (distance[1][next] > cur.cost) {
                        distance[1][next] = cur.cost;  // 传送不花费时间
                        heap.add(new Node(1, next, distance[1][next]));
                    }
                }
            }
        }
        
        // 返回到达目标城市的最短时间（考虑两种到达方式）
        return Math.min(distance[0][n - 1], distance[1][n - 1]);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 双世界最短路径问题 ===\n");
        
        // 测试用例1：基本示例
        System.out.println("测试用例1：基本示例");
        int n1 = 4;
        // 表世界道路：1-2, 2-3, 3-4
        int[][] roads1 = {{1}, {0, 2}, {1, 3}, {2}};
        // 里世界传送门：1-3, 2-4
        int[][] gates1 = {{2}, {3}, {0}, {1}};
        
        int result1 = fast(n1, roads1, gates1);
        System.out.println("城市数量: " + n1);
        System.out.println("表世界道路: 1-2, 2-3, 3-4");
        System.out.println("里世界传送门: 1-3, 2-4");
        System.out.println("最短时间: " + result1 + " 分钟");
        System.out.println("路径分析: 1(走路到2)→2(传送到4)→4，总时间1分钟");
        System.out.println();
        
        // 测试用例2：必须全程走路
        System.out.println("测试用例2：必须全程走路");
        int n2 = 3;
        // 表世界道路：1-2, 2-3
        int[][] roads2 = {{1}, {0, 2}, {1}};
        // 里世界传送门：无直接有用的传送门
        int[][] gates2 = {{}, {}, {}};
        
        int result2 = fast(n2, roads2, gates2);
        System.out.println("城市数量: " + n2);
        System.out.println("表世界道路: 1-2, 2-3");
        System.out.println("里世界传送门: 无");
        System.out.println("最短时间: " + result2 + " 分钟");
        System.out.println("路径分析: 1→2→3，全程走路，总时间2分钟");
        System.out.println();
        
        // 测试用例3：传送门优势明显
        System.out.println("测试用例3：传送门优势明显");
        int n3 = 5;
        // 表世界道路：链式连接
        int[][] roads3 = {{1}, {0, 2}, {1, 3}, {2, 4}, {3}};
        // 里世界传送门：1直接到5
        int[][] gates3 = {{4}, {}, {}, {}, {0}};
        
        int result3 = fast(n3, roads3, gates3);
        System.out.println("城市数量: " + n3);
        System.out.println("表世界道路: 1-2-3-4-5 (链式)");
        System.out.println("里世界传送门: 1-5 (直达)");
        System.out.println("最短时间: " + result3 + " 分钟");
        System.out.println("路径分析: 1(走路到2)→2(传送到5)→5，总时间1分钟");
        System.out.println("对比: 全程走路需要4分钟");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 双重图结构：表世界道路 + 里世界传送门");
        System.out.println("   - 状态约束：传送后下一步不能再传送");
        System.out.println("   - 时间成本：走路1分钟，传送0分钟");
        System.out.println();
        System.out.println("2. 状态设计：");
        System.out.println("   - 二维状态：(上一步操作类型, 当前位置)");
        System.out.println("   - 操作类型：0表示走路，1表示传送");
        System.out.println("   - 状态转移：根据约束条件选择可行操作");
        System.out.println();
        System.out.println("3. Dijkstra变种：");
        System.out.println("   - 优先队列：按累计时间排序");
        System.out.println("   - 状态扩展：同时考虑两种移动方式");
        System.out.println("   - 重复访问控制：避免同一状态的重复处理");
        System.out.println();
        System.out.println("4. 关键约束：");
        System.out.println("   - 传送限制：只有上一步走路才能传送");
        System.out.println("   - 时间计算：走路+1，传送+0");
        System.out.println("   - 最优选择：在两种到达方式中选择最优");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O((M1+M2)*log(N))，类似标准Dijkstra");
        System.out.println("   - 空间复杂度：O(N)，状态空间为2N");
        System.out.println("   - 实际性能：适合处理中等规模的图");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 游戏路径规划（多种移动方式）");
        System.out.println("   - 交通网络优化（不同交通工具）");
        System.out.println("   - 带约束的最短路径问题");
    }
}
