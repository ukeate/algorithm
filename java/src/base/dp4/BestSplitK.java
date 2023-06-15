package base.dp4;

// https://leetcode.com/problems/split-array-largest-sum/
// 划分k部分，使部分最大和最小
public class BestSplitK {
    private static int sum(int[] sums, int l, int r) {
        return sums[r + 1] - sums[l];
    }

    public static int split1(int[] nums, int k) {
        int n = nums.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        int[][] dp = new int[n][k + 1];
        for (int j = 1; j <= k; j++) {
            dp[0][j] = nums[0];
        }
        for (int i = 1; i < n; i++) {
            dp[i][1] = sum(sum, 0, i);
        }
        for (int i = 1; i < n; i++) {
            for (int j = 2; j <= k; j++) {
                int ans = Integer.MAX_VALUE;
                for (int leftEnd = 0; leftEnd <= i; leftEnd++) {
                    int leftCost = leftEnd == -1 ? 0 : dp[leftEnd][j - 1];
                    int rightCost = leftEnd == -1 ? 0 : sum(sum, leftEnd + 1, i);
                    int cur = Math.max(leftCost, rightCost);
                    if (cur < ans) {
                        ans = cur;
                    }
                }
                dp[i][j] = ans;
            }
        }
        return dp[n - 1][k];
    }

    //

    public static int split2(int[] nums, int k) {
        int n = nums.length;
        int[] sums = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        int[][] dp = new int[n][k + 1];
        int[][] best = new int[n][k + 1];
        for (int j = 1; j <= k; j++) {
            dp[0][j] = nums[0];
            best[0][j] = -1;
        }
        for (int i = 1; i < n; i++) {
            dp[i][1] = sum(sums, 0, i);
            best[i][1] = -1;
        }
        for (int j = 2; j <= k; j++) {
            for (int i = n - 1; i >= 1; i--) {
                int down = best[i][j - 1];
                int up = i == n - 1 ? n - 1 : best[i + 1][j];
                int ans = Integer.MAX_VALUE;
                int bestChoose = -1;
                for (int leftEnd = down; leftEnd <= up; leftEnd++) {
                    int leftCost = leftEnd == -1 ? 0 : dp[leftEnd][j - 1];
                    int rightCost = leftEnd == i ? 0 : sum(sums, leftEnd + 1, i);
                    int cur = Math.max(leftCost, rightCost);
                    if (cur < ans) {
                        ans = cur;
                        bestChoose = leftEnd;
                    }
                }
                dp[i][j] = ans;
                best[i][j] = bestChoose;
            }
        }
        return dp[n - 1][k];
    }

    //

    public static int getNeedParts(int[] arr, long aim) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > aim) {
                return Integer.MAX_VALUE;
            }
        }
        int parts = 1;
        int all = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (all + arr[i] > aim) {
                parts++;
                all = arr[i];
            } else {
                all += arr[i];
            }
        }
        return parts;
    }

    public static int split3(int[] nums, int m) {
        long sum = 0;
        for(int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }
        long l = 0;
        long r = sum;
        long ans = 0;
        while (l <= r) {
            long mid = (l + r) / 2;
            long cur = getNeedParts(nums, mid);
            if (cur <= m) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return (int) ans;
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int m = (int) ((maxLen + 1) * Math.random()) + 1;
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = split1(arr, m);
            int ans2 = split2(arr, m);
            int ans3 = split3(arr, m);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
