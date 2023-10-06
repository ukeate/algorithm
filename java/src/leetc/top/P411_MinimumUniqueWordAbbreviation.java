package leetc.top;

public class P411_MinimumUniqueWordAbbreviation {
    private static int min = Integer.MAX_VALUE;
    private static int best = 0;

    private static boolean canFix(int[] words, int fix) {
        for (int word : words) {
            if ((fix & word) == 0) {
                return false;
            }
        }
        return true;
    }

    private static int abbrLen(int fix, int len) {
        int ans = 0;
        int cnt = 0;
        for (int i = 0; i < len; i++) {
            if ((fix & (1 << i)) != 0) {
                ans++;
                if (cnt != 0) {
                    ans += 1;
                }
                cnt = 0;
            } else {
                cnt++;
            }
        }
        if (cnt != 0) {
            ans += 1;
        }
        return ans;
    }

    private static void dfs1(int[] words, int len, int fix, int idx) {
        if (!canFix(words, fix)) {
            if (idx < len) {
                dfs1(words, len, fix, idx + 1);
                dfs1(words, len, fix | (1 << idx), idx + 1);
            } else {
                int ans = abbrLen(fix, len);
                if (ans < min) {
                    min = ans;
                    best = fix;
                }
            }
        }
    }

    public static String minAbbreviation1(String target, String[] dictionary) {
        min = Integer.MAX_VALUE;
        best = 0;
        char[] t = target.toCharArray();
        int len = t.length;
        int siz = 0;
        for (String word : dictionary) {
            if (word.length() == len) {
                siz++;
            }
        }
        int[] words = new int[siz];
        int idx = 0;
        for (String word : dictionary) {
            if (word.length() == len) {
                char[] w = word.toCharArray();
                int status = 0;
                for (int j = 0; j < len; j++) {
                    if (t[j] != w[j]) {
                        status |= 1 << j;
                    }
                }
                words[idx++] = status;
            }
        }
        dfs1(words, len, 0, 0);
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if ((best & (1 << i)) != 0) {
                if (count > 0) {
                    builder.append(count);
                }
                builder.append(t[i]);
                count = 0;
            } else {
                count++;
            }
        }
        if (count > 0) {
            builder.append(count);
        }
        return builder.toString();
    }
}
