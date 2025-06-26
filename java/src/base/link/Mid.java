package base.link;

import java.util.ArrayList;

/**
 * 链表中点问题 - 快慢指针经典应用
 * 
 * 问题场景：
 * 在单链表中寻找中点是很多算法的基础，比如链表排序、回文检测等。
 * 不同的需求可能需要不同的中点定义。
 * 
 * 四种中点情况：
 * 1. 上中点：奇数长度返回正中间，偶数长度返回上面的中点
 * 2. 下中点：奇数长度返回正中间，偶数长度返回下面的中点  
 * 3. 上中点的前一个：用于链表断开等操作
 * 4. 下中点的前一个：用于链表断开等操作
 * 
 * 核心思想：
 * 快慢指针法 - 快指针每次走2步，慢指针每次走1步
 * 当快指针到达链表末尾时，慢指针正好在中点位置
 * 
 * 时间复杂度：O(n) - 只需要一次遍历
 * 空间复杂度：O(1) - 只使用常数个指针
 */
public class Mid {
    
    /**
     * 链表节点定义
     */
    public static class Node {
        public int val;
        public Node next;
        public Node(int v) {
            val = v;
        }
    }

    /**
     * 返回链表的上中点
     * 奇数长度链表：返回正中间节点
     * 偶数长度链表：返回上面的中点
     * 例：1->2->3->4->5 返回3，1->2->3->4 返回2
     */
    public static Node midUpNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return head;
        }
        Node slow = head.next;      // 慢指针从第二个节点开始
        Node fast = head.next.next; // 快指针从第三个节点开始
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 返回链表的下中点
     * 奇数长度链表：返回正中间节点
     * 偶数长度链表：返回下面的中点
     * 例：1->2->3->4->5 返回3，1->2->3->4 返回3
     */
    public static Node midDownNode(Node head) {
        if (head == null || head.next == null){
            return head;
        }
        Node slow = head.next;  // 慢指针从第二个节点开始
        Node fast = head.next;  // 快指针也从第二个节点开始
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 返回链表上中点的前一个节点
     * 主要用于需要断开链表的场景
     * 例：1->2->3->4->5 返回1，1->2->3->4 返回1
     */
    public static Node midUpPreNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        Node slow = head;           // 慢指针从头节点开始
        Node fast = head.next.next; // 快指针从第三个节点开始
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 返回链表下中点的前一个节点
     * 主要用于需要断开链表的场景
     * 例：1->2->3->4->5 返回1，1->2->3->4 返回2
     */
    public static Node midDownPreNode(Node head) {
        if (head == null || head.next == null) {
            return null;
        }
        Node slow = head;       // 慢指针从头节点开始
        Node fast = head.next;  // 快指针从第二个节点开始
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // ======================== 以下是验证方法 ========================
    // 使用朴素方法实现，用于验证快慢指针方法的正确性

    /**
     * 验证方法：上中点的朴素实现
     * 遍历链表收集所有节点，然后直接计算中点位置
     */
    private static Node midUpNodeSure(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        ArrayList<Node> arr = new ArrayList<>();
        while (cur != null){
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 1) / 2);
    }

    /**
     * 验证方法：下中点的朴素实现
     */
    private static Node midDownNodeSure(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        ArrayList<Node> arr = new ArrayList<>();
        while (cur != null){
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size()) / 2);
    }

    /**
     * 验证方法：上中点前一个节点的朴素实现
     */
    private static Node midUpPreNodeSure(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        ArrayList<Node> arr = new ArrayList<>();
        while (cur != null){
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 3) / 2);
    }

    /**
     * 验证方法：下中点前一个节点的朴素实现
     */
    private static Node midDownPreNodeSure(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        ArrayList<Node> arr = new ArrayList<>();
        while (cur != null){
            arr.add(cur);
            cur = cur.next;
        }
        return arr.get((arr.size() - 2) / 2);
    }

    public static void main(String[] args) {
        Node test = null;
        test = new Node(0);
        test.next = new Node(1);
        test.next.next = new Node(2);
        test.next.next.next = new Node(3);
        test.next.next.next.next = new Node(4);
        test.next.next.next.next.next = new Node(5);
        test.next.next.next.next.next.next = new Node(6);
        test.next.next.next.next.next.next.next = new Node(7);
        test.next.next.next.next.next.next.next.next = new Node(8);

        Node ans1 = null;
        Node ans2 = null;

        ans1 = midUpNode(test);
        ans2 = midUpNodeSure(test);
        System.out.println(ans1 != null ? ans1.val : "无");
        System.out.println(ans2 != null ? ans2.val : "无");

        ans1 = midDownNode(test);
        ans2 = midDownNodeSure(test);
        System.out.println(ans1 != null ? ans1.val : "无");
        System.out.println(ans2 != null ? ans2.val : "无");

        ans1 = midUpPreNode(test);
        ans2 = midUpPreNodeSure(test);
        System.out.println(ans1 != null ? ans1.val : "无");
        System.out.println(ans2 != null ? ans2.val : "无");

        ans1 = midDownPreNode(test);
        ans2 = midDownPreNodeSure(test);
        System.out.println(ans1 != null ? ans1.val : "无");
        System.out.println(ans2 != null ? ans2.val : "无");
    }
}
