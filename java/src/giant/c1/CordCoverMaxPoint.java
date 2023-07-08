package giant.c1;

import java.util.Arrays;

// arr中是点的位置(有序)，给长度l的绳子，返回最多覆盖的点数
public class CordCoverMaxPoint {
    private static int nearestIdx(int[] arr, int r, int val) {
        int l = 0;
        int idx = r;
        while (l <= r) {
            int mid = l + ((r - l) >> 1);
            if (arr[mid] >= val) {
                idx = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return idx;
    }

    public static int max1(int[] arr, int l) {
        int res = 1;
        for (int i = 0; i < arr.length; i++) {
            int nearest = nearestIdx(arr, i, arr[i] - l);
            res = Math.max(res, i - nearest + 1);
        }
        return res;
    }

    //

    public static int max2(int[] arr, int l) {
        int left = 0;
        int right = 0;
        int n = arr.length;
        int max = 0;
        while (left < n) {
            while (right < n && arr[right] - arr[left] <= l) {
                right++;
            }
            max = Math.max(max, right - (left++));
        }
        return max;
    }

    //

    public static int maxSure(int[] arr, int l) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            int pre = i - 1;
            while (pre >= 0 && arr[i] - arr[pre] <= l) {
                pre--;
            }
            max = Math.max(max, i - pre);
        }
        return max;
    }

    private static int[] randomArr(int maxLen, int max) {
        int[] ans = new int[(int) (maxLen * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) ((max + 1) * Math.random());
        }
        Arrays.sort(ans);
        return ans;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 1000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int l = (int) ((maxVal + 1) * Math.random());
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = max1(arr, l);
            int ans2 = max2(arr, l);
            int ans3 = maxSure(arr, l);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
