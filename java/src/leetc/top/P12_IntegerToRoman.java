package leetc.top;

/**
 * LeetCode 12. 整数转罗马数字 (Integer to Roman)
 * 
 * 问题描述：
 * 罗马数字包含以下七种字符： I， V， X， L，C，D 和 M。
 * 字符          数值
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * 
 * 给定一个整数，将其转为罗马数字。输入确保在 1 到 3999 的范围内。
 * 
 * 解法思路：
 * 预定义映射表 + 按位处理：
 * 1. 为每个位数（个位、十位、百位、千位）预定义罗马数字映射
 * 2. 每个位数对应一个数组，索引0-9分别对应该位为0-9时的罗马表示
 * 3. 按位提取数字，然后拼接对应的罗马数字
 * 
 * 核心观察：
 * - 罗马数字按位独立：千位、百位、十位、个位可以分别处理
 * - 特殊情况（4, 9等）已在映射表中预处理
 * - 从高位到低位依次拼接
 * 
 * 映射表设计：
 * - 个位：""(0), "I"(1), "II"(2), "III"(3), "IV"(4), "V"(5), "VI"(6), "VII"(7), "VIII"(8), "IX"(9)
 * - 十位：""(0), "X"(10), "XX"(20), ..., "XC"(90)
 * - 百位：""(0), "C"(100), "CC"(200), ..., "CM"(900)
 * - 千位：""(0), "M"(1000), "MM"(2000), "MMM"(3000)
 * 
 * 算法优势：
 * - 预处理避免复杂的规则判断
 * - 时间复杂度稳定，与输入大小无关
 * - 代码简洁，易于理解和维护
 * 
 * 时间复杂度：O(1) - 固定的4次操作
 * 空间复杂度：O(1) - 映射表大小固定
 * 
 * LeetCode链接：https://leetcode.com/problems/integer-to-roman/
 */
public class P12_IntegerToRoman {
    
    /**
     * 将整数转换为罗马数字
     * 
     * @param num 输入的整数（1-3999）
     * @return 对应的罗马数字字符串
     */
    public static String intToRoman(int num) {
        // 预定义映射表：[位数][数值] -> 罗马数字
        // c[0]: 个位 (1-9)
        // c[1]: 十位 (10-90)  
        // c[2]: 百位 (100-900)
        // c[3]: 千位 (1000-3000)
        String[][] c = {
                {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"},     // 个位
                {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"},     // 十位
                {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"},     // 百位
                {"", "M", "MM", "MMM"}                                             // 千位
        };
        
        StringBuilder roman = new StringBuilder();
        
        // 按位提取并拼接罗马数字
        roman.append(c[3][num / 1000 % 10])      // 千位：num/1000取整，再对10取余
             .append(c[2][num / 100 % 10])       // 百位：num/100取整，再对10取余
             .append(c[1][num / 10 % 10])        // 十位：num/10取整，再对10取余
             .append(c[0][num % 10]);            // 个位：num对10取余
        
        return roman.toString();
    }
}
