package base.str2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree {

    public static HashMap<Character, Integer> countMap(String str) {
        HashMap<Character, Integer> ans = new HashMap<>();
        char[] s = str.toCharArray();
        for (char cha : s) {
            if (!ans.containsKey(cha)) {
                ans.put(cha, 1);
            } else {
                ans.put(cha, ans.get(cha) + 1);
            }
        }
        return ans;
    }

    private static class Node {
        public int count;
        public Node left;
        public Node right;

        public Node(int c) {
            count = c;
        }
    }

    private static class NodeComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.count - o2.count;
        }
    }

    private static void fill(Node head, String pre, HashMap<Node, Character> nodes, HashMap<Character, String> ans) {
        if (nodes.containsKey(head)) {
            ans.put(nodes.get(head), pre);
        } else {
            fill(head.left, pre + "0", nodes, ans);
            fill(head.right, pre + "1", nodes, ans);
        }
    }

    // 传入词频表
    public static HashMap<Character, String> createHuffman(HashMap<Character, Integer> countMap) {
        HashMap<Character, String> ans = new HashMap<>();
        // 一个字符特殊处理为0，因为不能满叶子节点
        if (countMap.size() == 1) {
            for (char key : countMap.keySet()) {
                ans.put(key, "0");
            }
            return ans;
        }
        HashMap<Node, Character> nodes = new HashMap<>();
        PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComp());
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            Node cur = new Node(entry.getValue());
            char cha = entry.getKey();
            nodes.put(cur, cha);
            heap.add(cur);
        }
        while (heap.size() > 1) {
            Node a = heap.poll();
            Node b = heap.poll();
            Node h = new Node(a.count + b.count);
            h.left = a;
            h.right = b;
            heap.add(h);
        }
        Node head = heap.poll();
        fill(head, "", nodes, ans);
        return ans;
    }

    public static String encode(String str, HashMap<Character, String> huffman) {
        char[] s = str.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char cha : s) {
            builder.append(huffman.get(cha));
        }
        return builder.toString();
    }

    private static class TrieNode {
        public char val;
        public TrieNode[] nexts;
        public TrieNode() {
            val = 0;
            nexts = new TrieNode[2];
        }
    }

    private static TrieNode createTrie(HashMap<Character, String> huffman) {
        TrieNode root = new TrieNode();
        for (char key : huffman.keySet()) {
            char[] path = huffman.get(key).toCharArray();
            TrieNode cur = root;
            for (int i = 0; i < path.length; i++) {
                int idx = path[i] == '0' ? 0 : 1;
                if (cur.nexts[idx] == null) {
                    cur.nexts[idx] = new TrieNode();
                }
                cur = cur.nexts[idx];
            }
            cur.val = key;
        }
        return root;
    }

    public static String decode(String raw, HashMap<Character, String> huffman) {
        TrieNode root = createTrie(huffman);
        TrieNode cur = root;
        char[] encode = raw.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < encode.length; i++) {
            int idx = encode[i] == '0' ? 0 : 1;
            cur = cur.nexts[idx];
            if (cur.nexts[0] == null && cur.nexts[1] == null) {
                builder.append(cur.val);
                cur = root;
            }
        }
        return builder.toString();
    }

    //

    private static String randomStr(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (range) * Math.random() + 'a');
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('A', 60);
		map.put('B', 45);
		map.put('C', 13);
		map.put('D', 69);
		map.put('E', 14);
		map.put('F', 5);
		map.put('G', 3);
        HashMap<Character, String> huffman = createHuffman(map);
        for (Map.Entry<Character, String> entry : huffman.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("======");
        String str = "CBBBAABBACAABDDEFBA";
        System.out.println(str);
        HashMap<Character, Integer> countMap = countMap(str);
        HashMap<Character, String> hf = createHuffman(countMap);
        String encode = encode(str, hf);
        System.out.println(encode);
        String decode = decode(encode, hf);
        System.out.println(decode);
        System.out.println("==========");
        System.out.println("大样本测试");
        int times = 100000;
        int maxLen = 500;
        int range = 26;
        for (int i = 0; i < times; i++) {
            int n = (int) ((maxLen) * Math.random()) + 1;
            String test = randomStr(n, range);
            HashMap<Character, Integer> counts = countMap(test);
            HashMap<Character, String> hf2 = createHuffman(counts);
            String encode2 = encode(test, hf2);
            String decode2 = decode(encode2, hf2);
            if (!test.equals(decode2)) {
                System.out.println("Wrong");
                System.out.println(test);
                System.out.println(encode);
                System.out.println(decode);
                break;
            }
        }
        System.out.println("大样本测试结束");
    }
}
