package leetc.top;

/**
 * LeetCode 13. 罗马数字转整数 (Roman to Integer)
 * 
 * 问题描述：
 * 罗马数字包含以下七种字符: I, V, X, L, C, D 和 M。
 * 字符          数值
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * 
 * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例：
 * - I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9
 * - X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90
 * - C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900
 * 
 * 解法思路：
 * 从左到右扫描 + 减法规则：
 * 1. 将罗马字符转换为对应的数值数组
 * 2. 从左到右遍历数值数组
 * 3. 如果当前数值小于下一个数值，则减去当前数值（特殊情况）
 * 4. 否则加上当前数值（正常情况）
 * 5. 最后加上最后一个数值
 * 
 * 核心规则：
 * - 正常情况：较小数字在较大数字右边，直接相加
 * - 特殊情况：较小数字在较大数字左边，用减法处理
 * 
 * 算法特点：
 * - 预处理：先转换为数值数组，便于比较
 * - 一次遍历：只需要扫描一遍数组
 * - 规则简单：通过大小比较判断加减
 * 
 * 示例解析：
 * "MCMXC" -> [1000,100,1000,10,100]
 * 1000 + (-100) + 1000 + (-10) + 100 = 1990
 * 
 * 时间复杂度：O(n) - n为字符串长度
 * 空间复杂度：O(n) - 数值数组空间
 * 
 * LeetCode链接：https://leetcode.com/problems/roman-to-integer/
 */
public class P13_RomanToInteger {
    
    /**
     * 罗马数字转整数主方法
     * 
     * @param s 罗马数字字符串
     * @return 对应的整数值
     */
    public static int romanToInt(String s) {
        // 转换为数值数组
        int nums[] = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case 'M':
                    nums[i] = 1000;
                    break;
                case 'D':
                    nums[i] = 500;
                    break;
                case 'C':
                    nums[i] = 100;
                    break;
                case 'L':
                    nums[i] = 50;
                    break;
                case 'X':
                    nums[i] = 10;
                    break;
                case 'V':
                    nums[i] = 5;
                    break;
                case 'I':
                    nums[i] = 1;
                    break;
            }
        }
        
        int sum = 0;
        
        // 遍历数值数组（除最后一个元素）
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] < nums[i + 1]) {
                // 特殊情况：当前数值小于下一个数值，使用减法
                // 如 IV(4), IX(9), XL(40), XC(90), CD(400), CM(900)
                sum -= nums[i];
            } else {
                // 正常情况：当前数值大于等于下一个数值，使用加法
                sum += nums[i];
            }
        }
        
        // 加上最后一个数值（最后一个数值总是正数）
        return sum + nums[nums.length - 1];
    }
}
