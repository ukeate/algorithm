package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P301_RemoveInvalidParentheses {
    private static void remove(String s, List<String> ans, int checkIdx, int delIdx, char[] par) {
        for (int count = 0, i = checkIdx; i < s.length(); i++) {
            if (s.charAt(i) == par[0]) {
                count++;
            }
            if (s.charAt(i) == par[1]) {
                count--;
            }
            if (count < 0) {
                for (int j = delIdx; j <= i; ++j) {
                    if (s.charAt(j) == par[1] && (j == delIdx || s.charAt(j - 1) != par[1])) {
                        remove(s.substring(0, j) + s.substring(j + 1, s.length()), ans, i, j, par);
                    }
                }
                return;
            }
        }
        // 左括号数>=右括号数
        String reversed = new StringBuilder(s).reverse().toString();
        if (par[0] == '(') {
            remove(reversed, ans, 0, 0, new char[] {')','('});
        } else {
            ans.add(reversed);
        }
    }

    public List<String> removeInvalidParentheses(String s) {
        List<String> ans = new ArrayList<>();
        remove(s, ans, 0, 0, new char[]{'(', ')'});
        return ans;
    }
}
