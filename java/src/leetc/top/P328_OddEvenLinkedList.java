package leetc.top;

/**
 * LeetCode 328. 奇偶链表 (Odd Even Linked List)
 * 
 * 问题描述：
 * 给定单链表的头节点 head，将所有索引为奇数的节点和索引为偶数的节点分别组合在一起，
 * 然后返回重新排序的列表。
 * 
 * 第一个节点的索引被认为是奇数，第二个节点的索引为偶数，以此类推。
 * 请注意，偶数组和奇数组内部的相对顺序应该与输入时保持一致。
 * 
 * 你必须在 O(1) 的额外空间复杂度和 O(n) 的时间复杂度下解决这个问题。
 * 
 * 示例：
 * 输入：head = [1,2,3,4,5]
 * 输出：[1,3,5,2,4]
 * 解释：奇数位置的节点是 1,3,5，偶数位置的节点是 2,4
 * 
 * 输入：head = [2,1,3,5,6,4,7]
 * 输出：[2,3,6,7,1,5,4]
 * 
 * 解法思路：
 * 双指针分离 + 链表重组：
 * 
 * 1. 基本策略：
 *    - 使用两个指针分别维护奇数位置和偶数位置的链表
 *    - 遍历原链表，按位置奇偶性分别连接到对应的链表
 *    - 最后将偶数链表连接到奇数链表的末尾
 * 
 * 2. 关键步骤：
 *    - odd指针：指向当前奇数位置节点的末尾
 *    - even指针：指向当前偶数位置节点的末尾
 *    - evenHead：保存偶数链表的头节点
 *    - 交替前进：odd → odd.next.next, even → even.next.next
 * 
 * 3. 链表重组：
 *    - 奇数链表：1 → 3 → 5 → ...
 *    - 偶数链表：2 → 4 → 6 → ...
 *    - 最终连接：奇数链表末尾 → 偶数链表头部
 * 
 * 核心思想：
 * - 原地重组：不创建新节点，只修改指针连接
 * - 分而治之：将原问题分解为两个子链表的构建
 * - 相对顺序保持：奇偶子链表内部顺序与原链表一致
 * 
 * 关键技巧：
 * - 双指针同时前进：每次跳过一个节点
 * - 保存偶数头节点：用于最后的连接
 * - 空指针处理：避免访问null节点的next
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(1) - 只使用常数个额外指针
 * 
 * LeetCode链接：https://leetcode.com/problems/odd-even-linked-list/
 */
public class P328_OddEvenLinkedList {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        int val;              // 节点值
        ListNode next;        // 指向下一个节点的指针
        
        ListNode() {}
        
        ListNode(int val) {
            this.val = val;
        }
        
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
    
    /**
     * 重新排列奇偶链表
     * 
     * 算法流程：
     * 1. 初始化奇偶指针和偶数头节点
     * 2. 交替遍历链表，分别构建奇数和偶数子链表
     * 3. 将偶数链表连接到奇数链表末尾
     * 4. 返回重组后的链表头节点
     * 
     * @param head 原链表头节点
     * @return 重排后的链表头节点
     */
    public ListNode oddEvenList(ListNode head) {
        // 边界情况：空链表或只有一个节点
        if (head == null || head.next == null) {
            return head;
        }
        
        // 初始化指针
        ListNode odd = head;           // 奇数位置链表的当前末尾节点
        ListNode even = head.next;     // 偶数位置链表的当前末尾节点
        ListNode evenHead = even;      // 保存偶数链表的头节点
        
        // 遍历链表，分离奇偶位置的节点
        // 循环条件：偶数节点和其下一个节点都不为空
        while (even != null && even.next != null) {
            // 连接奇数位置的节点
            // odd当前指向位置1，要连接到位置3
            odd.next = even.next;      // 1 → 3
            odd = odd.next;            // odd移动到位置3
            
            // 连接偶数位置的节点
            // even当前指向位置2，要连接到位置4
            even.next = odd.next;      // 2 → 4
            even = even.next;          // even移动到位置4
            
            // 现在：奇数链表为 1→3→..., 偶数链表为 2→4→...
        }
        
        // 将偶数链表连接到奇数链表的末尾
        odd.next = evenHead;
        
        return head; // 返回重组后的链表头节点
    }
}
