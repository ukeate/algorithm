package leetc.followup;

public class PrintStar {
    private static void set(char[][] m, int lu, int rd) {
        for (int col = lu; col <= rd; col++) {
            m[lu][col] = '*';
        }
        for (int row = lu + 1; row <= rd; row++) {
            m[row][rd] = '*';
        }
        for (int col = rd - 1; col > lu; col--) {
            m[rd][col] = '*';
        }
        for (int row = rd - 1; row > lu + 1; row--) {
            m[row][lu + 1] = '*';
        }
    }

    public static void printStar(int n) {
        int leftUp = 0, rightDown = n - 1;
        char[][] m = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = ' ';
            }
        }
        while (leftUp <= rightDown) {
            set(m, leftUp, rightDown);
            leftUp += 2;
            rightDown -= 2;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        printStar(20);
    }
}
