package base.link;

public class Partition {
    public static class Node {
        public int val;
        public Node next;

        public Node(int v) {
            this.val = v;
        }
    }

    private static void swap(Node[] arr, int a, int b) {
        Node tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    private static void arrPartition(Node[] arr, int p) {
        int small = -1;
        int big = arr.length;
        int i = 0;
        while (i < big) {
            if (arr[i].val < p) {
                swap(arr, ++small, i++);
            } else if (arr[i].val == p) {
                i++;
            } else {
                swap(arr, --big, i);
            }
        }
    }

    public static Node partition1(Node head, int p) {
        if (head == null) {
            return head;
        }
        Node cur = head;
        int i = 0;
        while (cur != null) {
            i++;
            cur = cur.next;
        }
        Node[] arr = new Node[i];
        i = 0;
        cur = head;
        for (i = 0; i < arr.length; i++) {
            arr[i] = cur;
            cur = cur.next;
        }
        arrPartition(arr, p);
        for (i = 1; i < arr.length; i++) {
            arr[i - 1].next = arr[i];
        }
        arr[i - 1].next = null;
        return arr[0];
    }

    public static Node partition2(Node head, int p) {
        Node sh = null;
        Node st = null;
        Node eh = null;
        Node et = null;
        Node bh = null;
        Node bt = null;
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = null;
            if (head.val < p) {
                if (sh == null) {
                    sh = head;
                    st = head;
                } else {
                    st.next = head;
                    st = head;
                }
            } else if (head.val == p) {
                if (eh == null) {
                    eh = head;
                    et = head;
                } else {
                    et.next = head;
                    et = head;
                }
            } else {
                if (bh == null) {
                    bh = head;
                     bt = head;
                } else {
                     bt.next = head;
                     bt = head;
                }
            }
            head = next;
        }
        // 区域间连接
        if (st != null) {
            st.next = eh;
            et = et == null ? st : et;
        }
        if (et != null) {
            et.next = bh;
        }
        return sh != null ? sh : (eh != null ? eh : bh);
    }

    private static void print(Node node) {
        while (node != null) {
            System.out.print(node.val + ",");
            node = node.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Node head1 = new Node(7);
        head1.next = new Node(9);
        head1.next.next = new Node(1);
        head1.next.next.next = new Node(8);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(2);
        head1.next.next.next.next.next.next = new Node(5);
        print(head1);
        // head1 = listPartition1(head1, 4);
        head1 = partition2(head1, 5);
        print(head1);

    }
}
