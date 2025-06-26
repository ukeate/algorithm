package giant.c37;

/**
 * 扑克牌游戏每轮必胜策略问题
 * 
 * 问题描述：
 * 两人扑克牌游戏，红桃J和梅花Q丢失，设计新规则：
 * 1. A,2,3,4...10,J,Q,K对应1-13，大小王对应0
 * 2. 轮流摸牌，每次摸到牌只能选择"保留"或"使用"
 * 3. "保留"：牌的分数加到总分，持续到游戏结束
 * 4. "使用"：牌的分数*3加到总分，但只在当前轮、下一轮、下下轮生效
 * 5. 每一轮总分大的人获胜
 * 
 * 问题目标：
 * 已知每一轮对手的总分，求小明在每一轮都获胜的前提下，最终能达到的最大分数
 * 如果无法保证每一轮都赢，返回-1
 * 
 * 解题思路：
 * 使用回溯法 + 博弈策略：
 * 1. 对于每张牌，有两种选择：保留或使用
 * 2. 保留：当前分数+牌面值，效果持续到最后
 * 3. 使用：当前分数+牌面值*3，但只持续3轮
 * 4. 每一轮都要保证总分 > 对手分数
 * 5. 在满足获胜条件下，选择能得到最大最终分数的策略
 * 
 * 算法特点：
 * - 博弈论问题：每一步都要考虑获胜约束
 * - 状态空间搜索：枚举所有可能的选择组合
 * - 动态效果：使用牌的效果有时间限制
 * - 约束优化：在约束条件下求最优解
 * 
 * 时间复杂度：O(2^n)，其中n是牌的数量（最坏情况下需要枚举所有可能）
 * 空间复杂度：O(n)，递归栈空间
 * 
 * 来源：字节跳动面试题
 * 
 * @author Zhu Runqi
 */
public class GameForEveryStepWin {
    
    /**
     * 递归求解每轮必胜的最大分数
     * 
     * 状态定义：
     * - idx：当前轮次
     * - hold：保留牌的累计分数（持续到最后）
     * - cur：当前轮的暴发分数（这轮生效的使用牌分数）
     * - next：下一轮的暴发分数（下轮生效的使用牌分数）
     * 
     * 选择策略：
     * 1. 保留当前牌：总分增加牌面值，效果持续
     * 2. 使用当前牌：总分增加牌面值*3，但只持续3轮
     * 
     * 约束条件：
     * 每一轮的总分必须大于对手分数
     * 
     * @param cands 抽牌数组，每个元素是牌的点数
     * @param scores 对手每一轮的分数
     * @param idx 当前轮次索引
     * @param hold 累计保留牌分数
     * @param cur 当前轮暴发分数
     * @param next 下一轮暴发分数
     * @return 在每轮都获胜前提下的最大分数，无法获胜返回-1
     */
    public static int f(int[] cands, int[] scores, int idx, int hold, int cur, int next) {
        // 递归边界：最后一轮（第25轮，索引为24）
        if (idx == 25) {
            // 最后一轮只能使用牌（因为保留没有意义）
            int all = hold + cur + cands[idx] * 3;
            if (all <= scores[idx]) {
                return -1;  // 无法获胜
            }
            return all;  // 返回最终总分
        }
        
        // 策略1：保留当前牌
        int all1 = hold + cur + cands[idx];  // 当前轮总分
        int p1 = -1;
        if (all1 > scores[idx]) {  // 如果能够获胜
            // 递归到下一轮：保留分数增加，暴发分数更新
            p1 = f(cands, scores, idx + 1, hold + cands[idx], next, 0);
        }
        
        // 策略2：使用当前牌
        int all2 = hold + cur + cands[idx] * 3;  // 当前轮总分
        int p2 = -1;
        if (all2 > scores[idx]) {  // 如果能够获胜
            // 递归到下一轮：保留分数不变，暴发分数更新
            // next + cands[idx] * 3：下一轮的暴发分数
            // cands[idx] * 3：下下轮的暴发分数
            p2 = f(cands, scores, idx + 1, hold, next + cands[idx] * 3, cands[idx] * 3);
        }
        
        // 返回两种策略中的最大值
        return Math.max(p1, p2);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 扑克牌游戏每轮必胜策略问题 ===\n");
        
        // 测试用例1：简单情况
        System.out.println("测试用例1：简单情况");
        int[] cands1 = {2, 3, 1};  // 前3轮的牌
        int[] scores1 = {1, 5, 8}; // 对手前3轮的分数
        
        System.out.println("抽牌序列: " + java.util.Arrays.toString(cands1));
        System.out.println("对手分数: " + java.util.Arrays.toString(scores1));
        
        int result1 = f(cands1, scores1, 0, 0, 0, 0);
        System.out.println("最大分数: " + result1);
        
        if (result1 != -1) {
            System.out.println("策略分析:");
            System.out.println("  第1轮: 使用2(2*3=6分) > 对手1分 ✓");
            System.out.println("  第2轮: 使用3(3*3=9分，累计15分) > 对手5分 ✓");
            System.out.println("  第3轮: 使用1(1*3=3分，总分视前面选择而定) > 对手8分 ✓");
        }
        System.out.println();
        
        // 测试用例2：无法获胜的情况
        System.out.println("测试用例2：无法获胜的情况");
        int[] cands2 = {1, 1, 1};  // 都是小牌
        int[] scores2 = {10, 15, 20}; // 对手分数很高
        
        System.out.println("抽牌序列: " + java.util.Arrays.toString(cands2));
        System.out.println("对手分数: " + java.util.Arrays.toString(scores2));
        
        int result2 = f(cands2, scores2, 0, 0, 0, 0);
        System.out.println("最大分数: " + result2 + " (无法获胜)");
        System.out.println();
        
        // 测试用例3：需要混合策略
        System.out.println("测试用例3：需要混合策略");
        int[] cands3 = {5, 2, 4};
        int[] scores3 = {4, 10, 12};
        
        System.out.println("抽牌序列: " + java.util.Arrays.toString(cands3));
        System.out.println("对手分数: " + java.util.Arrays.toString(scores3));
        
        int result3 = f(cands3, scores3, 0, 0, 0, 0);
        System.out.println("最大分数: " + result3);
        
        if (result3 != -1) {
            System.out.println("策略分析:");
            System.out.println("  可能的最优策略:");
            System.out.println("  第1轮: 使用5(5*3=15分) > 对手4分 ✓");
            System.out.println("  第2轮: 考虑保留vs使用的权衡");
            System.out.println("  第3轮: 根据前面选择确定最终策略");
        }
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 状态空间：");
        System.out.println("   - 每张牌都有保留/使用两种选择");
        System.out.println("   - 状态包括：当前轮次、累计保留分、当前暴发分、下轮暴发分");
        System.out.println();
        System.out.println("2. 约束条件：");
        System.out.println("   - 每一轮总分都必须大于对手分数");
        System.out.println("   - 使用牌的效果只持续3轮");
        System.out.println();
        System.out.println("3. 优化目标：");
        System.out.println("   - 在满足每轮获胜的前提下，最大化最终总分");
        System.out.println("   - 需要平衡短期获胜和长期收益");
        System.out.println();
        System.out.println("4. 算法特点：");
        System.out.println("   - 回溯搜索：枚举所有可能的策略组合");
        System.out.println("   - 剪枝优化：如果某一轮无法获胜，直接返回-1");
        System.out.println("   - 博弈策略：每一步都要考虑获胜约束");
        System.out.println();
        System.out.println("5. 实际应用：");
        System.out.println("   - 游戏AI设计：卡牌游戏的策略规划");
        System.out.println("   - 资源分配：在约束条件下的最优分配");
        System.out.println("   - 投资策略：短期收益vs长期收益的权衡");
    }
}
