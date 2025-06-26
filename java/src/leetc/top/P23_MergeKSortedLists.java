package leetc.top;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * LeetCode 23. 合并K个升序链表 (Merge k Sorted Lists)
 * 
 * 问题描述：
 * 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 * 
 * 示例：
 * 输入：lists = [[1,4,5],[1,3,4],[2,6]]
 * 输出：[1,1,2,3,4,4,5,6]
 * 解释：链表数组如下：
 * [
 *   1->4->5,
 *   1->3->4,
 *   2->6
 * ]
 * 将它们合并到一个有序链表中得到：1->1->2->3->4->4->5->6
 * 
 * 解法思路：
 * 优先队列（最小堆）+ 多路归并：
 * 1. 使用最小堆维护K个链表的当前最小节点
 * 2. 每次从堆中取出最小节点，加入结果链表
 * 3. 如果取出的节点有后续节点，将后续节点加入堆
 * 4. 重复直到堆为空
 * 
 * 核心思想：
 * - 多路归并：同时处理多个有序序列的合并
 * - 使用堆优化：避免每次都扫描K个链表头部找最小值
 * - 动态维护：随着链表遍历，动态更新堆中的节点
 * 
 * 算法优势：
 * - 时间效率：比逐个合并更高效
 * - 空间效率：只使用O(k)的额外空间
 * - 扩展性好：适用于任意数量的有序链表合并
 * 
 * 时间复杂度：O(n*log k) - n为总节点数，每个节点入堆出堆操作为O(log k)
 * 空间复杂度：O(k) - 最小堆最多存储k个节点
 * 
 * LeetCode链接：https://leetcode.com/problems/merge-k-sorted-lists/
 */
public class P23_MergeKSortedLists {
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        public int val;        // 节点值
        public ListNode next;  // 下一个节点
    }

    /**
     * 链表节点比较器：根据节点值进行升序排列
     */
    public static class Comp implements Comparator<ListNode> {
        @Override
        public int compare(ListNode o1, ListNode o2) {
            return o1.val - o2.val;  // 按节点值升序排列
        }
    }

    /**
     * 合并K个升序链表
     * 
     * 算法步骤：
     * 1. 创建最小堆，将所有非空链表的头节点加入堆
     * 2. 从堆中取出最小节点作为结果链表的头节点
     * 3. 如果该节点有后续节点，将后续节点加入堆
     * 4. 继续从堆中取节点，构建结果链表
     * 5. 重复直到堆为空
     * 
     * 工作原理：
     * - 堆始终维护各个链表当前未处理的最小节点
     * - 每次取出全局最小节点，保证结果链表有序
     * - 动态更新堆，确保每个链表都能被完全遍历
     * 
     * @param lists 升序链表数组
     * @return 合并后的升序链表头节点
     */
    public ListNode mergeKLists(ListNode[] lists) {
        // 边界情况：空数组
        if (lists == null) {
            return null;
        }
        
        // 创建最小堆
        PriorityQueue<ListNode> heap = new PriorityQueue<>(new Comp());
        
        // 将所有非空链表的头节点加入堆
        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                heap.add(lists[i]);
            }
        }
        
        // 如果堆为空，说明所有链表都为空
        if (heap.isEmpty()) {
            return null;
        }
        
        // 取出最小节点作为结果链表的头节点
        ListNode head = heap.poll();
        ListNode pre = head;  // 用于链接结果链表的前一个节点
        
        // 如果头节点有后续节点，将其加入堆
        if (pre.next != null) {
            heap.add(pre.next);
        }
        
        // 继续构建结果链表
        while (!heap.isEmpty()) {
            ListNode cur = heap.poll();  // 取出当前最小节点
            pre.next = cur;              // 链接到结果链表
            pre = cur;                   // 更新前一个节点
            
            // 如果当前节点有后续节点，将其加入堆
            if (cur.next != null) {
                heap.add(cur.next);
            }
        }
        
        return head;  // 返回合并后的链表头节点
    }
}
