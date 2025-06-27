package basic.c32;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 二维柱状图接雨水问题
 * 
 * 问题描述：
 * 给定一个二维数组表示高度图，求能够接住多少雨水
 * 
 * 算法思路：
 * 1. 使用优先队列（小根堆）从边界开始向内部扩展
 * 2. 维护一个当前的最大水位线
 * 3. 每次取出堆中高度最小的点，向四个方向扩展
 * 4. 如果相邻点的高度小于当前水位线，则可以接住雨水
 * 
 * 时间复杂度：O(m*n*log(m*n))，其中m和n分别是矩阵的行数和列数
 * 空间复杂度：O(m*n)
 */
public class TrappingRainWaterII {
    
    /**
     * 节点类，存储位置和高度信息
     */
    public static class Node {
        public int val;  // 高度值
        public int row;  // 行坐标
        public int col;  // 列坐标

        public Node(int v, int r, int c) {
            val = v;
            row = r;
            col = c;
        }
    }

    /**
     * 比较器，用于优先队列的小根堆排序
     * 按照高度值从小到大排序
     */
    public static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.val - o2.val;
        }
    }

    /**
     * 计算二维高度图能接住的雨水总量
     * 
     * @param map 二维高度图
     * @return 能接住的雨水总量
     */
    public static int water(int[][] map) {
        // 边界条件检查
        if (map == null || map.length == 0 || map[0] == null || map[0].length == 0) {
            return 0;
        }
        
        int n = map.length;    // 行数
        int m = map[0].length; // 列数
        boolean[][] visited = new boolean[n][m]; // 访问标记数组
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp()); // 小根堆
        
        // 将边界上的所有点加入堆中，并标记为已访问
        // 上边界
        for (int col = 0; col < m - 1; col++) {
            visited[0][col] = true;
            heap.add(new Node(map[0][col], 0, col));
        }
        // 右边界
        for (int row = 0; row < n - 1; row++) {
            visited[row][m - 1] = true;
            heap.add(new Node(map[row][m - 1], row, m - 1));
        }
        // 下边界
        for (int col = m - 1; col > 0; col--) {
            visited[n - 1][col] = true;
            heap.add(new Node(map[n - 1][col], n - 1, col));
        }
        // 左边界
        for (int row = n - 1; row > 0; row--) {
            visited[row][0] = true;
            heap.add(new Node(map[row][0], row, 0));
        }

        int water = 0; // 总的接水量
        int max = 0;   // 当前的最大水位线
        
        // 从边界向内部逐步扩展
        while (!heap.isEmpty()) {
            Node cur = heap.poll(); // 取出当前高度最小的点
            max = Math.max(max, cur.val); // 更新最大水位线
            int r = cur.row;
            int c = cur.col;
            
            // 向上扩展
            if (r > 0 && !visited[r - 1][c]) {
                water += Math.max(0, max - map[r - 1][c]); // 计算接水量
                visited[r - 1][c] = true;
                heap.add(new Node(map[r - 1][c], r - 1, c));
            }
            // 向下扩展
            if (r < n - 1 && !visited[r + 1][c]) {
                water += Math.max(0, max - map[r + 1][c]);
                visited[r + 1][c] = true;
                heap.add(new Node(map[r + 1][c], r + 1, c));
            }
            // 向左扩展
            if (c > 0 && !visited[r][c - 1]) {
                water += Math.max(0, max - map[r][c - 1]);
                visited[r][c - 1] = true;
                heap.add(new Node(map[r][c - 1], r, c - 1));
            }
            // 向右扩展
            if (c < m - 1 && !visited[r][c + 1]) {
                water += Math.max(0, max - map[r][c + 1]);
                visited[r][c + 1] = true;
                heap.add(new Node(map[r][c + 1], r, c + 1));
            }
        }
        return water;
    }
}
