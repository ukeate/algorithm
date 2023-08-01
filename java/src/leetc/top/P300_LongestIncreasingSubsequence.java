package leetc.top;

public class P300_LongestIncreasingSubsequence {
    public int lengthOfLIS(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int[] ends = new int[arr.length];
        ends[0] = arr[0];
        int endsR = 0, max = 1;
        for (int i = 1; i < arr.length; i++) {
            int l = 0, r = endsR;
            // ends[l] >= arr[i] || l = r + 1
            while (l <= r) {
                int m = (l + r) / 2;
                if (arr[i] > ends[m]) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
            endsR = Math.max(endsR, l);
            ends[l] = arr[i];
            max = Math.max(max, l + 1);
        }
        return max;
    }
}
