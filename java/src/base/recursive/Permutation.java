package base.recursive;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
    private static void f1(ArrayList<Character> rest, String path, List<String> ans) {
        if (rest.isEmpty()) {
            ans.add(path);
        } else {
            for (int i = 0; i < rest.size(); i++) {
                char c = rest.get(i);
                rest.remove(i);
                f1(rest, path + c, ans);
                rest.add(i, c);
            }
        }
    }

    public static List<String> permutation1(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        ArrayList<Character> rest = new ArrayList<>();
        for (char cha : str) {
            rest.add(cha);
        }
        String path = "";
        f1(rest, path, ans);
        return ans;
    }

    //

    private static void swap(char[] chs, int i, int j) {
        char tmp = chs[i];
        chs[i] = chs[j];
        chs[j] = tmp;
    }

    private static void f2(char[] str, int idx, List<String> ans) {
        if (idx == str.length) {
            ans.add(String.valueOf(str));
        } else {
            for (int i = idx; i < str.length; i++) {
                swap(str, idx, i);
                f2(str, idx + 1, ans);
                swap(str, idx, i);
            }
        }
    }

    public static List<String> permutation2(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        f2(str, 0, ans);
        return ans;
    }

    //

    private static void f3(char[] str, int idx, List<String> ans) {
        if (idx == str.length) {
            ans.add(String.valueOf(str));
        } else {
            boolean[] visited = new boolean[256];
            for (int i = idx; i < str.length; i++) {
                if (!visited[str[i]]) {
                    visited[str[i]] = true;
                    swap(str, idx, i);
                    f3(str, idx + 1, ans);
                    swap(str, idx, i);
                }
            }
        }
    }
    public static List<String> permutation3(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return ans;
        }
        char[] str = s.toCharArray();
        f3(str, 0, ans);
        return ans;
    }

    public static void main(String[] args) {
        String s = "acc";
        List<String> ans1 = permutation1(s);
        for (String str : ans1) {
            System.out.println(str);
        }
        System.out.println("=======");
        List<String> ans2 = permutation2(s);
        for (String str : ans2) {
            System.out.println(str);
        }
        System.out.println("=======");
        List<String> ans3 = permutation3(s);
        for (String str : ans3) {
            System.out.println(str);
        }
    }
}
