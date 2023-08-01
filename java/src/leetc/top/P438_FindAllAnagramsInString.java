package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class P438_FindAllAnagramsInString {
    public static List<Integer> findAnagrams(String str, String pstr) {
        List<Integer> ans = new ArrayList<>();
        if (str == null || pstr == null || str.length() < pstr.length()) {
            return ans;
        }
        char[] s = str.toCharArray();
        int n = s.length;
        char[] p = pstr.toCharArray();
        int m = p.length;
        HashMap<Character, Integer> map = new HashMap<>();
        for (char cha : p) {
            if (!map.containsKey(cha)) {
                map.put(cha, 1);
            } else {
                map.put(cha, map.get(cha) + 1);
            }
        }
        int all = m;
        for (int end = 0; end < m - 1; end++) {
            if (map.containsKey(s[end])) {
                int count = map.get(s[end]);
                if (count > 0) {
                    all--;
                }
                map.put(s[end], count - 1);
            }
        }
        for (int end = m - 1, start = 0; end < n; end++, start++) {
            if (map.containsKey(s[end])) {
                int count = map.get(s[end]);
                if (count > 0) {
                    all--;
                }
                map.put(s[end], count - 1);
            }
            if (all == 0) {
                ans.add(start);
            }
            if (map.containsKey(s[start])) {
                int count = map.get(s[start]);
                if (count >= 0) {
                    all++;
                }
                map.put(s[start], count + 1);
            }
        }
        return ans;
    }
}
