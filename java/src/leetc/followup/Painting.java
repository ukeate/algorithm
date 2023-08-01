package leetc.followup;

import java.util.ArrayList;
import java.util.List;

// n*m的棋盘，各颜色的格子数必须相同，相邻颜色不同，求最少的颜色数
public class Painting {
    private static boolean process(int[][] matrix, int n, int m, int kind, int row, int col, List<Integer> rest) {
        if (row == n) {
            return true;
        }
        if (col == m) {
            return process(matrix, n, m, kind, row + 1, 0, rest);
        }
        int left = col == 0 ? 0 : matrix[row][col - 1];
        int up = row == 0 ? 0 : matrix[row - 1][col];
        for (int clr = 1; clr <= kind; clr++) {
            if (clr != left && clr != up && rest.get(clr) > 0) {
                int cnt = rest.get(clr);
                rest.set(clr, cnt - 1);
                matrix[row][col] = clr;
                if (process(matrix, n, m, kind, row, col + 1, rest)) {
                    return true;
                }
                rest.set(clr, cnt);
                matrix[row][col] = 0;
            }
        }
        return false;
    }

    private static boolean can(int[][] matrix, int n, int m, int kind) {
        int all = n * m;
        int every = all / kind;
        ArrayList<Integer> rest = new ArrayList<>();
        rest.add(0);
        for (int i = 1; i <= kind; i++) {
            rest.add(every);
        }
        return process(matrix, n, m, kind, 0, 0, rest);
    }

    public static int min(int n, int m) {
        for (int i = 2; i <= n * m; i++) {
            int[][] matrix = new int[n][m];
            if ((n * m) % i == 0 && can(matrix, n, m, i)) {
                return i;
            }
        }
        return n * m;
    }

    public static void main(String[] args) {
        // ans为n或m的最小因子
        for (int n = 2; n < 11; n++) {
            for (int m = 2; m < 11; m++) {
                System.out.println("n = " + n);
                System.out.println("m = " + m);
                System.out.println("ans = " + min(n, m));
                System.out.println("=====");
            }
        }
    }
}
