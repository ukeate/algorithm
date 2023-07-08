package basic.c46;

import java.util.Arrays;

// 完美洗牌，长度为2N的数组[L1,L2,..,Ln,R1,R2,..,Rn]调整为[R1,L1,R2,L2,..,Rn,Ln]
public class Shuffle {
    private static int modifyIdx2(int i, int len) {
        if (i <= len / 2) {
            return 2 * i;
        } else {
            return 2 * (i - (len / 2)) - 1;
        }
    }

    private static int modifyIdx(int i, int len) {
        return (2 * i) % (len + 1);
    }

    private static void cycles(int[] arr, int start, int len, int k) {
        // k个trigger, trigger从1开始算
        for (int i = 0, trigger = 1; i < k; i++, trigger *= 3) {
            int preVal = arr[trigger + start - 1];
            int cur = modifyIdx(trigger, len);
            while (cur != trigger) {
                int tmp = arr[cur + start - 1];
                arr[cur + start - 1] = preVal;
                preVal = tmp;
                cur = modifyIdx(cur, len);
            }
            arr[cur + start - 1] = preVal;
        }
    }

    private static void reverse(int[] arr, int l, int r) {
        while (l < r) {
            int tmp = arr[l];
            arr[l++] = arr[r];
            arr[r--] = tmp;
        }
    }

    private static void rotate(int[] arr, int l, int m, int r) {
        reverse(arr, l, m);
        reverse(arr, m + 1, r);
        reverse(arr, l, r);
    }

    private static void shuffle(int[] arr, int l, int r) {
        while (r - l + 1 > 0) {
            int len = r - l + 1;
            int base = 3;
            int k = 1;
            // 找最大k使base = 3^k <= len + 1
            while (base <= (len + 1) / 3) {
                base *= 3;
                k++;
            }
            int half = (base - 1) / 2;
            int mid = (l + r) / 2;
            rotate(arr, l + half, mid, mid + half);
            cycles(arr, l, base - 1, k);
            l = l + base - 1;
        }
    }

    // [R1,L1...Rn,Ln]
    public static void shuffle(int[] arr) {
        if (arr != null && arr.length != 0 && (arr.length & 1) == 0) {
            shuffle(arr, 0, arr.length - 1);
        }
    }

    //

    public static void wiggleShuffle(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        Arrays.sort(arr);
        if ((arr.length & 1) == 1) {
            shuffle(arr, 1, arr.length - 1);
        } else {
            shuffle(arr, 0, arr.length - 1);
            for (int i = 0; i < arr.length; i += 2) {
                int tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
            }
        }
    }

    //

    public static boolean isValidWiggle(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if ((i & 1) == 1 && arr[i] < arr[i - 1]) {
                return false;
            }
            if ((i & 1) == 0 && arr[i] > arr[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    private static int[] randomArr() {
        int len = (int) (10 * Math.random()) * 2;
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (100 * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println("test begin");
        for (int i = 0; i < 5000000; i++) {
            int[] arr = randomArr();
            wiggleShuffle(arr);
            if (!isValidWiggle(arr)) {
                System.out.println("Wrong");
                print(arr);
                break;
            }
        }
        System.out.println("test end");
    }
}
