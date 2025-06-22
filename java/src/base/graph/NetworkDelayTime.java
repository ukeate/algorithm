package base.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 网络延迟时间问题
 * LeetCode 743: https://leetcode.com/problems/network-delay-time
 * 
 * 问题描述：
 * 有n个网络节点，从节点k发送信号，求信号到达所有节点的最短时间
 * 如果不能到达所有节点，返回-1
 * 
 * 解法：使用Dijkstra算法求单源最短路径
 */
public class NetworkDelayTime {

    /**
     * 方法1：使用系统优先队列实现Dijkstra算法
     * @param times 边的信息数组，times[i] = [u, v, w] 表示从u到v的边权重为w
     * @param n 节点数量
     * @param k 起始节点
     * @return 信号到达所有节点的最短时间，如果不能到达所有节点返回-1
     */
    public static int networkDelayTime1(int[][] times, int n, int k) {
        // 构建邻接表
        ArrayList<ArrayList<int[]>> nexts = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            nexts.add(new ArrayList<>());
        }
        for (int[] delay : times) {
            nexts.get(delay[0]).add(new int[]{delay[1], delay[2]});
        }
        
        // 使用优先队列实现Dijkstra算法
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.add(new int[]{k, 0});  // [节点, 距离]
        boolean[] used = new boolean[n + 1];  // 标记已访问的节点
        int num = 0;   // 已访问的节点数
        int max = 0;   // 最大延迟时间
        
        while (!heap.isEmpty() && num < n) {
            int[] record = heap.poll();
            int cur = record[0];    // 当前节点
            int delay = record[1];  // 到当前节点的延迟时间
            
            if (used[cur]) {
                continue;  // 跳过已访问的节点
            }
            
            used[cur] = true;
            num++;
            max = Math.max(max, delay);  // 更新最大延迟时间
            
            // 更新邻接节点的距离
            for (int[] next : nexts.get(cur)) {
                if (!used[next[0]]) {
                    heap.add(new int[]{next[0], delay + next[1]});
                }
            }
        }
        return num < n ? -1 : max;  // 如果不能到达所有节点返回-1
    }

    //

    /**
     * 手工实现的堆，支持节点距离的动态更新
     * 相比系统优先队列，避免了重复插入同一节点的问题
     */
    public static class Heap {
        public boolean[] used;   // 标记节点是否已被访问
        public int[][] heap;     // 堆数组，存储[节点, 距离]
        public int[] hIdx;       // 节点在堆中的位置索引
        public int size;         // 堆的大小

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

        /**
         * 添加新节点或更新现有节点的距离
         * @param cur 节点编号
         * @param delay 到该节点的延迟时间
         */
        public void add(int cur, int delay) {
            if (used[cur]) {
                return;  // 已访问的节点不再处理
            }
            if (hIdx[cur] == -1) {
                // 添加新节点
                heap[size][0] = cur;
                heap[size][1] = delay;
                hIdx[cur] = size;
                heapInsert(size++);
            } else {
                // 更新现有节点的距离（如果新距离更短）
                int hi = hIdx[cur];
                if (delay <= heap[hi][1]) {
                    heap[hi][1] = delay;
                    heapInsert(hi);  // 向上调整堆
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
