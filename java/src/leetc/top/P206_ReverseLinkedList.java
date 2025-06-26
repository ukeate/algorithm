package leetc.top;

/**
 * LeetCode 206. 反转链表 (Reverse Linked List)
 * 
 * 问题描述：
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 * 
 * 示例：
 * 输入：head = [1,2,3,4,5]
 * 输出：[5,4,3,2,1]
 * 
 * 解法思路：
 * 迭代法 + 三指针：
 * 1. 使用三个指针：pre（前驱）、node（当前）、next（后继）
 * 2. 遍历链表，逐个反转每个节点的指向
 * 3. 具体步骤：
 *    - 保存当前节点的下一个节点（next = node.next）
 *    - 让当前节点指向前驱节点（node.next = pre）
 *    - 移动前驱指针和当前指针（pre = node, node = next）
 * 4. 重复直到当前节点为空，返回pre（新的头节点）
 * 
 * 核心思想：
 * - 原链表：A -> B -> C -> D -> null
 * - 反转后：null <- A <- B <- C <- D
 * - 关键是在遍历过程中逐步改变指针方向，同时保持链表连接性
 * 
 * 算法特点：
 * - 迭代解法：相比递归更节省空间
 * - 原地操作：只改变指针指向，不需要额外空间
 * - 三指针技巧：确保在改变指向时不丢失节点
 * 
 * 时间复杂度：O(n) - 需要遍历链表一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/reverse-linked-list/
 */
public class P206_ReverseLinkedList {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        int val;         // 节点值
        ListNode next;   // 指向下一个节点的指针
    }

    /**
     * 反转链表
     * 
     * 算法步骤：
     * 1. 初始化三个指针：pre=null, node=head, next=null
     * 2. 遍历链表，对每个节点执行反转操作：
     *    - 保存下一个节点：next = node.next
     *    - 反转当前节点指向：node.next = pre
     *    - 移动指针：pre = node, node = next
     * 3. 循环结束时，pre指向新的头节点
     * 
     * 图解过程：
     * 初始：null <- pre  node -> next -> ...
     * 步骤1：保存next，让node指向pre
     * 步骤2：移动pre和node指针
     * 重复：null <- A <- B <- C    D -> ...
     *                      pre   node
     * 
     * @param node 原链表的头节点
     * @return 反转后链表的头节点
     */
    public static ListNode reverseList(ListNode node) {
        ListNode pre = null;   // 前驱指针，初始为null
        ListNode next = null;  // 后继指针，用于保存下一个节点
        
        // 遍历链表，逐个反转节点指向
        while (node != null) {
            next = node.next;  // 保存下一个节点，防止丢失
            node.next = pre;   // 反转当前节点的指向
            pre = node;        // 前驱指针向前移动
            node = next;       // 当前指针向前移动
        }
        
        // 循环结束时，pre指向反转后链表的头节点
        return pre;
    }
}
