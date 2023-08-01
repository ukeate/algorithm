package leetc.top;

public class P348_DesignTicTacToe {
    class TicTacToe {
        private int[][] rows;
        private int[][] cols;
        private int[] leftUp;
        private int[] rightUp;
        private boolean[][] matrix;
        private int n;
        public TicTacToe(int n) {
            rows = new int[n][3];
            cols = new int[n][3];
            leftUp = new int[3];
            rightUp = new int[3];
            matrix = new boolean[n][n];
            this.n = n;
        }

        public int move(int row, int col, int player) {
            if (matrix[row][col]) {
                return 0;
            }
            matrix[row][col] = true;
            rows[row][player]++;
            cols[col][player]++;
            if (row == col) {
                leftUp[player]++;
            }
            if (row + col == n - 1) {
                rightUp[player]++;
            }
            if (rows[row][player] == n || cols[col][player] == n || leftUp[player] == n || rightUp[player] == n) {
                return player;
            }
            return 0;
        }
    }
}
