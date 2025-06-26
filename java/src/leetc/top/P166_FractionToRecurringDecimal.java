package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 166. 分数到小数 (Fraction to Recurring Decimal)
 * 
 * 问题描述：
 * 给定两个整数，分别表示分数的分子 numerator 和分母 denominator，
 * 以字符串形式返回小数。如果小数部分为循环小数，则将循环的部分括在括号内。
 * 如果存在多个答案，只需返回任意一个。
 * 对于所有给定的输入，保证答案字符串的长度小于 10^4。
 * 
 * 解法思路：
 * 模拟长除法 + 循环检测：
 * 1. 处理符号：根据分子分母的符号确定结果符号
 * 2. 计算整数部分：分子除以分母
 * 3. 计算小数部分：模拟长除法过程
 * 4. 循环检测：使用HashMap记录余数出现的位置
 * 5. 发现重复余数时，说明开始循环
 * 
 * 长除法模拟：
 * - 余数 * 10 / 分母 = 下一位小数
 * - 新余数 = (余数 * 10) % 分母
 * - 余数为0时除法结束，无循环
 * - 余数重复时开始循环
 * 
 * 循环检测机制：
 * - HashMap<余数, 小数点后的位置>
 * - 如果当前余数已存在，说明循环开始
 * - 在对应位置插入左括号，结尾添加右括号
 * 
 * 关键处理：
 * - 符号处理：使用异或判断符号
 * - 溢出处理：转换为long避免溢出
 * - 边界情况：分子为0、整除情况
 * 
 * 时间复杂度：O(分母) - 最多除法次数等于分母大小
 * 空间复杂度：O(分母) - HashMap存储余数
 * 
 * LeetCode链接：https://leetcode.com/problems/fraction-to-recurring-decimal/
 */
public class P166_FractionToRecurringDecimal {
    
    /**
     * 将分数转换为小数字符串表示
     * 
     * @param numerator 分子
     * @param denominator 分母
     * @return 小数的字符串表示
     */
    public static String fractionToDecimal(int numerator, int denominator) {
        // 特殊情况：分子为0
        if (numerator == 0) {
            return "0";
        }
        
        StringBuilder res = new StringBuilder();
        
        // 处理符号：异或运算判断正负
        res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");
        
        // 转换为长整型避免溢出
        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);
        
        // 计算整数部分
        res.append(num / den);
        num %= den;  // 计算余数
        
        // 如果余数为0，说明整除，直接返回
        if (num == 0) {
            return res.toString();
        }
        
        // 添加小数点
        res.append(".");
        
        // 使用HashMap检测循环：余数 -> 小数点后的位置
        HashMap<Long, Integer> map = new HashMap<>();
        map.put(num, res.length());  // 记录当前余数的位置
        
        // 模拟长除法过程
        while (num != 0) {
            num *= 10;  // 余数乘以10
            res.append(num / den);  // 添加下一位小数
            num %= den;  // 计算新余数
            
            if (map.containsKey(num)) {
                // 发现重复余数，说明循环开始
                int idx = map.get(num);  // 获取循环开始的位置
                res.insert(idx, "(");    // 在循环开始位置插入左括号
                res.append(")");         // 在结尾添加右括号
                break;
            } else {
                // 记录新余数的位置
                map.put(num, res.length());
            }
        }
        
        return res.toString();
    }
}
