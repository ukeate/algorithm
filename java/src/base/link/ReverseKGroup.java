package base.link;

/**
 * K个一组反转链表
 * 
 * 问题描述：
 * 给定一个链表，每K个节点一组进行反转，如果最后不足K个节点则保持原有顺序
 * 
 * 例如：1->2->3->4->5->6，K=3
 * 结果：3->2->1->6->5->4
 * 
 * 算法思路：
 * 1. 分组：每次找到K个节点作为一组
 * 2. 反转：对每组内的节点进行反转
 * 3. 连接：将反转后的组与前面已处理的部分连接
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 */
public class ReverseKGroup {

    /**
     * 链表节点定义
     */
    static class Node {
        public Node next;   // 指向下一个节点的指针
        public int val;     // 节点值

        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * 获取从start开始的第K个节点
     * 
     * @param start 起始节点
     * @param k 要获取的第几个节点
     * @return 第K个节点，如果不足K个则返回null
     */
    private static Node getKGroupEnd(Node start, int k) {
        while (--k != 0 && start != null) {
            start = start.next;
        }
        return start;
    }

    /**
     * 反转从start到end之间的链表（不包括end.next）
     * 
     * 反转过程：
     * 1. 保存end.next作为反转范围的边界
     * 2. 使用标准的链表反转算法
     * 3. 将原start节点（反转后的尾节点）连接到end.next
     * 
     * @param start 反转范围的起始节点
     * @param end 反转范围的结束节点
     */
    private static void reverse(Node start, Node end) {
        end = end.next;     // 保存反转范围之后的节点
        Node pre = null;
        Node cur = start;
        Node next = null;
        
        // 标准的链表反转过程
        while (cur != end) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        
        // 将原start节点（现在的尾节点）连接到后续节点
        start.next = end;
    }

    /**
     * K个一组反转链表的主方法
     * 
     * 算法流程：
     * 1. 找到第一组的K个节点
     * 2. 如果找到则反转这一组，并更新新的头节点
     * 3. 继续寻找下一组的K个节点
     * 4. 反转并连接每一组
     * 5. 重复直到不足K个节点为止
     * 
     * @param head 原链表头节点
     * @param k 每组的节点数量
     * @return 反转后的链表头节点
     */
    public static Node reverseKGroup(Node head, int k) {
        Node start = head;
        Node end = getKGroupEnd(start, k);
        
        // 如果第一组就不足K个节点，直接返回原链表
        if (end == null) {
            return head;
        }
        
        // 反转第一组，end成为新的头节点
        head = end;
        reverse(start, end);
        
        // 记录上一组反转后的尾节点（原来的start）
        Node lastEnd = start;
        
        // 继续处理后续的组
        while (lastEnd.next != null) {
            start = lastEnd.next;
            end = getKGroupEnd(start, k);
            
            // 如果不足K个节点，则保持原有顺序
            if (end == null) {
                return head;
            }
            
            // 反转当前组
            reverse(start, end);
            
            // 连接上一组的尾节点与当前组的头节点（反转后的end）
            lastEnd.next = end;
            
            // 更新lastEnd为当前组反转后的尾节点（原来的start）
            lastEnd = start;
        }
        
        return head;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建测试链表：1->2->3->4->5->6
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(5);
        head.next.next.next.next.next = new Node(6);
        
        // 打印原链表
        System.out.print("原链表: ");
        printList(head);
        
        // K=3进行反转
        head = reverseKGroup(head, 3);
        
        // 打印反转后的链表
        System.out.print("反转后: ");
        printList(head);
    }
    
    /**
     * 打印链表的辅助方法
     * @param head 链表头节点
     */
    private static void printList(Node head) {
        while (head != null) {
            System.out.print(head.val);
            if (head.next != null) {
                System.out.print("->");
            }
            head = head.next;
        }
        System.out.println();
    }
}
