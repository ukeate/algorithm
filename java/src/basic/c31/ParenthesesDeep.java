package basic.c31;

/**
 * 括号字符串的深度和最长有效长度问题
 * 
 * 问题描述：
 * 1. 判断括号字符串是否有效
 * 2. 计算有效括号字符串的最大嵌套深度
 * 3. 计算括号字符串中最长有效括号子串的长度
 * 
 * 解题思路：
 * 1. 有效性验证：使用计数器方法
 * 2. 深度计算：在验证过程中记录最大计数值
 * 3. 最长有效长度：使用动态规划，dp[i]表示以位置i结尾的最长有效括号长度
 * 
 * 时间复杂度：O(N)，其中N是字符串长度
 * 空间复杂度：O(N)，用于动态规划数组
 */
public class ParenthesesDeep {
    
    /**
     * 判断括号字符串是否有效
     * @param str 字符数组，只能包含'('和')'
     * @return true表示有效，false表示无效
     */
    public static boolean isValid(char[] str) {
        if (str == null || str.length == 0) {
            return false;
        }
        
        int status = 0; // 记录当前未匹配的左括号数量
        
        for (int i = 0; i < str.length; i++) {
            // 检查字符是否合法
            if (str[i] != ')' && str[i] != '(') {
                return false;
            }
            
            // 遇到右括号，先减一再检查是否为负
            if (str[i] == ')' && --status < 0) {
                return false;
            }
            
            // 遇到左括号，计数器加一
            if (str[i] == '(') {
                status++;
            }
        }
        
        // 最终必须完全匹配
        return status == 0;
    }

    /**
     * 计算有效括号字符串的最大嵌套深度
     * @param s 括号字符串
     * @return 最大嵌套深度，如果字符串无效则返回0
     */
    public static int deep(String s) {
        char[] str = s.toCharArray();
        
        // 首先检查字符串是否有效
        if (!isValid(str)) {
            return 0;
        }
        
        int count = 0; // 当前嵌套深度
        int max = 0;   // 最大嵌套深度
        
        for (int i = 0; i < str.length; i++) {
            if (str[i] == '(') {
                // 遇到左括号，深度增加，更新最大值
                max = Math.max(max, ++count);
            } else {
                // 遇到右括号，深度减少
                count--;
            }
        }
        
        return max;
    }

    /**
     * 计算括号字符串中最长有效括号子串的长度
     * 使用动态规划：dp[i]表示以位置i结尾的最长有效括号长度
     * 
     * @param s 可能包含无效括号的字符串
     * @return 最长有效括号子串的长度
     */
    public static int maxLength(String s) {
        if (s == null || s.length() < 2) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        int[] dp = new int[str.length]; // dp[i]表示以i结尾的最长有效括号长度
        int pre = 0; // 可能的匹配左括号位置
        int ans = 0; // 最终答案
        
        for (int i = 1; i < str.length; i++) {
            if (str[i] == ')') {
                // 只有右括号才可能形成有效对
                // 计算可能匹配的左括号位置
                pre = i - dp[i - 1] - 1;
                
                // 检查pre位置是否存在且为左括号
                if (pre >= 0 && str[pre] == '(') {
                    // 当前长度 = 内部长度 + 2（当前这一对） + 前面的长度
                    dp[i] = dp[i - 1] + 2 + (pre > 0 ? dp[pre - 1] : 0);
                }
            }
            
            // 更新最大长度
            ans = Math.max(ans, dp[i]);
        }
        
        return ans;
    }

    public static void main(String[] args) {
        String s = "((()))";
        System.out.println(deep(s));
        System.out.println(maxLength(s));
    }
}
