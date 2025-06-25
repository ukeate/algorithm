package leetc.top;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * LeetCode 739. 每日温度 (Daily Temperatures)
 * 
 * 问题描述：
 * 给定一个整数数组 temperatures，表示每天的温度，
 * 返回一个数组 answer，其中 answer[i] 是指在第 i 天之后，
 * 才会有更高的温度。如果气温在这之后都不会升高，请在该位置用 0 来代替。
 * 
 * 示例：
 * 输入: temperatures = [73,74,75,71,69,72,76,73]
 * 输出: [1,1,4,2,1,1,0,0]
 * 
 * 解法思路：
 * 使用单调栈求解下一个更大元素问题：
 * 1. 维护一个单调递减栈，栈中存储相同温度的索引列表
 * 2. 遍历温度数组，对于每个温度：
 *    - 如果大于栈顶温度，弹出所有小于当前温度的元素并计算距离
 *    - 如果等于栈顶温度，加入栈顶列表
 *    - 如果小于栈顶温度，作为新元素入栈
 * 3. 这样可以高效找到每个位置右侧第一个更大的元素
 * 
 * 时间复杂度：O(n) - 每个元素最多入栈出栈一次
 * 空间复杂度：O(n) - 单调栈空间
 */
public class P739_DailyTemperatures {
    /**
     * 计算每日温度升高的等待天数
     * 
     * @param arr 温度数组
     * @return 每个位置需要等待的天数数组
     */
    public static int[] dailyTemperatures(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        
        int n = arr.length;
        int[] ans = new int[n]; // 结果数组，默认为0
        Stack<List<Integer>> stack = new Stack<>(); // 单调栈，存储索引列表
        
        for (int i = 0; i < n; i++) {
            // 弹出所有温度小于当前温度的元素
            while (!stack.isEmpty() && arr[stack.peek().get(0)] < arr[i]) {
                List<Integer> cs = stack.pop();
                for (Integer c : cs) {
                    ans[c] = i - c; // 计算等待天数
                }
            }
            
            // 如果栈顶温度等于当前温度，加入同一列表
            if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
                stack.peek().add(i);
            } else {
                // 创建新的温度组并入栈
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                stack.push(list);
            }
        }
        return ans;
    }
}
