package leetc.top;

public class P14_LongestCommonPrefix {
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        char[] chs = strs[0].toCharArray();
        int min = chs.length;
        for (String str : strs) {
            char[] tmp = str.toCharArray();
            int idx = 0;
            while (idx < tmp.length && idx < min) {
                if (chs[idx] != tmp[idx]) {
                    break;
                }
                idx++;
            }
            min = Math.min(idx, min);
            if (min == 0) {
                return "";
            }
        }
        return strs[0].substring(0, min);
    }
}
