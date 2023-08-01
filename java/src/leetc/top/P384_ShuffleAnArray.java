package leetc.top;

public class P384_ShuffleAnArray {
    class Solution {
        private int[] origin;
        private int[] shuffle;
        private int n;

        public Solution(int[] nums) {
            origin = nums;
            n = nums.length;
            shuffle = new int[n];
            for (int i = 0; i < n; i++) {
                shuffle[i] = origin[i];
            }
        }

        public int[] reset() {
            return origin;
        }

        public int[] shuffle() {
            for (int i = n - 1; i >= 0; i--) {
                int r = (int) (Math.random() * (i + 1));
                int tmp = shuffle[r];
                shuffle[r] = shuffle[i];
                shuffle[i] = tmp;
            }
            return shuffle;
        }
    }
}
