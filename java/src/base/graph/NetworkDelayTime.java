package base.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

// https://leetcode.com/problems/network-delay-time
public class NetworkDelayTime {

    public static int networkDelayTime1(int[][] times, int n, int k) {
        ArrayList<ArrayList<int[]>> nexts = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            nexts.add(new ArrayList<>());
        }
        for (int[] delay : times) {
            nexts.get(delay[0]).add(new int[]{delay[1], delay[2]});
        }
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.add(new int[]{k, 0});
        boolean[] used = new boolean[n + 1];
        int num = 0;
        int max = 0;
        while (!heap.isEmpty() && num < n) {
            int[] record = heap.poll();
            int cur = record[0];
            int delay = record[1];
            if (used[cur]) {
                continue;
            }
            used[cur] = true;
            num++;
            max = Math.max(max, delay);
            for (int[] next : nexts.get(cur)) {
                if (!used[next[0]]) {
                    heap.add(new int[]{next[0], delay + next[1]});
                }
            }
        }
        return num < n ? -1 : max;
    }

    //

    public static class Heap {
        public boolean[] used;
        public int[][] heap;
        public int[] hIdx;
        public int size;

        public Heap(int n) {
            used = new boolean[n + 1];
            heap = new int[n + 1][2];
            hIdx = new int[n + 1];
            Arrays.fill(hIdx, -1);
        }

        private void swap(int i, int j) {
            int[] o1 = heap[i];
            int[] o2 = heap[j];
            int o1hi = hIdx[o1[0]];
            int o2hi = hIdx[o2[0]];
            heap[i] = o2;
            heap[j] = o1;
            hIdx[o1[0]] = o2hi;
            hIdx[o2[0]] = o1hi;
        }

        private void heapInsert(int i) {
            int parent = (i - 1) / 2;
            while (heap[i][1] < heap[parent][1]) {
                swap(i, parent);
                i = parent;
                parent = (i - 1) / 2;
            }
        }

        private void heapify(int i) {
            int l = (i * 2) + 1;
            while (l < size) {
                int small = l + 1 < size && heap[l + 1][1] < heap[l][1] ? (l + 1) : l;
                if (heap[i][1] < heap[small][1]) {
                    break;
                }
                swap(small, i);
                i = small;
                l = (i * 2) + 1;
            }
        }

        public void add(int cur, int delay) {
            if (used[cur]) {
                return;
            }
            if (hIdx[cur] == -1) {
                // add
                heap[size][0] = cur;
                heap[size][1] = delay;
                hIdx[cur] = size;
                heapInsert(size++);
            } else {
                // update
                int hi = hIdx[cur];
                if (delay <= heap[hi][1]) {
                    heap[hi][1] = delay;
                    heapInsert(hi);
                }
            }
        }

        public int[] poll() {
            int[] ans = heap[0];
            swap(0, --size);
            heapify(0);
            used[ans[0]] = true;
            hIdx[ans[0]] = -1;
            return ans;
        }

        public boolean isEmpty() {
            return size == 0;
        }
    }

    public static int networkDelayTime2(int[][] times, int n, int k) {
        ArrayList<ArrayList<int[]>> nexts = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            nexts.add(new ArrayList<>());
        }
        for (int[] delay : times) {
            nexts.get(delay[0]).add(new int[]{delay[1], delay[2]});
        }
        Heap heap = new Heap(n);
        heap.add(k, 0);
        int num = 0;
        int max = 0;
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int cur = record[0];
            int delay = record[1];
            num++;
            max = Math.max(max, delay);
            for (int[] next : nexts.get(cur)) {
                heap.add(next[0], delay + next[1]);
            }
        }
        return num < n ? -1 : max;
    }
}
