package leetc.top;

public class P206_ReverseLinkedList {
    public static class ListNode {
        int val;
        ListNode next;
    }

    public static ListNode reverseList(ListNode node) {
        ListNode pre = null, next = null;
        while (node != null) {
            next = node.next;
            node.next = pre;
            pre = node;
            node = next;
        }
        return pre;
    }
}
