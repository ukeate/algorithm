package base.link;

public class IntersectNode {
    public static class Node {
        public int val;
        public Node next;

        public Node(int v) {
            this.val = v;
        }
    }

    private static Node getLoopNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        Node slow = head.next;
        Node fast = head.next.next;
        while (slow != fast) {
            if (fast.next == null || fast.next.next == null) {
                return null;
            }
            fast = fast.next.next;
            slow = slow.next;
        }
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    private static Node noLoop(Node h1, Node h2) {
        if (h1 == null || h2 == null) {
            return null;
        }
        Node cur1 = h1;
        Node cur2 = h2;
        int n = 0;
        while (cur1.next != null) {
            n++;
            cur1 = cur1.next;
        }
        while (cur2.next != null) {
            n--;
            cur2 = cur2.next;
        }
        if (cur1 != cur2) {
            return null;
        }
        cur1 = n > 0 ? h1 : h2;
        cur2 = cur1 == h1 ? h2 : h1;
        n = Math.abs(n);
        while (n != 0) {
            n--;
            cur1 = cur1.next;
        }
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
    }

    private static Node bothLoop(Node h1, Node l1, Node h2, Node l2) {
        Node cur1 = null;
        Node cur2 = null;
        if (l1 == l2) {
            cur1 = h1;
            cur2 = h2;
            int n = 0;
            while (cur1 != l1) {
                n++;
                cur1 = cur1.next;
            }
            while (cur2 != l2) {
                n--;
                cur2 = cur2.next;
            }
            cur1 = n > 0 ? h1 : h2;
            cur2 = cur1 == h1 ? h2 : h1;
            n = Math.abs(n);
            while (n != 0) {
                n--;
                cur1 = cur1.next;
            }
            while (cur1 != cur2) {
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
            return cur1;
        } else {
            cur1 = l1.next;
            while (cur1 != l1) {
                if (cur1 == l2) {
                    return l1;
                }
                cur1 = cur1.next;
            }
            return null;
        }
    }

    public static Node intersectNode(Node h1, Node h2) {
        if (h1 == null || h2 == null) {
            return null;
        }
        Node loop1 = getLoopNode(h1);
        Node loop2 = getLoopNode(h2);
        if (loop1 == null && loop2 == null) {
            return noLoop(h1, h2);
        }
        if (loop1 != null && loop2 != null) {
            return bothLoop(h1, loop1, h2, loop2);
        }
        return null;
    }

    public static void main(String[] args) {
        // 1->2->3->4->5->6->7->null
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);

        // 0->9->8->6->7->null
        Node head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(intersectNode(head1, head2).val);

        // 1->2->3->4->5->6->7->4...
        head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);
        head1.next.next.next.next.next.next = head1.next.next.next; // 7->4

        // 0->9->8->2...
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next; // 8->2
        System.out.println(intersectNode(head1, head2).val);

        // 0->9->8->6->4->5->6..
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(intersectNode(head1, head2).val);

    }

}
