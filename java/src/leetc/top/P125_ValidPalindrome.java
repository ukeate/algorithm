package leetc.top;

/**
 * LeetCode 125. 验证回文串 (Valid Palindrome)
 * 
 * 问题描述：
 * 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
 * 说明：本题中，我们将空字符串定义为有效的回文串。
 * 
 * 示例：
 * 输入: "A man, a plan, a canal: Panama"
 * 输出: true
 * 解释: "amanaplanacanalpanama" 是回文串
 * 
 * 解法思路：
 * 双指针技术：
 * 1. 左指针从字符串开始，右指针从字符串结束
 * 2. 跳过非字母数字字符
 * 3. 对于有效字符，比较是否相等（忽略大小写）
 * 4. 如果所有对应字符都相等，则是回文串
 * 
 * 核心处理：
 * - 字符有效性检查：只处理字母和数字
 * - 大小写忽略：通过ASCII码差值判断（大小写字母相差32）
 * - 指针移动：遇到无效字符时单独移动对应指针
 * 
 * 字符比较逻辑：
 * - 数字字符：直接比较
 * - 字母字符：忽略大小写比较（ASCII差值为32）
 * 
 * 时间复杂度：O(n) - 每个字符最多被访问一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/valid-palindrome/
 */
public class P125_ValidPalindrome {
    
    /**
     * 比较两个字符是否相等（忽略大小写）
     * 
     * @param c1 第一个字符
     * @param c2 第二个字符
     * @return 是否相等
     */
    private static boolean equal(char c1, char c2) {
        // 如果其中有数字，直接比较
        if (isNumber(c1) || isNumber(c2)) {
            return c1 == c2;
        }
        
        // 字母比较：相同或者大小写差异（ASCII差值为32）
        return (c1 == c2) || (Math.max(c1, c2) - Math.min(c1, c2) == 32);
    }
    
    /**
     * 判断字符是否为数字
     * 
     * @param c 输入字符
     * @return 是否为数字
     */
    private static boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    /**
     * 判断字符是否为字母
     * 
     * @param c 输入字符
     * @return 是否为字母
     */
    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * 判断字符是否有效（字母或数字）
     * 
     * @param c 输入字符
     * @return 是否有效
     */
    private static boolean validChar(char c) {
        return isLetter(c) || isNumber(c);
    }

    /**
     * 验证回文串主方法
     * 
     * @param s 输入字符串
     * @return 是否为回文串
     */
    public static boolean isPalindrome(String s) {
        // 边界情况：空字符串或null认为是回文
        if (s == null || s.length() == 0) {
            return true;
        }
        
        char[] str = s.toCharArray();
        int l = 0, r = str.length - 1;  // 双指针
        
        while (l < r) {
            if (validChar(str[l]) && validChar(str[r])) {
                // 两个都是有效字符，比较是否相等
                if (!equal(str[l], str[r])) {
                    return false;
                }
                l++;
                r--;
            } else {
                // 有无效字符，移动对应指针
                l += validChar(str[l]) ? 0 : 1;  // 左边无效则移动左指针
                r -= validChar(str[r]) ? 0 : 1;  // 右边无效则移动右指针
            }
        }
        
        return true;
    }
}
