package leetc.top;

public class P934_ShortestBridge {
    private static int infect(int[][] m, int i, int j, int ln, int lm, int[] curs, int idx, int[] record) {
        if (i < 0 || i == ln || j < 0 || j == lm || m[i][j] != 1) {
            return idx;
        }
        m[i][j] = 2;
        int p = i * ln + j;
        record[p] = 1;
        curs[idx++] = p;
        idx = infect(m, i - 1, j, ln, lm, curs, idx, record);
        idx = infect(m, i + 1, j, ln, lm, curs, idx, record);
        idx = infect(m, i, j - 1, ln, lm, curs, idx, record);
        idx = infect(m, i, j + 1, ln, lm, curs, idx, record);
        return idx;
    }

    private static int bfs(int ln, int lm, int all, int v, int[] curs, int size, int[] nexts, int[] record) {
        int nexti = 0;
        for (int i = 0; i < size; i++) {
            int up = curs[i] < lm ? -1 : curs[i] - lm;
            int down = curs[i] + lm >= all ? -1 : curs[i] + lm;
            int left = curs[i] % lm == 0 ? -1 : curs[i] - 1;
            int right = curs[i] % lm == lm - 1 ? -1 : curs[i] + 1;
            if (up != -1 && record[up] == 0) {
                record[up] = v;
                nexts[nexti++] = up;
            }
            if (down != -1 && record[down] == 0) {
                record[down] = v;
                nexts[nexti++] = down;
            }
            if (left != -1 && record[left] == 0) {
                record[left] = v;
                nexts[nexti++] = left;
            }
            if (right != -1 && record[right] == 0) {
                record[right] = v;
                nexts[nexti++] = right;
            }
        }
        return nexti;
    }

    public static int shortestBridge(int[][] m) {
        int ln = m.length, lm = m[0].length;
        int all = ln * lm;
        int island = 0;
        // queue,有值的一维坐标
        int[] curs = new int[all];
        int[] nexts = new int[all];
        // [岛id][一维坐标]:坐标值
        int[][] records = new int[2][all];
        for (int i = 0; i < ln; i++) {
            for (int j = 0; j < lm; j++) {
                if (m[i][j] == 1) {
                    int queueSize = infect(m, i, j, ln, lm, curs, 0, records[island]);
                    int v = 1;
                    while (queueSize != 0) {
                        v++;
                        queueSize = bfs(ln, lm, all, v, curs, queueSize, nexts, records[island]);
                        int[] tmp = curs;
                        curs = nexts;
                        nexts = tmp;
                    }
                    island++;
                }
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < all; i++) {
            min = Math.min(min, records[0][i] + records[1][i]);
        }
        return min - 3;
    }
}
