package base.dp5;

import java.util.ArrayList;
import java.util.List;

// Travelling Salesman Problem
public class TSP {
    private static int f1(int[][] matrix, List<Integer> set, int start) {
        int cityNum = 0;
        for (int i = 0; i < set.size(); i++) {
            if (set.get(i) != null) {
                cityNum++;
            }
        }
        if (cityNum == 1) {
            return matrix[start][0];
        }
        set.set(start, null);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < set.size(); i++) {
            if (set.get(i) != null) {
                int cur = matrix[start][i] + f1(matrix, set, i);
                min = Math.min(min, cur);
            }
        }
        set.set(start, 1);
        return min;
    }

    public static int t1(int[][] matrix) {
        int n = matrix.length;
        List<Integer> set = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            set.add(i);
        }
        return f1(matrix, set, 0);
    }

    //

    private static int f2(int[][] matrix, int cityStatus, int start) {
        if (cityStatus == (cityStatus & -cityStatus)) {
            return matrix[start][0];
        }
        cityStatus &= (~(1 << start));
        int min = Integer.MAX_VALUE;
        for (int move = 0; move < matrix.length; move++) {
            if ((cityStatus & (1 << move)) != 0) {
                int cur = matrix[start][move] + f2(matrix, cityStatus, move);
                min = Math.min(min, cur);
            }
        }
        cityStatus |= (1 << start);
        return min;
    }

    public static int t2(int[][] matrix) {
        int n = matrix.length;
        int allCity = (1 << n) - 1;
        return f2(matrix, allCity, 0);
    }

    //

    private static int f3(int[][] matrix, int cityStatus, int start, int[][] dp) {
        if (dp[cityStatus][start] != -1) {
            return dp[cityStatus][start];
        }
        if (cityStatus == (cityStatus & -cityStatus)) {
            dp[cityStatus][start] = matrix[start][0];
            return dp[cityStatus][start];
        }
        cityStatus &= (~(1 << start));
        int min = Integer.MAX_VALUE;
        for (int move = 0; move < matrix.length; move++) {
            if ((cityStatus & (1 << move)) != 0) {
                int cur = matrix[start][move] + f3(matrix, cityStatus, move, dp);
                min = Math.min(min, cur);
            }
        }
        cityStatus |= (1 << start);
        dp[cityStatus][start] = min;
        return dp[cityStatus][start];
    }

    public static int t3(int[][] matrix) {
        int n = matrix.length;
        int allCity = (1 << n) - 1;
        int[][] dp = new int[1 << n][n];
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -1;
            }
        }
        return f3(matrix, allCity, 0, dp);
    }

    //

    public static int t4(int[][] matrix) {
        int n = matrix.length;
        int statusNums = 1 << n;
        int[][] dp = new int[statusNums][n];
        for (int status = 0; status < statusNums; status++) {
            for (int start = 0; start < n; start++) {
                if ((status & (1 << start)) != 0) {
                    if (status == (status & -status)) {
                        dp[status][start] = matrix[start][0];
                    } else {
                        int min = Integer.MAX_VALUE;
                        int preStatus = status & (~(1 << start));
                        for (int i = 0; i < n; i++) {
                            if ((preStatus & (1 << i)) != 0) {
                                int cur = matrix[start][i] + dp[preStatus][i];
                                min = Math.min(min, cur);
                            }
                        }
                        dp[status][start] = min;
                    }
                }
            }
        }
        return dp[statusNums - 1][0];
    }

    //

    public static int t5(int[][] matrix, int origin) {
        if (matrix == null || matrix.length < 2 || origin < 0 || origin >= matrix.length) {
            return 0;
        }
        // 去除origin
        int n = matrix.length - 1;
        int s = 1 << n;
        int[][] dp = new int[s][n];
        int icity = 0;
        int kcity = 0;
        for (int i = 0; i < n; i++) {
            icity = i < origin ? i : i + 1;
            dp[0][i] = matrix[icity][origin];
        }
        for (int status = 1; status < s; status++) {
            for (int i = 0; i < n; i++) {
                dp[status][i] = Integer.MAX_VALUE;
                if ((1 << i & status) != 0) {
                    icity = i < origin ? i : i + 1;
                    for (int k = 0; k < n; k++) {
                        if ((1 << k & status) != 0) {
                            kcity = k < origin ? k : k + 1;
                            dp[status][i] = Math.min(dp[status][i], matrix[icity][kcity] + dp[status ^ (1 << i)][k]);
                        }
                    }
                }
            }
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            icity = i < origin ? i : i + 1;
            ans = Math.min(ans, dp[s - 1][i] + matrix[origin][icity]);
        }
        return ans;
    }

    public static int[][] randomGraph(int maxSize, int maxVal) {
        int len = (int) (maxSize * Math.random()) + 1;
        int[][] matrix = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                matrix[i][j] = (int) (maxVal + Math.random()) + 1;
            }
        }
        for (int i = 0; i < len; i++) {
            matrix[i][i] = 0;
        }
        return matrix;
    }

    public static void main(String[] args) {
        int times = 1000;
        int maxLen = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[][] matrix = randomGraph(maxLen, maxVal);
            int origin = (int) (matrix.length * Math.random());
            int ans1 = t1(matrix);
            int ans2 = t2(matrix);
            int ans3 = t3(matrix);
            int ans4 = t4(matrix);
            int ans5 = t5(matrix, 0);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3 || ans1 != ans5) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4 + "|" + ans5);
                break;
            }
        }
        System.out.println("test end");
    }
}
