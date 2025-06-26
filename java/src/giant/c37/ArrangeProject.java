package giant.c37;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 项目任务调度问题（甘特图时间预估）
 * 
 * 问题描述：
 * 小易要进行mini项目管理，每项任务有预计花费时间和前置任务表。
 * 必须完成前置任务才能开始当前任务，没有前置任务或前置任务全部完成的任务可以同时进行。
 * 求完成所有任务至少需要多长时间。
 * 
 * 输入格式：
 * - 第一行：数据组数T
 * - 每组数据第一行：任务数N
 * - 接下来N行：Di Ki M1 M2 ... MKi
 *   - Di：完成第i个任务需要的天数
 *   - Ki：前置任务个数
 *   - Mj：第j个前置任务的编号
 * 
 * 解题思路：
 * 这是一个典型的拓扑排序 + 关键路径问题：
 * 1. 构建有向无环图（DAG），边表示依赖关系
 * 2. 使用拓扑排序确定任务执行顺序
 * 3. 在拓扑排序过程中计算每个任务的最早完成时间
 * 4. 最终答案是所有任务中最早完成时间的最大值
 * 
 * 算法核心：
 * - 使用入度数组记录每个任务的前置任务数量
 * - 使用队列处理入度为0的任务（可以立即开始的任务）
 * - 动态更新每个任务的最早开始时间
 * 
 * 时间复杂度：O(V + E)，其中V是任务数，E是依赖关系数
 * 空间复杂度：O(V + E)，用于存储图结构和辅助数组
 * 
 * 来源：网易面试题
 * 
 * @author Zhu Runqi
 */
public class ArrangeProject {
    
    /**
     * 获取所有入度为0的任务（即没有前置任务或前置任务已完成的任务）
     * 
     * @param headCount 每个任务的入度数组
     * @return 包含所有入度为0任务的队列
     */
    private static Queue<Integer> head(int[] headCount) {
        Queue<Integer> que = new LinkedList<>();
        for (int i = 0; i < headCount.length; i++) {
            if (headCount[i] == 0) {
                que.offer(i);
            }
        }
        return que;
    }

    /**
     * 计算完成所有任务所需的最少天数
     * 
     * 算法流程：
     * 1. 初始化所有入度为0的任务到队列中
     * 2. 从队列中取出任务，更新其完成时间
     * 3. 将该任务的后续任务的入度减1，如果减为0则加入队列
     * 4. 更新后续任务的最早开始时间
     * 5. 重复直到所有任务处理完毕
     * 6. 返回所有任务中的最大完成时间
     * 
     * @param nums 邻接表，nums[i]存储任务i的所有后续任务
     * @param days 每个任务需要的天数
     * @param headCount 每个任务的入度（前置任务数量）
     * @return 完成所有任务所需的最少天数
     */
    public static int dayCount(ArrayList<Integer>[] nums, int[] days, int[] headCount) {
        // 获取所有可以立即开始的任务
        Queue<Integer> head = head(headCount);
        int maxDay = 0;
        
        // countDay[i]表示任务i的最早完成时间
        int[] countDay = new int[days.length];
        
        // 拓扑排序处理
        while (!head.isEmpty()) {
            int cur = head.poll();
            
            // 更新当前任务的完成时间（开始时间 + 执行时间）
            countDay[cur] += days[cur];
            
            // 处理当前任务的所有后续任务
            for (int j = 0; j < nums[cur].size(); j++) {
                int nextTask = nums[cur].get(j);
                
                // 后续任务的前置任务数减1
                headCount[nextTask]--;
                
                // 如果后续任务的所有前置任务都已完成，加入队列
                if (headCount[nextTask] == 0) {
                    head.offer(nextTask);
                }
                
                // 更新后续任务的最早开始时间
                // 后续任务的开始时间 = max(当前开始时间, 所有前置任务的最大完成时间)
                countDay[nextTask] = Math.max(countDay[nextTask], countDay[cur]);
            }
        }
        
        // 找出所有任务中的最大完成时间
        for (int i = 0; i < countDay.length; i++) {
            maxDay = Math.max(maxDay, countDay[i]);
        }
        
        return maxDay;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 项目任务调度问题 ===\n");
        
        // 测试用例1：简单的线性依赖
        System.out.println("测试用例1：线性依赖任务");
        System.out.println("任务0(3天) -> 任务1(2天) -> 任务2(4天)");
        
        ArrayList<Integer>[] graph1 = new ArrayList[3];
        for (int i = 0; i < 3; i++) graph1[i] = new ArrayList<>();
        graph1[0].add(1);  // 任务0 -> 任务1
        graph1[1].add(2);  // 任务1 -> 任务2
        
        int[] days1 = {3, 2, 4};
        int[] headCount1 = {0, 1, 1};  // 任务0无前置，任务1有1个前置，任务2有1个前置
        
        int result1 = dayCount(graph1, days1, headCount1.clone());
        System.out.println("完成时间: " + result1 + "天 (期望: 9天)");
        System.out.println("执行顺序: 任务0(0-3天) -> 任务1(3-5天) -> 任务2(5-9天)");
        System.out.println();
        
        // 测试用例2：并行任务
        System.out.println("测试用例2：并行任务");
        System.out.println("任务0(2天) -> 任务2(3天)");
        System.out.println("任务1(4天) -> 任务2(3天)");
        
        ArrayList<Integer>[] graph2 = new ArrayList[3];
        for (int i = 0; i < 3; i++) graph2[i] = new ArrayList<>();
        graph2[0].add(2);  // 任务0 -> 任务2
        graph2[1].add(2);  // 任务1 -> 任务2
        
        int[] days2 = {2, 4, 3};
        int[] headCount2 = {0, 0, 2};  // 任务0,1无前置，任务2有2个前置
        
        int result2 = dayCount(graph2, days2, headCount2.clone());
        System.out.println("完成时间: " + result2 + "天 (期望: 7天)");
        System.out.println("执行顺序: 任务0,1并行(0-4天) -> 任务2(4-7天)");
        System.out.println();
        
        // 测试用例3：复杂依赖网络  
        System.out.println("测试用例3：复杂依赖网络");
        System.out.println("任务0(1天) -> 任务2(2天) -> 任务4(1天)");
        System.out.println("任务1(3天) -> 任务3(2天) -> 任务4(1天)");
        
        ArrayList<Integer>[] graph3 = new ArrayList[5];
        for (int i = 0; i < 5; i++) graph3[i] = new ArrayList<>();
        graph3[0].add(2);  // 任务0 -> 任务2
        graph3[1].add(3);  // 任务1 -> 任务3
        graph3[2].add(4);  // 任务2 -> 任务4
        graph3[3].add(4);  // 任务3 -> 任务4
        
        int[] days3 = {1, 3, 2, 2, 1};
        int[] headCount3 = {0, 0, 1, 1, 2};
        
        int result3 = dayCount(graph3, days3, headCount3.clone());
        System.out.println("完成时间: " + result3 + "天 (期望: 6天)");
        System.out.println("分析: 任务0,1并行 -> 关键路径是任务1(3天)->任务3(2天)->任务4(1天)");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 拓扑排序：");
        System.out.println("   - 维护每个任务的入度（前置任务数量）");
        System.out.println("   - 从入度为0的任务开始处理");
        System.out.println("   - 处理完任务后，更新其后续任务的入度");
        System.out.println();
        System.out.println("2. 关键路径计算：");
        System.out.println("   - countDay[i]记录任务i的最早完成时间");
        System.out.println("   - 任务完成时间 = 最晚前置任务完成时间 + 自身执行时间");
        System.out.println("   - 项目总时间 = 所有任务完成时间的最大值");
        System.out.println();
        System.out.println("3. 实际应用：");
        System.out.println("   - 项目管理中的甘特图制作");
        System.out.println("   - 软件开发中的任务依赖分析");
        System.out.println("   - 制造业中的生产流程优化");
        System.out.println();
        System.out.println("4. 复杂度优势：");
        System.out.println("   - 时间复杂度：O(V + E)，线性复杂度");
        System.out.println("   - 空间复杂度：O(V + E)，与图的规模成正比");
        System.out.println("   - 相比动态规划等方法，更加高效直观");
    }
}
