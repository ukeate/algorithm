package base.dp6_biz;

// 值在[1,200], [i]<=max([i-1],[i+1]), 0是缺失的数，返回有多少种填充方式
public class RestoreWays {
    private static boolean isValid(int[] arr) {
        if (arr[0] > arr[1]) {
            return false;
        }
        if (arr[arr.length - 1] > arr[arr.length - 2]) {
            return false;
        }
        for (int i = 1; i < arr.length - 1; i++) {
            if (arr[i] > Math.max(arr[i - 1], arr[i + 1])) {
                return false;
            }
        }
        return true;
    }

    private static int process0(int[] arr, int idx) {
        if (idx == arr.length) {
            return isValid(arr) ? 1 : 0;
        } else {
            if (arr[idx] != 0) {
                return process0(arr, idx + 1);
            } else {
                int ways = 0;
                for (int v = 1; v <= 200; v++) {
                    arr[idx] = v;
                    ways += process0(arr, idx + 1);
                }
                arr[idx] = 0;
                return ways;
            }
        }
    }

    public static int ways0(int[] arr) {
        return process0(arr, 0);
    }

    //

    // [0, i]上试有几种方法
    // s 0时右大, 1时右等, 2时右小
    private static int process1(int[] arr, int i, int v, int s) {
        if (i == 0) {
            return ((s == 0 || s == 1) && (arr[0] == 0 || v == arr[0])) ? 1 : 0;
        }
        if (arr[i] != 0 && v != arr[i]) {
            return 0;
        }
        int ways = 0;
        if (s == 0 || s == 1) {
            for (int pre = 1; pre < v; pre++) {
                ways += process1(arr, i - 1, pre, 0);
            }
        }
        ways += process1(arr, i - 1, v, 1);
        for (int pre = v + 1; pre <= 200; pre++) {
            ways += process1(arr, i - 1, pre, 2);
        }
        return ways;
    }

    public static int ways1(int[] arr) {
        int n = arr.length;
        if (arr[n - 1] != 0) {
            return process1(arr, n - 1, arr[n - 1], 2);
        } else {
            int ways = 0;
            for (int v = 1; v <= 200; v++) {
                ways += process1(arr, n - 1, v, 2);
            }
            return ways;
        }
    }

    //

    public static int ways2(int[] arr) {
        int n = arr.length;
        int[][][] dp = new int[n][201][3];
        if (arr[0] != 0) {
            dp[0][arr[0]][0] = 1;
            dp[0][arr[0]][1] = 1;
        } else {
            for (int v = 1; v <= 200; v++) {
                dp[0][v][0] = 1;
                dp[0][v][1] = 1;
            }
        }
        for (int i = 1; i < n; i++) {
            for (int v = 1; v <= 200; v++) {
                for (int s = 0; s < 3; s++) {
                    if (arr[i] == 0 || v == arr[i]) {
                        if (s == 0 || s == 1) {
                            for (int pre = 1; pre < v; pre++) {
                                dp[i][v][s] += dp[i - 1][pre][0];
                            }
                        }
                        dp[i][v][s] += dp[i - 1][v][1];
                        for (int pre = v + 1; pre <= 200; pre++) {
                            dp[i][v][s] += dp[i - 1][pre][2];
                        }
                    }
                }
            }
        }
        if (arr[n - 1] != 0) {
            return dp[n - 1][arr[n - 1]][2];
        } else {
            int ways = 0;
            for (int v = 1; v <= 200; v++) {
                ways += dp[n - 1][v][2];
            }
            return ways;
        }
    }

    //

    private static int sum(int begin, int end, int relation, int[][] presum) {
        return presum[end][relation] - presum[begin - 1][relation];
    }

    public static int ways3(int[] arr) {
        int n = arr.length;
        int[][][] dp = new int[n][201][3];
        if (arr[0] != 0) {
            dp[0][arr[0]][0] = 1;
            dp[0][arr[0]][1] = 1;
        } else {
            for (int v = 1; v <= 200; v++) {
                dp[0][v][0] = 1;
                dp[0][v][1] = 1;
            }
        }
        int[][] presum = new int[201][3];
        for (int v = 1; v <= 200; v++) {
            for (int s = 0; s < 3; s++) {
                presum[v][s] = presum[v - 1][s] + dp[0][v][s];
            }
        }
        for (int i = 1; i < n; i++) {
            for (int v = 1; v <= 200; v++) {
                for (int s = 0; s < 3; s++) {
                    if (arr[i] == 0 || v == arr[i]) {
                        if (s == 0 || s == 1) {
                            dp[i][v][s] += sum(1, v - 1, 0, presum);
                        }
                        dp[i][v][s] += dp[i - 1][v][1];
                        dp[i][v][s] += sum(v + 1, 200, 2, presum);
                    }
                }
            }
            for (int v = 1; v <= 200; v++) {
                for (int s = 0; s < 3; s++) {
                    presum[v][s] = presum[v - 1][s] + dp[i][v][s];
                }
            }
        }
        if (arr[n - 1] != 0) {
            return dp[n - 1][arr[n - 1]][2];
        } else {
            return sum(1, 200, 2, presum);
        }
    }

    //

    private static int[] randomArr(int len) {
        int[] ans = new int[len];
        for (int i = 0; i < ans.length; i++) {
            if (Math.random() < 0.5) {
                ans[i] = 0;
            } else {
                ans[i] = (int) (Math.random() * 200) + 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 15;
        int maxLen = 5;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int n = (int) (maxLen * Math.random()) + 2;
            int[] arr = randomArr(n);
            int ans0 = ways0(arr);
            int ans1 = ways1(arr);
            int ans2 = ways2(arr);
            int ans3 = ways3(arr);
            if (ans0 != ans1 || ans2 != ans3 || ans0 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans0 + "|" + ans1 + "|" + ans2 + "|" + ans3);
                break;
            }
        }
        System.out.println("test end");
        int n = 1000000;
        int[] arr = randomArr(n);
        long begin = System.currentTimeMillis();
        ways3(arr);
        long end = System.currentTimeMillis();
        System.out.println("ways3 time:" + (end - begin) + " ms");
    }
}
