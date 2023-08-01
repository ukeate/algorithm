package leetc.top;

public class P76_MinimumWindowSubstring {
    public static String minWindow(String str, String target) {
        char[] s = str.toCharArray();
        char[] t = target.toCharArray();
        int[] map = new int[256];
        for (char c : t) {
            map[c]++;
        }
        int l = 0, r = 0;
        int need = t.length;
        int minLen = -1;
        int ansl = -1, ansr = -1;
        while (r < s.length) {
            map[s[r]]--;
            if (map[s[r]] >= 0) {
                need--;
            }
            if (need == 0) {
                while (map[s[l]] < 0) {
                    map[s[l++]]++;
                }
                if (minLen == -1 || minLen > r - l + 1) {
                    minLen = r - l + 1;
                    ansl = l;
                    ansr = r;
                }
                need++;
                map[s[l++]]++;
            }
            r++;
        }
        return minLen == -1 ? "" : str.substring(ansl, ansr + 1);
    }
}
