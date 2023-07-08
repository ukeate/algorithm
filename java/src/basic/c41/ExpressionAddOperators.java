package basic.c41;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// 数位间添加+ - *, 得到target的方法路径
public class ExpressionAddOperators {

    private static boolean check(String path, int target) {
        // 未实现
        return true;
    }

    private static void process1(char[] str, int idx, int target, String path, List<String> ans) {
        if (idx == str.length) {
            char last = path.charAt(path.length() - 1);
            path = (last == '+' || last == '-' || last == '*') ? path.substring(0, path.length() - 1) : path;
            if (check(path, target)) {
                ans.add(path);
            }
            return;
        }
        String p0 = String.valueOf(str[idx]);
        String p1 = p0 + "+";
        String p2 = p0 + "-";
        String p3 = p0 + "*";
        process1(str, idx + 1, target, path + p0, ans);
        process1(str, idx + 1, target, path + p1, ans);
        process1(str, idx + 1, target, path + p2, ans);
        process1(str, idx + 1, target, path + p3, ans);
    }

    public static List<String> ways1(String num, int target) {
        List<String> ans = new ArrayList<>();
        char[] str = num.toCharArray();
        process1(str, 0, target, "", ans);
        return ans;
    }

    //

    private static int process2(char[] str, int idx, int left, int cur, int target) {
        if (idx == str.length) {
            return (left + cur) == target ? 1 : 0;
        }
        int ways = 0;
        int num = str[idx] - '0';
        // +
        ways += process2(str, idx + 1, left + cur, num, target);
        // -
        ways += process2(str, idx + 1, left + cur, -num, target);
        // *
        ways += process2(str, idx + 1, left, cur * num, target);
        // 不加符号
        if (cur != 0) {
            if (cur > 0) {
                ways += process2(str, idx + 1, left, cur * 10 + num, target);
            } else {
                ways += process2(str, idx + 1, left, cur * 10 - num, target);
            }
        }
        return ways;
    }

    public static int ways2(String num, int target) {
        char[] str = num.toCharArray();
        int first = str[0] - '0';
        return process2(str, 1, 0, first, target);
    }

    //

    private static void dfs(List<String> res, char[] path, int pathIdx, long left, long cur, char[] digits, int idx, int target) {
        if (idx == digits.length) {
            if (left + cur == target) {
                res.add(new String(path, 0, pathIdx));
            }
            return;
        }
        long num = 0;
        int j = pathIdx + 1;
        for (int i = idx; i < digits.length; i++) {
            num = num * 10 + digits[i] - '0';
            path[j++] = digits[i];
            path[pathIdx] = '+';
            dfs(res, path, j, left + cur, num, digits, i + 1, target);
            path[pathIdx] = '-';
            dfs(res, path, j, left + cur, -num, digits, i + 1, target);
            path[pathIdx] = '*';
            dfs(res, path, j, left, cur * num, digits, i + 1, target);
            // 一种情况'0'拼符号，一种情况'0'结合前数
            if (digits[idx] == '0') {
                break;
            }
        }
    }

    public static List<String> ways3(String num, int target) {
        List<String> ret = new LinkedList<>();
        if (num.length() == 0) {
            return ret;
        }
        char[] path = new char[num.length() * 2 - 1];
        char[] digits = num.toCharArray();
        long cur = 0;
        for (int i = 0; i < digits.length; i++) {
            cur = cur * 10 + digits[i] - '0';
            path[i] = digits[i];
            dfs(ret, path, i + 1, 0, cur, digits, i + 1, target);
            if (cur == 0) {
                break;
            }
        }
        return ret;
    }
}
