package leetc.top;

/**
 * LeetCode 2. 两数相加 (Add Two Numbers)
 * 
 * 问题描述：
 * 给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，
 * 并且每个节点只能存储一位数字。请你将两个数相加，并以相同形式返回一个表示和的链表。
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * 
 * 示例：
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[7,0,8]
 * 解释：342 + 465 = 807
 * 
 * 解法思路：
 * 模拟加法运算 + 链表构建：
 * 1. 由于数字是逆序存储，正好符合加法从低位到高位的计算顺序
 * 2. 遍历两个链表，对应位相加，处理进位
 * 3. 使用头插法构建结果链表，最后反转得到正确顺序
 * 
 * 算法步骤：
 * 1. 同时遍历两个链表，计算对应位的和
 * 2. 处理进位：sum = val1 + val2 + carry
 * 3. 当前位结果：sum % 10，进位：sum / 10
 * 4. 用头插法构建结果链表（逆序）
 * 5. 处理最后的进位
 * 6. 反转链表得到最终结果
 * 
 * 实现细节：
 * - 使用头插法：新节点插入到链表头部
 * - 处理链表长度不同的情况：短链表对应位视为0
 * - 最终进位处理：如果还有进位，需要额外添加节点
 * - 链表反转：将头插法得到的逆序链表反转为正序
 * 
 * 时间复杂度：O(max(m,n)) - m和n分别是两个链表的长度
 * 空间复杂度：O(max(m,n)) - 结果链表的空间
 * 
 * LeetCode链接：https://leetcode.com/problems/add-two-numbers/
 */
public class P2_AddTwoNumbers {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode(int value) {
            this.val = value;
        }
    }

    /**
     * 反转链表
     * 
     * @param node 链表头节点
     * @return 反转后的链表头节点
     */
    private static ListNode reverseList(ListNode node) {
        ListNode pre = null, next = null;
        while (node != null) {
            next = node.next;   // 保存下一个节点
            node.next = pre;    // 反转当前节点的指向
            pre = node;         // pre向前移动
            node = next;        // node向前移动
        }
        return pre;  // pre成为新的头节点
    }

    /**
     * 两数相加的主函数
     * 
     * @param l1 第一个数的链表表示（逆序）
     * @param l2 第二个数的链表表示（逆序）
     * @return 两数和的链表表示（逆序）
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode c1 = l1, c2 = l2;      // 遍历指针
        ListNode node = null, pre = null; // 结果链表构建指针
        int carry = 0;                    // 进位
        
        // 同时遍历两个链表
        while (c1 != null || c2 != null) {
            // 获取当前位的值，空节点视为0
            int n1 = c1 != null ? c1.val : 0;
            int n2 = c2 != null ? c2.val : 0;
            int n = n1 + n2 + carry;  // 当前位的和（包括进位）
            
            // 使用头插法构建结果链表
            pre = node;
            node = new ListNode(n % 10);  // 当前位的结果
            node.next = pre;              // 头插法
            carry = n / 10;               // 更新进位
            
            // 移动到下一位
            c1 = c1 != null ? c1.next : null;
            c2 = c2 != null ? c2.next : null;
        }
        
        // 处理最后的进位
        if (carry == 1) {
            pre = node;
            node = new ListNode(1);
            node.next = pre;
        }
        
        // 反转链表得到正确的顺序
        return reverseList(node);
    }
}
