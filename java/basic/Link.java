package basic;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Link {
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

    public static Node2 reverseDouble(Node2 head) {
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

    private static Node getKGroupEnd(Node start, int k) {
        while (--k != 0 && start != null) {
            start = start.next;
        }
        return start;
    }

    private static void reverse(Node start, Node end) {
        end = end.next;
        Node pre = null;
        Node cur = start;
        Node next = null;
        while (cur != end) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        start.next = end;
    }

    public static Node reverseKGroup(Node head, int k) {
        Node start = head;
        Node end = getKGroupEnd(start, k);
        if (end == null) {
            return head;
        }
        head = end;
        reverse(start, end);
        Node lastEnd = start;
        while (lastEnd.next != null) {
            start = lastEnd.next;
            end = getKGroupEnd(start, k);
            if (end == null) {
                return head;
            }
            reverse(start, end);
            lastEnd.next = end;
            lastEnd = start;
        }
        return head;
    }

    private static int length(Node head) {
        int len = 0;
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }

    public static Node addNumbers(Node head1, Node head2) {
        int len1 = length(head1);
        int len2 = length(head2);
        Node l = len1 >= len2 ? head1 : head2;
        Node s = l == head1 ? head2 : head1;
        Node curL = l;
        Node curS = s;
        Node last = curL;
        int carry = 0;
        int curNum = 0;
        while (curS != null) {
            curNum = curL.val + curS.val + carry;
            curL.val = (curNum % 10);
            carry = curNum / 10;
            last = curL;
            curL = curL.next;
            curS = curS.next;
        }
        while (curL != null) {
            curNum = curL.val + carry;
            curL.val = (curNum % 10);
            carry = curNum / 10;
            last = curL;
            curL = curL.next;
        }
        if (carry != 0) {
            last.next = new Node(1);
        }
        return l;
    }

    public static Node mergeLink(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return head1 == null ? head2 : head1;
        }
        Node head = head1.val <= head2.val ? head1 : head2;
        Node cur1 = head.next;
        Node cur2 = head == head1 ? head2 : head1;
        Node pre = head;
        while (cur1 != null && cur2 != null) {
            if (cur1.val <= cur2.val) {
                pre.next = cur1;
                cur1 = cur1.next;
            } else {
                pre.next = cur2;
                cur2 = cur2.next;
            }
            pre = pre.next;
        }
        pre.next = cur1 != null ? cur1 : cur2;
        return head;
    }

    //
    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.val - o2.val;
        }
    }

    public static Node mergeK(Node[] lists) {
        if (lists == null) {
            return null;
        }
        PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());
        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                heap.add(lists[i]);
            }
        }
        if (heap.isEmpty()) {
            return null;
        }
        Node head = heap.poll();
        Node pre = head;
        if (pre.next != null) {
            heap.add(pre.next);
        }
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            pre.next = cur;
            pre = cur;
            if (cur.next != null) {
                heap.add(cur.next);
            }
        }
        return head;
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
