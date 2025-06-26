package giant.c35;

import java.util.Arrays;

/**
 * 魔法石染色最小代价问题
 * 
 * 问题描述：
 * 来自小红书算法题
 * 给定一批魔法石，每个石头用三元组[color, redCost, blueCost]表示：
 * - [0, 4, 7]: 表示石头无颜色，变红代价4，变蓝代价7
 * - [1, X, X]: 表示石头已经是红色，不能改变颜色（后两个数无意义）
 * - [2, X, X]: 表示石头已经是蓝色，不能改变颜色（后两个数无意义）
 * 
 * 要求：
 * 1. 最后所有石头都必须有颜色
 * 2. 红色和蓝色石头数量必须相等
 * 3. 求达到目标的最小代价
 * 4. 如果无法达到目标，返回-1
 * 
 * 例如：
 * stones = [[1,5,3], [2,7,9], [0,6,4], [0,7,9], [0,2,1], [0,5,9]]
 * 总共6个石头，需要3红3蓝
 * 已有1红1蓝，需要从4个无色石头中选2个染红，2个染蓝
 * 
 * 解决方案：
 * 使用贪心算法，优先染成代价差值最大的颜色
 * 
 * 核心思想：
 * 对于无色石头，如果red_cost - blue_cost的差值越大，
 * 越应该优先染成蓝色（避免更大的红色代价损失）
 * 
 * 算法复杂度：
 * 时间复杂度：O(N*logN)，主要是排序
 * 空间复杂度：O(1)
 */
public class MagicStone {
    
    /**
     * 计算魔法石染色的最小代价
     * 
     * 算法思路：
     * 1. 首先检查基本约束：总数必须是偶数
     * 2. 统计已染色石头数量，检查是否可行
     * 3. 对无色石头按"红色代价-蓝色代价"降序排序
     * 4. 贪心策略：优先将差值大的石头染成蓝色
     * 
     * 贪心策略证明：
     * 假设有两个无色石头A和B：
     * A: red_cost=10, blue_cost=3, diff=7
     * B: red_cost=8, blue_cost=6, diff=2
     * 
     * 如果A选红色，B选蓝色：总代价 = 10 + 6 = 16
     * 如果A选蓝色，B选红色：总代价 = 3 + 8 = 11
     * 
     * 显然应该让差值大的A选蓝色，这样可以避免更大的代价损失
     * 
     * 算法步骤：
     * 1. 排序：无色石头按(red_cost - blue_cost)降序排列
     * 2. 计算：默认所有无色石头都染红色的总代价
     * 3. 调整：将需要染蓝的石头从红色切换到蓝色
     * 
     * @param stones 石头数组，每个元素[color, redCost, blueCost]
     * @return 最小代价，如果无解返回-1
     */
    public static int minCost(int[][] stones) {
        int n = stones.length;
        
        // 基本约束检查：总数必须是偶数（保证红蓝数量相等）
        if ((n & 1) != 0) {
            return -1;
        }
        
        // 排序策略：
        // 1. 有色石头(color!=0)排在前面
        // 2. 无色石头按(red_cost - blue_cost)降序排列
        Arrays.sort(stones, (a, b) -> {
            // 如果都是无色石头，按红蓝代价差值降序排列
            if (a[0] == 0 && b[0] == 0) {
                return (b[1] - b[2]) - (a[1] - a[2]);
            }
            // 有色石头排在前面
            return a[0] - b[0];
        });
        
        // 统计各类石头数量和初始代价
        int zero = 0;    // 无色石头数量
        int red = 0;     // 已有红色石头数量
        int blue = 0;    // 已有蓝色石头数量
        int cost = 0;    // 总代价（假设所有无色石头都染红）
        
        for (int i = 0; i < n; i++) {
            if (stones[i][0] == 0) {
                zero++;
                cost += stones[i][1];  // 先假设都染红色
            } else if (stones[i][0] == 1) {
                red++;
            } else {  // stones[i][0] == 2
                blue++;
            }
        }
        
        // 可行性检查：已有红色或蓝色数量不能超过总数的一半
        int target = n >> 1;  // 目标红色（或蓝色）数量
        if (red > target || blue > target) {
            return -1;
        }
        
        // 计算需要染成蓝色的无色石头数量
        int needBlue = target - red;
        
        // 贪心调整：将前needBlue个无色石头从红色改为蓝色
        // 由于已经按差值排序，前面的石头改成蓝色能节省更多代价
        for (int i = 0; i < needBlue; i++) {
            cost += stones[i][2] - stones[i][1];  // 蓝色代价 - 红色代价
        }
        
        return cost;
    }

    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 魔法石染色最小代价问题测试 ===");
        
        // 测试用例1：标准情况
        int[][] stones1 = {{1, 5, 3}, {2, 7, 9}, {0, 6, 4}, {0, 7, 9}, {0, 2, 1}, {0, 5, 9}};
        System.out.println("测试用例1:");
        System.out.println("石头配置: [[1,5,3], [2,7,9], [0,6,4], [0,7,9], [0,2,1], [0,5,9]]");
        System.out.println("分析: 6个石头需要3红3蓝，已有1红1蓝，需要从4个无色中选2红2蓝");
        System.out.println("策略: 优先将代价差值大的石头染蓝");
        int result1 = minCost(stones1);
        System.out.println("最小代价: " + result1);
        System.out.println();
        
        // 测试用例2：奇数个石头（无解）
        int[][] stones2 = {{0, 1, 2}, {0, 3, 4}, {1, 0, 0}};
        System.out.println("测试用例2:");
        System.out.println("石头配置: [[0,1,2], [0,3,4], [1,0,0]]");
        System.out.println("分析: 3个石头无法平均分配红蓝");
        int result2 = minCost(stones2);
        System.out.println("结果: " + result2 + " (期望-1，因为奇数个石头)");
        System.out.println();
        
        // 测试用例3：已有红色过多（无解）
        int[][] stones3 = {{1, 0, 0}, {1, 0, 0}, {1, 0, 0}, {0, 1, 2}};
        System.out.println("测试用例3:");
        System.out.println("石头配置: [[1,0,0], [1,0,0], [1,0,0], [0,1,2]]");
        System.out.println("分析: 4个石头需要2红2蓝，但已有3红，无解");
        int result3 = minCost(stones3);
        System.out.println("结果: " + result3 + " (期望-1，因为红色过多)");
        System.out.println();
        
        // 测试用例4：简单情况
        int[][] stones4 = {{0, 10, 1}, {0, 1, 10}};
        System.out.println("测试用例4:");
        System.out.println("石头配置: [[0,10,1], [0,1,10]]");
        System.out.println("分析: 2个石头需要1红1蓝");
        System.out.println("策略: 第一个石头染蓝(代价1)，第二个石头染红(代价1)");
        int result4 = minCost(stones4);
        System.out.println("最小代价: " + result4 + " (期望2)");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
