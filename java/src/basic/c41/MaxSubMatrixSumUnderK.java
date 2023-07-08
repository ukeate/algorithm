package basic.c41;

import java.util.TreeSet;

// 最大子矩阵累加和小于等于k
public class MaxSubMatrixSumUnderK {
    public static int max(int[][] matrix, int k) {
        if (matrix == null || matrix[0] == null) {
            return 0;
        }
        int row = matrix.length, col = matrix[0].length;
        int res = Integer.MIN_VALUE;
        TreeSet<Integer> set = new TreeSet<>();
        for (int s = 0; s < row; s++) {
            int[] colSum = new int[col];
            for (int e = s; e < row; e++) {
                set.add(0);
                int rowSum = 0;
                for (int c = 0; c < col; c++) {
                    colSum[c] += matrix[e][c];
                    rowSum += colSum[c];
                    Integer it = set.ceiling(rowSum - k);
                    if (it != null) {
                        res = Math.max(res, rowSum - it);
                    }
                    set.add(rowSum);
                }
                set.clear();
            }
        }
        return res;
    }

}
