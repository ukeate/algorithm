package basic.link;

import java.util.Comparator;
import java.util.PriorityQueue;

public class LinkMerge {

    static class Node {
        public Node next;
        public int val;

        public Node(int val) {
            this.val = val;
        }
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

    public static void main(String[] args) {

    }

}
