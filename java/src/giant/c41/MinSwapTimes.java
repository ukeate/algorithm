package giant.c41;

// 来自小红书
// 一个无序数组长度为n，所有数字都不一样，并且值都在[0...n-1]范围上
// 返回让这个无序数组变成有序数组的最小交换次数
public class MinSwapTimes {
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static int process1(int[] arr, int times) {
        boolean sorted = true;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                sorted = false;
                break;
            }
        }
        if (sorted) {
            return times;
        }
        if (times >= arr.length - 1) {
            return Integer.MAX_VALUE;
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                swap(arr, i, j);
                ans = Math.min(ans, process1(arr, times + 1));
                swap(arr, i, j);
            }
        }
        return ans;
    }

    public static int min1(int[] arr) {
        return process1(arr, 0);
    }

    //

    public static int min2(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            while (i != arr[i]) {
                swap(arr, i, arr[i]);
                ans++;
            }
        }
        return ans;
    }

    //

    private static int[] randomArr(int len) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = i;
        }
        for (int i = 0; i < len; i++) {
            swap(arr, i, (int) (Math.random() * len));
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 2000;
        int n = 6;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * n) + 1;
            int[] arr = randomArr(len);
            int ans1 = min1(arr);
            int ans2 = min2(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
