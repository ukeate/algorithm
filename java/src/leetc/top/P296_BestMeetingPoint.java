package leetc.top;

public class P296_BestMeetingPoint {
    public static int minTotalDistance(int[][] grid) {
        int n = grid.length, m = grid[0].length;
        int[] iOnes = new int[n], jOnes = new int[m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    iOnes[i]++;
                    jOnes[j]++;
                }
            }
        }
        int total = 0;
        int p = 0, q = n - 1;
        int pRest = 0, qRest = 0;
        while (p < q) {
            if (iOnes[p] + pRest <= iOnes[q] + qRest) {
                total += iOnes[p] + pRest;
                pRest += iOnes[p++];
            } else {
                total += iOnes[q] + qRest;
                qRest += iOnes[q--];
            }
        }
        p = 0;
        q = m - 1;
        pRest = 0;
        qRest = 0;
        while (p < q) {
            if (jOnes[p] + pRest <= jOnes[q] + qRest) {
                total += jOnes[p] + pRest;
                pRest += jOnes[p++];
            } else {
                total += jOnes[q] + qRest;
                qRest += jOnes[q--];
            }
        }
        return total;
    }
}
