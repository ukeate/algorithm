package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P22_GenerateParentheses {
    private static void process(char[] str, int idx, int leftNum, int leftRest, List<String> ans) {
        if (idx == str.length) {
            ans.add(String.valueOf(str));
            return;
        }
        if (leftRest > 0) {
            str[idx] = '(';
            process(str, idx + 1, leftNum + 1, leftRest - 1, ans);
        }
        if (leftNum > 0) {
            str[idx] = ')';
            process(str, idx + 1, leftNum - 1, leftRest, ans);
        }
    }

    public static List<String> generateParenthesis(int n) {
        char[] str = new char[n << 1];
        List<String> ans = new ArrayList<>();
        process(str, 0, 0, n, ans);
        return ans;
    }
}
