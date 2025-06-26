package leetc.top;

/**
 * LeetCode 160. 相交链表 (Intersection of Two Linked Lists)
 * 
 * 问题描述：
 * 给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。
 * 如果两个链表不存在相交节点，返回 null 。
 * 
 * 题目数据保证整个链式结构中不存在环。
 * 注意，函数返回结果后，链表必须保持其原始结构。
 * 
 * 解法思路：
 * 长度差值法：
 * 1. 分别遍历两个链表，计算长度差值
 * 2. 同时检查两个链表的尾节点是否相同（判断是否相交）
 * 3. 让较长的链表先走差值步数，使两个指针到终点距离相等
 * 4. 然后两个指针同步向前，相遇的节点就是交点
 * 
 * 算法步骤：
 * 1. 第一次遍历：计算两链表长度差，判断是否相交
 * 2. 让长链表的指针先走 |len1-len2| 步
 * 3. 两个指针同步向前，直到相遇
 * 
 * 数学原理：
 * - 如果相交，设交点前A链表长度为a，B链表长度为b，交点后共同长度为c
 * - A链表总长：a + c，B链表总长：b + c
 * - 消除长度差后，两指针到交点的距离相等
 * 
 * 判断相交的关键：
 * - 相交的链表必然有相同的尾节点
 * - 通过比较尾节点可以提前判断是否相交
 * 
 * 时间复杂度：O(m + n) - 遍历两个链表
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/intersection-of-two-linked-lists/
 */
public class P160_IntersectionOfTwoLinkedLists {
    
    /**
     * 链表节点定义
     */
    public class ListNode {
        int val;
        ListNode next;
    }

    /**
     * 找到两个链表的交点
     * 
     * @param head1 第一个链表的头节点
     * @param head2 第二个链表的头节点
     * @return 交点节点，如果不相交返回null
     */
    public static ListNode getIntersectionNode(ListNode head1, ListNode head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        
        ListNode cur1 = head1, cur2 = head2;
        int n = 0;  // 记录两链表的长度差
        
        // 遍历第一个链表，计算长度
        while (cur1.next != null) {
            n++;
            cur1 = cur1.next;
        }
        
        // 遍历第二个链表，计算长度差
        while (cur2.next != null) {
            n--;
            cur2 = cur2.next;
        }
        
        // 检查尾节点是否相同，判断是否相交
        if (cur1 != cur2) {
            return null;  // 尾节点不同，两链表不相交
        }
        
        // 确定哪个链表更长，设置指针
        cur1 = n > 0 ? head1 : head2;  // cur1指向较长的链表
        cur2 = cur1 == head1 ? head2 : head1;  // cur2指向较短的链表
        n = Math.abs(n);  // 长度差的绝对值
        
        // 让较长链表的指针先走差值步数
        while (n != 0) {
            n--;
            cur1 = cur1.next;
        }
        
        // 两个指针同步向前，直到相遇
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        
        return cur1;  // 返回交点节点
    }
}
