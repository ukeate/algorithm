package base.link;

/**
 * 合并两个有序链表
 * 
 * 问题描述：
 * 将两个升序链表合并为一个新的升序链表并返回
 * 新链表是通过拼接给定的两个链表的所有节点组成的
 * 
 * 例如：
 * 输入：1->2->4, 1->3->4
 * 输出：1->1->2->3->4->4
 * 
 * 算法思路：
 * 使用双指针技术，类似于归并排序的合并过程
 * 
 * 时间复杂度：O(m + n)，m和n分别是两个链表的长度
 * 空间复杂度：O(1)，只使用了常数额外空间
 */
public class Merge {

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
     * 合并两个有序链表
     * 
     * 算法步骤：
     * 1. 处理边界情况：如果其中一个链表为空，直接返回另一个
     * 2. 选择较小值的节点作为合并后链表的头节点
     * 3. 使用双指针同时遍历两个链表，每次选择较小的节点
     * 4. 将剩余的节点直接连接到结果链表末尾
     * 
     * 关键技巧：
     * - 复用原有节点，不创建新节点，节省空间
     * - 使用pre指针跟踪当前构建的链表尾部
     * 
     * @param head1 第一个有序链表头节点
     * @param head2 第二个有序链表头节点
     * @return 合并后的有序链表头节点
     */
    public static Node mergeLink(Node head1, Node head2) {
        // 边界情况处理
        if (head1 == null || head2 == null) {
            return head1 == null ? head2 : head1;
        }
        
        // 选择较小值作为头节点
        Node head = head1.val <= head2.val ? head1 : head2;
        Node cur1 = head.next;                              // 第一个链表的当前位置
        Node cur2 = head == head1 ? head2 : head1;          // 第二个链表的当前位置
        Node pre = head;                                     // 结果链表的尾部指针
        
        // 合并过程：同时遍历两个链表
        while (cur1 != null && cur2 != null) {
            if (cur1.val <= cur2.val) {
                pre.next = cur1;        // 连接较小的节点
                cur1 = cur1.next;       // 移动第一个链表指针
            } else {
                pre.next = cur2;        // 连接较小的节点
                cur2 = cur2.next;       // 移动第二个链表指针
            }
            pre = pre.next;             // 移动结果链表尾部指针
        }
        
        // 连接剩余节点
        // 当其中一个链表遍历完毕时，直接连接另一个链表的剩余部分
        pre.next = cur1 != null ? cur1 : cur2;
        
        return head;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建第一个有序链表：1->2->4
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(4);
        
        // 创建第二个有序链表：1->3->4
        Node head2 = new Node(1);
        head2.next = new Node(3);
        head2.next.next = new Node(4);
        
        System.out.print("链表1: ");
        printList(head1);
        System.out.print("链表2: ");
        printList(head2);
        
        // 合并链表
        Node merged = mergeLink(head1, head2);
        
        System.out.print("合并后: ");
        printList(merged);
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
