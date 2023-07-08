package basic.c33;

import java.util.*;

// https://www.lintcode.com/problem/top-k-frequent-words-ii/description
public class TopKStrRealtime2 {
    private static class Node {
        public String str;
        public int times;

        public Node(String s, int t) {
            str = s;
            times = t;
        }
    }

    public static class HeapComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.times != o2.times ? (o1.times - o2.times) : (o2.str.compareTo(o1.str));
        }
    }

    public static class TreeComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.times != o2.times ? (o2.times - o1.times) : (o1.str.compareTo(o2.str));
        }
    }

    private int k;
    private HashMap<String, Node> strNodeMap;
    private HashMap<Node, Integer> nodeIdxMap;
    private HeapComp comp;
    private TreeSet<Node> treeSet;

    public TopKStrRealtime2(int k) {
        this.k = k;
        strNodeMap = new HashMap<>();
        nodeIdxMap = new HashMap<>();
        comp = new HeapComp();
        treeSet = new TreeSet<>(comp);
    }

    public void add(String str) {
        Node cur = null;
        int idx = -1;
        if (!strNodeMap.containsKey(str)) {
            cur = new Node(str, 1);
            strNodeMap.put(str, cur);
            nodeIdxMap.put(cur, -1);
        } else {
            cur = strNodeMap.get(str);
            if (treeSet.contains(cur)) {
                treeSet.remove(cur);
            }
            cur.times++;
            idx = nodeIdxMap.get(cur);
        }
        if (idx == -1) {
            if (treeSet.size() == k) {
                if (comp.compare(treeSet.first(), cur) < 0) {
                    treeSet.pollFirst();
                    treeSet.add(cur);
                }
            } else {
                treeSet.add(cur);
            }
        } else {
            treeSet.add(cur);
        }
    }

    public List<String> topK() {
        int n = treeSet.size();
        String[] arr = new String[n];
        Iterator<Node> it = treeSet.iterator();
        for (int i = n - 1; i >= 0; i--) {
            arr[i] = it.next().str;
        }
        return Arrays.asList(arr);
    }

    public static void main(String[] args) {
        TopKStrRealtime2 top = new TopKStrRealtime2(2);
        top.add("aaa");
        for (String s : top.topK()) {
            System.out.println(s);
        }
        System.out.println("====");
        top.add("bbb");
        top.add("bbb");
        for (String s : top.topK()) {
            System.out.println(s);
        }
        System.out.println("====");
        top.add("ccc");
        top.add("ccc");
        for (String s : top.topK()) {
            System.out.println(s);
        }
        System.out.println("====");
        top.add("ddd");
        top.add("ddd");
        top.add("ddd");
        for (String s : top.topK()) {
            System.out.println(s);
        }
        System.out.println("====");
    }
}
