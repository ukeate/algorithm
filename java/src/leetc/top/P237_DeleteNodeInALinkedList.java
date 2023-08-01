package leetc.top;

public class P237_DeleteNodeInALinkedList {
    public static class ListNode {
        int val;
        ListNode next;
    }

    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
}
