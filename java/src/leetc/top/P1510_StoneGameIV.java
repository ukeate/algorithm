package leetc.top;

public class P1510_StoneGameIV {

    private static boolean process1(int n, int[] dp) {
       if(dp[n] != 0) {
           return dp[n] == 1;
       }
       boolean ans = false;
       for (int i = 1; i * i <= n; i++) {
           if (!process1(n - i * i, dp)) {
               ans = true;
               break;
           }
       }
       dp[n] = ans ? 1 : -1;
       return ans;
    }

    public static boolean winnerSquareGame1(int n) {
        int[] dp = new int[n + 1];
        dp[0] = -1;
        return process1(n, dp);
    }

    //

    public static boolean winnerSquareGame2(int n) {
        boolean[] dp = new boolean[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                if (!dp[i - j * j]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }
}
