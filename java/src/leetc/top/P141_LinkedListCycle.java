package leetc.top;

/**
 * LeetCode 141. 环形链表 (Linked List Cycle)
 * 
 * 问题描述：
 * 给你一个链表的头节点 head ，判断链表中是否有环。
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。
 * 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。
 * 注意：pos 不作为参数进行传递。仅仅是为了标识链表的实际情况。
 * 
 * 解法思路：
 * Floyd判圈算法（快慢指针）：
 * 1. 使用两个指针：slow每次移动1步，fast每次移动2步
 * 2. 如果链表有环，快指针最终会追上慢指针
 * 3. 如果链表无环，快指针会先到达链表末尾（null）
 * 
 * 数学原理：
 * 假设链表有环，环的长度为C，非环部分长度为N
 * - 当slow走了N步进入环时，fast已经走了2N步
 * - fast在环中的位置为 (2N - N) % C = N % C
 * - slow和fast的相对速度为1，所以最多C步内必然相遇
 * 
 * 算法步骤：
 * 1. 初始化slow = head.next, fast = head.next.next
 * 2. 在快指针未到末尾且两指针未相遇时继续移动
 * 3. 如果fast到达末尾（null），说明无环
 * 4. 如果slow == fast，说明有环
 * 
 * 边界情况：
 * - 空链表：无环
 * - 单节点：无环
 * - 两节点：需要检查
 * 
 * 时间复杂度：O(n) - 最多遍历链表两次
 * 空间复杂度：O(1) - 只使用两个指针
 * 
 * LeetCode链接：https://leetcode.com/problems/linked-list-cycle/
 */
public class P141_LinkedListCycle {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        int val;
        ListNode next;
    }

    /**
     * 寻找环的入口节点（如果存在环）
     * 这是一个辅助方法，同时解决了环检测和环入口定位
     * 
     * @param head 链表头节点
     * @return 环的入口节点，如果无环返回null
     */
    private static ListNode loopNode(ListNode head) {
        // 边界检查：链表长度小于3无法构成环
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        
        // 第一阶段：快慢指针检测环的存在
        ListNode slow = head.next;      // 慢指针，每次走1步
        ListNode fast = head.next.next; // 快指针，每次走2步
        
        // 移动指针直到相遇或fast到达末尾
        while (slow != fast) {
            // 快指针到达末尾，说明无环
            if (fast.next == null || fast.next.next == null) {
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 第二阶段：定位环的入口
        // 数学证明：当slow和fast在环中相遇时，
        // 从head和相遇点同时以相同速度出发，会在环入口相遇
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        
        return slow;  // 返回环的入口节点
    }

    /**
     * 判断链表是否有环
     * 
     * @param head 链表头节点
     * @return 如果有环返回true，否则返回false
     */
    public static boolean hasCycle(ListNode head) {
        // 通过检查环入口节点是否存在来判断是否有环
        return loopNode(head) != null;
    }
}
