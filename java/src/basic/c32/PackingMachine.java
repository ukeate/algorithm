package basic.c32;

/**
 * 包装机器问题（物品均分问题）
 * 
 * 问题描述：
 * 有N台包装机器排成一排，每台机器上有不同数量的物品。
 * 每一轮操作中，每台机器可以向相邻的机器传递一个物品。
 * 求最少需要多少轮操作才能使所有机器上的物品数量相等。
 * 如果无法实现均分，返回-1。
 * 
 * 解题思路：
 * 1. 首先判断是否可能均分（总数能被机器数整除）
 * 2. 对于每个位置i，计算该位置作为"瓶颈"时需要的轮数
 * 3. 瓶颈计算考虑左侧和右侧的物品流动需求
 * 4. 如果左右两侧都缺物品，则当前位置需要同时向两边传递
 * 5. 如果只有一侧缺物品，则按较大的需求量计算
 * 
 * 时间复杂度：O(N)
 * 空间复杂度：O(1)
 */
public class PackingMachine {
    
    /**
     * 计算最少需要的操作轮数使所有机器物品数量相等
     * @param arr 每台机器上的物品数量
     * @return 最少操作轮数，如果无法均分则返回-1
     */
    public static int min(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int size = arr.length;
        int sum = 0;
        
        // 计算物品总数
        for (int i = 0; i < size; i++) {
            sum += arr[i];
        }
        
        // 判断是否可能均分
        if (sum % size != 0) {
            return -1;
        }
        
        int avg = sum / size;       // 每台机器应该有的物品数
        int leftSum = 0;           // 当前位置左侧的物品总数
        int ans = 0;               // 最少操作轮数
        
        // 遍历每个位置，计算作为瓶颈位置时的操作轮数
        for (int i = 0; i < arr.length; i++) {
            // leftRest: 左侧区域相对于平均值的差额（负数表示缺少）
            int leftRest = leftSum - i * avg;
            
            // rightRest: 右侧区域相对于平均值的差额（负数表示缺少）
            int rightRest = (sum - leftSum - arr[i]) - (size - i - 1) * avg;
            
            if (leftRest < 0 && rightRest < 0) {
                // 左右两侧都缺物品，当前位置需要同时向两边传递
                // 总需求 = 左侧缺少的 + 右侧缺少的
                ans = Math.max(ans, Math.abs(leftRest) + Math.abs(rightRest));
            } else {
                // 只有一侧缺物品，或者有一侧多余物品可以通过当前位置流向另一侧
                // 瓶颈 = max(左侧需求, 右侧需求)
                ans = Math.max(ans, Math.max(Math.abs(leftRest), Math.abs(rightRest)));
            }
            
            // 更新左侧物品总数
            leftSum += arr[i];
        }
        
        return ans;
    }
}
