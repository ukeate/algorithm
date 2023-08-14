package giant.c35;

import java.util.ArrayList;
import java.util.List;

// 给定一个长度len，表示一共有几位
// 所有字符都是小写(a~z)，可以生成长度为1，长度为2，
// 长度为3...长度为len的所有字符串
// 如果把所有字符串根据字典序排序，每个字符串都有所在的位置。
// 给定一个字符串str，给定len，请返回str是总序列中的第几个
// 比如len = 4，字典序的前几个字符串为:
// a aa aaa aaaa aaab ... aaaz ... azzz b ba baa baaa ... bzzz c ...
// a是这个序列中的第1个，bzzz是这个序列中的第36558个
public class StringKth {
    private static int f(int len) {
        int ans = 1;
        for (int i = 1, base = 26; i <= len; i++, base *= 26) {
            ans += base;
        }
        return ans;
    }

    public static int kth(String s, int len) {
        if (s == null || s.length() == 0 || s.length() > len) {
            return -1;
        }
        char[] num = s.toCharArray();
        int ans = 0;
        for (int i = 0, rest = len - 1; i < num.length; i++, rest--) {
            ans += (num[i] - 'a') * f(rest) + 1;
        }
        return ans;
    }

    //

    private static void process(char[] path, int idx, List<String> ans) {
        if (idx == path.length) {
            ans.add(String.valueOf(path));
        } else {
            for (char c = 'a'; c <= 'z'; c++) {
                path[idx] = c;
                process(path, idx + 1, ans);
            }
        }
    }

    public static List<String> all(int len) {
        List<String> ans = new ArrayList<>();
        for (int i = 1; i <= len; i++) {
            char[] path = new char[i];
            process(path, 0, ans);
        }
        return ans;
    }

    public static void main(String[] args) {
        int len = 4;
        List<String> ans = all(len);
        ans.sort((a, b) -> a.compareTo(b));
        String test = "bzzz";
        int idx = kth(test, len) - 1;
        System.out.println(ans.get(idx));
    }
}
