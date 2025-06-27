package leetc.top;

/**
 * LeetCode 273. 整数转换英文表示 (Integer to English Words)
 * 
 * 问题描述：
 * 将非负整数 num 转换为其对应的英文表示。
 * 
 * 示例：
 * 输入：num = 123
 * 输出："One Hundred Twenty Three"
 * 
 * 输入：num = 12345
 * 输出："Twelve Thousand Three Hundred Forty Five"
 * 
 * 解法思路：
 * 递归分解 + 字符串拼接：
 * 1. 按照英文数字的表示规律，将数字分解为三位一组
 * 2. 对每三位数字单独处理（百位、十位、个位）
 * 3. 根据位置添加相应的单位（Billion, Million, Thousand）
 * 4. 特殊处理边界情况（0、负数、Integer.MIN_VALUE）
 * 
 * 核心思想：
 * - 1-19的数字有特殊的英文表示
 * - 20-90的整十数有专门的表示
 * - 100以上按照"数字+单位"的模式
 * - 每三位为一组，从高位到低位处理
 * 
 * 时间复杂度：O(1) - 32位整数的位数是固定的
 * 空间复杂度：O(1) - 使用固定大小的数组存储单词
 * 
 * LeetCode链接：https://leetcode.com/problems/integer-to-english-words/
 */
public class P273_IntegerToEnglishWords {
    private static String num1To19(int num) {
        if (num < 1 || num > 19) {
            return "";
        }
        String[] names = {"One ", "Two ", "Three ", "Four ", "Five ", "Six ", "Seven ", "Eight ", "Nine ", "Ten ",
                "Eleven ", "Twelve ", "Thirteen ", "Fourteen ", "Fifteen ", "Sixteen ", "Seventeen ", "Eighteen ", "Nineteen "};
        return names[num - 1];
    }

    private static String num1To99(int num) {
        if (num < 1 || num > 99) {
            return "";
        }
        if (num < 20) {
            return num1To19(num);
        }
        int high = num / 10;
        String[] tyNames = {"Twenty ", "Thirty ", "Forty ", "Fifty ", "Sixty ", "Seventy ", "Eighty ", "Ninety "};
        return tyNames[high - 2] + num1To19(num % 10);
    }

    private static String num1To999(int num) {
        if (num < 1 || num > 999) {
            return "";
        }
        if (num < 100) {
            return num1To99(num);
        }
        int high = num / 100;
        return num1To19(high) + "Hundred " + num1To99(num % 100);
    }

    public static String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        String res = "";
        if (num < 0) {
            res = "Negative ";
        }
        if (num == Integer.MIN_VALUE) {
            res += "Two Billion ";
            num %= -2000000000;
        }
        num = Math.abs(num);
        int high = 1000000000;
        int highIdx = 0;
        String[] names = {"Billion ", "Million ", "Thousand ", ""};
        while (num != 0) {
            int cur = num / high;
            num %= high;
            if (cur != 0) {
                res += num1To999(cur);
                res += names[highIdx];
            }
            high /= 1000;
            highIdx++;
        }
        return res.trim();
    }

    public static void main(String[] args) {
        int test = Integer.MIN_VALUE;
        System.out.println(numberToWords(test));
        System.out.println(numberToWords(10001));
        System.out.println(numberToWords(-10001));
    }
}
