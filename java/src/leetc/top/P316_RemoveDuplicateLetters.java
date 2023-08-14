package leetc.top;

public class P316_RemoveDuplicateLetters {
    public static String removeDuplicateLetters1(String str) {
        if (str == null || str.length() < 2) {
            return str;
        }
        int[] map = new int[256];
        for (int i = 0; i < str.length(); i++) {
            map[str.charAt(i)]++;
        }
        int minACSIdx = 0;
        for (int i = 0; i < str.length(); i++) {
            minACSIdx = str.charAt(minACSIdx) > str.charAt(i) ? i : minACSIdx;
            if (--map[str.charAt(i)] == 0) {
                break;
            }
        }
        return str.charAt(minACSIdx) + removeDuplicateLetters1(str.substring(minACSIdx + 1).replaceAll(String.valueOf(str.charAt(minACSIdx)), ""));
    }

    //

    public static String removeDuplicateLetters2(String s) {
        char[] str = s.toCharArray();
        int[] map = new int[26];
        for (int i = 0; i < str.length; i++) {
            map[str[i] - 'a']++;
        }
        char[] res = new char[26];
        int idx = 0, l = 0, r = 0;
        while (r < str.length) {
            if (map[str[r] - 'a'] == -1 || --map[str[r] - 'a'] > 0) {
                r++;
            } else {
                int pick = -1;
                for (int i = l; i <= r; i++) {
                    if (map[str[i] - 'a'] != -1 && (pick == -1 || str[i] < str[pick])) {
                        pick = i;
                    }
                }
                res[idx++] = str[pick];
                map[str[pick] - 'a'] = -1;
                for (int i = pick + 1; i <= r; i++) {
                    if (map[str[i] - 'a'] != -1) {
                        map[str[i] - 'a']++;
                    }
                }
                l = pick + 1;
                r = l;
            }
        }
        return String.valueOf(res, 0, idx);
    }
}