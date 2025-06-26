package leetc.top;

/**
 * LeetCode 19. 删除链表的倒数第 N 个结点 (Remove Nth Node From End of List)
 * 
 * 问题描述：
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 * 
 * 进阶：你能尝试使用一趟扫描实现吗？
 * 
 * 示例：
 * 输入：head = [1,2,3,4,5], n = 2
 * 输出：[1,2,3,5]
 * 
 * 解法思路：
 * 一次遍历 + 计数器法：
 * 1. 使用一个指针遍历链表，同时维护一个计数器
 * 2. 当计数器减到0时，开始记录前驱节点
 * 3. 继续遍历，前驱指针同步移动
 * 4. 遍历结束后，前驱指针指向待删除节点的前一个节点
 * 
 * 算法核心思想：
 * - 倒数第n个节点 = 正数第(length-n+1)个节点
 * - 通过计数器n的变化来确定节点位置
 * - n的状态变化：正数 -> 0 -> 负数，每个状态有不同含义
 * 
 * 计数器状态解析：
 * - n > 0：还未到达倒数第n个位置
 * - n = 0：当前位置就是倒数第n个节点
 * - n = -1：开始记录前驱节点
 * - n < -1：前驱节点同步向前移动
 * 
 * 边界情况处理：
 * - 链表长度不足n：返回原链表
 * - 删除头节点：前驱为null，返回head.next
 * - 删除普通节点：前驱.next = 前驱.next.next
 * 
 * 算法优势：
 * - 一次遍历即可完成
 * - 不需要预先计算链表长度
 * - 空间复杂度O(1)
 * 
 * 时间复杂度：O(L) - L为链表长度，需要遍历一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/remove-nth-node-from-end-of-list/
 */
public class P19_RemoveNthNodeFromEndOfList {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        public int val;
        public ListNode next;
    }

    /**
     * 删除链表的倒数第n个节点
     * 
     * @param head 链表头节点
     * @param n 倒数第n个位置
     * @return 删除节点后的链表头节点
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode cur = head, pre = null;  // 当前节点和前驱节点
        boolean enough = false;           // 链表长度是否足够
        
        // 遍历链表，同时更新计数器和前驱指针
        while (cur != null) {
            n--;  // 计数器递减
            
            if (n <= 0) {
                if (n == 0) {
                    // n=0：当前位置是倒数第n个节点
                    enough = true;  // 标记链表长度足够
                } else if (n == -1) {
                    // n=-1：开始记录前驱节点（倒数第n个节点的前一个）
                    pre = head;
                } else {
                    // n<-1：前驱节点向前移动
                    pre = pre.next;
                }
            }
            
            cur = cur.next;  // 当前节点向前移动
        }
        
        // 检查链表长度是否足够
        if (!enough) {
            return head;  // 长度不足，返回原链表
        }
        
        // 执行删除操作
        if (pre == null) {
            // 前驱为null，说明要删除头节点
            return head.next;
        } else {
            // 删除普通节点：跳过下一个节点
            pre.next = pre.next.next;
            return head;
        }
    }
}
