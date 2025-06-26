package leetc.top;

/**
 * LeetCode 142. 环形链表 II (Linked List Cycle II)
 * 
 * 问题描述：
 * 给定一个链表的头节点 head ，返回链表开始入环的第一个节点。如果链表无环，则返回 null。
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。
 * 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。
 * 注意：pos 不作为参数进行传递。仅仅是为了标识链表的实际情况。
 * 
 * 解法思路：
 * Floyd判圈算法 + 数学推导：
 * 
 * 第一阶段：快慢指针检测环
 * - slow每次走1步，fast每次走2步
 * - 如果有环，它们必定会在环内某点相遇
 * 
 * 第二阶段：定位环入口
 * - 数学证明：当slow和fast在环中相遇时，从head和相遇点同时出发，
 *   以相同速度（每次1步）移动，会在环入口处相遇
 * 
 * 数学推导：
 * 设链表非环部分长度为a，环长度为b，相遇点距环入口距离为c
 * 当slow和fast相遇时：
 * - slow走过的距离：a + c
 * - fast走过的距离：a + c + kb（k为fast在环中多走的圈数）
 * - 由于fast速度是slow的2倍：2(a + c) = a + c + kb
 * - 化简得：a + c = kb，即a = kb - c = (k-1)b + (b-c)
 * 
 * 这意味着从head出发走a步，等于从相遇点出发走(k-1)圈再走(b-c)步，
 * 两者都会在环入口相遇。
 * 
 * 算法步骤：
 * 1. 使用快慢指针检测环的存在
 * 2. 如果存在环，重置一个指针到head
 * 3. 两个指针以相同速度移动，相遇点即为环入口
 * 
 * 时间复杂度：O(n) - 最多遍历链表两次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/linked-list-cycle-ii/
 */
public class P142_LinkedListCycleII {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        public int val;
        public ListNode next;
    }

    /**
     * 检测环并返回环的入口节点
     * 
     * @param head 链表头节点
     * @return 环的入口节点，如果无环返回null
     */
    public static ListNode detectCycle(ListNode head) {
        // 边界检查：链表长度小于3无法构成环
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        
        // 第一阶段：快慢指针检测环
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
        // 重置fast指针到链表头部
        fast = head;
        
        // 两个指针以相同速度移动，相遇点即为环入口
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        
        return slow;  // 返回环的入口节点
    }
}
