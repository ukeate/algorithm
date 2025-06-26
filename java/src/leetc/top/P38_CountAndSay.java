package leetc.top;

/**
 * LeetCode 38. 外观数列 (Count and Say)
 * 
 * 问题描述：
 * 给定一个正整数 n ，输出外观数列的第 n 项。
 * 外观数列是一个整数序列，从数字 1 开始，序列中的每一项都是对前一项的描述。
 * 
 * 外观数列的前几项：
 * 1. "1"
 * 2. "11"      (一个1)
 * 3. "21"      (两个1)
 * 4. "1211"    (一个2一个1)
 * 5. "111221"  (一个1一个2两个1)
 * 6. "312211"  (三个1两个2一个1)
 * 
 * 解法思路：
 * 递归 + 字符串处理：
 * 1. 递归获取第n-1项的结果
 * 2. 遍历上一项的字符串，统计连续相同字符的个数
 * 3. 对每组连续相同的字符，输出"个数+字符"的描述
 * 4. 拼接所有描述得到第n项的结果
 * 
 * 核心思想：
 * - 外观数列的定义就是递归的：当前项依赖于前一项
 * - 关键是如何"读出"一个数字序列：计数连续相同的数字
 * - 使用StringBuilder高效拼接字符串
 * 
 * 算法特点：
 * - 递归结构清晰，符合问题的自然定义
 * - 字符数组访问效率高于字符串charAt
 * - 边界处理：最后一组字符需要特殊处理
 * 
 * 时间复杂度：O(4^n) - 每一项的长度大约是前一项的1.3倍，最坏情况呈指数增长
 * 空间复杂度：O(4^n) - 递归栈深度为n，每层存储的字符串长度呈指数增长
 * 
 * LeetCode链接：https://leetcode.com/problems/count-and-say/
 */
public class P38_CountAndSay {
    
    /**
     * 生成外观数列的第n项
     * 
     * 算法步骤：
     * 1. 递归边界：n=1返回"1"，n<1返回空字符串
     * 2. 递归获取第n-1项的字符数组
     * 3. 遍历字符数组，计数连续相同字符
     * 4. 每当字符发生变化时，记录"计数+字符"
     * 5. 处理最后一组字符
     * 6. 返回拼接后的结果字符串
     * 
     * @param n 要生成的外观数列项数（正整数）
     * @return 外观数列的第n项
     */
    public static String countAndSay(int n) {
        // 边界情况：非法输入
        if (n < 1) {
            return "";
        }
        
        // 递归边界：第1项是"1"
        if (n == 1) {
            return "1";
        }
        
        // 递归获取第n-1项，转换为字符数组便于遍历
        char[] last = countAndSay(n - 1).toCharArray();
        StringBuilder ans = new StringBuilder();
        
        int times = 1;  // 当前连续相同字符的计数
        
        // 遍历上一项的字符数组，从第2个字符开始比较
        for (int i = 1; i < last.length; i++) {
            if (last[i - 1] == last[i]) {
                // 当前字符与前一个字符相同，计数增加
                times++;
            } else {
                // 字符发生变化，记录前一组字符的描述
                ans.append(times);          // 添加计数
                ans.append(last[i - 1]);    // 添加字符
                times = 1;                  // 重置计数为1
            }
        }
        
        // 处理最后一组字符（循环结束时还没有处理）
        ans.append(times);
        ans.append(last[last.length - 1]);
        
        return ans.toString();
    }
}
