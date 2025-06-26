package leetc.top;

/**
 * LeetCode 21. 合并两个有序链表 (Merge Two Sorted Lists)
 * 
 * 问题描述：
 * 将两个升序链表合并为一个新的升序链表并返回。
 * 新链表是通过拼接给定的两个链表的所有节点组成的。
 * 
 * 示例：
 * 输入：l1 = [1,2,4], l2 = [1,3,4]
 * 输出：[1,1,2,3,4,4]
 * 
 * 解法思路：
 * 双指针归并算法：
 * 1. 选择两个链表中较小的头节点作为合并后链表的头节点
 * 2. 使用三个指针：cur1和cur2分别指向两个链表的当前待比较节点，pre指向已合并链表的尾部
 * 3. 比较cur1和cur2的值，将较小的节点连接到pre后面，并移动对应指针
 * 4. 重复步骤3直到其中一个链表为空
 * 5. 将剩余的非空链表直接连接到结果链表末尾
 * 
 * 核心思想：
 * - 利用两个链表已经有序的特性，使用双指针进行归并
 * - 不需要创建新节点，只需要调整指针连接关系
 * - 时间复杂度优于重新排序的方法
 * 
 * 算法特点：
 * - 原地操作：不需要额外空间存储新节点
 * - 线性时间：每个节点最多被访问一次
 * - 稳定性：相等元素的相对顺序保持不变
 * 
 * 时间复杂度：O(m + n) - m和n分别是两个链表的长度
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/merge-two-sorted-lists/
 */
public class P21_MergeTwoSortedLists {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        public int val;      // 节点值
        public ListNode next; // 指向下一个节点的指针
    }

    /**
     * 合并两个有序链表
     * 
     * 算法步骤：
     * 1. 处理边界情况：任一链表为空
     * 2. 选择较小头节点作为合并链表的头节点
     * 3. 初始化双指针和前驱指针
     * 4. 循环比较并连接较小的节点
     * 5. 连接剩余的非空链表
     * 
     * @param n1 第一个有序链表的头节点
     * @param n2 第二个有序链表的头节点
     * @return 合并后有序链表的头节点
     */
    public ListNode mergeTwoLists(ListNode n1, ListNode n2) {
        // 边界情况：任一链表为空，直接返回另一个链表
        if (n1 == null || n2 == null) {
            return n1 == null ? n2 : n1;
        }
        
        // 选择较小的头节点作为合并链表的头节点
        ListNode head = n1.val <= n2.val ? n1 : n2;
        
        // 初始化指针
        ListNode cur1 = head.next;                    // 指向头节点对应链表的下一个节点
        ListNode cur2 = head == n1 ? n2 : n1;        // 指向另一个链表的头节点
        ListNode pre = head;                          // 指向已合并链表的尾部
        
        // 双指针归并：比较两个链表的当前节点，连接较小的节点
        while (cur1 != null && cur2 != null) {
            if (cur1.val <= cur2.val) {
                // cur1的值较小或相等，连接cur1
                pre.next = cur1;
                cur1 = cur1.next;  // cur1向前移动
            } else {
                // cur2的值较小，连接cur2
                pre.next = cur2;
                cur2 = cur2.next;  // cur2向前移动
            }
            pre = pre.next;  // pre向前移动到新连接的节点
        }
        
        // 连接剩余的非空链表（其中一个链表已经遍历完）
        pre.next = cur1 != null ? cur1 : cur2;
        
        return head;
    }
}
