package base.dp2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

// (不重复,只1张)/(可重复，限张数)，求最小张数
public class MinCoinsLimit {
    private static int process(int[] arr, int idx, int rest) {
        if (rest < 0) {
            return Integer.MAX_VALUE;
        }
        if (idx == arr.length) {
            return rest == 0 ? 0 : Integer.MAX_VALUE;
        }
        int p1 = process(arr, idx + 1, rest);
        int p2 = process(arr, idx + 1, rest - arr[idx]);
        if (p2 != Integer.MAX_VALUE) {
            p2++;
        }
        return Math.min(p1, p2);
    }

    public static int minCoins(int[] arr, int aim) {
        return process(arr, 0, aim);
    }

    //

    public static int dp1(int[] arr, int aim) {
        if (aim == 0) {
            return 0;
        }
        int n = arr.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;
        for (int j = 1; j <= aim; j++) {
            dp[n][j] = Integer.MAX_VALUE;
        }
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                int p1 = dp[idx + 1][rest];
                int p2 = rest - arr[idx] >= 0 ? dp[idx + 1][rest - arr[idx]] : Integer.MAX_VALUE;
                if (p2 != Integer.MAX_VALUE) {
                    p2++;
                }
                dp[idx][rest] = Math.min(p1, p2);
            }
        }
        return dp[0][aim];
    }

    private static class Info {
        public int[] coins;
        public int[] nums;

        public Info(int[] c, int[] z) {
            coins = c;
            nums = z;
        }
    }

    private static Info getInfo(int[] arr) {
        HashMap<Integer, Integer> counts = new HashMap<>();
        for (int val : arr) {
            if (!counts.containsKey(val)) {
                counts.put(val, 1);
            } else {
                counts.put(val, counts.get(val) + 1);
            }
        }
        int n = counts.size();
        int[] coins = new int[n];
        int[] nums = new int[n];
        int idx = 0;
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            coins[idx] = entry.getKey();
            nums[idx++] = entry.getValue();
        }
        return new Info(coins, nums);
    }

    // O(arr长度) + O(货币种数 * aim * 货币平均张数)
    public static int dp2(int[] arr, int aim) {
        if (aim == 0) {
            return 0;
        }
        Info info = getInfo(arr);
        int[] coins = info.coins;
        int[] nums = info.nums;
        int n = coins.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;
        for (int j = 1; j <= aim; j++) {
            dp[n][j] = Integer.MAX_VALUE;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int rest = 0; rest <= aim; rest++) {
                dp[i][rest] = dp[i + 1][rest];
                for (int num = 1; num * coins[i] <= rest && num <= nums[i]; num++) {
                    if (dp[i + 1][rest - num * coins[i]] != Integer.MAX_VALUE) {
                        dp[i][rest] = Math.min(dp[i][rest], num + dp[i + 1][rest - num * coins[i]]);
                    }
                }
            }
        }
        return dp[0][aim];
    }

    private static int num(int pre, int cur, int coin) {
        return (cur - pre) / coin;
    }

    // O(arr长度) + O(货币种数 * aim)
    public static int dp3(int[] arr, int aim) {
        if (aim == 0) {
            return 0;
        }
        Info info = getInfo(arr);
        int[] c = info.coins;
        int[] z = info.nums;
        int n = c.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;
        for (int j = 1; j <= aim; j++) {
            dp[n][j] = Integer.MAX_VALUE;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int mod = 0; mod < Math.min(aim + 1, c[i]); mod++) {
                LinkedList<Integer> w = new LinkedList<>();
                w.add(mod);
                dp[i][mod] = dp[i + 1][mod];
                for (int r = mod + c[i]; r <= aim; r += c[i]) {
                    while (!w.isEmpty() && (dp[i + 1][w.peekLast()] == Integer.MAX_VALUE
                            || dp[i + 1][w.peekLast()] + num(w.peekLast(), r, c[i]) >= dp[i + 1][r])) {
                        w.pollLast();
                    }
                    w.addLast(r);
                    int overdue = r - c[i] * (z[i] + 1);
                    if (w.peekFirst() == overdue) {
                        w.pollFirst();
                    }
                    if (dp[i + 1][w.peekFirst()] == Integer.MAX_VALUE) {
                        dp[i][r] = Integer.MAX_VALUE;
                    } else {
                        dp[i][r] = dp[i + 1][w.peekFirst()] + num(w.peekFirst(), r, c[i]);
                    }
                }
            }
        }
        return dp[0][aim];
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return arr;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        dp2(new int[]{25, 15, 26}, 15);
        dp3(new int[]{25, 15, 26}, 15);
        int times = 100000;
        int maxLen = 20;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int aim = (int) ((maxVal + 1) * 2 * Math.random());
            int ans1 = minCoins(arr, aim);
            int ans2 = dp1(arr, aim);
            int ans3 = dp2(arr, aim);
            int ans4 = dp3(arr, aim);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(aim);
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                break;
            }
        }
        System.out.println("test end");

        System.out.println("==========");

        int aim = 0;
        int[] arr = null;
        long start;
        long end;
        int ans2;
        int ans3;

        System.out.println("性能测试开始");
        maxLen = 30000;
        maxVal = 20;
        aim = 60000;
        arr = randomArr(maxLen, maxVal);

        start = System.currentTimeMillis();
        ans2 = dp2(arr, aim);
        end = System.currentTimeMillis();
        System.out.println("dp2答案 : " + ans2 + ", dp2运行时间 : " + (end - start) + " ms");

        start = System.currentTimeMillis();
        ans3 = dp3(arr, aim);
        end = System.currentTimeMillis();
        System.out.println("dp3答案 : " + ans3 + ", dp3运行时间 : " + (end - start) + " ms");
        System.out.println("性能测试结束");

        System.out.println("===========");

        System.out.println("货币大量重复出现情况下，");
        System.out.println("大数据量测试dp3开始");
        maxLen = 20000000;
        aim = 10000;
        maxVal = 10000;
        arr = randomArr(maxLen, maxVal);
        start = System.currentTimeMillis();
        ans3 = dp3(arr, aim);
        end = System.currentTimeMillis();
        System.out.println("dp3运行时间 : " + (end - start) + " ms");
        System.out.println("大数据量测试dp3结束");

        System.out.println("===========");

        System.out.println("当货币很少出现重复，dp2比dp3有常数时间优势");
        System.out.println("当货币大量出现重复，dp3时间复杂度明显优于dp2");
        System.out.println("dp3的优化用到了窗口内最小值的更新结构");
    }
}
