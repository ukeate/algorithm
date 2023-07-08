package basic.c44;

// 约瑟夫环问题
// 数到m就杀死节点，求活下来节点的最初编号
public class Josephus {
    public static class Node {
        public int val;
        public Node next;

        public Node(int v) {
            val = v;
        }
    }

    public static Node live1(Node head, int m) {
        if (head == null || head.next == head || m < 1) {
            return head;
        }
        Node last = head;
        while (last.next != head) {
            last = last.next;
        }
        int cnt = 0;
        while (head != last) {
            if (++cnt == m) {
                last.next = head.next;
                cnt = 0;
            } else {
                last = last.next;
            }
            head = last.next;
        }
        return head;
    }

    //

    // 旧号 = (新号 + 被杀号 - 1) % 队长i + 1
    // 被杀号 = (数m - 1) % 队长i + 1
    // 旧号 = (新号 + (m - 1) % i) % i + 1 = (新号 + m - 1) % i + 1
    private static int liveIdx(int i, int m) {
        if (i == 1) {
            return 1;
        }
        return (liveIdx(i - 1, m) + m - 1) % i + 1;
    }

    public static Node live2(Node head, int m) {
        if (head == null || head.next == head || m < 1) {
            return head;
        }
        Node cur = head.next;
        int size = 1;
        while (cur != head) {
            size++;
            cur = cur.next;
        }
        int li = liveIdx(size, m);
        while (--li > 0) {
            head = head.next;
        }
        head.next = head;
        return head;
    }

    //

    private static void print(Node head) {
        if (head == null) {
            return;
        }
        System.out.print("list: " + head.val + " ");
        Node cur = head.next;
        while (cur != head) {
            System.out.print(cur.val + " ");
            cur = cur.next;
        }
        System.out.println("-> " + head.val);
    }

    public static void main(String[] args) {
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = head1;
        print(head1);
        head1 = live1(head1, 3);
        print(head1);

        Node head2 = new Node(1);
        head2.next = new Node(2);
        head2.next.next = new Node(3);
        head2.next.next.next = new Node(4);
        head2.next.next.next.next = new Node(5);
        head2.next.next.next.next.next = head2;
        print(head2);
        head2 = live2(head2, 3);
        print(head2);

    }
}
