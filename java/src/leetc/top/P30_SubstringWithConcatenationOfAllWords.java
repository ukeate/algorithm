package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class P30_SubstringWithConcatenationOfAllWords {
    private static void find(String s, int start, int wordLen, int wordNum, HashMap<String, Integer> map, List<Integer> ans) {
        HashMap<String, Integer> window = new HashMap<>();
        int debt = wordNum;
        // 首个全词窗口
        for (int part = 0; part < wordNum; part++) {
            int l = start + part * wordLen;
            int r = l + wordLen;
            if (r > s.length()) {
                break;
            }
            String cur = s.substring(l, r);
            if (!window.containsKey(cur)) {
                window.put(cur, 1);
            } else {
                window.put(cur, window.get(cur) + 1);
            }
            if (map.containsKey(cur) && window.get(cur) <= map.get(cur)) {
                debt--;
            }
        }
        if (debt == 0) {
            ans.add(start);
        }
        // 窗口滑到结尾
        int n = s.length();
        int allLen = wordLen * wordNum;
        for (int next = start + wordLen; next + allLen <= n; next += wordLen) {
            String out = s.substring(next - wordLen, next);
            String in = s.substring(next + allLen - wordLen, next + allLen);
            window.put(out, window.get(out) - 1);
            if (map.containsKey(out) && window.get(out) < map.get(out)) {
                debt++;
            }
            if (!window.containsKey(in)) {
                window.put(in, 1);
            } else {
                window.put(in, window.get(in) + 1);
            }
            if (map.containsKey(in) && window.get(in) <= map.get(in)) {
                debt--;
            }
            if (debt == 0) {
                ans.add(next);
            }
        }
    }

    public static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> ans = new ArrayList<>();
        if (s == null || s.length() == 0 || words == null || words.length == 0) {
            return ans;
        }
        int wordLen = words[0].length();
        int wordNum = words.length;
        if (s.length() < wordLen * wordNum) {
            return ans;
        }
        HashMap<String, Integer> map = new HashMap<>();
        for (String key : words) {
            if (!map.containsKey(key)) {
                map.put(key, 1);
            } else {
                map.put(key, map.get(key) + 1);
            }
        }
        // 枚举窗口开头位置
        for (int start = 0; start < wordLen; start++) {
            find(s, start, wordLen, wordNum, map, ans);
        }
        return ans;
    }
}
