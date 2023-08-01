package leetc.top;

import java.util.Comparator;
import java.util.PriorityQueue;

public class P378_KthSmallestElementInASortedMatrix {
    private static class Node {
        public int value;
        public int row;
        public int col;
        public Node(int v, int r, int c) {
            value = v;
            row = r;
            col = c;
        }
    }

    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.value - o2.value;
        }
    }

    public static int kthSmallest1(int[][] matrix, int k) {
        int n = matrix.length;
        int m = matrix[0].length;
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        boolean[][] set = new boolean[n][m];
        heap.add(new Node(matrix[0][0], 0, 0));
        set[0][0] = true;
        int count = 0;
        Node ans = null;
        while (!heap.isEmpty()) {
            ans = heap.poll();
            if (++count == k) {
                break;
            }
            int row = ans.row;
            int col = ans.col;
            if (row + 1 < n && !set[row + 1][col]) {
                heap.add(new Node(matrix[row + 1][col], row + 1, col));
                set[row + 1][col] = true;
            }
            if (col + 1 < m && !set[row][col + 1]) {
                heap.add(new Node(matrix[row][col + 1], row, col + 1));
                set[row][col + 1] = true;
            }
        }
        return ans.value;
    }

    //

    private static class Info {
        public int val;
        public int idx;
        public Info(int n1, int n2) {
            val = n1;
            idx = n2;
        }
    }

    private static Info lessEq(int[][] matrix, int target) {
        int val = Integer.MIN_VALUE, idx = 0;
        int n = matrix.length, m = matrix[0].length;
        int row = 0, col = m - 1;
        while (row < n && col >= 0) {
            if (matrix[row][col] <= target) {
                val = Math.max(val, matrix[row][col]);
                idx += col + 1;
                row++;
            } else {
                col--;
            }
        }
        return new Info(val, idx);
    }

    public static int kthSmallest2(int[][] matrix, int k) {
        int n = matrix.length, m = matrix[0].length;
        int left = matrix[0][0], right = matrix[n - 1][m - 1];
        int ans = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            Info info = lessEq(matrix, mid);
            if (info.idx < k) {
                left = mid + 1;
            } else {
                ans = info.val;
                right = mid - 1;
            }
        }
        return ans;
    }
}
