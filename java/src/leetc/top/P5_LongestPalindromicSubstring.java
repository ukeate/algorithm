package leetc.top;

public class P5_LongestPalindromicSubstring {
    private static char[] manacherString(String s) {
        char[] chas = s.toCharArray();
        char[] res = new char[s.length() * 2 + 1];
        int idx = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chas[idx++];
        }
        return res;
    }

    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        int p = -1, pr = -1;
        char[] ss = manacherString(s);
        int[] ra = new int[ss.length];
        int max = Integer.MIN_VALUE, maxIdx = 0;
        for (int i = 0; i < ss.length; i++) {
            ra[i] = pr > i ? Math.min(ra[2 * p - i], pr - i) : 1;
            while (i + ra[i] < ss.length && i - ra[i] > -1) {
                if (ss[i + ra[i]] == ss[i - ra[i]]) {
                    ra[i]++;
                } else break;
            }
            if (i + ra[i] > pr) {
                pr = i + ra[i];
                p = i;
            }
            if (max < ra[i]) {
                max = ra[i];
                maxIdx = i;
            }
        }
        maxIdx = (maxIdx - 1) / 2;
        int maxR = (max - 1) / 2;
        return s.substring((max & 1) == 0 ? maxIdx - maxR : maxIdx - maxR + 1, maxIdx + maxR + 1);
    }
}
