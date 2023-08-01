package leetc.top;

public class P328_OddEvenLinkedList {
    public static class ListNode {
        int val;
        ListNode next;
    }
    public ListNode oddEvenList(ListNode head) {
        ListNode firstOdd = null, firstEven = null, odd = null, even = null;
        int count = 1;
        while (head != null) {
            ListNode next = head.next;
            head.next = null;
            if ((count & 1) == 1) {
                firstOdd = firstOdd == null ? head : firstOdd;
                if (odd != null) {
                    odd.next = head;
                }
                odd = head;
            } else {
                firstEven = firstEven == null ? head : firstEven;
                if (even != null) {
                    even.next = head;
                }
                even = head;
            }
            count++;
            head = next;
        }
        if (odd != null) {
            odd.next = firstEven;
        }
        return firstOdd != null ? firstOdd : firstEven;
    }
}
