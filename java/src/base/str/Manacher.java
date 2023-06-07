package base.str;

public class Manacher {
    private static char[] manacherStr(String str) {
        char[] chars = str.toCharArray();
        char[] res = new char[str.length() * 2 + 1];
        int idx = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chars[idx++];
        }
        return res;
    }

    public static int manacher(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = manacherStr(s);
        int[] rd = new int[str.length];
        int c = -1, r = -1;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < str.length; i++) {
            rd[i] = i < r ? Math.min(rd[2 * c - i], r - i) : 1;
            while (i - rd[i] > -1 && i + rd[i] < str.length) {
                if (str[i - rd[i]] == str[i + rd[i]]) {
                    rd[i]++;
                } else {
                    break;
                }
            }
            if (i + rd[i] > r) {
                r = i + rd[i];
                c = i;
            }
            max = Math.max(max, rd[i]);
        }
        return max - 1;
    }

    //

    public static int maxSure(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = manacherStr(s);
        int max = 0;
        for (int i = 0; i < str.length; i++) {
            int l = i - 1;
            int r = i + 1;
            while (l >= 0 && r < str.length && str[l] == str[r]) {
                l--;
                r++;
            }
            max = Math.max(max, r - l - 1);
        }
        return max / 2;
    }

    //

    private static String randomStr(int maxKind, int maxLen) {
        char[] ans = new char[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (char) ((maxKind + 1) * Math.random() + 'a');
        }
        return String.valueOf(ans);
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxKind = 5;
        int maxLen = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            String str = randomStr(maxKind, maxLen);
            if (manacher(str) != maxSure(str)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
