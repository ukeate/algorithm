package leetc.top;

public class P141_LinkedListCycle {
    public static class ListNode {
        int val;
        ListNode next;
    }

    private static ListNode loopNode(ListNode head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        ListNode slow = head.next;
        ListNode fast = head.next.next;
        while (slow != fast) {
            if (fast.next == null || fast.next.next == null) {
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    public static boolean hasCycle(ListNode head) {
        return loopNode(head) != null;
    }

}
