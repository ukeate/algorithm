package basic.c37;

// 无重复字符最长子串的长度
public class LongestNoRepeatSubstring {
    public static int max(String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        char[] s = str.toCharArray();
        int[] map = new int[256];
        // 字符最后出现的位置
        for (int i = 0; i < 256; i++) {
            map[i] = -1;
        }
        int len = 0;
        int pre = -1;
        int cur = 0;
        for (int i = 0; i < s.length; i++) {
            pre = Math.max(pre, map[s[i]]);
            cur = i - pre;
            len = Math.max(len, cur);
            map[s[i]] = i;
        }
        return len;
    }

    //

    private static String randomStr(int len) {
        char[] str = new char[len];
        int base = 'a';
        int range = 'z' - 'a' + 1;
        for (int i = 0; i < len; i++) {
            str[i] = (char)((int) (range * Math.random()) + base);
        }
        return String.valueOf(str);
    }

    public static String maxStr(String str) {
        if (str == null || str.equals("")) {
            return str;
        }
        char[] s = str.toCharArray();
        int[] map = new int[256];
        for (int i = 0; i < 256; i++) {
            map[i] = -1;
        }
        int len = -1;
        int pre = -1;
        int cur = 0;
        int end = -1;
        for (int i = 0; i < s.length; i++) {
            pre = Math.max(pre, map[s[i]]);
            cur = i - pre;
            if (cur > len) {
                len = cur;
                end = i;
            }
            map[s[i]] = i;
        }
        return str.substring(end - len + 1, end + 1);
    }

    public static void main(String[] args) {
        String str = randomStr(20);
        System.out.println(str);
        System.out.println(max(str));
        System.out.println(maxStr(str));
    }
}
