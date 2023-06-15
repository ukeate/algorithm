package base.print;

public class CurlMatrix {
    private static void set(char[][] m, int leftUp, int rightDown) {
        for (int col = leftUp; col <= rightDown; col++) {
            m[leftUp][col] = '*';
        }
        for (int row = leftUp + 1; row <= rightDown; row++) {
            m[row][rightDown] = '*';
        }
        for (int col = rightDown - 1; col >= leftUp + 1; col--) {
            m[rightDown][col] = '*';
        }
        for (int row = rightDown - 1; row >= leftUp + 2; row--) {
            m[row][leftUp + 1] = '*';
        }
    }

    public static void curl(int n) {
        int leftUp = 0;
        int rightDown = n - 1;
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
        curl(10);
    }
}
