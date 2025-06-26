package leetc.top;

/**
 * LeetCode 171. Excel表列序号 (Excel Sheet Column Number)
 * 
 * 问题描述：
 * 给你一个字符串 columnTitle ，表示 Excel 表格中的列名称。返回该列名称对应的列序号。
 * 
 * Excel 列名称规则：
 * A -> 1, B -> 2, C -> 3, ..., Z -> 26
 * AA -> 27, AB -> 28, ..., AZ -> 52
 * BA -> 53, ..., ZZ -> 702
 * AAA -> 703, AAB -> 704, ...
 * 
 * 示例：
 * 输入：columnTitle = "A"    输出：1
 * 输入：columnTitle = "AB"   输出：28
 * 输入：columnTitle = "ZY"   输出：701
 * 
 * 解法思路：
 * 26进制转换（特殊的26进制）：
 * 1. 这是一个类似26进制的转换问题，但没有0
 * 2. A-Z对应1-26，而不是0-25
 * 3. 从左到右处理每一位，类似于数字进制转换
 * 4. 每一位的值 = (字符 - 'A' + 1)，权重 = 26^(从右数起的位数-1)
 * 
 * 转换公式：
 * - 对于每个字符c，其数值为 (c - 'A' + 1)
 * - 使用秦九韶算法：result = result * 26 + 当前位的值
 * 
 * 与普通26进制的区别：
 * - 普通26进制：0,1,2,...,25 对应 A,B,C,...,Z
 * - Excel进制：1,2,3,...,26 对应 A,B,C,...,Z
 * - 没有"0"这个概念，最小值是1(A)
 * 
 * 算法步骤：
 * 1. 从左到右遍历字符串
 * 2. 对于每个字符，计算其对应的数值(1-26)
 * 3. 使用进制转换公式：ans = ans * 26 + 当前位的值
 * 4. 累加得到最终结果
 * 
 * 举例说明：
 * "AB" = A*26^1 + B*26^0 = 1*26 + 2*1 = 28
 * "ZY" = Z*26^1 + Y*26^0 = 26*26 + 25*1 = 701
 * 
 * 时间复杂度：O(n) - n为字符串长度，需要遍历每个字符
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/excel-sheet-column-number/
 */
public class P171_ExcelSheetColumnNumber {
    
    /**
     * 将Excel列名称转换为列序号
     * 
     * @param s Excel列名称字符串
     * @return 对应的列序号
     */
    public static int titleToNumber(String s) {
        char[] str = s.toCharArray();
        int ans = 0;
        
        // 从左到右处理每个字符
        for (int i = 0; i < str.length; i++) {
            // 26进制转换：ans = ans * 26 + 当前位的值
            // (str[i] - 'A') + 1 将字符转换为1-26的数值
            ans = ans * 26 + (str[i] - 'A') + 1;
        }
        
        return ans;
    }
}
