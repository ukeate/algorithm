package leetc.top;

public class P340_LongestSubstringWithAtMostKDistinctCharacters {
    public static int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (s == null || s.length() == 0 || k < 1) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[] count = new int[256];
        int diff = 0, r = 0, ans = 0;
        for (int i = 0; i < n; i++) {
            while (r < n && (diff < k || (diff == k && count[str[r]] > 0))) {
                diff += count[str[r]] == 0 ? 1 : 0;
                count[str[r++]]++;
            }
            ans = Math.max(ans, r - i);
            diff -= count[str[i]] == 1 ? 1 : 0;
            count[str[i]]--;
        }
        return ans;
    }
}
