package leetc.top;

public class P289_GameOfLife {
    private static boolean ok(int[][] board, int i, int j) {
        return i >= 0 && i < board.length && j >= 0 && j < board[0].length && (board[i][j] & 1) == 1;
    }

    private static int neighbors(int[][] board, int i, int j) {
        int count = 0;
        count += ok(board, i - 1, j - 1) ? 1 : 0;
        count += ok(board, i - 1, j) ? 1 : 0;
        count += ok(board, i - 1, j + 1) ? 1 : 0;
        count += ok(board, i, j - 1) ? 1 : 0;
        count += ok(board, i, j + 1) ? 1 : 0;
        count += ok(board, i + 1, j - 1) ? 1 : 0;
        count += ok(board, i + 1, j) ? 1 : 0;
        count += ok(board, i + 1, j + 1) ? 1 : 0;
        return count;
    }

    private static void set(int[][] board, int i, int j) {
        board[i][j] |= 2;
    }

    private static int get(int[][] board, int i, int j) {
        return board[i][j] >> 1;
    }

    public static void gameOfLife(int[][] board) {
        int n = board.length, m = board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int neighbors = neighbors(board, i, j);
                if (neighbors == 3 || (board[i][j] == 1 && neighbors == 2)) {
                    set(board, i, j);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j] = get(board, i, j);
            }
        }
    }
}
