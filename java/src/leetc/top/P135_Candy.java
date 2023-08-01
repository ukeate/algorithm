package leetc.top;

public class P135_Candy {
    public static int candy1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        int[] left = new int[n];
        for (int i= 1; i < n; i++) {
            if (arr[i - 1] < arr[i]) {
                left[i] = left[i - 1] + 1;
            }
        }
        int[] right = new int[n];
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {
                right[i] = right[i + 1] + 1;
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans += Math.max(left[i], right[i]);
        }
        return ans + n;
    }

    //

    private static int nextMinIdx2(int[] arr, int start) {
        for (int i = start; i < arr.length - 1; i++) {
            if (arr[i] <= arr[i + 1]) {
                return i;
            }
        }
        return arr.length - 1;
    }

    private static int rightCands(int[] arr, int left, int right) {
        int n = right - left + 1;
        return n + n * (n - 1) / 2;
    }

    public static int candy2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int idx = nextMinIdx2(arr, 0);
        int res = rightCands(arr, 0, idx++);
        int lbase = 1, next = 0, rcands = 0, rbase = 0;
        while (idx != arr.length) {
            if (arr[idx] > arr[idx - 1]) {
                res += ++lbase;
                idx++;
            } else if (arr[idx] < arr[idx - 1]) {
                next = nextMinIdx2(arr, idx - 1);
                rcands = rightCands(arr, idx - 1, next++);
                rbase = next - idx + 1;
                res += rcands + (rbase > lbase ? -lbase : -rbase);
                lbase = 1;
                idx = next;
            } else {
                res += 1;
                lbase = 1;
                idx++;
            }
        }
        return res;
    }

}
