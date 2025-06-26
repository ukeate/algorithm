package leetc.top;

/**
 * LeetCode 517. 超级洗衣机 (Super Washing Machines)
 * 
 * 问题描述：
 * 假设有 n 台超级洗衣机放在同一排上。开始的时候，每台洗衣机内可能有一定量的衣服，
 * 也可能是空的。在每一步操作中，你可以选择任意 m (1 ≤ m ≤ n) 台洗衣机，
 * 与此同时将每台洗衣机的一件衣服送到相邻的洗衣机。
 * 
 * 给定一个非负整数数组代表从左到右每台洗衣机中的衣服数量，
 * 请给出能让所有洗衣机中剩下的衣服数量相等的最少的操作步数。
 * 如果不能使所有洗衣机中衣服的数量相等，则返回 -1。
 * 
 * 示例：
 * - 输入: [1,0,5]
 * - 输出: 3
 * - 解释: 
 *   第1步:    1     0 <-- 5    =>    1     1     4
 *   第2步:    1 <-- 1 <-- 4    =>    2     1     3
 *   第3步:    2     1 <-- 3    =>    2     2     2
 * 
 * - 输入: [0,3,0]
 * - 输出: 2
 * - 解释:
 *   第1步:    0 <-- 3     0    =>    1     2     0
 *   第2步:    1     2 --> 0    =>    1     1     1
 * 
 * 解法思路：
 * 流量平衡分析：
 * 1. 首先检查是否可能达到平衡（总衣服数能被机器数整除）
 * 2. 计算每台机器的目标衣服数（平均值）
 * 3. 分析每台机器的"流量需求"：需要流入/流出多少衣服
 * 4. 对于每台机器，计算左右两侧的累积流量需求
 * 5. 瓶颈是流量需求最大的位置，决定了最少操作步数
 * 
 * 核心思想：
 * - 流量分析：将问题转化为网络流量平衡问题
 * - 瓶颈理论：操作步数由最繁忙的"通道"决定
 * - 方向性考虑：如果左右都缺衣服，需要当前机器同时向两边输出
 * 
 * 数学模型：
 * - leftRest: 左侧区域的衣服缺额/盈余
 * - rightRest: 右侧区域的衣服缺额/盈余
 * - 如果左右都缺衣服，当前机器需要同时向两边输出
 * 
 * 时间复杂度：O(n) - 遍历数组两次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/super-washing-machines/
 */
public class P517_SuperWashingMachines {
    
    /**
     * 计算使所有洗衣机衣服数量相等的最少操作步数
     * 
     * 算法步骤：
     * 1. 计算总衣服数，检查是否能平均分配
     * 2. 计算每台机器应有的衣服数（平均值）
     * 3. 遍历每台机器，分析其左右两侧的流量需求
     * 4. 计算每个位置的瓶颈程度，取最大值
     * 
     * 关键洞察：
     * - 如果左侧缺k1件，右侧缺k2件，当前机器需要向两边各输出
     * - 此时需要k1+k2步操作（两个方向并行）
     * - 如果一侧缺一侧多，流量可以"穿越"当前机器，取max(|k1|,|k2|)
     * 
     * @param arr 每台洗衣机的衣服数量
     * @return 最少操作步数，如果无法平衡返回-1
     */
    public static int findMinMoves(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int size = arr.length;
        int sum = 0;
        
        // 计算总衣服数
        for (int i = 0; i < size; i++) {
            sum += arr[i];
        }
        
        // 检查是否能平均分配
        if (sum % size != 0) {
            return -1;
        }
        
        int avg = sum / size; // 每台机器应有的衣服数
        int leftSum = 0;      // 左侧累积衣服数
        int ans = 0;          // 最少操作步数
        
        // 遍历每台机器，分析流量需求
        for (int i = 0; i < arr.length; i++) {
            // 计算左侧区域的流量需求
            // leftRest > 0: 左侧多余，需要向右流出
            // leftRest < 0: 左侧不足，需要从右侧流入
            int leftRest = leftSum - i * avg;
            
            // 计算右侧区域的流量需求
            // rightRest > 0: 右侧多余，需要向左流出
            // rightRest < 0: 右侧不足，需要从左侧流入
            int rightRest = (sum - leftSum - arr[i]) - (size - i - 1) * avg;
            
            if (leftRest < 0 && rightRest < 0) {
                // 左右都不足，当前机器需要同时向两边输出
                // 这种情况下，两个方向的输出必须串行进行
                ans = Math.max(ans, Math.abs(leftRest) + Math.abs(rightRest));
            } else {
                // 一侧不足一侧多余，或者都多余
                // 流量可以从多的一侧穿越到少的一侧
                // 瓶颈是流量较大的一侧
                ans = Math.max(ans, Math.max(Math.abs(leftRest), Math.abs(rightRest)));
            }
            
            // 更新左侧累积和
            leftSum += arr[i];
        }
        
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] machines1 = {1, 0, 5};
        System.out.println("测试用例1:");
        System.out.println("输入: " + java.util.Arrays.toString(machines1));
        System.out.println("输出: " + findMinMoves(machines1));
        System.out.println("期望: 3");
        System.out.println("分析: 总共6件衣服，平均2件/台");
        System.out.println("机器0: 需要+1, 机器1: 需要+2, 机器2: 需要-3");
        System.out.println();
        
        // 测试用例2：另一个示例
        int[] machines2 = {0, 3, 0};
        System.out.println("测试用例2:");
        System.out.println("输入: " + java.util.Arrays.toString(machines2));
        System.out.println("输出: " + findMinMoves(machines2));
        System.out.println("期望: 2");
        System.out.println("分析: 总共3件衣服，平均1件/台");
        System.out.println("机器1需要向左右各输出1件，需要2步");
        System.out.println();
        
        // 测试用例3：无法平衡
        int[] machines3 = {0, 2, 0};
        System.out.println("测试用例3 (无法平衡):");
        System.out.println("输入: " + java.util.Arrays.toString(machines3));
        System.out.println("输出: " + findMinMoves(machines3));
        System.out.println("期望: -1");
        System.out.println("分析: 总共2件衣服，无法平均分给3台机器");
        System.out.println();
        
        // 测试用例4：已经平衡
        int[] machines4 = {1, 1, 1, 1};
        System.out.println("测试用例4 (已平衡):");
        System.out.println("输入: " + java.util.Arrays.toString(machines4));
        System.out.println("输出: " + findMinMoves(machines4));
        System.out.println("期望: 0");
        System.out.println("分析: 已经平衡，无需操作");
        System.out.println();
        
        // 测试用例5：极端情况
        int[] machines5 = {0, 0, 0, 0, 6};
        System.out.println("测试用例5 (极端不平衡):");
        System.out.println("输入: " + java.util.Arrays.toString(machines5));
        System.out.println("输出: " + findMinMoves(machines5));
        System.out.println("期望: 4");
        System.out.println("分析: 最后一台机器需要向前4台各传1件，需要4步");
        System.out.println();
        
        // 测试用例6：复杂流量
        int[] machines6 = {9, 1, 8, 8, 9};
        System.out.println("测试用例6 (复杂流量):");
        System.out.println("输入: " + java.util.Arrays.toString(machines6));
        System.out.println("输出: " + findMinMoves(machines6));
        System.out.println("期望: 4");
        System.out.println("分析: 总35件，平均7件/台，需要复杂的流量调配");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 流量平衡分析，找到瓶颈位置");
        System.out.println("- 时间复杂度: O(n) - 两次线性遍历");
        System.out.println("- 空间复杂度: O(1) - 只使用常数额外空间");
        System.out.println("- 关键洞察: 双向输出需要串行，单向流动可以并行");
        System.out.println("- 数学模型: 将物理问题抽象为网络流量问题");
        System.out.println("- 实际应用: 负载均衡、资源调度等领域");
    }
}
