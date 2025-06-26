package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 956. 最高的广告牌 (Tallest Billboard)
 * 
 * 问题描述：
 * 你正在安装一个广告牌，并希望它尽可能的高。这块广告牌将有两个钢制支架，两个支架必须等长。
 * 你有一堆可以焊接在一起的钢筋 rods。举个例子，如果你有长度为 1, 2, 和 3 的钢筋，
 * 你可以将它们焊接在一起形成长度为 6 的钢筋。
 * 
 * 返回广告牌的最大可能安装高度。如果没法安装广告牌，请返回 0。
 * 
 * 示例：
 * - 输入：[1,2,3,6]
 * - 输出：6
 * - 解释：我们有两个不相交的子集 {1,2,3} 和 {6}，它们具有相同的和 = 6。
 * 
 * - 输入：[1,2,3,4,5,6]
 * - 输出：10
 * - 解释：我们有两个不相交的子集 {2,3,5} 和 {4,6}，它们具有相同的和 = 10。
 * 
 * - 输入：[1,2]
 * - 输出：0
 * - 解释：没法让两个不相交的子集具有相同的和。
 * 
 * 解法思路：
 * 动态规划 + 哈希表优化：
 * 1. 将问题转化为：找两个不相交子集，使其和相等，求最大和
 * 2. 用差值作为状态：dp[diff] = 较小子集的最大和
 * 3. 对每个钢筋，可以放入大集合、小集合或不使用
 * 4. 动态更新所有可能的差值状态
 * 
 * 核心思想：
 * - 状态压缩：用差值代替两个和，减少状态空间
 * - 滚动数组：每次迭代只依赖上一轮结果
 * - 最优性：对每个差值，维护较小和的最大值
 * 
 * 时间复杂度：O(n × sum) - n个钢筋，sum为所有钢筋长度和
 * 空间复杂度：O(sum) - 哈希表大小
 * 
 * LeetCode链接：https://leetcode.com/problems/tallest-billboard/
 */
public class P956_TallestBillboard {
    
    /**
     * 计算最高广告牌的高度
     * 
     * 算法思想：
     * 设两个支架的高度分别为big和small（big >= small），差值为diff = big - small
     * 状态定义：dp[diff] = 当差值为diff时，small的最大可能值
     * 
     * 状态转移：
     * 对于每个钢筋num，有三种选择：
     * 1. 不使用：状态不变
     * 2. 放入big中：差值增加num，small值不变
     * 3. 放入small中：分两种情况
     *    - 如果small+num <= big：差值减少num，small增加num
     *    - 如果small+num > big：角色互换，新的差值和small需要重新计算
     * 
     * @param rods 钢筋长度数组
     * @return 最高广告牌高度
     */
    public int tallestBillboard(int[] rods) {
        // dp哈希表：<差值, 较小子集的最大和>
        HashMap<Integer, Integer> dp = new HashMap<>();
        HashMap<Integer, Integer> cur; // 当前轮次的状态
        
        // 初始状态：差值为0，较小和为0（两个空集合）
        dp.put(0, 0);
        
        // 处理每个钢筋
        for (int num : rods) {
            if (num != 0) { // 跳过长度为0的钢筋
                // 复制当前状态，避免在迭代中修改
                cur = new HashMap<>(dp);
                
                // 遍历所有当前可能的差值状态
                for (int d : cur.keySet()) {
                    int small = cur.get(d); // 当前差值d对应的较小和
                    
                    // 选择1：将num放入big中
                    // 新差值 = d + num，较小和不变
                    dp.put(d + num, Math.max(small, dp.getOrDefault(d + num, 0)));
                    
                    // 选择2：将num放入small中
                    int dpSmall = dp.getOrDefault(Math.abs(num - d), 0);
                    
                    if (d >= num) {
                        // 情况2a：small + num <= big
                        // 新差值 = d - num，新的较小和 = small + num
                        dp.put(d - num, Math.max(small + num, dpSmall));
                    } else {
                        // 情况2b：small + num > big，角色互换
                        // 原来：big = small + d, small = small
                        // 现在：new_big = small + num, new_small = small + d
                        // 新差值 = num - d，新的较小和 = small + d
                        dp.put(num - d, Math.max(small + d, dpSmall));
                    }
                }
            }
        }
        
        // 返回差值为0时的较小和，也就是两个相等子集的和
        return dp.get(0);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P956_TallestBillboard solution = new P956_TallestBillboard();
        
        // 测试用例1：基本示例
        int[] rods1 = {1, 2, 3, 6};
        System.out.println("测试用例1:");
        System.out.println("输入: " + java.util.Arrays.toString(rods1));
        System.out.println("输出: " + solution.tallestBillboard(rods1));
        System.out.println("期望: 6");
        System.out.println("解释: 子集{1,2,3}和{6}都等于6");
        System.out.println();
        
        // 测试用例2：较复杂示例
        int[] rods2 = {1, 2, 3, 4, 5, 6};
        System.out.println("测试用例2:");
        System.out.println("输入: " + java.util.Arrays.toString(rods2));
        System.out.println("输出: " + solution.tallestBillboard(rods2));
        System.out.println("期望: 10");
        System.out.println("解释: 子集{2,3,5}和{4,6}都等于10");
        System.out.println();
        
        // 测试用例3：无解情况
        int[] rods3 = {1, 2};
        System.out.println("测试用例3 (无解):");
        System.out.println("输入: " + java.util.Arrays.toString(rods3));
        System.out.println("输出: " + solution.tallestBillboard(rods3));
        System.out.println("期望: 0");
        System.out.println("解释: 无法找到两个相等的不相交子集");
        System.out.println();
        
        // 测试用例4：所有钢筋相同
        int[] rods4 = {2, 2, 2, 2};
        System.out.println("测试用例4 (相同长度):");
        System.out.println("输入: " + java.util.Arrays.toString(rods4));
        System.out.println("输出: " + solution.tallestBillboard(rods4));
        System.out.println("期望: 4");
        System.out.println("解释: 两边各放两个钢筋，每边高度为4");
        System.out.println();
        
        // 测试用例5：单个钢筋
        int[] rods5 = {1};
        System.out.println("测试用例5 (单个钢筋):");
        System.out.println("输入: " + java.util.Arrays.toString(rods5));
        System.out.println("输出: " + solution.tallestBillboard(rods5));
        System.out.println("期望: 0");
        System.out.println("解释: 只有一个钢筋，无法组成两个支架");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 动态规划，用差值压缩状态空间");
        System.out.println("- 时间复杂度: O(n×sum) - n个钢筋，sum为总长度");
        System.out.println("- 空间复杂度: O(sum) - 哈希表存储状态");
        System.out.println("- 关键技巧: 维护差值到较小和的映射");
        System.out.println("- 状态转移: 考虑每个钢筋的三种使用方式");
        System.out.println("- 最优性: 对每个差值保持较小和的最大值");
    }
}
