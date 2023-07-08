package basic.c36;

// 输入包含"01&|^"的字符串，与目标值true或false, 返回不改字符串顺序结合得到目标的方法数
public class Expression {
    private static boolean isValid(char[] exp) {
        if ((exp.length & 1) == 0) {
            return false;
        }
        for (int i = 0; i < exp.length; i = i + 2) {
            if ((exp[i] != '1') && (exp[i] != '0')) {
                return false;
            }
        }
        for (int i = 1; i < exp.length; i = i + 2) {
            if ((exp[i] != '&') && (exp[i] != '|') && (exp[i] != '^')) {
                return false;
            }
        }
        return true;
    }

    private static int f1(char[] str, boolean desired, int l, int r) {
        if (l == r) {
            if (str[l] == '1') {
                return desired ? 1 : 0;
            } else {
                return desired ? 0 : 1;
            }
        }
        int res = 0;
        if (desired) {
            for (int i = l + 1; i < r; i += 2) {
                switch (str[i]) {
                    case '&':
                        res += f1(str, true, l, i - 1) * f1(str, true, i + 1, r);
                        break;
                    case '|':
                        res += f1(str, true, l, i - 1) * f1(str, false, i + 1, r);
                        res += f1(str, false, l, i - 1) * f1(str, true, i + 1, r);
                        res += f1(str, true, l, i - 1) * f1(str, true, i + 1, r);
                        break;
                    case '^':
                        res += f1(str, true, l, i - 1) * f1(str, false, i + 1, r);
                        res += f1(str, false, l, i - 1) * f1(str, true, i + 1, r);
                        break;
                }
            }
        } else {
            for (int i = l + 1; i < r; i += 2) {
                switch (str[i]) {
                    case '&':
                        res += f1(str, false, l, i - 1) * f1(str, true, i + 1, r);
                        res += f1(str, true, l, i - 1) * f1(str, false, i + 1, r);
                        res += f1(str, false, l, i - 1) * f1(str, false, i + 1, r);
                        break;
                    case '|':
                        res += f1(str, false, l, i - 1) * f1(str, false, i + 1, r);
                        break;
                    case '^':
                        res += f1(str, true, l, i - 1) * f1(str, true, i + 1, r);
                        res += f1(str, false, l, i - 1) * f1(str, false, i + 1, r);
                        break;
                }
            }
        }
        return res;
    }

    public static int ways1(String express, boolean desired) {
        if (express == null || express.equals("")) {
            return 0;
        }
        char[] exp = express.toCharArray();
        if (!isValid(exp)) {
            return 0;
        }
        return f1(exp, desired, 0, exp.length - 1);
    }

    //

    public static int dp1(String s, boolean d) {
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] tMap = new int[n][n];
        int[][] fMap = new int[n][n];
        for (int i = 0; i < n; i += 2) {
            tMap[i][i] = str[i] == '1' ? 1 : 0;
            fMap[i][i] = str[i] == '0' ? 1 : 0;
        }
        for (int row = n - 3; row >= 0; row = row - 2) {
            for (int col = row + 2; col < n; col = col + 2) {
                for (int i = row + 1; i < col; i += 2) {
                    switch (str[i]) {
                        case '&':
                            tMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                            break;
                        case '|':
                            tMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                            tMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                            tMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                            break;
                        case '^':
                            tMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                            tMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                            break;
                    }
                    switch (str[i]) {
                        case '&':
                            fMap[row][col] += fMap[row][i - 1] * tMap[i + 1][col];
                            fMap[row][col] += tMap[row][i - 1] * fMap[i + 1][col];
                            fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                            break;
                        case '|':
                            fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                            break;
                        case '^':
                            fMap[row][col] += tMap[row][i - 1] * tMap[i + 1][col];
                            fMap[row][col] += fMap[row][i - 1] * fMap[i + 1][col];
                            break;
                    }
                }
            }
        }
        return d ? tMap[0][n - 1] : fMap[0][n - 1];
    }

    //

    public static int dp2(String express, boolean desired) {
        if (express == null || express.equals("")) {
            return 0;
        }
        char[] exp = express.toCharArray();
        if (!isValid(exp)) {
            return 0;
        }
        int[][] t = new int[exp.length][exp.length];
        int[][] f = new int[exp.length][exp.length];
        t[0][0] = exp[0] == '0' ? 0 : 1;
        f[0][0] = exp[0] == '1' ? 0 : 1;
        for (int i = 2; i < exp.length; i += 2) {
            t[i][i] = exp[i] == '0' ? 0 : 1;
            f[i][i] = exp[i] == '1' ? 0 : 1;
            for (int j = i - 2; j >= 0; j -= 2) {
                for (int k = j; k < i; k += 2) {
                    if (exp[k + 1] == '&') {
                        t[j][i] += t[j][k] * t[k + 2][i];
                        f[j][i] += (f[j][k] + t[j][k]) * f[k + 2][i] + f[j][k] * t[k + 2][i];
                    } else if (exp[k + 1] == '|') {
                        t[j][i] += (f[j][k] + t[j][k]) * t[k + 2][i] + t[j][k] * f[k + 2][i];
                        f[j][i] += f[j][k] * f[k + 2][i];
                    } else {
                        t[j][i] += f[j][k] * t[k + 2][i] + t[j][k] * f[k + 2][i];
                        f[j][i] += f[j][k] * f[k + 2][i] + t[j][k] * t[k + 2][i];
                    }
                }
            }
        }
        return desired ? t[0][t.length - 1] : f[0][f.length - 1];
    }

    public static void main(String[] args) {
        String express = "1^0&0|1&1^0&0^1|0|1&1";
        boolean desired = true;
        System.out.println(ways1(express, desired));
        System.out.println(dp1(express, desired));
        System.out.println(dp2(express, desired));
    }
}
