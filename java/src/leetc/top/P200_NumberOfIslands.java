package leetc.top;

public class P200_NumberOfIslands {
    private static void infect(char[][] m, int i, int j, int ln, int lm) {
        if (i < 0 || i >= ln || j < 0 || j >= lm || m[i][j] != '1') {
            return;
        }
        m[i][j] = '2';
        infect(m, i + 1, j, ln, lm);
        infect(m, i - 1, j, ln, lm);
        infect(m, i, j + 1, ln, lm);
        infect(m, i, j - 1, ln, lm);
    }

    public static int numIslands(char[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 0;
        }
        int ln = m.length, lm = m[0].length;
        int res = 0;
        for (int i = 0; i < ln; i++) {
            for (int j = 0; j < lm; j++) {
                if (m[i][j] == '1') {
                    res++;
                    infect(m, i, j, ln, lm);
                }
            }
        }
        return res;
    }
}
