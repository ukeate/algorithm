package leetc.top;

public class P568_MaximumVacationDays {
    // fly[城市i][城市j]=能否飞, day[城市][第几周]=可休几天, 飞不算时间
    public static int maxVacationDays(int[][] fly, int[][] day) {
        // 城数
        int n = fly.length;
        // 周数
        int k = day[0].length;
        // pass[a][0]=b, pass[a][1]=c, 从b、c可以到a
        int[][] pass = new int[n][];
        for (int i = 0; i < n; i++) {
            int s = 0;
            for (int j = 0; j < n; j++) {
                if (fly[j][j] != 0) {
                    s++;
                }
            }
            pass[i] = new int[s];
            for (int j = n - 1; j >= 0; j--) {
                if (fly[j][i] != 0) {
                    pass[i][--s] = j;
                }
            }
        }
        // dp[i][j], 第i周必在j城
        int[][] dp = new int[k][n];
        dp[0][0] = day[0][0];
        for (int j = 1; j < n; j++) {
            dp[0][j] = fly[0][j] != 0 ? day[j][0] : -1;
        }
        for (int i = 1; i < k; i++) {
            for (int j = 0; j < n; j++) {
                int max = dp[i - 1][j];
                for (int p : pass[j]) {
                    max = Math.max(max, dp[i - 1][p]);
                }
                dp[i][j] = max != -1 ? max + day[j][i] : -1;
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, dp[k - 1][i]);
        }
        return ans;
    }
}
