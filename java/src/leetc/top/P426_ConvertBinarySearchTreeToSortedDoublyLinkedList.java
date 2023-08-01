package leetc.top;

public class P426_ConvertBinarySearchTreeToSortedDoublyLinkedList {
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int data) {
            this.value = data;
        }
    }

    private static class Info {
        public Node start;
        public Node end;

        public Info(Node start, Node end) {
            this.start = start;
            this.end = end;
        }
    }

    private static Info process(Node x) {
        if (x == null) {
            return new Info(null, null);
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        if (li.end != null) {
            li.end.right = x;
        }
        x.left = li.end;
        x.right = ri.start;
        if (ri.start != null) {
            ri.start.left = x;
        }
        return new Info(li.start != null ? li.start : x, ri.end != null ? ri.end : x);
    }

    public static Node treeToDoublyList(Node head) {
        if (head == null) {
            return null;
        }
        Info all = process(head);
        all.end.right = all.start;
        all.start.left = all.end;
        return all.start;
    }
}
