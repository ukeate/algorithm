package leetc.top;

public class P48_RotateImage {
    private static void rotateEdge(int[][] m, int tr, int tc, int dr, int dc) {
        int times = dc - tc;
        int tmp = 0;
        for (int i = 0; i < times; i++) {
            tmp = m[tr][tc + i];
            m[tr][tc + i] = m[dr - i][tc];
            m[dr - i][tc] = m[dr][dc - i];
            m[dr][dc - i] = m[tr + i][dc];
            m[tr + i][dc] = tmp;
        }
    }

    public void rotate(int[][] matrix) {
        int tr = 0, tc = 0, dr = matrix.length - 1, dc = matrix[0].length - 1;
        while (tr < dr) {
            rotateEdge(matrix, tr++, tc++, dr--, dc--);
        }
    }
}
