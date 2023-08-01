package leetc.top;

public class P125_ValidPalindrome {
    private static boolean equal(char c1, char c2) {
        if (isNumber(c1) || isNumber(c2)) {
            return c1 == c2;
        }
        return (c1 == c2) || (Math.max(c1, c2) - Math.min(c1, c2) == 32);
    }
    private static boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private static boolean validChar(char c) {
        return isLetter(c) || isNumber(c);
    }

    public static boolean isPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        char[] str = s.toCharArray();
        int l = 0, r = str.length - 1;
        while (l < r) {
            if (validChar(str[l]) && validChar(str[r])) {
                if (!equal(str[l], str[r])) {
                    return false;
                }
                l++;
                r--;
            } else {
                l += validChar(str[l]) ? 0 : 1;
                r -= validChar(str[r]) ? 0 : 1;
            }
        }
        return true;
    }
}
