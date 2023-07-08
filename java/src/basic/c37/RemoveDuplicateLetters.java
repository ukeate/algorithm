package basic.c37;

// 删除重复字符，并使结果字典序最小
public class RemoveDuplicateLetters {
    public static String remove1(String str) {
        if (str == null || str.length() < 2) {
            return str;
        }
        int[] map = new int[256];
        for (int i = 0; i < str.length(); i++) {
            map[str.charAt(i)]++;
        }
        int minIdx = 0;
        for (int i = 0; i < str.length(); i++) {
            minIdx = str.charAt(minIdx) > str.charAt(i) ? i : minIdx;
            if (--map[str.charAt(i)] == 0) {
                break;
            }
        }
        return str.charAt(minIdx) + remove1(str.substring(minIdx + 1).replaceAll(String.valueOf(str.charAt(minIdx)), ""));
    }

    //

    public static String remove2(String s) {
        char[] str = s.toCharArray();
        int[] map = new int[26];
        for (int i = 0; i < str.length; i++) {
            map[str[i] - 'a']++;
        }
        char[] res = new char[26];
        int idx = 0;
        int l = 0;
        int r = 0;
        while (r < str.length) {
            if (map[str[r] - 'a'] == -1 || --map[str[r] - 'a'] > 0) {
                r++;
            } else {
                // r字符最后一次出现
                int pick = -1;
                // 之前最小字符
                for (int i = l; i <= r; i++) {
                    if (map[str[i] - 'a'] != -1 && (pick == -1 || str[i] < str[pick])) {
                        pick = i;
                    }
                }
                res[idx++] = str[pick];
                // 加回字符计数
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
