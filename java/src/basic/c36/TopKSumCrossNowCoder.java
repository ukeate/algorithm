package basic.c36;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

// https://www.nowcoder.com/practice/7201cacf73e7495aa5f88b223bbbf6d1
public class TopKSumCrossNowCoder {
    private static class Node {
        public int i1;
        public int i2;
        public int sum;

        public Node(int index1, int index2, int s) {
            i1 = index1;
            i2 = index2;
            sum = s;
        }
    }

    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o2.sum - o1.sum;
        }
    }

    private static long x(int i1, int i2, int m) {
        return (long) i1 * (long) m + (long) i2;
    }

    public static int[] topK(int[] arr1, int[] arr2, int k) {
        if (arr1 == null || arr2 == null || k < 1) {
            return null;
        }
        int n = arr1.length;
        int m = arr2.length;
        k = Math.min(k, n * m);
        int[] res = new int[k];
        int resIdx = 0;
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        HashSet<Long> set = new HashSet<>();
        int i1 = n - 1;
        int i2 = m - 1;
        heap.add(new Node(i1, i2, arr1[i1] + arr2[i2]));
        set.add(x(i1, i2, m));
        while (resIdx < k) {
            Node cur = heap.poll();
            res[resIdx++] = cur.sum;
            i1 = cur.i1;
            i2 = cur.i2;
            set.remove(x(i1, i2, m));
            if (i1 - 1 >= 0 && !set.contains(x(i1 - 1, i2, m))) {
                set.add(x(i1 - 1, i2, m));
                heap.add(new Node(i1 - 1, i2, arr1[i1 - 1] + arr2[i2]));
            }
            if (i1 - 1 >= 0 && !set.contains(x(i1, i2 - 1, m))) {
                set.add(x(i1, i2 - 1, m));
                heap.add(new Node(i1, i2 - 1, arr1[i1] + arr2[i2 - 1]));
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] arr1 = new int[n];
        int[] arr2 = new int[n];
        for (int i = 0; i < n; i++) {
            arr1[i] = sc.nextInt();
        }
        for (int i = 0; i < n; i++) {
            arr2[i] = sc.nextInt();
        }
        int[] topK = topK(arr1, arr2, k);
        for (int i = 0; i < k; i++) {
            System.out.print(topK[i] + " ");
        }
        System.out.println();
        sc.close();
    }
}
