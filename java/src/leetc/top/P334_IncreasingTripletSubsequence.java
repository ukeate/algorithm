package leetc.top;

public class P334_IncreasingTripletSubsequence {
    public static boolean increasingTriplet(int[] arr) {
        if (arr == null || arr.length < 3) {
            return false;
        }
        int[] ends = new int[2];
        ends[0] = arr[0];
        int right = 0, l = 0, r = 0, m = 0;
        for (int i = 1; i < arr.length; i++) {
            l = 0;
            r = right;
            // [l]>[i] 或 l = right + 1
            while (l <= r) {
                m = (l + r) / 2;
                if (arr[i] > ends[m]) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
            right = Math.max(right, l);
            if (right > 1) {
                return true;
            }
            ends[l] = arr[i];
        }
        return false;
    }
}
