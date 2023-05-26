package basic.link;

public class LinkReverse {

    static class Node {
        public Node next;
        public int val;

        public Node(int val) {
            this.val = val;
        }
    }

    static class Node2 {
        public Node2 last;
        public Node2 next;
        public int val;

        public Node2(int val) {
            this.val = val;
        }
    }

    //

    public static Node reverse(Node head) {
        Node pre = null;
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }

        return pre;
    }

    public static Node2 reverseDuo(Node2 head) {
        Node2 pre = null;
        Node2 next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            head.last = next;
            pre = head;
            head = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        Node n1 = new Node(1);
        n1.next = new Node(2);
        n1.next.next = new Node(3);
        n1 = reverse(n1);
        while (n1 != null) {
            System.out.print(n1.val + " ");
            n1 = n1.next;
        }
        System.out.println();
    }
}
