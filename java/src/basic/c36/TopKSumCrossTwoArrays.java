package basic.c36;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

// 返回topK两数累加和, 数分别来自两个有序数组
public class TopKSumCrossTwoArrays {
    public static class Node {
        public int i1;
        public int i2;
        public int sum;
        public Node(int idx1, int idx2, int s) {
            i1 = idx1;
            i2 = idx2;
            sum = s;
        }
    }

    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o2.sum - o1.sum;
        }
    }

    public static int[] topK(int[] arr1, int[] arr2, int k) {
        if (arr1 == null || arr2 == null || k < 1) {
            return null;
        }
        k = Math.min(k, arr1.length * arr2.length);
        int[] res = new int[k];
        int resIdx = 0;
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        boolean[][] set = new boolean[arr1.length][arr2.length];
        int i1 = arr1.length - 1;
        int i2 = arr2.length - 1;
        heap.add(new Node(i1, i2, arr1[i1] + arr2[i2]));
        set[i1][i2] = true;
        while (resIdx != k) {
            Node cur = heap.poll();
            res[resIdx++] = cur.sum;
            i1 = cur.i1;
            i2 = cur.i2;
            if (i1 - 1 >= 0 && !set[i1 - 1][i2]) {
                set[i1 - 1][i2] = true;
                heap.add(new Node(i1 - 1, i2, arr1[i1 - 1] + arr2[i2]));
            }
            if (i2 - 1 >= 0 && !set[i1][i2 - 1]) {
                set[i1][i2 - 1] = true;
                heap.add(new Node(i1, i2 - 1, arr1[i1] + arr2[i2 - 1]));
            }
        }
        return res;
    }

    //

    public static int[] topKSure(int[] arr1, int[] arr2, int k) {
        int[] all = new int[arr1.length * arr2.length];
        int idx = 0;
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
                all[idx++] = arr1[i] + arr2[j];
            }
        }
        Arrays.sort(all);
        int[] res = new int[Math.min(k, all.length)];
        idx = all.length - 1;
        for (int i = 0; i < res.length; i++) {
            res[i] = all[idx--];
        }
        return res;
    }

    //

    private static int[] randomSortedArr(int len) {
        int[] res = new int[len];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (50000 * Math.random()) + 1;
        }
        Arrays.sort(res);
        return res;
    }

    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1 == null ^ arr2 == null) {
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
        int a1len = 5000;
        int a2len = 4000;
        int k = 2000000;
        int[] arr1 = randomSortedArr(a1len);
        int[] arr2 = randomSortedArr(a2len);
        long start = System.currentTimeMillis();
        int[] ans1 = topK(arr1, arr2, k);
        long end = System.currentTimeMillis();
        System.out.println(end - start + " ms");
        start = System.currentTimeMillis();
        int[] ans2 = topKSure(arr1, arr2, k);
        end = System.currentTimeMillis();
        System.out.println(end - start + " ms");
        System.out.println(isEqual(ans1, ans2));
    }
}
