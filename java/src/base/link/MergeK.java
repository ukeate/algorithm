package base.link;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 合并K个有序链表
 * 
 * 问题描述：
 * 给定一个链表数组，每个链表都已经按升序排列，请将所有链表合并到一个升序链表中。
 * 
 * 解法分析：
 * 本实现采用优先队列(最小堆)方法：
 * 1. 将所有链表的头节点加入最小堆
 * 2. 每次从堆中取出值最小的节点，加入结果链表
 * 3. 如果取出的节点有下一个节点，将下一个节点加入堆
 * 4. 重复步骤2-3直到堆为空
 * 
 * 算法优势：
 * - 相比逐一合并两个链表，减少了比较次数
 * - 始终能找到当前所有链表头部的最小值
 * 
 * 其他解法对比：
 * 1. 分治合并：O(N*logk) 时间，O(logk) 空间（递归栈）
 * 2. 逐一合并：O(N*k) 时间，O(1) 空间
 * 3. 优先队列：O(N*logk) 时间，O(k) 空间
 * 
 * 时间复杂度：O(N*logk) - N是所有节点总数，k是链表个数，每次堆操作需要logk时间
 * 空间复杂度：O(k) - 优先队列最多存储k个节点
 */
public class MergeK {

    /**
     * 链表节点定义
     */
    static class Node {
        public Node next;
        public int val;

        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * 节点比较器，用于优先队列按值升序排列
     */
    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.val - o2.val;  // 升序排列，值小的优先级高
        }
    }

    /**
     * 合并K个有序链表 - 优先队列法
     * 
     * @param lists K个有序链表的头节点数组
     * @return 合并后的有序链表头节点
     */
    public static Node mergeK(Node[] lists) {
        if (lists == null) {
            return null;
        }
        
        // 创建最小堆，用于维护当前所有链表的头节点
        PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());
        
        // 将所有非空链表的头节点加入堆
        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                heap.add(lists[i]);
            }
        }
        
        // 如果所有链表都为空，直接返回null
        if (heap.isEmpty()) {
            return null;
        }
        
        // 取出堆顶（最小值）作为结果链表的头节点
        Node head = heap.poll();
        Node pre = head;
        
        // 如果头节点有后继节点，将其加入堆
        if (pre.next != null) {
            heap.add(pre.next);
        }
        
        // 持续从堆中取出最小节点，构建结果链表
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            pre.next = cur;        // 连接到结果链表
            pre = cur;             // 移动结果链表的尾指针
            
            // 如果当前节点有后继，将后继节点加入堆
            if (cur.next != null) {
                heap.add(cur.next);
            }
        }
        
        return head;
    }

    public static void main(String[] args) {
        // 测试用例可以在这里添加
    }
}
