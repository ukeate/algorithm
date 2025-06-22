package base.link;

import java.util.Stack;

/**
 * 判断链表是否为回文
 * 
 * 问题描述：
 * 给定一个单链表，判断该链表是否为回文结构
 * 
 * 例如：
 * 1->2->3->2->1 是回文
 * 1->2->3->4->5 不是回文
 * 
 * 三种解决方案：
 * 1. 使用栈存储所有节点（空间复杂度O(n)）
 * 2. 使用栈存储后半部分节点（空间复杂度O(n/2)）
 * 3. 通过反转后半部分链表实现（空间复杂度O(1)）
 */
public class IsPalindrome {
    /**
     * 链表节点定义
     */
    public static class Node {
        public int val;     // 节点值
        public Node next;   // 指向下一个节点的指针
        
        public Node(int v) {
            this.val = v;
        }
    }
    
    /**
     * 方法一：使用栈存储所有节点
     * 
     * 算法思路：
     * 1. 将所有节点压入栈中
     * 2. 重新遍历链表，与栈顶元素比较
     * 3. 如果都相等，则为回文
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param head 链表头节点
     * @return 是否为回文
     */
    public static boolean isPalindrome1(Node head) {
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        
        // 将所有节点压入栈中
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }
        
        // 重新遍历链表，与栈顶元素比较
        while (head != null) {
            if (head.val != stack.pop().val) {
                return false;
            }
            head = head.next;
        }
        
        return true;
    }

    /**
     * 方法二：使用栈存储后半部分节点（空间优化）
     * 
     * 算法思路：
     * 1. 使用快慢指针找到链表中点
     * 2. 将后半部分节点压入栈中
     * 3. 前半部分与栈中元素比较
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n/2)
     * 
     * @param head 链表头节点
     * @return 是否为回文
     */
    public static boolean isPalindrome2(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        
        // 使用快慢指针找到后半部分的起始位置
        Node right = head.next;     // 慢指针，指向后半部分起始
        Node cur = head;            // 快指针
        
        while (cur.next != null && cur.next.next != null) {
            right = right.next;
            cur = cur.next.next;
        }
        
        // 将后半部分节点压入栈中
        Stack<Node> stack = new Stack<>();
        while (right != null) {
            stack.push(right);
            right = right.next;
        }
        
        // 前半部分与栈中元素比较
        while (!stack.isEmpty()) {
            if (head.val != stack.pop().val) {
                return false;
            }
            head = head.next;
        }
        
        return true;
    }

    /**
     * 方法三：通过反转后半部分链表实现（最优解）
     * 
     * 算法思路：
     * 1. 使用快慢指针找到链表中点
     * 2. 反转后半部分链表
     * 3. 从两端同时向中间比较
     * 4. 恢复链表原始结构
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @param head 链表头节点
     * @return 是否为回文
     */
    public static boolean isPalindrome3(Node head) {
        if (head == null || head.next == null) {
            return true;
        }
        
        // 第一步：找到链表中点
        Node n1 = head;         // 慢指针
        Node n2 = head;         // 快指针
        
        while (n2.next != null && n2.next.next != null) {
            n1 = n1.next;       // 慢指针移动1步
            n2 = n2.next.next;  // 快指针移动2步
        }
        
        // 第二步：反转后半部分链表
        n2 = n1.next;           // n2指向后半部分第一个节点
        n1.next = null;         // 断开前后两部分
        Node n3 = null;         // 临时节点
        
        while (n2 != null) {
            n3 = n2.next;       // 保存下一个节点
            n2.next = n1;       // 反转指针
            n1 = n2;            // 移动n1
            n2 = n3;            // 移动n2
        }
        
        // 第三步：从两端向中间比较
        n3 = n1;                // 保存反转后的后半部分头节点
        n2 = head;              // 前半部分头节点
        boolean res = true;     // 比较结果
        
        while (n2 != null) {
            if (n1.val != n2.val) {
                res = false;
                break;
            }
            n1 = n1.next;
            n2 = n2.next;
        }
        
        // 第四步：恢复链表原始结构
        n1 = n3.next;           // n1指向反转链表的第二个节点
        n3.next = null;         // 断开第一个节点
        
        while (n1 != null) {
            n2 = n1.next;       // 保存下一个节点
            n1.next = n3;       // 反转指针
            n3 = n1;            // 移动n3
            n1 = n2;            // 移动n1
        }
        
        return res;
    }

    /**
     * 打印链表的辅助方法
     * @param node 链表头节点
     */
    private static void print(Node node) {
        while (node != null) {
            System.out.print(node.val + ",");
            node = node.next;
        }
        System.out.println();
    }
    
    /**
     * 测试方法 - 验证各种情况下的回文判断
     */
    public static void main(String[] args) {

        // 测试用例1：空链表
        Node head = null;
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");

        // 测试用例2：单节点链表
        head = new Node(1);
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");

        // 测试用例3：两节点非回文链表
        head = new Node(1);
        head.next = new Node(2);
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");

        // 测试用例4：两节点回文链表
        head = new Node(1);
        head.next = new Node(1);
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");

        // 测试用例5：三节点非回文链表
        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");

        // 测试用例6：三节点回文链表
        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(1);
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");

        // 测试用例7：四节点非回文链表
        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(1);
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");

        // 测试用例8：四节点回文链表
        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(2);
        head.next.next.next = new Node(1);
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");

        // 测试用例9：五节点回文链表
        head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(2);
        head.next.next.next.next = new Node(1);
        print(head);
        System.out.print(isPalindrome1(head) + " | ");
        System.out.print(isPalindrome2(head) + " | ");
        System.out.println(isPalindrome3(head) + " | ");
        print(head);
        System.out.println("=========================");
    }
}
