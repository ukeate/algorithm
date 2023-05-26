package base.array;

public class RangeSum {
    public static class PreSum {
        private int[] preSum;

        public PreSum(int[] arr) {
            if (arr == null || arr.length < 1) {
                return;
            }

            int n = arr.length;
            this.preSum = new int[n];
            preSum[0] = arr[0];
            for (int i = 1; i < n; i++) {
                preSum[i] = preSum[i - 1] + arr[i];
            }
        }

        public int rangeSum(int l, int r) {
            if (this.preSum == null) {
                return 0;
            }
            return l == 0 ? preSum[r] : preSum[r] - preSum[l - 1];
        }
    }

    private static int rangeSumSure(int[] arr, int l, int r) {
        if (arr == null || l > r || arr.length - 1 < r) {
            return 0;
        }
        int sum = 0;
        for (int i = l; i <= r; i++) {
            sum += arr[i];
        }
        return sum;
    }

    private static int[] randomArr(int ms, int mv) {
        int[] arr = new int[(int) (Math.random() * (ms + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (mv + 1));
        }
        return arr;
    }

    private static boolean equal(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if ((arr1 == null ^ arr2 == null)) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int i1 = (int) (Math.random() * (arr.length));
            int i2 = (int) (Math.random() * (arr.length));
            int l = Math.min(i1, i2);
            int r = Math.max(i1, i2);
            int ans1 = rangeSumSure(arr, l, r);
            PreSum ps = new PreSum(arr);
            int ans2 = ps.rangeSum(l, r);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
