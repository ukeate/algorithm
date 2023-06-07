package base.heap;

import java.util.Comparator;
import java.util.PriorityQueue;

public class SimpleHeap {
    public static class MyHeap {
        private int[] heap;
        private final int limit;
        private int size;

        public MyHeap(int limit) {
            heap = new int[limit];
            this.limit = limit;
        }

        private void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }

        private void heapInsert(int[] arr, int i) {
            int pi = (i - 1) / 2;
            while (arr[i] < arr[pi]) {
                swap(arr, i, pi);
                i = pi;
                pi = (i - 1) / 2;
            }
        }

        public void push(int val) {
            if (size == limit) {
                throw new RuntimeException("heap is full");
            }
            heap[size] = val;
            heapInsert(heap, size++);

        }

        private void heapify(int[] arr, int i, int n) {
            int li = i * 2 + 1;
            while (li < n) {
                int mi = li + 1 < n && arr[li + 1] < arr[li] ? li + 1 : li;
                if (arr[mi] >= arr[i]) {
                    break;
                }
                swap(arr, mi, i);
                i = mi;
                li = i * 2 + 1;
            }
        }

        public int pop() {
            int ans = heap[0];
            swap(heap, 0, --size);
            heapify(heap, 0, size);
            return ans;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == limit;
        }
    }

    private static class MinComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxVal = 1000;
        int maxLimit = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int limit = (int) ((maxLimit + 1) * Math.random());
            MyHeap heap1 = new MyHeap(limit);
            PriorityQueue<Integer> heap2 = new PriorityQueue<>(new MinComparator());
            int opTimes = (int) ((limit + 1) * Math.random());
            for (int j = 0; j < opTimes; j++) {
                if (heap1.isEmpty() != heap2.isEmpty()) {
                    System.out.println("Wrong");
                }
                if (heap1.isEmpty()) {
                    int v = (int) ((maxVal + 1) * Math.random());
                    heap1.push(v);
                    heap2.add(v);
                } else if (heap1.isFull()) {
                    if (heap1.pop() != heap2.poll()) {
                        System.out.println("Wrong2");
                    }
                } else {
                    if (Math.random() < 0.5) {
                        int v = (int) ((maxVal + 1) * Math.random());
                        heap1.push(v);
                        heap2.add(v);
                    } else {
                        int ans1 = heap1.pop();
                        int ans2 = heap2.poll();
                        if (ans1 != ans2) {
                            System.out.println(ans1 + "|" + ans2);
                            System.out.println("Wrong3");
                        }
                    }
                }
            }
        }
        System.out.println("test end");
    }
}
