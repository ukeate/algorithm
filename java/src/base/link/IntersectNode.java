package base.link;

/**
 * 链表相交节点检测问题
 * 
 * 问题描述：
 * 给定两个可能有环也可能无环的单链表，头节点head1和head2。
 * 请实现一个函数，如果两个链表相交，请返回相交的第一个节点。如果不相交，返回null。
 * 
 * 要求：
 * - 如果两个链表长度之和为N，时间复杂度请达到O(N)，额外空间复杂度请达到O(1)
 * 
 * 解题思路：
 * 根据链表是否有环，分为三种情况：
 * 1. 两个链表都无环：可能相交，可能不相交
 * 2. 一个链表有环，一个链表无环：一定不相交
 * 3. 两个链表都有环：可能相交，可能不相交
 * 
 * 核心算法：
 * 1. getLoopNode(): 快慢指针检测环的入口节点
 * 2. noLoop(): 处理两个无环链表的相交问题  
 * 3. bothLoop(): 处理两个有环链表的相交问题
 * 
 * 时间复杂度：O(N) - N为两链表长度之和
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class IntersectNode {
    public static class Node {
        public int val;
        public Node next;

        public Node(int v) {
            this.val = v;
        }
    }

    /**
     * 检测链表是否有环，如果有环则返回环的入口节点
     * 
     * 算法思路：Floyd判圈算法（快慢指针）
     * 第一阶段：快指针一次走2步，慢指针一次走1步，如果有环必定相遇
     * 第二阶段：一个指针从头开始，一个指针从相遇点开始，都走1步，相遇点就是环入口
     * 
     * 数学证明：
     * 设链表头到环入口距离为a，环入口到相遇点距离为b，相遇点到环入口距离为c
     * 第一次相遇时：slow走了a+b，fast走了a+b+c+b = a+2b+c
     * 由于fast速度是slow的2倍：a+2b+c = 2(a+b) => c = a
     * 所以从头和从相遇点同时出发，会在环入口相遇
     * 
     * @param head 链表头节点
     * @return 环的入口节点，如果无环返回null
     */
    private static Node getLoopNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        
        // 初始化快慢指针
        Node slow = head.next;
        Node fast = head.next.next;
        
        // 第一阶段：快慢指针相遇检测
        while (slow != fast) {
            if (fast.next == null || fast.next.next == null) {
                return null; // 无环
            }
            fast = fast.next.next; // 快指针每次走2步
            slow = slow.next;      // 慢指针每次走1步
        }
        
        // 第二阶段：找环入口节点
        fast = head; // 一个指针回到链表头部
        while (slow != fast) {
            slow = slow.next; // 两个指针都每次走1步
            fast = fast.next;
        }
        return slow; // 相遇点即为环入口
    }

    /**
     * 处理两个无环链表的相交问题
     * 
     * 算法思路：
     * 1. 先遍历两个链表到尾部，统计长度差n
     * 2. 如果尾节点不同，说明不相交，直接返回null
     * 3. 如果尾节点相同，说明相交，让长链表先走|n|步
     * 4. 然后两个链表同时走，第一个相同的节点就是相交点
     * 
     * @param h1 第一个链表头节点
     * @param h2 第二个链表头节点  
     * @return 相交的第一个节点，如果不相交返回null
     */
    private static Node noLoop(Node h1, Node h2) {
        if (h1 == null || h2 == null) {
            return null;
        }
        
        Node cur1 = h1;
        Node cur2 = h2;
        int n = 0; // 记录两链表长度差
        
        // 遍历第一个链表到尾部，统计长度
        while (cur1.next != null) {
            n++;
            cur1 = cur1.next;
        }
        
        // 遍历第二个链表到尾部，计算长度差
        while (cur2.next != null) {
            n--;
            cur2 = cur2.next;
        }
        
        // 如果尾节点不同，说明两链表不相交
        if (cur1 != cur2) {
            return null;
        }
        
        // 确定哪个链表更长
        cur1 = n > 0 ? h1 : h2;  // cur1指向较长的链表
        cur2 = cur1 == h1 ? h2 : h1;  // cur2指向较短的链表
        n = Math.abs(n);
        
        // 让较长的链表先走|n|步，这样两链表到尾部距离相等
        while (n != 0) {
            n--;
            cur1 = cur1.next;
        }
        
        // 两链表同时走，第一个相同节点就是相交点
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
    }

    /**
     * 处理两个有环链表的相交问题
     * 
     * 分析两个有环链表的相交情况：
     * 情况1：两个链表在环入口前就相交了（l1 == l2）
     *       此时类似于无环链表相交，只需在环入口前找相交点
     * 情况2：两个链表各自成环，但共享环的一部分（l1 != l2，但在同一个环上）
     *       此时返回任一环入口节点都可以
     * 情况3：两个链表各自成环，且不相交（l1 != l2，且不在同一个环上）
     *       此时返回null
     * 
     * @param h1 第一个链表头节点
     * @param l1 第一个链表的环入口节点
     * @param h2 第二个链表头节点
     * @param l2 第二个链表的环入口节点
     * @return 相交的第一个节点，如果不相交返回null
     */
    private static Node bothLoop(Node h1, Node l1, Node h2, Node l2) {
        Node cur1 = null;
        Node cur2 = null;
        
        if (l1 == l2) {
            // 情况1：两链表在环入口前相交，环入口是同一个节点
            // 此时问题转化为：在两个以l1/l2为终点的链表中找相交点
            cur1 = h1;
            cur2 = h2;
            int n = 0; // 统计到环入口前的长度差
            
            // 计算h1到环入口的距离
            while (cur1 != l1) {
                n++;
                cur1 = cur1.next;
            }
            
            // 计算h2到环入口的距离，并得到长度差
            while (cur2 != l2) {
                n--;
                cur2 = cur2.next;
            }
            
            // 应用无环链表相交的算法
            cur1 = n > 0 ? h1 : h2;  // cur1指向较长的链表
            cur2 = cur1 == h1 ? h2 : h1;  // cur2指向较短的链表
            n = Math.abs(n);
            
            // 较长链表先走|n|步
            while (n != 0) {
                n--;
                cur1 = cur1.next;
            }
            
            // 同时走直到相遇
            while (cur1 != cur2) {
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
            return cur1;
        } else {
            // 情况2或3：两个链表有不同的环入口
            // 需要判断l1和l2是否在同一个环上
            cur1 = l1.next;
            while (cur1 != l1) {
                if (cur1 == l2) {
                    // l1和l2在同一个环上，返回l1作为相交点
                    return l1;
                }
                cur1 = cur1.next;
            }
            // l1和l2不在同一个环上，两链表不相交
            return null;
        }
    }

    /**
     * 判断两个链表是否相交，返回相交的第一个节点
     * 
     * 总体算法流程：
     * 1. 分别检测两个链表是否有环
     * 2. 根据有环情况分类处理：
     *    - 都无环：调用noLoop处理
     *    - 都有环：调用bothLoop处理 
     *    - 一个有环一个无环：不可能相交，返回null
     * 
     * @param h1 第一个链表头节点
     * @param h2 第二个链表头节点
     * @return 相交的第一个节点，如果不相交返回null
     */
    public static Node intersectNode(Node h1, Node h2) {
        if (h1 == null || h2 == null) {
            return null;
        }
        
        // 检测两个链表是否有环
        Node loop1 = getLoopNode(h1);
        Node loop2 = getLoopNode(h2);
        
        if (loop1 == null && loop2 == null) {
            // 情况1：两个链表都无环
            return noLoop(h1, h2);
        }
        if (loop1 != null && loop2 != null) {
            // 情况2：两个链表都有环
            return bothLoop(h1, loop1, h2, loop2);
        }
        // 情况3：一个有环一个无环，不可能相交
        return null;
    }

    public static void main(String[] args) {
        // 1->2->3->4->5->6->7->null
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);

        // 0->9->8->6->7->null
        Node head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(intersectNode(head1, head2).val);

        // 1->2->3->4->5->6->7->4...
        head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);
        head1.next.next.next.next.next.next = head1.next.next.next; // 7->4

        // 0->9->8->2...
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next; // 8->2
        System.out.println(intersectNode(head1, head2).val);

        // 0->9->8->6->4->5->6..
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(intersectNode(head1, head2).val);

    }

}
