package base.dp5_cmpr;

public class CanIWin {
    private static boolean process1(int[] arr, int rest) {
        if (rest <= 0) {
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != -1) {
                int cur = arr[i];
                arr[i] = -1;
                boolean next = process1(arr, rest - cur);
                arr[i] = cur;
                if (!next) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canIWin0(int choose, int total) {
        if (total == 0) {
            return true;
        }
        if ((choose * (choose + 1) >> 1) < total) {
            return false;
        }
        int[] arr = new int[choose];
        for (int i = 0; i < choose; i++) {
            arr[i] = i + 1;
        }
        return process1(arr, total);
    }

    //

    private static boolean process1(int choose, int status, int rest) {
        if (rest <= 0) {
            return false;
        }
        for (int i = 1; i <= choose; i++) {
            if (((1 << i) & status) == 0) {
                if (!process1(choose, (status | (1 << i)), rest - i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean  canIWin1(int choose, int total) {
        if (total == 0) {
            return true;
        }
        if ((choose * (choose + 1) >> 1) < total) {
            return false;
        }
        return process1(choose, 0, total);
    }

    //

    private static boolean process2(int choose, int status, int rest, int[] dp) {
        if (dp[status] != 0) {
            return dp[status] == 1 ? true : false;
        }
        boolean ans = false;
        if (rest > 0) {
            for (int i = 1; i <= choose; i++) {
                if (((1 << i) & status) == 0) {
                    if (!process2(choose, (status | (1 << i)), rest - i, dp)) {
                        ans = true;
                        break;
                    }
                }
            }
        }
        dp[status] = ans ? 1 : -1;
        return ans;
    }

    // rest由status决定，所以dp参数不用rest
    public static boolean canIWin2(int choose, int total) {
        if (total == 0) {
            return true;
        }
        if ((choose * (choose + 1) >> 1) < total) {
            return false;
        }
        int[] dp = new int[1 << (choose + 1)];
        return process2(choose, 0, total, dp);
    }

}
