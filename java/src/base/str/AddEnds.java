package base.str;

// 添加最短后缀变回文
public class AddEnds {

    private static char[] manacherStr(String str) {
        char[] chars = str.toCharArray();
        char[] res = new char[str.length() * 2 + 1];
        int idx = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chars[idx++];
        }
        return res;
    }

    public static String addEnds(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        char[] str = manacherStr(s);
        int[] rd = new int[str.length];
        int c = -1, r = -1;
        int rdEnd = -1;
        for (int i = 0; i != str.length; i++) {
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
            if (r == str.length) {
                rdEnd = rd[i];
                break;
            }
        }
        char[] res = new char[s.length() - rdEnd + 1];
        for (int i = 0; i < res.length; i++) {
            res[res.length - 1 - i] = str[i * 2 + 1];
        }
        return String.valueOf(res);
    }

    public static void main(String[] args) {
        String str = "abcd123321";
        System.out.println(addEnds(str));
    }
}
