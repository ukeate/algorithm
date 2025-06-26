package basic.c36;

/**
 * 跳跃游戏 - 最小步数问题
 * 
 * 问题描述：
 * 给定一个非负整数数组，数组中每个元素表示在该位置可以跳跃的最大长度。
 * 初始位置在数组第一个位置，目标是到达数组最后一个位置。
 * 求到达最后位置所需的最小跳跃次数。
 * 
 * 例如：
 * 数组[3, 2, 3, 1, 1, 4]
 * 路径：0 -> 3 -> 5（跳2步），或 0 -> 1 -> 4 -> 5（跳3步）
 * 最优解是2步
 * 
 * 算法思路（贪心策略）：
 * 核心思想是在每一步中尽可能跳得更远：
 * 1. 维护当前步数能到达的右边界cur
 * 2. 维护下一步能到达的最远位置next
 * 3. 当遍历到cur边界时，必须跳到下一步，步数+1
 * 4. 在每个位置更新next为能到达的最远距离
 * 
 * 关键洞察：
 * - 如果当前位置超过了当前步数的边界，必须增加步数
 * - 在每个位置都计算从该位置能跳到的最远距离，为下一步做准备
 * 
 * 时间复杂度：O(N)，只需要一次遍历
 * 空间复杂度：O(1)，只使用常数额外空间
 */
public class JumpGame {
    
    /**
     * 计算跳到数组最后位置所需的最小步数
     * 
     * 算法步骤：
     * 1. 初始化step=0（步数），cur=0（当前边界），next=0（下一步边界）
     * 2. 遍历数组每个位置i：
     *    a. 如果i超过了当前边界cur，必须跳一步，更新cur=next
     *    b. 更新next为从位置i能到达的最远距离
     * 3. 返回总步数
     * 
     * @param arr 跳跃数组，arr[i]表示在位置i最多能跳多远
     * @return 到达最后位置的最小步数
     */
    public static int jump(int[] arr) {
        // 边界条件检查
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int step = 0;  // 跳过的步数
        int cur = 0;   // 当前步数能到达的右边界
        int next = 0;  // 下一步能到达的最远边界
        
        // 遍历数组，注意不需要遍历最后一个位置
        // 因为到达最后位置就已经成功了
        for (int i = 0; i < arr.length; i++) {
            // 如果当前位置超过了当前步数的边界
            // 必须增加一步跳跃
            if (cur < i) {
                step++;           // 步数增加
                cur = next;       // 当前边界扩展到下一步的边界
            }
            
            // 更新下一步能到达的最远位置
            // i + arr[i] 表示从位置i最远能跳到的位置
            next = Math.max(next, i + arr[i]);
        }
        
        return step;
    }
    
    /**
     * 扩展版本：判断是否能到达最后位置
     * 
     * @param arr 跳跃数组
     * @return 是否能到达最后位置
     */
    public static boolean canJump(int[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        
        int maxReach = 0;  // 能到达的最远位置
        
        for (int i = 0; i < arr.length; i++) {
            // 如果当前位置超过了能到达的最远位置，无法继续
            if (i > maxReach) {
                return false;
            }
            
            // 更新最远可达位置
            maxReach = Math.max(maxReach, i + arr[i]);
            
            // 如果已经能到达或超过最后位置，返回true
            if (maxReach >= arr.length - 1) {
                return true;
            }
        }
        
        return maxReach >= arr.length - 1;
    }

    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 跳跃游戏测试 ===");
        
        // 测试用例1：正常情况
        int[] arr1 = {3, 2, 3, 1, 1, 4};
        
        System.out.println("测试用例1：");
        printArray(arr1);
        int steps1 = jump(arr1);
        boolean canReach1 = canJump(arr1);
        System.out.println("最小步数: " + steps1);
        System.out.println("能否到达: " + canReach1);
        System.out.println();
        
        // 测试用例2：单元素数组
        int[] arr2 = {0};
        
        System.out.println("测试用例2（单元素）：");
        printArray(arr2);
        int steps2 = jump(arr2);
        boolean canReach2 = canJump(arr2);
        System.out.println("最小步数: " + steps2);
        System.out.println("能否到达: " + canReach2);
        System.out.println();
        
        // 测试用例3：无法到达的情况
        int[] arr3 = {1, 0, 0, 1};
        
        System.out.println("测试用例3（无法到达）：");
        printArray(arr3);
        boolean canReach3 = canJump(arr3);
        System.out.println("能否到达: " + canReach3);
        if (canReach3) {
            int steps3 = jump(arr3);
            System.out.println("最小步数: " + steps3);
        }
        System.out.println();
        
        // 测试用例4：每次只能跳1步
        int[] arr4 = {1, 1, 1, 1, 1};
        
        System.out.println("测试用例4（每次跳1步）：");
        printArray(arr4);
        int steps4 = jump(arr4);
        boolean canReach4 = canJump(arr4);
        System.out.println("最小步数: " + steps4);
        System.out.println("能否到达: " + canReach4);
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    /**
     * 辅助方法：打印数组
     * @param arr 要打印的数组
     */
    private static void printArray(int[] arr) {
        System.out.print("数组: [");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}
