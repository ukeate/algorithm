package basic.c36;

import java.util.HashMap;

// 最多划分多少块异或和为0的部分
public class MostSubEOR {

    public static int most(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        int[] dp = new int[n];
        // 异或和出现的最晚位置
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum ^= arr[i];
            if (map.containsKey(sum)) {
                // 最后一块为[pre+1, i]
                int pre = map.get(sum);
                dp[i] = pre == -1 ? 1 : (dp[pre] + 1);
            }
            if (i > 0) {
                dp[i] = Math.max(dp[i - 1], dp[i]);
            }
            map.put(sum, i);
        }
        return dp[dp.length - 1];
    }

    public static int sure(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int[] eors = new int[arr.length];
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
            eors[i] = eor;
        }
        int[] dp = new int[arr.length];
        dp[0] = arr[0] == 0 ? 1 : 0;
        for (int i = 1; i < arr.length; i++) {
            dp[i] = eors[i] == 0 ? 1 : 0;
            for (int j = 0; j < i; j++) {
                if ((eors[i] ^ eors[j]) == 0) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            dp[i] = Math.max(dp[i], dp[i - 1]);
        }
        return dp[dp.length - 1];
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 500000;
        int maxLen = 300;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = most(arr);
            int ans2 = sure(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
