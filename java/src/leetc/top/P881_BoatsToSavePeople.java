package leetc.top;

import java.util.Arrays;

public class P881_BoatsToSavePeople {
    public int numRescueBoats(int[] arr, int limit) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        Arrays.sort(arr);
        if (arr[n - 1] > limit) {
            return -1;
        }
        int leftR  =-1;
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] <= (limit / 2)) {
                leftR = i;
                break;
            }
        }
        if (leftR == -1) {
            return n;
        }
        int l = leftR, r = leftR + 1;
        int leftUnused = 0;
        while (l >= 0) {
            int rightUsed = 0;
            while (r < n && arr[l] + arr[r] <= limit) {
                r++;
                rightUsed++;
            }
            if (rightUsed == 0) {
                leftUnused++;
                l--;
            } else {
                l = Math.max(-1, l - rightUsed);
            }
        }
        int leftAll = leftR + 1;
        int leftUsed = leftAll - leftUnused;
        int rightUnused = (n - leftAll) - leftUsed;
        return leftUsed + ((leftUnused + 1) >> 1) + rightUnused;
    }

    //

    public static int numRescueBoats2(int[] arr, int limit) {
        Arrays.sort(arr);
        int l = 0, r = arr.length - 1;
        int ans = 0, sum = 0;
        while (l <= r) {
            sum = l == r ? arr[l] : arr[l] + arr[r];
            if (sum > limit) {
                r--;
            } else {
                l++;
                r--;
            }
            ans++;
        }
        return ans;
    }
}
