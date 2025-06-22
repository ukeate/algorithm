package base.dp5_cmpr;

/**
 * 铺砖问题 - 用1x2的瓷砖铺满n*m的矩形
 * 经典的状态压缩DP问题，使用位运算表示每一行的铺砖状态
 * 关键思想：逐行铺砖，用状态压缩表示当前行哪些位置已被上一行的竖砖占据
 */
public class PavingTile {
    /**
     * 深度优先搜索 - 尝试在当前行填充瓷砖
     * @param op 当前行状态数组，1表示已被占据，0表示空闲
     * @param col 当前考虑的列位置
     * @param level 当前行数
     * @param n 总行数
     * @return 铺砖的方案数
     */
    private static int dfs(int[] op, int col, int level, int n) {
        // 当前行处理完毕，进入下一行
        if (col == op.length) {
            return process(op, level + 1, n);
        }
        int ans = 0;
        // 方案1：当前位置不放砖（必须被上一行的竖砖占据）
        ans += dfs(op, col + 1, level, n);
        // 方案2：在当前位置和下一位置放横砖
        if (col + 1 < op.length && op[col] == 0 && op[col + 1] == 0) {
            op[col] = 1;
            op[col + 1] = 1;
            ans += dfs(op, col + 2, level, n);
            op[col] = 0; // 恢复状态
            op[col + 1] = 0;
        }
        return ans;
    }

    /**
     * 根据上一行状态生成当前行的初始状态
     * @param pre 上一行状态，1表示有竖砖延伸到当前行
     * @return 当前行初始状态，0表示空闲，1表示被占据
     */
    private static int[] getOp(int[] pre) {
        int[] cur = new int[pre.length];
        for (int i = 0; i < pre.length; i++) {
            cur[i] = pre[i] ^ 1; // 上一行有竖砖的位置在当前行被占据
        }
        return cur;
    }

    /**
     * 处理第level行的铺砖
     * @param pre 上一行状态
     * @param level 当前行数
     * @param n 总行数
     * @return 铺砖方案数
     */
    private static int process(int[] pre, int level, int n) {
        // 到达边界，检查最后一行是否完全填满
        if (level == n) {
            for (int i = 0; i < pre.length; i++) {
                if (pre[i] == 0) {
                    return 0; // 有空位，方案无效
                }
            }
            return 1; // 完全填满，方案有效
        }
        int[] op = getOp(pre);
        return dfs(op, 0, level, n);
    }

    /**
     * 方法1：使用数组表示状态的递归解法
     */
    public static int ways1(int n, int m) {
        // 奇数个格子无法用1x2瓷砖完全填满
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        // 单行或单列的情况
        if (n == 1 || m == 1) {
            return 1;
        }
        // 初始状态：第0行之前全部填满
        int[] pre = new int[m];
        for (int i = 0; i < pre.length; i++) {
            pre[i] = 1;
        }
        return process(pre, 0, n);
    }

    //

    /**
     * 深度优先搜索 - 使用位运算优化版本
     * @param op 当前行状态（位表示）
     * @param col 当前列位置
     * @param level 当前行数
     * @param m 列数
     * @param n 行数
     * @return 铺砖方案数
     */
    private static int dfs2(int op, int col, int level, int m, int n) {
        if (col == -1) {
            return process2(op, level + 1, m, n);
        }
        int ans = 0;
        // 不放砖，跳过当前位置
        ans += dfs2(op, col - 1, level, m, n);
        // 尝试放横砖
        if ((op & (1 << col)) == 0 && col - 1 >= 0 && (op & (1 << (col - 1))) == 0) {
            ans += dfs2((op | (3 << (col - 1))), col - 2, level, m, n);
        }
        return ans;
    }

    /**
     * 处理第i行的状态转移（位运算版本）
     */
    private static int process2(int pre, int i, int m, int n) {
        if (i == m) {
            return pre == ((1 << n) - 1) ? 1 : 0;
        }
        int op = ((~pre) & ((1 << n) - 1)); // 当前行可用位置
        return dfs2(op, n - 1, i, m, n);
    }

    /**
     * 方法2：使用位运算优化的递归解法
     */
    public static int ways2(int n, int m) {
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        if (n == 1 || m == 1) {
            return 1;
        }
        int max = Math.max(n, m);
        int min = Math.min(n, m);
        int pre = (1 << min) - 1; // 初始状态：全部填满
        return process2(pre, 0, max, min);
    }

    //

    /**
     * 带记忆化的深度优先搜索
     */
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

    /**
     * 带记忆化的状态处理
     */
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

    /**
     * 方法3：带记忆化的递归解法
     */
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
        // dp[状态][行数] = 方案数
        int[][] dp = new int[1 << min][max + 1];
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                dp[i][j] = -1;
            }
        }
        return process3(pre, 0, max, min, dp);
    }

    //

    /**
     * 优化版深度优先搜索 - 直接计算状态转移
     * @param way 当前状态的方案数
     * @param op 当前状态
     * @param idx 当前位置
     * @param end 结束位置
     * @param cur 下一行状态的方案数组
     */
    private static void dfs4(int way, int op, int idx, int end, int[] cur) {
        if (idx >= end) {
            cur[op] += way; // 累加到目标状态
            return;
        }
        // 放竖砖或不放砖
        dfs4(way, op, idx + 1, end, cur);
        // 放横砖（占据两个位置）
        if (((3 << idx) & op) == 0) {
            dfs4(way, op | (3 << idx), idx + 2, end, cur);
        }
    }

    /**
     * 方法4：动态规划优化版本
     * 时间复杂度：O(big * 2^small * small)
     * 空间复杂度：O(2^small)
     */
    public static int ways4(int n, int m) {
        if (n < 1 || m < 1 || ((n * m) & 1) != 0) {
            return 0;
        }
        if (n == 1 || m == 1) {
            return 1;
        }
        int big = n > m ? n : m; // 较大维度作为行
        int small = big == n ? m : n; // 较小维度作为列
        int sn = 1 << small; // 状态总数
        int limit = sn - 1; // 全填满状态
        
        // dp[i]表示状态i的方案数
        int[] dp = new int[sn];
        dp[limit] = 1; // 初始状态：第0行之前全部填满
        int[] cur = new int[sn]; // 临时数组
        
        // 逐行处理
        for (int level = 0; level < big; level++) {
            for (int status = 0; status < sn; status++) {
                if (dp[status] != 0) {
                    int op = (~status) & limit; // 当前行可用位置
                    dfs4(dp[status], op, 0, small - 1, cur);
                }
            }
            // 清空dp数组
            for (int i = 0; i < sn; i++) {
                dp[i] = 0;
            }
            // 交换数组
            int[] tmp = dp;
            dp = cur;
            cur = tmp;
        }
        return dp[limit]; // 最终状态：全部填满
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
