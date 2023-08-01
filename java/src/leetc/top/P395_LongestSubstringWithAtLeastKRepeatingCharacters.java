package leetc.top;

public class P395_LongestSubstringWithAtLeastKRepeatingCharacters {
    public static int longestSubstring1(String s, int k) {
        char[] str = s.toCharArray();
        int n = str.length;
        int max = 0;
        for (int i = 0; i < n; i++) {
            int[] count = new int[256];
            int collect = 0;
            int satisfy = 0;
            for (int j = i; j < n; j++) {
                count[str[j]]++;
                if (count[str[j]] == 1) {
                    collect++;
                }
                if (count[str[j]] == k) {
                    satisfy++;
                }
                if (collect == satisfy) {
                    max = Math.max(max, j - i + 1);
                }
            }
        }
        return max;
    }

    //

    public static int longestSubstring2(String s, int k) {
        char[] str = s.toCharArray();
        int n = str.length;
        int max = 0;
        for (int require = 1; require <= 26; require++) {
            int[] count = new int[26];
            int collect = 0;
            int satisfy = 0;
            int r = -1;
            for (int l = 0; l < n; l++) {
                while (r + 1 < n && !(collect == require && count[str[r + 1] - 'a'] == 0)) {
                    r++;
                    count[str[r] - 'a']++;
                    if (count[str[r] - 'a'] == 1) {
                        collect++;
                    }
                    if (count[str[r] - 'a'] == k) {
                        satisfy++;
                    }
                }
                if (collect == satisfy) {
                    max = Math.max(max, r - l + 1);
                }
                if (count[str[l] - 'a'] == 1) {
                    collect--;
                }
                if (count[str[l] - 'a'] == k) {
                    satisfy--;
                }
                count[str[l] - 'a']--;
            }
        }
        return max;
    }

    //

    private static int process(char[] str, int l, int r, int k) {
        if (l > r) {
            return 0;
        }
        int[] counts = new int[26];
        for (int i = l; i <= r; i++) {
            counts[str[i] - 'a']++;
        }
        char few = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < 26; i++) {
            if (counts[i] != 0 && min > counts[i]) {
                few = (char) (i + 'a');
                min = counts[i];
            }
        }
        if (min >= k) {
            return r - l + 1;
        }
        int pre = 0;
        int max = Integer.MIN_VALUE;
        for (int i = l; i <= r; i++) {
            if (str[i] == few) {
                max = Math.max(max, process(str, pre, i - 1, k));
                pre = i + 1;
            }
        }
        if (pre != r + 1) {
            max = Math.max(max, process(str, pre, r, k));
        }
        return max;
    }

    public static int longestSubstring3(String s, int k) {
        return process(s.toCharArray(), 0, s.length() - 1, k);
    }
}
