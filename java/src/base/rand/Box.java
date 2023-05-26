package base.rand;

public class Box {

    private static class RandBox {
        private final int min;
        private final int max;

        public RandBox(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int rand() {
            return min + (int) (Math.random() * (max - min + 1));
        }

        public int rand01() {
            int size = this.max - this.min + 1;
            boolean odd = (size & 1) != 0;
            int mid = size >> 1;
            int ans = 0;
            do {
                ans = this.rand() - min;
            } while (odd && ans == mid);
            return ans < mid ? 0 : 1;
        }

        // from <= to
        public int rand(int from, int to) {
            if (from == to) {
                return from;
            }
            int range = to - from;
            int num = 1;
            while ((1 << num) - 1 < range) {
                num++;
            }
            int ans = 0;
            do {
                ans = 0;
                for (int i = 0; i < num; i++) {
                    ans |= (this.rand01() << i);
                }
            } while (ans > range);
            return ans + from;
        }
    }

    public static void main(String[] args) {

    }

}
