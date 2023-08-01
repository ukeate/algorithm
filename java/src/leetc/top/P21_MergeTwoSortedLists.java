package leetc.top;

public class P21_MergeTwoSortedLists {
    public static class ListNode {
        public int val;
        public ListNode next;
    }

    public ListNode mergeTwoLists(ListNode n1, ListNode n2) {
        if (n1 == null || n2 == null) {
            return n1 == null ? n2 : n1;
        }
        ListNode head = n1.val <= n2.val ? n1 : n2;
        ListNode cur1 = head.next;
        ListNode cur2 = head == n1 ? n2 : n1;
        ListNode pre = head;
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
}
