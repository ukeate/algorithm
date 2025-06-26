package basic.c44;

/**
 * Nim博弈问题
 * 
 * 问题描述：
 * 有若干堆铜板，每堆有不同数量的铜板
 * 两人轮流操作，每次可以从任意一堆中取走任意数量的铜板（至少取1个）
 * 取走最后一个铜板的人获胜
 * 给定初始状态，判断先手是否必胜
 * 
 * 博弈论基础：
 * 这是一个经典的公平博弈问题，基于Sprague-Grundy定理
 * 
 * 核心定理：
 * Nim游戏的必胜策略：计算所有堆数量的异或和(XOR)
 * - 如果异或和 = 0：当前玩家处于必败态（后手必胜）
 * - 如果异或和 ≠ 0：当前玩家处于必胜态（先手必胜）
 * 
 * 数学原理：
 * 1. 异或运算的性质：a ⊕ a = 0, a ⊕ 0 = a
 * 2. 如果异或和为0，任何操作都会使异或和变为非0
 * 3. 如果异或和非0，总存在一种操作使异或和变为0
 * 4. 必胜玩家可以始终保持对手处于异或和为0的状态
 * 
 * 时间复杂度：O(n) - 遍历所有堆
 * 空间复杂度：O(1) - 只需常数空间
 * 
 * @author 算法学习
 */
public class Nim {
    
    /**
     * 判断Nim博弈的胜负
     * 
     * @param arr 每堆铜板的数量数组
     * 
     * 算法步骤：
     * 1. 计算所有堆数量的异或和
     * 2. 根据异或和判断先手是否必胜
     * 
     * 策略解释：
     * - 异或和为0：无论先手如何操作，后手都能通过适当操作
     *   重新让异或和变为0，从而保持优势直到胜利
     * - 异或和非0：先手可以通过适当操作让异或和变为0，
     *   然后后手的任何操作都会让异或和变为非0，先手重新获得优势
     * 
     * 实际应用：
     * 这个结论不仅适用于经典Nim游戏，也适用于很多变种博弈问题
     */
    public static void win(int[] arr) {
        int eor = 0; // 异或和，初始为0
        
        // 计算所有堆数量的异或和
        for (int num : arr) {
            eor ^= num;
        }
        
        // 根据异或和判断胜负
        if (eor == 0) {
            System.out.println("后手赢");
        } else {
            System.out.println("先手赢");
        }
    }
    
    /**
     * 扩展方法：不仅判断胜负，还返回结果
     * 
     * @param arr 每堆铜板的数量数组
     * @return true表示先手必胜，false表示后手必胜
     */
    public static boolean canFirstPlayerWin(int[] arr) {
        int eor = 0;
        for (int num : arr) {
            eor ^= num;
        }
        return eor != 0;
    }
    
    /**
     * 扩展方法：给出必胜策略的具体操作
     * 当先手必胜时，返回应该如何操作
     * 
     * @param arr 每堆铜板的数量数组
     * @return 操作建议：[堆索引, 取走数量]，如果后手必胜返回null
     */
    public static int[] getWinningMove(int[] arr) {
        int eor = 0;
        for (int num : arr) {
            eor ^= num;
        }
        
        // 如果异或和为0，后手必胜，没有必胜策略
        if (eor == 0) {
            return null;
        }
        
        // 找到第一个可以使异或和变为0的操作
        for (int i = 0; i < arr.length; i++) {
            int target = arr[i] ^ eor; // 目标值
            if (target < arr[i]) {
                // 从第i堆取走 (arr[i] - target) 个铜板
                return new int[]{i, arr[i] - target};
            }
        }
        
        return null; // 理论上不会到达这里
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：经典Nim游戏
        int[] game1 = {3, 4, 5}; // 3⊕4⊕5 = 2 ≠ 0，先手必胜
        System.out.println("游戏1 - 堆数量: " + java.util.Arrays.toString(game1));
        win(game1);
        
        // 测试用例2：平衡状态
        int[] game2 = {1, 4, 5}; // 1⊕4⊕5 = 0，后手必胜
        System.out.println("\n游戏2 - 堆数量: " + java.util.Arrays.toString(game2));
        win(game2);
        
        // 测试用例3：单堆游戏
        int[] game3 = {7}; // 7 ≠ 0，先手必胜
        System.out.println("\n游戏3 - 堆数量: " + java.util.Arrays.toString(game3));
        win(game3);
        
        // 演示必胜策略
        if (canFirstPlayerWin(game1)) {
            int[] move = getWinningMove(game1);
            System.out.println("必胜策略：从第" + move[0] + "堆取走" + move[1] + "个铜板");
        }
    }
}
