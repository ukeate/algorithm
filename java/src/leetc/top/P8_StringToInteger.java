package leetc.top;

public class P8_StringToInteger {
    private static String removeHeadZero(String str) {
        boolean r = (str.startsWith("+") || str.startsWith("-"));
        int s = r ? 1 : 0;
        for (; s < str.length(); s++) {
            if (str.charAt(s) != '0') {
                break;
            }
        }
        // 截止到末尾字符串
        int e = -1;
        for (int i = str.length() - 1; i >= (r ? 1 : 0); i--) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                e = i;
            }
        }
        return (r ? String.valueOf(str.charAt(0)) : "") + str.substring(s, e == -1 ? str.length() : e);
    }

    private static boolean isValid(char[] chas) {
        if (chas[0] != '-' && chas[0] != '+' && (chas[0] < '0' || chas[0] > '9')) {
            return false;
        }
        if ((chas[0] == '-' || chas[0] == '+') && chas.length == 1) {
            return false;
        }
        for (int i = 1; i < chas.length; i++) {
            if (chas[i] < '0' || chas[i] > '9') {
                return false;
            }
        }
        return true;
    }

    public int myAtoi(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        s = removeHeadZero(s.trim());
        if (s == null || s.equals("")) {
            return 0;
        }
        char[] str = s.toCharArray();
        if (!isValid(str)) {
            return 0;
        }
        boolean neg = str[0] == '-' ? false : true;
        int minq = Integer.MIN_VALUE / 10;
        int minr = Integer.MIN_VALUE % 10;
        int res = 0, cur = 0;
        for (int i = (str[0] == '-' || str[0] == '+') ? 1 : 0; i < str.length; i++) {
            cur = '0' - str[i];
            if ((res < minq) || (res == minq && cur < minr)) {
                return neg ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            res = res * 10 + cur;
        }
        if (neg && res == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        return neg ? -res : res;
    }
}
