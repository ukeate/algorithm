package base.dp2;

/**
 * N皇后问题
 * 
 * 问题描述：
 * 在N×N的棋盘上放置N个皇后，使得它们不能相互攻击。
 * 皇后可以攻击同一行、同一列或同一对角线上的任意棋子。
 * 求有多少种不同的放置方案。
 * 
 * 经典的回溯算法问题，也是组合优化问题的代表。
 * 
 * 解法分析：
 * 1. 常规回溯：使用数组记录每行皇后的列位置，检查冲突
 * 2. 位运算优化：使用位掩码快速检查和更新冲突信息
 * 
 * 优化技巧：
 * - 由于皇后必须在不同行，可以逐行放置
 * - 使用位运算可以快速检查列冲突和对角线冲突
 * - 左对角线和右对角线的冲突可以用位移操作处理
 * 
 * 时间复杂度：O(N!)，实际运行中由于剪枝会更快
 * 空间复杂度：O(N)
 */
public class NQueens {
    /**
     * 检查在位置(i,j)放置皇后是否合法
     * 
     * @param record 记录数组，record[x] = y表示第x行的皇后放在第y列
     * @param i 当前要放置皇后的行
     * @param j 当前要放置皇后的列
     * @return 是否可以在(i,j)放置皇后
     */
    private static boolean isValid(int[] record, int i, int j) {
        // 检查之前的行中是否有皇后与当前位置冲突
        for (int k = 0; k < i; k++) {
            // 检查列冲突或对角线冲突
            // j == record[k]：同一列
            // Math.abs(record[k] - j) == Math.abs(i - k)：同一对角线
            if (j == record[k] || Math.abs(record[k] - j) == Math.abs(i - k)) {
                return false;
            }
        }
        return true;
    }

    // record[x] = y表示x行的皇后放在y列上
    // 返回i之后的策略数
    private static int process1(int i, int[] record, int n) {
        if (i == n) {
            return 1;
        }
        int res = 0;
        for (int j = 0; j < n; j++) {
            if (isValid(record, i, j)) {
                record[i] = j;
                res += process1(i + 1, record, n);
            }
        }
        return res;
    }

    public static int num1(int n) {
        if (n < 1) {
            return 0;
        }
        int[] record = new int[n];
        return process1(0, record, n);
    }


    private static int process2(int limit, int colLim, int leftDiaLim, int rightDiaLim) {
        if (colLim == limit) {
            return 1;
        }
        // 可尝试的位置为1
        int pos = limit & (~(colLim | leftDiaLim | rightDiaLim));
        int mostRightOne = 0;
        int res = 0;
        while (pos != 0) {
            mostRightOne = pos & (~pos + 1);
            pos = pos - mostRightOne;
            res += process2(limit, colLim | mostRightOne, (leftDiaLim | mostRightOne) << 1, (rightDiaLim | mostRightOne) >>> 1);
        }
        return res;
    }

    public static int num2(int n) {
        if (n < 1 || n > 32) {
            return 0;
        }
        int limit = n == 32 ? -1 : (1 << n) - 1;
        return process2(limit, 0, 0, 0);
    }

    public static void main(String[] args) {
        int n = 15;
        long start = System.currentTimeMillis();
        System.out.println(num1(n));
        long end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + "ms");
        start = System.currentTimeMillis();
        System.out.println(num2(n));
        end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + "ms");
    }
}
