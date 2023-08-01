package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P17_LetterCombinationsOfAPhoneNumber {
    public static char[][] phone = {
            {'a', 'b', 'c'},
            {'d', 'e', 'f'},
            {'g', 'h', 'i'},
            {'j', 'k', 'l'},
            {'m', 'n', 'o'},
            {'p', 'q', 'r', 's'},
            {'t', 'u', 'v'},
            {'w', 'x', 'y', 'z'}};

    private static void process(char[] str, int idx, char[] path, List<String> ans) {
        if (idx == str.length) {
            ans.add(String.valueOf(path));
            return;
        }
        char[] chooses = phone[str[idx] - '2'];
        for (char cur : chooses) {
            path[idx] = cur;
            process(str, idx + 1, path, ans);
        }
    }

    public static List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        if (digits == null || digits.length() == 0) {
            return ans;
        }
        char[] str = digits.toCharArray();
        char[] path = new char[str.length];
        process(str, 0, path, ans);
        return ans;
    }
}
