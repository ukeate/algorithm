package basic.c55;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// https://leetcode.com/problems/palindrome-pairs/
// 输入word数组, 返回两个组合可成回文的成对列表
public class PalindromePairs {
    private static String reverse(String str) {
        char[] chs = str.toCharArray();
        int l = 0;
        int r = chs.length - 1;
        while (l < r) {
            char tmp = chs[l];
            chs[l++] = chs[r];
            chs[r--] = tmp;
        }
        return String.valueOf(chs);
    }

    private static void add(List<List<Integer>> res, int left, int right) {
        List<Integer> pair = new ArrayList<>();
        pair.add(left);
        pair.add(right);
        res.add(pair);
    }

    private static char[] manachers(String word) {
        char[] chs = word.toCharArray();
        char[] ans = new char[chs.length * 2 + 1];
        int idx = 0;
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (i & 1) == 0 ? '#' : chs[idx++];
        }
        return ans;
    }

    private static int[] manacher(String word) {
        char[] s = manachers(word);
        // 回文半径数组
        int[] rs = new int[s.length];
        int center = -1;
        // position right 右边界
        int pr = -1;
        for (int i = 0; i < s.length; i++) {
            // 右边界内，先默认镜像位置长度
            rs[i] = pr > i ? Math.min(rs[(center << 1) - i], pr - i) : 1;
            while (i + rs[i] < s.length && i - rs[i] > -1) {
                if (s[i + rs[i]] != s[i - rs[i]]) {
                    break;
                }
                rs[i]++;
                if (i + rs[i] > pr) {
                    pr = i + rs[i];
                    center = i;
                }
            }
        }
        return rs;
    }

    private static List<List<Integer>> findPairs(String word, int idx, HashMap<String, Integer> words) {
        List<List<Integer>> res = new ArrayList<>();
        String reverse = reverse(word);
        Integer rest = words.get("");
        if (rest != null && rest != idx && word.equals(reverse)) {
            add(res, rest, idx);
            add(res, idx, rest);
        }
        int[] rs = manacher(word);
        int mid = rs.length >> 1;
        // 回文不可能过半
        for (int i = 1; i < mid; i++) {
            if (i - rs[i] == -1) {
                // 前缀是回文，找原后缀的逆序
                rest = words.get(reverse.substring(0, mid - i));
                if (rest != null && rest != idx) {
                    add(res, rest, idx);
                }
            }
        }
        for (int i = mid + 1; i < rs.length; i++) {
            if (i + rs[i] == rs.length) {
                // 后缀是回文, 找原前缀逆序
                rest = words.get(reverse.substring((mid << 1) - i));
                if (rest != null && rest != idx) {
                    add(res, idx, rest);
                }
            }
        }
        return res;
    }

    // O(N*K^2)
    public static List<List<Integer>> pairs(String[] words) {
        HashMap<String, Integer> set = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            set.put(words[i], i);
        }
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            res.addAll(findPairs(words[i], i, set));
        }
        return res;
    }

    //

    public static void main(String[] args) {
        String[] words = {"abcd", "dcba","lls", "s", "sssll"};
        List<List<Integer>> rst = pairs(words);
    }
}
