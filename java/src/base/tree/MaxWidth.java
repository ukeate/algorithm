package base.tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MaxWidth {
    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            val = v;
        }
    }

    public static int maxWidth1(Node head) {
        if (head == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        HashMap<Node, Integer> levelMap = new HashMap<>();
        levelMap.put(head, 1);
        int level = 1;
        int width = 0;
        int max = 0;
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int nodeLevel = levelMap.get(node);
            if (node.left != null) {
                levelMap.put(node.left, nodeLevel + 1);
                queue.add(node.left);
            }
            if (node.right != null) {
                levelMap.put(node.right, nodeLevel + 1);
                queue.add(node.right);
            }
            if (nodeLevel == level) {
                width++;
            } else {
                max = Math.max(max, width);
                level++;
                width = 1;
            }
        }
        max = Math.max(max, width);
        return max;
    }

    public static int maxWidth2(Node head) {
        if (head == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        Node curEnd = head;
        Node nextEnd = null;
        int max = 0;
        int width = 0;
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node.left != null) {
                queue.add(node.left);
                nextEnd = node.left;
            }
            if (node.right != null) {
                queue.add(node.right);
                nextEnd = node.right;
            }
            width++;
            if (node == curEnd) {
                max = Math.max(max, width);
                width = 0;
                curEnd = nextEnd;
            }
        }
        return max;
    }

    private static Node randomLevel(int level, int maxLevel, int maxVal) {
        if(level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) ((maxVal + 1) * Math.random()));
        head.left = randomLevel(level + 1, maxLevel, maxVal);
        head.right = randomLevel(level + 1, maxLevel, maxVal);
        return head;
    }

    private static Node randomTree(int maxLevel, int maxVal) {
        return randomLevel(1, maxLevel, maxVal);
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLevel = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxVal);
            if (maxWidth1(head) != maxWidth2(head)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
