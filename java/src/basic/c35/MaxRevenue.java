package basic.c35;

import java.util.*;

/**
 * 活动收益最大化问题
 * 
 * 问题描述：
 * 给定一组活动，每个活动有：
 * 1. 收益值（revenue）
 * 2. 完成时间（times）
 * 3. 依赖关系（dependents）- 某些活动必须在其他活动完成后才能开始
 * 
 * 目标：在给定的总时间限制下，选择活动参与以获得最大收益。
 * 
 * 约束条件：
 * - 如果选择了某个活动，必须完成它的所有依赖活动
 * - 活动按依赖关系必须串行执行（不能并行）
 * - 总时间不能超过限制
 * 
 * 算法思路：
 * 1. 构建活动依赖图的反向图（找到每个活动的前驱）
 * 2. 从终端活动（没有后续依赖的活动）开始反向BFS
 * 3. 使用动态规划记录每个活动在不同时间成本下的最大收益
 * 4. 最终合并所有可能的活动方案，选择最优解
 * 
 * 时间复杂度：O(V*E*T)，V为活动数，E为依赖关系数，T为平均时间成本
 * 空间复杂度：O(V*T)
 */
public class MaxRevenue {
    
    /**
     * 计算在给定时间限制下的最大收益和所需时间
     * 
     * @param allTime 总时间限制
     * @param revenue 每个活动的收益数组
     * @param times 每个活动的完成时间数组
     * @param dependents 依赖关系矩阵，dependents[i][j]=1表示活动i依赖于活动j
     * @return 数组[最优时间成本, 最大收益]
     */
    public static int[] max(int allTime, int[] revenue, int[] times, int[][] dependents) {
        int size = revenue.length;
        
        // 构建反向依赖图：parents[i]存储所有依赖于活动i的活动列表
        HashMap<Integer, ArrayList<Integer>> parents = new HashMap<>();
        for (int i = 0; i < size; i++) {
            parents.put(i, new ArrayList<>());
        }
        
        // 寻找终端活动（没有任何活动依赖它的活动）
        int end = -1;
        for (int i = 0; i < dependents.length; i++) {
            boolean allZero = true;  // 检查第i行是否全为0
            for (int j = 0; j < dependents[0].length; j++) {
                if (dependents[i][j] != 0) {
                    // 活动i依赖于活动j，所以j是i的前驱
                    parents.get(j).add(i);
                    allZero = false;
                }
            }
            // 如果第i行全为0，说明活动i不依赖任何其他活动，它是终端活动
            if (allZero) {
                end = i;
            }
        }
        
        // 为每个活动创建时间-收益映射表
        // actMap[i]是TreeMap<时间成本, 在该时间成本下的最大收益>
        HashMap<Integer, TreeMap<Integer, Integer>> actMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            actMap.put(i, new TreeMap<>());
        }
        
        // 初始化终端活动：只做这一个活动的时间成本和收益
        actMap.get(end).put(times[end], revenue[end]);
        
        // 从终端活动开始反向BFS处理所有活动
        LinkedList<Integer> que = new LinkedList<>();
        que.add(end);
        
        while (!que.isEmpty()) {
            int cur = que.poll();  // 当前处理的活动
            
            // 处理所有依赖于当前活动的前驱活动
            for (int last : parents.get(cur)) {
                // 对于当前活动的每种时间-收益方案
                for (Map.Entry<Integer, Integer> entry : actMap.get(cur).entrySet()) {
                    // 计算如果选择前驱活动last的总时间成本和总收益
                    int lastCost = entry.getKey() + times[last];      // 总时间 = 当前方案时间 + 前驱活动时间
                    int lastRevenue = entry.getValue() + revenue[last]; // 总收益 = 当前方案收益 + 前驱活动收益
                    
                    TreeMap<Integer, Integer> lastMap = actMap.get(last);
                    
                    // 检查是否需要更新前驱活动的最优方案
                    // 如果在lastCost时间内没有更好的收益方案，则更新
                    if (lastMap.floorKey(lastCost) == null || 
                        lastMap.get(lastMap.floorKey(lastCost)) < lastRevenue) {
                        lastMap.put(lastCost, lastRevenue);
                    }
                }
                que.add(last);  // 将前驱活动加入队列继续处理
            }
        }
        
        // 合并所有活动的方案，找到全局最优解
        TreeMap<Integer, Integer> allMap = new TreeMap<>();
        for (TreeMap<Integer, Integer> curMap : actMap.values()) {
            for (Map.Entry<Integer, Integer> entry : curMap.entrySet()) {
                int t = entry.getKey();    // 时间成本
                int r = entry.getValue();  // 收益值
                
                // 如果在时间t内没有更好的收益方案，则更新全局方案
                if (allMap.floorKey(t) == null || 
                    allMap.get(allMap.floorKey(t)) < r) {
                    allMap.put(t, r);
                }
            }
        }
        
        // 在时间限制内找到最优方案
        Integer bestTime = allMap.floorKey(allTime);
        if (bestTime == null) {
            return new int[]{0, 0};  // 没有可行方案
        }
        
        return new int[]{bestTime, allMap.get(bestTime)};
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        // 测试数据：8个活动的收益、时间和依赖关系
        int allTime = 10;  // 总时间限制
        
        // 每个活动的收益
        int[] revenue = {2000, 4000, 2500, 1600, 3800, 2600, 4000, 3500};
        
        // 每个活动的完成时间
        int[] times = {3, 3, 2, 1, 4, 2, 4, 3};
        
        // 依赖关系矩阵：dependents[i][j]=1表示活动i依赖活动j
        int[][] dependents = {
                {0, 1, 1, 0, 0, 0, 0, 0},  // 活动0依赖活动1和2
                {0, 0, 0, 1, 1, 0, 0, 0},  // 活动1依赖活动3和4
                {0, 0, 0, 1, 0, 0, 0, 0},  // 活动2依赖活动3
                {0, 0, 0, 0, 1, 1, 1, 0},  // 活动3依赖活动4、5、6
                {0, 0, 0, 0, 0, 0, 0, 1},  // 活动4依赖活动7
                {0, 0, 0, 0, 0, 0, 0, 1},  // 活动5依赖活动7
                {0, 0, 0, 0, 0, 0, 0, 1},  // 活动6依赖活动7
                {0, 0, 0, 0, 0, 0, 0, 0}   // 活动7无依赖（终端活动）
        };
        
        int[] res = max(allTime, revenue, times, dependents);
        
        System.out.println("=== 活动收益最大化测试 ===");
        System.out.println("总时间限制: " + allTime);
        System.out.println("最优方案 - 时间成本: " + res[0] + ", 最大收益: " + res[1]);
        
        // 分析结果
        System.out.println("\n活动信息:");
        for (int i = 0; i < revenue.length; i++) {
            System.out.println("活动" + i + ": 收益=" + revenue[i] + ", 时间=" + times[i]);
        }
    }
}
