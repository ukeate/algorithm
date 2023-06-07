package base.link;

import java.util.ArrayList;
import java.util.List;

public class Reverse {

    static class Node {
        public Node next;
        public int val;

        public Node(int val) {
            this.val = val;
        }
    }

    static class DuoNode {
        public DuoNode last;
        public DuoNode next;
        public int val;

        public DuoNode(int val) {
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

    public static DuoNode reverseDuo(DuoNode head) {
        DuoNode pre = null;
        DuoNode next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            head.last = next;
            pre = head;
            head = next;
        }
        return pre;
    }

    private static Node reverseSure(Node head) {
        if (head == null) {
            return null;
        }
        List<Node> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        list.get(0).next = null;
        int n = list.size();
        for (int i = 1; i < n; i++) {
            list.get(i).next = list.get(i - 1);
        }
        return list.get(n - 1);
    }

    private static DuoNode reverseDuoList(DuoNode head) {
        if (head == null) {
            return null;
        }
        List<DuoNode> list = new ArrayList<>();
        while (head != null) {
            list.add(head);
            head = head.next;
        }
        list.get(0).next = null;
        DuoNode pre = list.get(0);
        int n = list.size();
        for (int i = 1; i < n; i++) {
            DuoNode cur = list.get(i);
            cur.last = null;
            cur.next = pre;
            pre.last = cur;
            pre = cur;
        }
        return list.get(n - 1);
    }

    private static Node randomList(int maxLen, int maxVal) {
        int size = (int) ((maxLen + 1) * Math.random());
        if (size == 0) {
            return null;
        }
        Node head = new Node((int) ((maxVal + 1) * Math.random()));
        Node pre = head;
        size--;
        while (size != 0) {
            Node cur = new Node((int) ((maxVal + 1) * Math.random()));
            pre.next = cur;
            pre = cur;
            size--;
        }
        return head;
    }

    private static DuoNode randomDuoList(int maxLen, int maxVal) {
        int size = (int) ((maxLen + 1) * Math.random());
        if (size == 0) {
            return null;
        }
        DuoNode head = new DuoNode((int) ((maxVal + 1) * Math.random()));
        DuoNode pre = head;
        while (size != 0) {
            DuoNode cur = new DuoNode((int) ((maxVal + 1) * Math.random()));
            cur.last = pre;
            pre.next = cur;
            pre = cur;
            size--;

        }
        return head;
    }

    private static List<Integer> getOrder(Node head) {
        List<Integer> ans = new ArrayList<>();
        while (head != null) {
            ans.add(head.val);
            head = head.next;
        }
        return ans;
    }

    private static List<Integer> getDuoOrder(DuoNode head) {
        List<Integer> ans = new ArrayList<>();
        while (head != null) {
            ans.add(head.val);
            head = head.next;
        }
        return ans;
    }

    private static boolean checkReverse(List<Integer> ori, Node head) {
        for (int i = ori.size() - 1; i >= 0; i--) {
            if (!ori.get(i).equals(head.val)) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    private static boolean checkDuoReverse(List<Integer> ori, DuoNode head) {
        DuoNode end = null;
        for (int i = ori.size() - 1; i >= 0; i--) {
            if (!ori.get(i).equals(head.val)) {
                return false;
            }
            end = head;
            head = head.next;
        }
        for (int i = 0; i < ori.size(); i++) {
            if (!ori.get(i).equals(end.val)) {
                return false;
            }
            end = end.last;
        }
        return true;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 50;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Node node1 = randomList(maxLen, maxVal);
            List<Integer> list1 = getOrder(node1);
            node1 = reverse(node1);
            if (!checkReverse(list1, node1)) {
                System.out.println("Wrong1");
            }

            Node node2 = randomList(maxLen, maxVal);
            List<Integer> list2 = getOrder(node2);
            node2 = reverseSure(node2);
            if (!checkReverse(list2, node2)) {
                System.out.println("Wrong2");
            }

            DuoNode node3 = randomDuoList(maxLen, maxVal);
            List<Integer> list3 = getDuoOrder(node3);
            node3 = reverseDuo(node3);
            if (!checkDuoReverse(list3, node3)) {
                System.out.println("Wrong3");
            }

            DuoNode node4 = randomDuoList(maxLen, maxVal);
            List<Integer> list4 = getDuoOrder(node4);
            node4 = reverseDuo(node4);
            if (!checkDuoReverse(list4, node4)) {
                System.out.println("Wrong4");
            }
        }
        System.out.println("test end");
    }
}
