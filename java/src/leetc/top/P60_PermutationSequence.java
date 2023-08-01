package leetc.top;

public class P60_PermutationSequence {
    // [0,9]全排列数
    public static int[] factorial = {0, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880};
    // i个数, j开头全排列最后一个
    public static int[][] fdp = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 4, 6, 0, 0, 0, 0, 0, 0},
            {0, 6, 12, 18, 24, 0, 0, 0, 0, 0},
            {0, 24, 48, 72, 96, 120, 0, 0, 0, 0},
            {0, 120, 240, 360, 480, 600, 720, 0, 0, 0},
            {0, 720, 1440, 2160, 2880, 3600, 4320, 5040, 0, 0},
            {0, 5040, 10080, 15120, 20160, 25200, 30240, 35280, 40320, 0},
            {0, 40320, 80640, 120960, 161280, 201600, 241920, 282240, 322560, 362880}};

    private static int[] thArr(int n, int k) {
        int[] arr = new int[n];
        int restK = k, restN = n;
        for (int i = 0; i < n; i++) {
            int kth = 1;
            while (fdp[restN][kth] < restK) {
                kth++;
            }
            arr[i] = kth;
            restK -= fdp[restN--][kth - 1];
        }
        return arr;
    }

    private static char restKthChar(int kth, char[] chas) {
        int idx = 0;
        for (int i = 1; i < chas.length; i++) {
            if (chas[i] != 0) {
                if (--kth == 0) {
                    idx = i;
                    break;
                }
            }
        }
        char ans = chas[idx];
        chas[idx] = 0;
        return ans;
    }

    public static String getPermutation(int n, int k) {
        if (n < 1 || n > 9 || k < 1 || k > factorial[n]) {
            return "";
        }
        // [i]表示[1,n]中未先用过的第几个数
        int[] thArr = thArr(n, k);
        char[] chas = new char[n + 1];
        for (int i = 1; i <= n; i++) {
            chas[i] = (char) ('0' + i);
        }
        char[] ans = new char[n];
        for (int i = 0; i < thArr.length; i++) {
            ans[i] = restKthChar(thArr[i], chas);
        }
        return String.valueOf(ans);
    }
}
