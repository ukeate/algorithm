package giant.c35;

import java.util.PriorityQueue;

/**
 * 网格最短路径问题
 * 
 * 问题描述：
 * 来自网易算法题
 * 给定一个二维网格地图，每个格子有不同的类型和对应的移动代价：
 * - map[i][j] == 0: 海洋，移动代价为2
 * - map[i][j] == 1: 陆地，移动代价为1  
 * - map[i][j] == 2: 障碍，无法通过
 * 
 * 要求从左上角(0,0)走到右下角(n-1,m-1)，每步只能上下左右移动，
 * 求最小移动代价，如果无法到达返回-1。
 * 
 * 例如：
 * [[1,0,1],
 *  [1,1,0], 
 *  [2,1,1]]
 * 从(0,0)到(2,2)的最短路径可能是: (0,0)→(1,0)→(1,1)→(2,1)→(2,2)
 * 代价: 1 + 1 + 1 + 1 = 4
 * 
 * 解决方案：
 * 使用Dijkstra算法求最短路径
 * 
 * 核心思想：
 * 这是一个带权重的最短路径问题，用Dijkstra算法可以得到最优解
 * 
 * 算法复杂度：
 * 时间复杂度：O(N*M*log(N*M))，其中N*M是网格大小
 * 空间复杂度：O(N*M)，用于visited数组和优先队列
 */
public class WalkToEnd {
    
    /**
     * 节点类：用于Dijkstra算法中的优先队列
     * 记录位置坐标和到达该位置的累计代价
     */
    private static class Node {
        public int row;    // 行坐标
        public int col;    // 列坐标
        public int cost;   // 到达该位置的累计代价

        public Node(int a, int b, int c) {
            row = a;
            col = b;
            cost = c;
        }
    }

    /**
     * 向优先队列中添加可达的相邻节点
     * 
     * 检查条件：
     * 1. 坐标在网格范围内
     * 2. 不是障碍物(值不等于2)
     * 3. 尚未访问过
     * 
     * @param m 网格地图
     * @param i 目标行坐标
     * @param j 目标列坐标
     * @param pre 到达当前位置的代价
     * @param heap 优先队列（按代价升序）
     * @param visited 访问标记数组
     */
    private static void add(int[][] m, int i, int j, int pre, PriorityQueue<Node> heap, boolean[][] visited) {
        // 边界检查和可达性检查
        if (i >= 0 && i < m.length && j >= 0 && j < m[0].length && m[i][j] != 2 && !visited[i][j]) {
            // 计算移动到(i,j)的代价：海洋(0)代价2，陆地(1)代价1
            int moveCost = (m[i][j] == 0) ? 2 : 1;
            heap.add(new Node(i, j, pre + moveCost));
            visited[i][j] = true;  // 标记为已访问，避免重复加入队列
        }
    }

    /**
     * 使用Dijkstra算法计算从左上角到右下角的最小代价
     * 
     * 算法思路：
     * 1. 初始化：将起点(0,0)加入优先队列
     * 2. 循环处理：每次取出代价最小的节点
     * 3. 扩展：将当前节点的所有可达邻居加入队列
     * 4. 终止：到达终点或队列为空
     * 
     * Dijkstra算法特点：
     * - 贪心策略：总是处理当前代价最小的节点
     * - 最优性：第一次到达目标点时就是最优解
     * - 适用性：适合非负权重的最短路径问题
     * 
     * 优化细节：
     * - 使用visited数组避免重复访问
     * - 优先队列按代价排序，保证贪心选择
     * - 四个方向的邻居扩展
     * 
     * @param map 网格地图
     * @return 最小移动代价，无法到达返回-1
     */
    public static int minCost(int[][] map) {
        // 特殊情况：起点就是障碍物
        if (map[0][0] == 2) {
            return -1;
        }
        
        int n = map.length;      // 网格行数
        int m = map[0].length;   // 网格列数
        
        // 优先队列：按代价升序排列，保证每次取出的是当前代价最小的节点
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        
        // 访问标记数组：避免重复访问同一个位置
        boolean[][] visited = new boolean[n][m];
        
        // 起点加入队列：位置(0,0)，代价根据地形确定
        add(map, 0, 0, 0, heap, visited);
        
        // Dijkstra主循环
        while (!heap.isEmpty()) {
            Node cur = heap.poll();  // 取出当前代价最小的节点
            
            // 到达终点检查
            if (cur.row == n - 1 && cur.col == m - 1) {
                return cur.cost;  // 第一次到达终点就是最优解
            }
            
            // 四个方向扩展邻居节点
            add(map, cur.row - 1, cur.col, cur.cost, heap, visited);  // 上
            add(map, cur.row + 1, cur.col, cur.cost, heap, visited);  // 下
            add(map, cur.row, cur.col - 1, cur.cost, heap, visited);  // 左
            add(map, cur.row, cur.col + 1, cur.cost, heap, visited);  // 右
        }
        
        // 队列为空仍未到达终点，说明无法到达
        return -1;
    }
    
    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 网格最短路径问题测试 ===");
        
        // 测试用例1：标准情况
        int[][] map1 = {
            {1, 0, 1},
            {1, 1, 0},
            {2, 1, 1}
        };
        System.out.println("测试用例1:");
        System.out.println("网格: [[1,0,1], [1,1,0], [2,1,1]]");
        System.out.println("分析: 从(0,0)到(2,2)，避开障碍物(2,0)");
        int result1 = minCost(map1);
        System.out.println("最小代价: " + result1);
        System.out.println();
        
        // 测试用例2：起点是障碍物
        int[][] map2 = {
            {2, 1},
            {1, 1}
        };
        System.out.println("测试用例2:");
        System.out.println("网格: [[2,1], [1,1]]");
        System.out.println("分析: 起点(0,0)是障碍物，无法开始");
        int result2 = minCost(map2);
        System.out.println("结果: " + result2 + " (期望-1)");
        System.out.println();
        
        // 测试用例3：无法到达
        int[][] map3 = {
            {1, 2},
            {2, 1}
        };
        System.out.println("测试用例3:");
        System.out.println("网格: [[1,2], [2,1]]");
        System.out.println("分析: 被障碍物完全阻隔，无法到达终点");
        int result3 = minCost(map3);
        System.out.println("结果: " + result3 + " (期望-1)");
        System.out.println();
        
        // 测试用例4：全是陆地
        int[][] map4 = {
            {1, 1, 1},
            {1, 1, 1}
        };
        System.out.println("测试用例4:");
        System.out.println("网格: [[1,1,1], [1,1,1]]");
        System.out.println("分析: 全是陆地，每步代价1");
        int result4 = minCost(map4);
        System.out.println("最小代价: " + result4 + " (期望3: 右→右→下 或 下→右→右)");
        System.out.println();
        
        // 测试用例5：全是海洋
        int[][] map5 = {
            {0, 0},
            {0, 0}
        };
        System.out.println("测试用例5:");
        System.out.println("网格: [[0,0], [0,0]]");
        System.out.println("分析: 全是海洋，每步代价2");
        int result5 = minCost(map5);
        System.out.println("最小代价: " + result5 + " (期望4: 每步代价2，共2步)");
        
        System.out.println("\n=== 测试完成 ===");
    }
}