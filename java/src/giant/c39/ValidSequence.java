package giant.c39;

// 来自腾讯
// 给定一个长度为n的数组arr，求有多少个子数组满足 :
// 子数组两端的值，是这个子数组的最小值和次小值，最小值和次小值谁在最左和最右无所谓
// n<=100000（10^5） n*logn  O(N)
public class ValidSequence {
    private static int cn2(int n) {
        return (n * (n - 1)) >> 1;
    }

    public static int nums(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int[] values = new int[n];
        int[] times = new int[n];
        int size = 0;
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            while (size != 0 && values[size - 1] > arr[i]) {
                size--;
                ans += times[size] + cn2(times[size]);
            }
            if (size != 0 && values[size - 1] == arr[i]) {
                times[size - 1]++;
            } else {
                values[size] = arr[i];
                times[size++] = 1;
            }
        }
        while (size != 0) {
            ans += cn2(times[--size]);
        }
        for (int i = arr.length - 1; i >= 0; i--) {
            while (size != 0 && values[size - 1] > arr[i]) {
                ans += times[--size];
            }
            if (size != 0 && values[size - 1] == arr[i]) {
                times[size - 1]++;
            } else {
                values[size] = arr[i];
                times[size++] = 1;
            }
        }
        return ans;
    }

    //

    public static int sure(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int ans = 0;
        for (int s = 0; s < arr.length; s++) {
            for (int e = s + 1; e < arr.length; e++) {
                int max = Math.max(arr[s], arr[e]);
                boolean valid = true;
                for (int i = s + 1; i < e; i++) {
                    if (arr[i] < max) {
                        valid = false;
                        break;
                    }
                }
                ans += valid ? 1 : 0;
            }
        }
        return ans;
    }

    //

    private static int[] randomArr(int n, int v) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * v);
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 100000;
        int n = 30;
        int v = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int m = (int) (Math.random() * n);
            int[] arr = randomArr(m, v);
            int ans1 = nums(arr);
            int ans2 = sure(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
