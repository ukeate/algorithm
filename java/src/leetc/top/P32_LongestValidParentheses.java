package leetc.top;

/**
 * LeetCode 32. 最长有效括号 (Longest Valid Parentheses)
 * 
 * 问题描述：
 * 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。
 * 
 * 示例：
 * - 输入：s = "(()"，输出：2
 *   解释：最长有效括号子串是"()"
 * - 输入：s = ")()())"，输出：4
 *   解释：最长有效括号子串是"()()"
 * - 输入：s = ""，输出：0
 * 
 * 解法思路：
 * 动态规划：
 * 1. dp[i]表示以字符s[i]结尾的最长有效括号子串长度
 * 2. 只有当s[i] == ')'时，dp[i]才可能大于0
 * 3. 状态转移：
 *    - 找到与s[i]配对的'('的位置pre
 *    - pre = i - dp[i-1] - 1（跳过s[i-1]结尾的有效括号段）
 *    - 如果s[pre] == '('，则形成新的有效括号对
 *    - dp[i] = dp[i-1] + 2 + dp[pre-1]
 *      其中：dp[i-1]是内部的有效长度
 *            2是新配对的'('和')'
 *            dp[pre-1]是配对前面可能存在的有效括号段
 * 
 * 核心思想：
 * - 利用已计算的结果，避免重复计算
 * - 每个')'都尝试向前寻找配对的'('
 * - 配对成功时，连接多个有效括号段
 * 
 * 时间复杂度：O(n) - 遍历字符串一次
 * 空间复杂度：O(n) - dp数组的空间开销
 * 
 * LeetCode链接：https://leetcode.com/problems/longest-valid-parentheses/
 */
public class P32_LongestValidParentheses {
    
    /**
     * 计算最长有效括号子串的长度
     * 
     * 算法步骤：
     * 1. 创建dp数组，dp[i]表示以s[i]结尾的最长有效括号长度
     * 2. 遍历字符串，只处理')'字符（'('不可能作为有效括号的结尾）
     * 3. 对于每个')'，计算其可能配对的'('的位置pre
     * 4. 如果找到配对，更新dp[i]并维护全局最大值
     * 
     * 状态转移详解：
     * - pre = i - dp[i-1] - 1：跳过s[i-1]结尾的有效括号段
     * - 验证s[pre] == '('：确保能形成配对
     * - dp[i] = dp[i-1] + 2 + dp[pre-1]：
     *   * dp[i-1]：内部有效括号长度
     *   * 2：当前配对的长度
     *   * dp[pre-1]：配对前的有效括号长度
     * 
     * @param str 输入的括号字符串
     * @return 最长有效括号子串的长度
     */
    public static int longestValidParentheses(String str) {
        // 边界情况：空字符串或null
        if (str == null || str.equals("")) {
            return 0;
        }
        
        char[] s = str.toCharArray();
        
        // dp[i]：以s[i]结尾的最长有效括号子串长度
        int[] dp = new int[s.length];
        
        int pre = 0;   // 配对的'('的位置
        int max = 0;   // 全局最长有效括号长度
        
        // 从第二个字符开始遍历（第一个字符不可能形成有效括号对）
        for (int i = 1; i < s.length; i++) {
            // 只有')'才可能作为有效括号的结尾
            if (s[i] == ')') {
                // 计算可能配对的'('的位置
                // 需要跳过s[i-1]结尾的有效括号段
                pre = i - dp[i - 1] - 1;
                
                // 验证是否存在配对的'('
                if (pre >= 0 && s[pre] == '(') {
                    // 找到配对，更新dp[i]
                    // = 内部有效长度 + 当前配对长度 + 配对前的有效长度
                    dp[i] = dp[i - 1] + 2 + (pre > 0 ? dp[pre - 1] : 0);
                }
                // 如果没有找到配对的'('，dp[i]保持为0（默认值）
            }
            // 如果s[i] == '('，dp[i]保持为0（'('不能作为有效括号的结尾）
            
            // 更新全局最大值
            max = Math.max(max, dp[i]);
        }
        
        return max;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1："(()" → 2
        System.out.println("输入: \"(()\"");
        System.out.println("输出: " + longestValidParentheses("(()"));
        System.out.println("期望: 2");
        System.out.println();
        
        // 测试用例2：")()())" → 4
        System.out.println("输入: \")()())\"");
        System.out.println("输出: " + longestValidParentheses(")()())"));
        System.out.println("期望: 4");
        System.out.println();
        
        // 测试用例3："" → 0
        System.out.println("输入: \"\"");
        System.out.println("输出: " + longestValidParentheses(""));
        System.out.println("期望: 0");
        System.out.println();
        
        // 测试用例4："()(()" → 2
        System.out.println("输入: \"()(()\"");
        System.out.println("输出: " + longestValidParentheses("()(()"));
        System.out.println("期望: 2");
        System.out.println();
        
        // 测试用例5："(()())" → 6
        System.out.println("输入: \"(()())\"");
        System.out.println("输出: " + longestValidParentheses("(()())"));
        System.out.println("期望: 6");
        System.out.println();
        
        System.out.println("算法特点：");
        System.out.println("- 时间复杂度：O(n)，只需要遍历字符串一次");
        System.out.println("- 空间复杂度：O(n)，需要dp数组存储中间结果");
        System.out.println("- 核心思想：动态规划 + 括号配对");
    }
}
