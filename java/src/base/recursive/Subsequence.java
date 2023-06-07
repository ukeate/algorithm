package base.recursive;

import java.util.ArrayList;
import java.util.List;

public class Subsequence {
    private static void process1(char[] str, int idx, List<String> ans, String path) {
        if (idx == str.length) {
            ans.add(path);
            return;
        }
        process1(str, idx + 1, ans, path);
        process1(str, idx + 1, ans, path + str[idx]);
    }
    public static List<String> subs(String s) {
        char[] str = s.toCharArray();
        String path = "";
        List<String> ans = new ArrayList<>();
        process1(str, 0, ans, path);
        return ans;
    }

    //

    public static void main(String[] args) {
        List<String> ans = subs("acccc");
        for (String str : ans) {
            System.out.println(str);
        }
    }
}
