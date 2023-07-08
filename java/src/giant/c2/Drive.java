package giant.c2;

import java.lang.reflect.Array;
import java.util.Arrays;

// N个司机, 在两个区域收入是income, 区域平均分人, 返回收入最大值
public class Drive {

    // idx向后分配, 0区还有rest个未分配, 返回收入
    private static int process1(int[][] income, int idx, int rest) {
        if (idx == income.length) {
            return 0;
        }
        // 1区满了
        if (income.length - idx == rest) {
            return income[idx][0] + process1(income, idx + 1, rest - 1);
        }
        // 0区满了
        if (rest == 0) {
            return income[idx][1] + process1(income, idx + 1, rest);
        }
        // 都没满
        int p1 = income[idx][0] + process1(income, idx + 1, rest - 1);
        int p2 = income[idx][1] + process1(income, idx + 1, rest);
        return Math.max(p1, p2);
    }

    public static int max1(int[][] income) {
        if (income == null || income.length < 2 || (income.length & 1) != 0) {
            return 0;
        }
        int n = income.length;
        int m = n >> 1;
        return process1(income, 0, m);
    }

    //

    public static int max2(int[][] income) {
        int n = income.length;
        int m = n >> 1;
        int[][] dp = new int[n + 1][m + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= m; j++) {
                if (n - i == j) {
                    dp[i][j] = income[i][0] + dp[i + 1][j - 1];
                } else if (j == 0) {
                    dp[i][j] = income[i][1] + dp[i + 1][j];
                } else {
                    int p1 = income[i][0] + dp[i + 1][j - 1];
                    int p2 = income[i][1] + dp[i + 1][j];
                    dp[i][j] = Math.max(p1, p2);
                }
            }
        }
        return dp[0][m];
    }

    //

    // 贪心策略，先所有司机去0区，再一半司机去换去1, 获得最大额外收益
    public static int max3(int[][] income) {
        int n = income.length;
        int[] arr = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            arr[i] = income[i][1] - income[i][0];
            sum += income[i][0];
        }
        Arrays.sort(arr);
        int m = n >> 1;
        for (int i = n - 1; i >= m; i--) {
            sum += arr[i];
        }
        return sum;
    }

    //

    private static int[][] randomMatrix(int len, int maxVal) {
        int[][] ans = new int[len << 1][2];
        for (int i = 0; i < ans.length; i++) {
            ans[i][0] = (int) ((maxVal + 1) * Math.random());
            ans[i][1] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 500;
        int maxLen = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) (maxLen * Math.random()) + 1;
            int[][] matrix = randomMatrix(len, maxVal);
            int ans1 = max1(matrix);
            int ans2 = max2(matrix);
            int ans3 = max3(matrix);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3);
                break;
            }
        }
        System.out.println("test end");
    }
}
