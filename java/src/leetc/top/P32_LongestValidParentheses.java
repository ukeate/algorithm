package leetc.top;

public class P32_LongestValidParentheses {
    public static int longestValidParentheses(String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        char[] s = str.toCharArray();
        // 最长匹配长度
        int[] dp = new int[s.length];
        int pre = 0;
        int max = 0;
        for (int i = 1; i < s.length; i++) {
            if (s[i] == ')') {
                pre = i - dp[i - 1] - 1;
                if (pre >= 0 && s[pre] == '(') {
                    dp[i] = dp[i - 1] + 2 + (pre > 0 ? dp[pre - 1] : 0);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(longestValidParentheses("(()"));
    }
}
