package leetc.top;

public class P248_StrobogrammaticNumberIII {
    private static boolean equalMore(char[] low, char[] cur) {
        if (low.length != cur.length) {
            return low.length < cur.length;
        }
        for (int i = 0; i < low.length; i++) {
            if (low[i] != cur[i]) {
                return low[i] < cur[i];
            }
        }
        return true;
    }

    private static int convert(char cha, boolean diff) {
        switch (cha) {
            case '0':
                return '0';
            case '1':
                return '1';
            case '6':
                return diff ? '9' : -1;
            case '8':
                return '8';
            case '9':
                return diff ? '6' : -1;
            default:
                return -1;
        }
    }

    private static boolean valid(char[] str) {
        int l = 0;
        int r = str.length - 1;
        while (l <= r) {
            if (convert(str[l++], l != r) != str[r--]) {
                return false;
            }
        }
        return true;
    }

    private static int num(int bits) {
        if (bits == 1) {
            return 3;
        }
        if (bits == 2) {
            return 5;
        }
        int pre2 = 3;
        int pre1 = 5;
        int ans = 0;
        for (int i = 3; i <= bits; i++) {
            ans = 5 * pre2;
            pre2 = pre1;
            pre1 = ans;
        }
        return ans;
    }

    // leftMore: true时大于原始，false时等。rightFlag: 0时小于原始，1时等，2时大
    private static int up(char[] low, int left, boolean leftMore, int rightFlag) {
        int n = low.length;
        int right = n - 1 - left;
        if (left > right) {
            return leftMore || (rightFlag != 0) ? 1 : 0;
        }
        if (leftMore) {
            return num(n - (left << 1));
        } else {
            int ways = 0;
            // left做决定大于原始
            for (char cha = (char) (low[left] + 1); cha <= '9'; cha++) {
                if (convert(cha, left != right) != -1) {
                    ways += up(low, left + 1, true, rightFlag);
                }
            }
            // left做决定等于原始, convert为[right]
            int convert = convert(low[left], left != right);
            if (convert != -1) {
                if (convert < low[right]) {
                    ways += up(low, left + 1, false, 0);
                } else if (convert == low[right]) {
                    ways += up(low, left + 1, false, rightFlag);
                } else {
                    ways += up(low, left + 1, false, 2);
                }
            }
            return ways;
        }
    }

    private static int all(int len, boolean init) {
        if (len == 0) {
            // init=true len=0 不会被调用
            return 1;
        }
        if (len == 1) {
            return 3;
        }
        if (init) {
            return all(len - 2, false) << 2;
        } else {
            return all(len - 2, false) * 5;
        }
    }

    // len >= 2
    private static int all(int len) {
        int ans = (len & 1) == 0 ? 1 : 3;
        for (int i = (len & 1) == 0 ? 2 : 3; i < len; i += 2) {
            ans *= 5;
        }
        return ans << 2;
    }

    // leftMore: true时大于原始，false时等。rightFlag: 0时小于原始，1时等，2时大
    private static int down(char[] high, int left, boolean leftLess, int rightFlag) {
        int n = high.length;
        int right = n - 1 - left;
        if (left > right) {
            return leftLess || (rightFlag != 2) ? 1 : 0;
        }
        if (leftLess) {
            return num(n - (left << 1));
        } else {
            int ways = 0;
            for (char cha = (n != 1 && left == 0) ? '1' : '0'; cha < high[left]; cha++) {
                if (convert(cha, left != right) != -1) {
                    ways += down(high, left + 1, true, rightFlag);
                }
            }
            int convert = convert(high[left], left != right);
            if (convert != -1) {
                if (convert < high[right]) {
                    ways += down(high, left + 1, false, 0);
                } else if (convert == high[right]) {
                    ways += down(high, left + 1, false, rightFlag);
                } else {
                    ways += down(high, left + 1, false, 2);
                }
            }
            return ways;
        }
    }

    public static int strobogrammaticInRange(String l, String h) {
        char[] low = l.toCharArray();
        char[] high = h.toCharArray();
        if (!equalMore(low, high)) {
            return 0;
        }
        int lowLen = low.length, highLen = high.length;
        if (lowLen == highLen) {
            int up1 = up(low, 0, false, 1);
            int up2 = up(high, 0, false, 1);
            return up1 - up2 + (valid(high) ? 1 : 0);
        }
        int ans = 0;
        for (int i = lowLen + 1; i < highLen; i++) {
            ans += all(i);
        }
        ans += up(low, 0, false, 1);
        ans += down(high, 0, false, 1);
        return ans;
    }
}
