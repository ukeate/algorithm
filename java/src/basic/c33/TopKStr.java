package basic.c33;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// 字符串数组中的topK字符串
public class TopKStr {
    private static class Node {
        public String str;
        public int times;
        public Node(String s, int t) {
            str = s;
            times = t;
        }
    }

    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.times - o2.times;
        }
    }

    public static void top(String[] arr, int topK) {
        if (arr == null || arr.length == 0 || topK < 1 || topK > arr.length) {
            return;
        }
        HashMap<String, Integer> map = new HashMap<>();
        for (String str : arr) {
            if (!map.containsKey(str)) {
                map.put(str, 1);
            } else {
                map.put(str, map.get(str) + 1);
            }
        }
        topK = Math.min(arr.length, topK);
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Node cur = new Node(entry.getKey(), entry.getValue());
            if (heap.size() < topK) {
                heap.add(cur);
            } else {
                if (heap.peek().times < cur.times) {
                    heap.poll();
                    heap.add(cur);
                }
            }
        }
        while (!heap.isEmpty()) {
            System.out.println(heap.poll().str);
        }
    }

    //

    private static String[] randomArr(int len, int maxVal) {
        String[] rst = new String[len];
        for (int i = 0; i < len; i++) {
            rst[i] = String.valueOf((int) ((maxVal + 1) * Math.random()));
        }
        return rst;
    }

    private static void print(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String[] arr1 = {"A", "B", "C", "A", "C", "B", "B", "K"};
        top(arr1, 2);
        String[] arr2 = randomArr(50, 10);
        int topK = 3;
        print(arr2);
        top(arr2, topK);
    }
}
