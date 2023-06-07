package base.array;

public class RingArray {

    public static class Ring {
        private int[] arr;
        private int pushIdx;
        private int pollIdx;
        private int size;
        private final int limit;

        public Ring(int limit) {
            arr = new int[limit];
            this.limit = limit;
        }

        private int nextIdx(int i) {
            return i < limit - 1 ? i + 1 : 0;
        }

        public void push(int val) {
            if (size == limit) {
                throw new RuntimeException("fulfilled");
            }
            size++;
            arr[pushIdx] = val;
            pushIdx = nextIdx(pushIdx);
        }

        public int pop() {
            if (size == 0) {
                throw new RuntimeException("fulfilled");
            }
            size--;
            int ans = arr[pollIdx];
            pollIdx = nextIdx(pollIdx);
            return ans;
        }

        public boolean isEmpty() {
            return size == 0;
        }
    }
}
