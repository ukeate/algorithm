package leetc.top;

public class P2_AddTwoNumbers {
    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int value) {
            this.val = value;
        }
    }

    private static ListNode reverseList(ListNode node) {
        ListNode pre = null, next = null;
        while (node != null) {
            next = node.next;
            node.next = pre;
            pre = node;
            node = next;
        }
        return pre;
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode c1 = l1, c2 = l2;
        ListNode node = null, pre = null;
        int carry = 0;
        while (c1 != null || c2 != null) {
            int n1 = c1 != null ? c1.val : 0;
            int n2 = c2 != null ? c2.val : 0;
            int n = n1 + n2 + carry;
            pre = node;
            node = new ListNode(n % 10);
            node.next = pre;
            carry = n / 10;
            c1 = c1 != null ? c1.next : null;
            c2 = c2 != null ? c2.next : null;
        }
        if (carry == 1) {
            pre = node;
            node = new ListNode(1);
            node.next = pre;
        }
        return reverseList(node);
    }
}
