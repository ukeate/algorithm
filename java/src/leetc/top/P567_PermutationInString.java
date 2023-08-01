package leetc.top;

import java.util.Arrays;

public class P567_PermutationInString {
    public static boolean checkInclusion1(String a, String s) {
        if (s == null || a == null || s.length() < a.length()) {
            return false;
        }
        char[] aim = a.toCharArray();
        Arrays.sort(aim);
        String aimSort = String.valueOf(aim);
        for (int l = 0; l < s.length(); l++) {
            for (int r = l; r < s.length(); r++) {
               char[] cur = s.substring(l, r + 1).toCharArray();
               Arrays.sort(cur);
               String curSort = String.valueOf(cur);
               if (curSort.equals(aimSort)) {
                   return true;
               }
            }
        }
        return false;
    }

    //

    private static boolean isCountEqual(char[] str, int l, char[] aim) {
        int[] count = new int[256];
        for (int i = 0; i < aim.length; i++) {
            count[aim[i]]++;
        }
        for (int i = 0; i < aim.length; i++) {
            if (count[str[l + i]]-- == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkInclusion2(String a, String s) {
        if (s == null || a == null || s.length() < a.length()) {
            return false;
        }
        char[] str = s.toCharArray();
        char[] aim = a.toCharArray();
        for (int l = 0; l <= str.length - aim.length; l++) {
            if (isCountEqual(str, l, aim)) {
                return true;
            }
        }
        return false;
    }

    //

    public static boolean checkInclusion3(String a, String s) {
        if (s == null || a == null || s.length() < a.length()) {
            return false;
        }
        char[] str = a.toCharArray();
        int m = str.length;
        int[] count = new int[256];
        for (int i = 0; i < m; i++) {
            count[str[i]]++;
        }
        int all = m;
        char[] chs = s.toCharArray();
        int r = 0;
        for (; r < m; r++) {
            if (count[chs[r]]-- > 0) {
                all--;
            }
        }
        for (; r < chs.length; r++) {
            if (all == 0) {
                return true;
            }
            if (count[chs[r]]-- > 0) {
                all--;
            }
            if (count[chs[r - m]]++ >= 0) {
                all++;
            }
        }
        return all == 0 ? true : false;
    }
}

