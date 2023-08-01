package base.dp5_cmpr;

public class PavingTile {
    private static int dfs(int[] op, int col, int level, int n) {
        if (col == op.length) {
            return process(op, level + 1, n);
        }
        int ans = 0;
        ans += dfs(op, col + 1, level, n);
        if (col + 1 < op.length && op[col] == 0 && op[col + 1] == 0) {
            op[col] = 1;
            op[col + 1] = 1;
            ans += dfs(op, col + 2, level, n);
            op[col] = 0;
            op[col + 1] = 0;
        }
        return ans;
    }

    private static int[] getOp(int[] pre) {
        int[] cur = new int[pre.length];
        for (int i = 0; i < pre.length; i++) {
            cur[i] = pre[i] ^ 1;
        }
        return cur;
    }

    private static int process(int[] pre, int level, int n) {
        if (level == n) {
            for (int i = 0; i < pre.length; i++) {
                if (pre[i] == 0) {
                    return 0;
                }
            }
            return 1;
        }
        int[] op = getOp(pre);
        return dfs(op, 0, level, n);
    }

    public static int ways1(int n, int m) {
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        if (n == 1 || m == 1) {
            return 1;
        }
        int[] pre = new int[m];
        for (int i = 0; i < pre.length; i++) {
            pre[i] = 1;
        }
        return process(pre, 0, n);
    }

    //

    private static int dfs2(int op, int col, int level, int m, int n) {
        if (col == -1) {
            return process2(op, level + 1, m, n);
        }
        int ans = 0;
        ans += dfs2(op, col - 1, level, m, n);
        if ((op & (1 << col)) == 0 && col - 1 >= 0 && (op & (1 << (col - 1))) == 0) {
            ans += dfs2((op | (3 << (col - 1))), col - 2, level, m, n);
        }
        return ans;
    }

    private static int process2(int pre, int i, int m, int n) {
        if (i == m) {
            return pre == ((1 << n) - 1) ? 1 : 0;
        }
        int op = ((~pre) & ((1 << n) - 1));
        return dfs2(op, n - 1, i, m, n);
    }

    public static int ways2(int n, int m) {
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        if (n == 1 || m == 1) {
            return 1;
        }
        int max = Math.max(n, m);
        int min = Math.min(n, m);
        int pre = (1 << min) - 1;
        return process2(pre, 0, max, min);
    }

    //

    private static int dfs3(int op, int col, int level, int n, int m, int[][] dp) {
        if (col == -1) {
            return process3(op, level + 1, n, m, dp);
        }
        int ans = 0;
        ans += dfs3(op, col - 1, level, n, m, dp);
        if (col > 0 && (op & (3 << (col - 1))) == 0) {
            ans += dfs3((op + (3 << (col - 1))), col - 2, level, n, m, dp);
        }
        return ans;
    }

    private static int process3(int pre, int i, int n, int m, int[][] dp) {
        if (dp[pre][i] != -1) {
            return dp[pre][i];
        }
        int ans = 0;
        if (i == n) {
            ans = pre == ((1 << m) - 1) ? 1 : 0;
        } else {
            int op = ((~pre) & ((1 << m) - 1));
            ans = dfs3(op, m - 1, i, n, m, dp);
        }
        dp[pre][i] = ans;
        return ans;
    }

    public static int ways3(int n, int m) {
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        if (n == 1 || m == 1) {
            return 1;
        }
        int max = Math.max(n, m);
        int min = Math.min(n, m);
        int pre = (1 << min) - 1;
        int[][] dp = new int[1 << min][max + 1];
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                dp[i][j] = -1;
            }
        }
        return process3(pre, 0, max, min, dp);
    }

    //

    private static void dfs4(int way, int op, int idx, int end, int[] cur) {
        if (idx >= end) {
            cur[op] += way;
            return;
        }
        dfs4(way, op, idx + 1, end, cur);
        if (((3 << idx) & op) == 0) {
            dfs4(way, op | (3 << idx), idx + 2, end, cur);
        }
    }

    public static int ways4(int n, int m) {
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        if (n == 1 || m == 1) {
            return 1;
        }
        int big = n > m ? n : m;
        int small = big == n ? m : n;
        int sn = 1 << small;
        int limit = sn - 1;
        int[] dp = new int[sn];
        dp[limit] = 1;
        int[] cur = new int[sn];
        for (int level = 0; level < big; level++) {
            for (int status = 0; status < sn; status++) {
                if (dp[status] != 0) {
                    int op = (~status) & limit;
                    dfs4(dp[status], op, 0, small - 1, cur);
                }
            }
            for (int i = 0; i < sn; i++) {
                dp[i] = 0;
            }
            int[] tmp = dp;
            dp = cur;
            cur = tmp;
        }
        return dp[limit];
    }

    public static void main(String[] args) {
        int max = 8;
        int min = 6;
        System.out.println(ways1(max, min));
        System.out.println(ways2(max, min));
        System.out.println(ways3(max, min));
        System.out.println(ways4(max, min));
        max = 10;
        min = 10;
//        System.out.println(ways1(max, min));
//        System.out.println(ways2(max, min));
        System.out.println(ways3(max, min));
        System.out.println(ways4(max, min));
    }
}
