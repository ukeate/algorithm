package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 412. Fizz Buzz
 * 
 * 问题描述：
 * 给你一个整数 n ，找出从 1 到 n 各个整数的 Fizz Buzz 表示，并用字符串数组返回。
 * 
 * 规则：
 * - 如果 i 能被 3 和 5 整除，将 i 替换为 "FizzBuzz"
 * - 如果 i 能被 3 整除，将 i 替换为 "Fizz"
 * - 如果 i 能被 5 整除，将 i 替换为 "Buzz"
 * - 其他情况，将 i 替换为 i 的字符串形式
 * 
 * 示例：
 * 输入：n = 3
 * 输出：["1","2","Fizz"]
 * 
 * 输入：n = 5
 * 输出：["1","2","Fizz","4","Buzz"]
 * 
 * 输入：n = 15
 * 输出：["1","2","Fizz","4","Buzz","Fizz","7","8","Fizz","Buzz","11","Fizz","13","14","FizzBuzz"]
 * 
 * 解法思路：
 * 简单模拟：
 * 1. 从1到n遍历每个数字
 * 2. 按优先级检查整除条件：
 *    - 先检查是否同时被3和5整除（即被15整除）
 *    - 再检查是否被5整除
 *    - 再检查是否被3整除  
 *    - 其他情况直接转换为字符串
 * 3. 将结果添加到列表中
 * 
 * 核心观察：
 * - 能被3和5整除等价于能被15整除
 * - 检查顺序很重要：必须先检查FizzBuzz条件
 * - 这是一个经典的编程入门题，考查基本的条件判断和循环
 * 
 * 优化技巧：
 * - 使用 i % 15 == 0 代替 i % 3 == 0 && i % 5 == 0
 * - 可以进一步优化为使用计数器而不是取模运算，但会增加代码复杂度
 * 
 * 扩展思考：
 * - 如果有更多的规则（如7对应"Bang"），如何设计可扩展的解决方案？
 * - 可以使用策略模式或配置驱动的方法
 * 
 * 时间复杂度：O(n) - 需要遍历1到n的每个数字
 * 空间复杂度：O(1) - 除了输出数组外，只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/fizz-buzz/
 */
public class P412_FizzBuzz {
    
    /**
     * 生成FizzBuzz序列
     * 
     * 算法步骤：
     * 1. 创建结果列表
     * 2. 从1到n遍历每个数字
     * 3. 按优先级应用替换规则：
     *    - 被15整除 → "FizzBuzz"
     *    - 被5整除 → "Buzz"  
     *    - 被3整除 → "Fizz"
     *    - 其他 → 数字字符串
     * 4. 返回结果列表
     * 
     * 判断顺序说明：
     * - 必须先判断FizzBuzz（被15整除），因为同时满足被3和5整除
     * - 如果先判断Fizz或Buzz，会错误地将15的倍数归类为单一条件
     * 
     * @param n 正整数，生成1到n的FizzBuzz序列
     * @return FizzBuzz字符串列表
     */
    public static List<String> fizzBuzz(int n) {
        List<String> ans = new ArrayList<>();  // 结果列表
        
        // 遍历从1到n的每个数字
        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) {
                // 优先级最高：同时被3和5整除（即被15整除）
                ans.add("FizzBuzz");
            } else if (i % 5 == 0) {
                // 仅被5整除
                ans.add("Buzz");
            } else if (i % 3 == 0) {
                // 仅被3整除
                ans.add("Fizz");
            } else {
                // 其他情况：转换为字符串
                ans.add(String.valueOf(i));
            }
        }
        
        return ans;
    }
}
