package basic;

public class Array {
    public static class RangeSum {
        private int[] preSum;

        public RangeSum(int[] arr) {
            int n = arr.length;
            this.preSum = new int[n];
            preSum[0] = arr[0];
            for (int i = 1; i < n; i++) {
                preSum[i] = preSum[i - 1] + arr[i];
            }
        }

        public int rangeSum(int l, int r) {
            return l == 0 ? preSum[r] : preSum[r] - preSum[l - 1];
        }
    }
}
