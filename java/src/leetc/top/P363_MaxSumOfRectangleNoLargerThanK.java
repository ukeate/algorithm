package leetc.top;

import java.util.TreeSet;

public class P363_MaxSumOfRectangleNoLargerThanK {
    private static int[][] rotate(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] r = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                r[j][i] = matrix[i][j];
            }
        }
        return r;
    }

    public static int maxSumSubmatrix(int[][] matrix, int k) {
        if (matrix == null || matrix[0] == null) {
            return Integer.MIN_VALUE;
        }
        if (matrix.length > matrix[0].length) {
            matrix = rotate(matrix);
        }
        int row = matrix.length, col = matrix[0].length;
        int res = Integer.MIN_VALUE;
        TreeSet<Integer> sumSet = new TreeSet<>();
        for (int s = 0; s < row; s++) {
            int[] colSum = new int[col];
            for (int e = s; e < row; e++) {

            }
        }
        return res;
    }
}
