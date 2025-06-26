package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * LeetCode 815. 公交路线 (Bus Routes)
 * 
 * 问题描述：
 * 现有一些公交路线。每一条路线 routes[i] 上都有一些车站 routes[i][j]。
 * 现在从 source 车站出发（并不在车上），要前往 target 车站。
 * 期间仅可乘坐公交车。
 * 
 * 求出最少乘坐的公交车数量。返回 -1 表示不可能到达终点车站。
 * 
 * 示例：
 * - 输入：routes = [[1,2,7],[3,6,7]], source = 1, target = 6
 * - 输出：2
 * - 解释：最优策略是先乘坐第一班车到达车站 7, 然后换乘第二班车到车站 6。
 * 
 * 解法思路：
 * BFS图论算法：
 * 1. 将问题转化为图论问题：车站作为节点，公交路线作为连接
 * 2. 构建车站到路线的映射关系
 * 3. 使用BFS搜索最少换乘次数
 * 4. 以"路线"为BFS的层级单位，而不是车站
 * 
 * 核心思想：
 * - 抽象级别：以路线为搜索单位，而非车站
 * - 状态转移：从一条路线可以到达所有能连通的其他路线
 * - 最短路径：BFS保证找到最少换乘次数
 * 
 * 时间复杂度：O(N×M) - N为路线数，M为车站数，每个车站最多被访问一次
 * 空间复杂度：O(N×M) - 哈希表和队列的空间
 * 
 * LeetCode链接：https://leetcode.com/problems/bus-routes/
 */
public class P815_BusRoutes {
    
    /**
     * 计算从source到target的最少公交车换乘次数
     * 
     * 算法步骤：
     * 1. 特殊情况：起点就是终点，直接返回0
     * 2. 构建车站到路线的映射表
     * 3. 从起点车站能到达的所有路线开始BFS
     * 4. 逐层搜索，每层代表一次换乘
     * 5. 在某层中找到目标车站时，返回换乘次数
     * 
     * BFS策略：
     * - 队列存储的是路线编号，不是车站编号
     * - 每次从队列取出一条路线，遍历该路线的所有车站
     * - 对每个车站，找到所有经过该车站的其他路线
     * - 将未访问过的路线加入下一层的队列
     * 
     * @param routes 公交路线数组，每个路线包含该路线经过的所有车站
     * @param source 起始车站
     * @param target 目标车站
     * @return 最少换乘次数，无法到达返回-1
     */
    public static int numBusesToDestination(int[][] routes, int source, int target) {
        // 特殊情况：起点就是终点
        if (source == target) {
            return 0;
        }
        
        int n = routes.length;
        
        // 构建车站到路线的映射：车站 -> 经过该车站的所有路线
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < routes[i].length; j++) {
                if (!map.containsKey(routes[i][j])) {
                    map.put(routes[i][j], new ArrayList<>());
                }
                map.get(routes[i][j]).add(i); // 路线i经过车站routes[i][j]
            }
        }
        
        // BFS初始化
        ArrayList<Integer> que = new ArrayList<>(); // 当前层的路线队列
        boolean[] set = new boolean[n];             // 路线访问标记
        
        // 将起始车站能直接到达的所有路线加入队列
        if (map.containsKey(source)) {
            for (int route : map.get(source)) {
                que.add(route);
                set[route] = true;
            }
        }
        
        int len = 1; // 当前换乘次数（已经上了第一班车）
        
        // BFS搜索过程
        while (!que.isEmpty()) {
            ArrayList<Integer> nextLevel = new ArrayList<>(); // 下一层的路线队列
            
            // 处理当前层的所有路线
            for (int route : que) {
                int[] bus = routes[route]; // 当前路线经过的所有车站
                
                // 遍历当前路线的所有车站
                for (int station : bus) {
                    // 检查是否到达目标车站
                    if (station == target) {
                        return len;
                    }
                    
                    // 从当前车站可以换乘到的所有其他路线
                    if (map.containsKey(station)) {
                        for (int nextRoute : map.get(station)) {
                            if (!set[nextRoute]) {
                                nextLevel.add(nextRoute);
                                set[nextRoute] = true;
                            }
                        }
                    }
                }
            }
            
            // 移动到下一层
            que = nextLevel;
            len++;
        }
        
        // 无法到达目标车站
        return -1;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[][] routes1 = {{1, 2, 7}, {3, 6, 7}};
        int source1 = 1, target1 = 6;
        System.out.println("测试用例1:");
        System.out.println("routes = [[1,2,7],[3,6,7]]");
        System.out.println("source = " + source1 + ", target = " + target1);
        System.out.println("输出: " + numBusesToDestination(routes1, source1, target1));
        System.out.println("期望: 2");
        System.out.println("路径: 车站1 -> 路线0 -> 车站7 -> 路线1 -> 车站6");
        System.out.println();
        
        // 测试用例2：起点就是终点
        int[][] routes2 = {{1, 2, 7}, {3, 6, 7}};
        int source2 = 1, target2 = 1;
        System.out.println("测试用例2 (起点=终点):");
        System.out.println("source = " + source2 + ", target = " + target2);
        System.out.println("输出: " + numBusesToDestination(routes2, source2, target2));
        System.out.println("期望: 0");
        System.out.println();
        
        // 测试用例3：无法到达
        int[][] routes3 = {{1, 2}, {3, 4}};
        int source3 = 1, target3 = 4;
        System.out.println("测试用例3 (无法到达):");
        System.out.println("routes = [[1,2],[3,4]]");
        System.out.println("source = " + source3 + ", target = " + target3);
        System.out.println("输出: " + numBusesToDestination(routes3, source3, target3));
        System.out.println("期望: -1");
        System.out.println();
        
        // 测试用例4：同一路线
        int[][] routes4 = {{1, 2, 3, 4, 5}};
        int source4 = 1, target4 = 5;
        System.out.println("测试用例4 (同一路线):");
        System.out.println("routes = [[1,2,3,4,5]]");
        System.out.println("source = " + source4 + ", target = " + target4);
        System.out.println("输出: " + numBusesToDestination(routes4, source4, target4));
        System.out.println("期望: 1");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 将公交换乘问题转化为图论BFS问题");
        System.out.println("- 抽象技巧: 以路线为BFS单位，而非车站");
        System.out.println("- 时间复杂度: O(N×M) - N路线数，M车站数");
        System.out.println("- 空间复杂度: O(N×M) - 哈希表和队列空间");
        System.out.println("- 关键映射: 车站 -> 经过该车站的所有路线");
        System.out.println("- BFS保证: 找到的第一个解就是最优解");
    }
}
