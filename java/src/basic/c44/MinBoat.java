package basic.c44;

// 一船最多坐两人，且不能超过载重。已知人体重数组(有序)和船载重，求最少船数
public class MinBoat {
    public static int min(int[] arr, int limit) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        if (arr[n - 1] > limit) {
            return -1;
        }
        int lessR = -1;
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] <= (limit / 2)) {
                lessR = i;
                break;
            }
        }
        if (lessR == -1) {
            return n;
        }
        int l = lessR;
        int r = lessR + 1;
        int noUsed = 0;
        while (l >= 0) {
            int solved = 0;
            while (r < n && arr[l] + arr[r] <= limit) {
                r++;
                solved++;
            }
            if (solved == 0) {
                noUsed++;
                l--;
            } else {
                l = Math.max(-1, l - solved);
            }
        }
        int left = (noUsed + 1) / 2;
        int leftAll = lessR + 1;
        int used = leftAll - noUsed;
        int right = (n - leftAll) - used;
        return used + left + right;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5};
        int weight = 6;
        System.out.println(min(arr, weight));
    }
}
