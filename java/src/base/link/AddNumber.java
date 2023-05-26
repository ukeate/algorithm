package base.link;

public class AddNumber {

    static class Node {
        public Node next;
        public int val;

        public Node(int val) {
            this.val = val;
        }
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

    public static void main(String[] args) {

    }
}
