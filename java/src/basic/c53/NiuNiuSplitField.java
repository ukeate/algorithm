package basic.c53;

import java.util.Scanner;

// https://www.nowcoder.com/questionTerminal/fe30a13b5fb84b339cb6cb3f70dca699
// 分田地，横竖切3刀，牛牛总选最小一份，如果划分使牛牛得到尽可能大
public class NiuNiuSplitField {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();
            int m = in.nextInt();
            int[][] matrix = new int[n][m];
            for (int i = 0; i < n; i++) {
                char[] chas = in.next().toCharArray();
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = chas[j] - '0';
                }
            }
            System.out.println(max(matrix));
        }
    }

    private static int[][] sum(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        int[][] record = new int[row][col];
        record[0][0] = matrix[0][0];
        for (int i = 1; i < row; i++) {
            record[i][0] = record[i - 1][0] + matrix[i][0];
        }
        for (int j = 1; j < col; j++) {
            record[0][j] = record[0][j - 1] + matrix[0][j];
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                record[i][j] = record[i][j - 1] + record[i - 1][j] - record[i - 1][j - 1] + matrix[i][j];
            }
        }
        return record;
    }

    private static int area(int[][] record, int i1, int j1, int i2, int j2) {
        int all = record[i2][j2];
        int left = j1 > 0 ? record[i2][j1 - 1] : 0;
        int up = i1 > 0 ? record[i1 - 1][j2] : 0;
        int makeUp = (i1 > 0 && j1 > 0) ? record[i1 - 1][j1 - 1] : 0;
        return all - left - up + makeUp;
    }

    private static int val(int[][] record, int c1, int c2, int c3, int prow, int crow) {
        int v1 = area(record, prow, 0, crow, c1);
        int v2 = area(record, prow, c1 + 1, crow, c2);
        int v3 = area(record, prow, c2 + 1, crow, c3);
        int v4 = area(record, prow, c3 + 1, crow, record[0].length - 1);
        return Math.min(Math.min(v1, v2), Math.min(v3, v4));
    }

    private static int twoRowMin(int[][] record, int c1, int c2, int c3, int i, int split, int j) {
        return Math.min(val(record, c1, c2, c3, i, split), val(record, c1, c2, c3, split + 1, j));
    }

    private static int[] upSplit(int[][] record, int c1, int c2, int c3) {
        int size = record.length;
        int[] up = new int[size];
        int split = 0;
        up[1] = Math.min(val(record, c1, c2, c3, 0, 0), val(record, c1, c2, c3, 1, 1));
        for (int i = 2; i < size; i++) {
            int upSplitMax = twoRowMin(record, c1, c2, c3, 0, split, i);
            while (split < i - 1) {
                int moved = twoRowMin(record, c1, c2, c3, 0, split + 1, i);
                if (moved < upSplitMax) {
                    // 过了增大点，以后只能单调减小
                    break;
                } else {
                    upSplitMax = moved;
                    split++;
                }
            }
            up[i] = upSplitMax;
        }
        return up;
    }

    private static int[] downSplit(int[][] record, int c1, int c2, int c3) {
        int size = record.length;
        int[] down = new int[size];
        int split = size - 1;
        down[size - 2] = Math.min(val(record, c1, c2, c3, size - 2, size - 2),
                val(record, c1, c2, c3, size - 1, size - 1));
        for (int i = size - 3; i >= 0; i--) {
            int downSplitMax = twoRowMin(record, c1, c2, c3, i, split - 1, size - 1);
            while (split > i + 1) {
                int moved = twoRowMin(record, c1, c2, c3, i, split - 2, size - 1);
                if (moved < downSplitMax) {
                    break;
                } else {
                    downSplitMax = moved;
                    split--;
                }
            }
            down[i] = downSplitMax;
        }
        return down;
    }

    private static int best(int[][] help, int c1, int c2, int c3) {
        // [0,i]切1刀8块最大值
        int[] up = upSplit(help, c1, c2, c3);
        // [i,n-1]切1刀8块最大值
        int[] down = downSplit(help, c1, c2, c3);
        int res = Integer.MIN_VALUE;
        for (int mid = 1; mid < help.length - 2; mid++) {
            res = Math.max(res, Math.min(up[mid], down[mid + 1]));
        }
        return res;
    }

    public static int max(int[][] matrix) {
        if (matrix == null || matrix.length < 4 || matrix[0].length < 4) {
            return 0;
        }
        // [0,0]到[i,j]的累加和
        int[][] help = sum(matrix);
        int col = matrix[0].length;
        int res = Integer.MIN_VALUE;
        // 竖三刀
        for (int c1 = 0; c1 < col - 3; c1++) {
            for (int c2 = c1 + 1; c2 < col - 2; c2++) {
                for (int c3 = c2 + 1; c3 < col - 1; c3++) {
                    res = Math.max(res, best(help, c1, c2, c3));
                }
            }
        }
        return res;
    }

}
