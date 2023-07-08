package basic.c37;

// 求最短子数组长度，使数组有序
public class MinSubForSort {
    public static int min(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int min = arr[arr.length - 1];
        int noMinIdx = -1;
        for (int i = arr.length - 2; i >= 0; i--) {
            if (arr[i] > min) {
                noMinIdx = i;
            } else {
                min = arr[i];
            }
        }
        if (noMinIdx == -1) {
            return 0;
        }
        int max = arr[0];
        int noMaxIdx = -1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < max) {
                noMaxIdx = i;
            } else {
                max = arr[i];
            }
        }
        return noMaxIdx - noMinIdx + 1;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 7, 10, 11, 7, 12, 6, 7, 16, 18, 19};
        System.out.println(min(arr));
    }

}
