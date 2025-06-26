package leetc.top;

/**
 * LeetCode 138. 复制带随机指针的链表 (Copy List with Random Pointer)
 * 
 * 问题描述：
 * 给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random，
 * 该指针可以指向链表中的任何节点或空节点。
 * 构造这个链表的深拷贝。
 * 
 * 解法思路：
 * 三步法（不使用额外哈希表）：
 * 1. 第一步：在每个原节点后插入对应的拷贝节点
 *    原链表：1 -> 2 -> 3 -> null
 *    变为：  1 -> 1' -> 2 -> 2' -> 3 -> 3' -> null
 * 
 * 2. 第二步：设置拷贝节点的random指针
 *    copy.random = cur.random != null ? cur.random.next : null
 *    利用原节点的random指向，找到对应拷贝节点
 * 
 * 3. 第三步：分离原链表和拷贝链表
 *    恢复原链表结构，同时构建拷贝链表
 * 
 * 核心思想：
 * - 通过在原链表中插入拷贝节点建立对应关系
 * - 避免使用哈希表的额外空间
 * - 原节点的next指向其拷贝，拷贝节点的next指向下一个原节点
 * 
 * 算法优势：
 * - 空间复杂度O(1)（不计算返回结果的空间）
 * - 不需要额外的哈希表映射
 * - 三次遍历，逻辑清晰
 * 
 * 时间复杂度：O(n) - 三次遍历链表
 * 空间复杂度：O(1) - 不使用额外存储空间
 * 
 * LeetCode链接：https://leetcode.com/problems/copy-list-with-random-pointer/
 */
public class P138_CopyListWithRandomPointer {
    
    /**
     * 链表节点定义
     */
    public static class Node {
        int val;
        Node next;
        Node random;
        
        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * 复制带随机指针的链表
     * 
     * @param head 原链表头节点
     * @return 拷贝链表的头节点
     */
    public static Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        
        // 第一步：在每个原节点后插入拷贝节点
        Node cur = head;
        Node next = null;
        while (cur != null) {
            next = cur.next;                    // 保存下一个原节点
            cur.next = new Node(cur.val);       // 创建拷贝节点
            cur.next.next = next;               // 拷贝节点指向下一个原节点
            cur = next;                         // 移动到下一个原节点
        }
        
        // 第二步：设置拷贝节点的random指针
        cur = head;
        Node copy = null;
        while (cur != null) {
            next = cur.next.next;               // 下一个原节点
            copy = cur.next;                    // 当前拷贝节点
            
            // 设置拷贝节点的random指针
            // 如果原节点的random为null，拷贝节点的random也为null
            // 否则指向原节点random指向节点的拷贝节点（即random.next）
            copy.random = cur.random != null ? cur.random.next : null;
            
            cur = next;                         // 移动到下一个原节点
        }
        
        // 第三步：分离原链表和拷贝链表
        cur = head;
        Node res = head.next;                   // 拷贝链表的头节点
        while (cur != null) {
            next = cur.next.next;               // 下一个原节点
            copy = cur.next;                    // 当前拷贝节点
            
            // 恢复原链表的next指针
            cur.next = next;
            
            // 设置拷贝链表的next指针
            copy.next = next != null ? next.next : null;
            
            cur = next;                         // 移动到下一个原节点
        }
        
        return res;
    }
}
