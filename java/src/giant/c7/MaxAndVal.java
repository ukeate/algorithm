package giant.c7;

// 哪两个数与的结果最大
public class MaxAndVal {
    public static int sure(int[] arr) {
        int n = arr.length;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                max = Math.max(max, arr[i] & arr[j]);
            }
        }
        return max;
    }

    //

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static int max(int[] arr) {
        int m = arr.length;
        int ans = 0;
        for (int bit = 30; bit >= 0; bit--) {
            int i = 0;
            int tmp = m;
            while (i < m) {
                if ((arr[i] & (1 << bit)) == 0) {
                    swap(arr, i, --m);
                } else {
                    i++;
                }
            }
            if (m == 2) {
                return arr[0] & arr[1];
            }
            if (m < 2) {
                m = tmp;
            } else {
                ans |= (1 << bit);
            }
        }
        return ans;
    }

    //

    private static int[] randomArr(int size, int range) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * range) + 1;
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 50;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int size = (int) (Math.random() * maxLen) + 2;
            int[] arr = randomArr(size, maxVal);
            int ans1 = sure(arr);
            int ans2 = max(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
