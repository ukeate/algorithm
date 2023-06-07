package base.sort;

import java.util.ArrayList;
import java.util.Comparator;

// 双向链表快排
public class QuickSortLink {
    public static class Node {
        public int val;
        public Node last;
        public Node next;

        public Node(int v) {
            this.val = v;
        }
    }

    public static class HeadTail {
        public Node h;
        public Node t;

        public HeadTail(Node head, Node tail) {
            this.h = head;
            this.t = tail;
        }
    }

    public static class Info {
        public Node lh;
        public Node lt;
        public int ls;
        public Node rh;
        public Node rt;
        public int rs;
        public Node eh;
        public Node et;

        public Info(Node lh, Node lt, int ls, Node rh, Node rt, int rs, Node eh, Node et) {
            this.lh = lh;
            this.lt = lt;
            this.ls = ls;
            this.rh = rh;
            this.rt = rt;
            this.rs = rs;
            this.eh = eh;
            this.et = et;
        }
    }

    public static Info partition(Node l, Node p) {
        Node lh = null, lt = null, rh = null, rt = null, eh = p, et = p;
        int ls = 0, rs = 0;
        Node nxt = null;
        while (l != null) {
            nxt = l.next;
            l.next = null;
            l.last = null;
            if (l.val < p.val) {
                ls++;
                if (lh == null) {
                    lh = l;
                    lt = l;
                } else {
                    lt.next = l;
                    l.last = lt;
                    lt = l;
                }
            } else if (l.val > p.val) {
                rs++;
                if (rh == null) {
                    rh = l;
                    rt = l;
                } else {
                    rt.next = l;
                    l.last = rt;
                    rt = l;
                }
            } else {
                et.next = l;
                l.last = et;
                et = l;
            }
            l = nxt;
        }
        return new Info(lh, lt, ls, rh, rt, rs, eh, et);
    }

    public static HeadTail process(Node l, Node r, int n) {
        if (l == null) {
            return null;
        }
        if (l == r) {
            return new HeadTail(l, r);
        }
        int randIdx = (int) (n * Math.random());
        Node p = l;
        while (randIdx-- != 0) {
            p = p.next;
        }
        if (p == l || p == r) {
            if (p == l) {
                l = p.next;
                l.last = null;
            } else {
                p.last.next = null;
            }
        } else {
            p.last.next = p.next;
            p.next.last = p.last;
        }
        p.last = null;
        p.next = null;
        Info info = partition(l, p);
        HeadTail lht = process(info.lh, info.lt, info.ls);
        HeadTail rht = process(info.rh, info.rt, info.rs);
        if (lht != null) {
            lht.t.next = info.eh;
            info.eh.last = lht.t;
        }
        if (rht != null) {
            info.et.next = rht.h;
            rht.h.last = info.et;
        }
        Node h = lht != null ? lht.h : info.eh;
        Node t = rht != null ? rht.t : info.et;
        return new HeadTail(h, t);
    }

    public static Node quickSort(Node h) {
        if (h == null) {
            return null;
        }
        int n = 0;
        Node c = h;
        Node e = null;
        while (c != null) {
            n++;
            e = c;
            c = c.next;
        }
        return process(h, e, n).h;
    }

    // for test

    private static class NodeComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.val - o2.val;
        }
    }

    private static Node sortSure(Node head) {
        if (head == null) {
            return null;
        }
        ArrayList<Node> arr = new ArrayList<>();
        while (head != null) {
            arr.add(head);
            head = head.next;
        }
        arr.sort(new NodeComp());
        Node h = arr.get(0);
        h.last = null;
        Node p = h;
        for (int i = 1; i < arr.size(); i++) {
            Node c = arr.get(i);
            p.next = c;
            c.last = p;
            c.next = null;
            p = c;
        }
        return h;
    }

    private static Node randomLink(int n, int maxV) {
        if (n == 0) {
            return null;
        }
        Node[] arr = new Node[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Node((int) ((maxV + 1) * Math.random()));
        }
        Node head = arr[0];
        Node pre = head;
        for (int i = 1; i < n; i++) {
            pre.next = arr[i];
            arr[i].last = pre;
            pre = arr[i];
        }
        return head;
    }

    private static Node cloneLink(Node head) {
        if (head == null) {
            return null;
        }
        Node h = new Node(head.val);
        Node p = h;
        head = head.next;
        while (head != null) {
            Node c = new Node(head.val);
            p.next = c;
            c.last = p;
            p = c;
            head = head.next;
        }
        return h;
    }

    private static String link2String(Node head) {
        Node cur = head;
        Node end = null;
        StringBuilder builder = new StringBuilder();
        while (cur != null) {
            builder.append(cur.val + " ");
            end = cur;
            cur = cur.next;
        }
        builder.append("| ");
        while (end != null) {
            builder.append(end.val + " ");
            end = end.last;
        }
        return builder.toString();
    }

    private static boolean isEqual(Node h1, Node h2) {
        return link2String(h1).equals(link2String(h2));
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 500;
        int maxVal = 500;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int size = (int) ((maxLen + 1) * Math.random());
            Node head1 = randomLink(size, maxVal);
            Node head2 = cloneLink(head1);
            Node sort1 = quickSort(head1);
            Node sort2 = sortSure(head2);
            if (!isEqual(sort1, sort2)) {
                System.out.println("Wrong");
                break;
            }

        }
        System.out.println("test end");
    }
}
