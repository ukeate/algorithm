package base.link;

/**
 * 两个用链表表示的数字相加
 * 
 * 问题描述：
 * 给定两个用链表表示的非负整数，每个节点存储一位数字，数字按照逆序存储
 * 计算两个数的和，并以同样的形式返回一个链表
 * 
 * 例如：
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 * 
 * 算法思路：
 * 1. 模拟手工加法过程，从低位到高位逐位相加
 * 2. 处理进位问题
 * 3. 复用较长的链表来存储结果，节省空间
 * 
 * 时间复杂度：O(max(m,n))，m和n分别是两个链表的长度
 * 空间复杂度：O(1)，只使用了常数额外空间
 */
public class AddNum {

    /**
     * 链表节点定义
     */
    static class Node {
        public Node next;   // 指向下一个节点的指针
        public int val;     // 节点值（0-9的数字）

        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * 计算链表长度
     * @param head 链表头节点
     * @return 链表长度
     */
    private static int length(Node head) {
        int len = 0;
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }

    /**
     * 两个链表表示的数字相加
     * 
     * 算法步骤：
     * 1. 确定较长和较短的链表
     * 2. 对应位相加，处理进位
     * 3. 处理较长链表的剩余部分
     * 4. 如果最后还有进位，需要新增节点
     * 
     * 优化点：
     * - 复用较长的链表来存储结果
     * - 避免创建新的链表节点（除了最后的进位）
     * 
     * @param head1 第一个数字的链表头节点
     * @param head2 第二个数字的链表头节点
     * @return 相加结果的链表头节点
     */
    public static Node addNumbers(Node head1, Node head2) {
        int len1 = length(head1);
        int len2 = length(head2);
        
        // l指向较长的链表，s指向较短的链表
        Node l = len1 >= len2 ? head1 : head2;
        Node s = l == head1 ? head2 : head1;
        
        Node curL = l;      // 遍历较长链表的指针
        Node curS = s;      // 遍历较短链表的指针
        Node last = curL;   // 记录较长链表的最后一个节点
        int carry = 0;      // 进位
        int curNum = 0;     // 当前位的和
        
        // 第一阶段：处理两个链表都有数字的部分
        while (curS != null) {
            curNum = curL.val + curS.val + carry;
            curL.val = (curNum % 10);   // 当前位的结果
            carry = curNum / 10;        // 进位
            last = curL;
            curL = curL.next;
            curS = curS.next;
        }
        
        // 第二阶段：处理较长链表的剩余部分
        while (curL != null) {
            curNum = curL.val + carry;
            curL.val = (curNum % 10);
            carry = curNum / 10;
            last = curL;
            curL = curL.next;
        }
        
        // 第三阶段：如果最后还有进位，需要新增节点
        if (carry != 0) {
            last.next = new Node(1);
        }
        
        return l;   // 返回较长链表的头节点（现在存储了结果）
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建第一个数字：342 (2->4->3)
        Node num1 = new Node(2);
        num1.next = new Node(4);
        num1.next.next = new Node(3);
        
        // 创建第二个数字：465 (5->6->4)
        Node num2 = new Node(5);
        num2.next = new Node(6);
        num2.next.next = new Node(4);
        
        System.out.print("第一个数字: ");
        printNumber(num1);
        System.out.print("第二个数字: ");
        printNumber(num2);
        
        // 计算和
        Node result = addNumbers(num1, num2);
        
        System.out.print("相加结果: ");
        printNumber(result);
    }
    
    /**
     * 打印链表表示的数字（逆序输出）
     * @param head 数字链表的头节点
     */
    private static void printNumber(Node head) {
        if (head == null) {
            System.out.println("0");
            return;
        }
        
        // 先收集所有数字
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            head = head.next;
        }
        
        // 逆序输出得到原始数字
        System.out.println(sb.reverse().toString());
    }
}
