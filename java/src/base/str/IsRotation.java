package base.str;

public class IsRotation {
    private static int[] nextArr(char[] s) {
        if (s.length == 1) {
            return new int[]{-1};
        }
        int[] next = new int[s.length];
        next[0] = -1;
        next[1] = 0;
        int i = 2;
        int cn = 0;
        while (i < next.length) {
            if (s[i - 1] == s[cn]) {
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        return next;
    }

    private static int match(String s, String m) {
        if (s.length() < m.length()) {
            return -1;
        }
        char[] ss = s.toCharArray();
        char[] mm = m.toCharArray();
        int[] next = nextArr(mm);
        int x = 0, y = 0;
        while (x < ss.length && y < mm.length) {
            if (ss[x] == mm[y]) {
                x++;
                y++;
            } else if (next[y] == -1) {
                x++;
            } else {
                y = next[y];
            }
        }
       return y == mm.length ? x - y : -1;
    }
    public static boolean isRotation(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        String b2 = b + b;
        return match(b2, a) != -1;
    }

    public static void main(String[] args) {
        String str1 = "aabbbbbb";
        String str2 = "bbbbbbaa";
        System.out.println(isRotation(str1, str2));
    }
}
