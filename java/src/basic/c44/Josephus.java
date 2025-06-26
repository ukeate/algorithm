package basic.c44;

/**
 * 约瑟夫环问题（Josephus Problem）
 * 
 * 问题描述：
 * n个人围成一个圆圈，从某个位置开始按顺序报数
 * 数到m的人被淘汰出局，然后从下一个人开始重新报数
 * 这个过程持续进行，直到圆圈中只剩下一个人
 * 求最后剩下的人的初始编号
 * 
 * 经典背景：
 * 41个犹太人和约瑟夫及他的朋友躲在洞中，41个犹太人决定自杀而不被敌人抓到
 * 他们决定围成一个圆圈，每隔3个人杀死一人，约瑟夫和他的朋友不想死
 * 于是约瑟夫要快速计算出他和朋友应该站的位置
 * 
 * 算法思路：
 * 方法1：链表模拟 - 直接模拟整个淘汰过程，O(n*m)
 * 方法2：数学递推 - 利用数学公式直接计算，O(n)
 * 
 * 核心数学公式：
 * live(i) = (live(i-1) + m - 1) % i + 1
 * 其中i表示当前圆圈中的人数，live(i)表示i个人时幸存者的位置
 * 
 * @author 算法学习
 */
public class Josephus {
    
    /**
     * 链表节点定义
     */
    public static class Node {
        public int val;      // 节点值（编号）
        public Node next;    // 下一个节点指针
        
        public Node(int v) {
            val = v;
        }
    }

    /**
     * 方法1：链表模拟解法
     * 直接用循环链表模拟约瑟夫环的淘汰过程
     * 
     * @param head 循环链表头节点
     * @param m 间隔数（数到m就淘汰）
     * @return 最后剩余的节点
     * 
     * 算法步骤：
     * 1. 找到链表的最后一个节点
     * 2. 模拟报数过程，数到m就删除当前节点
     * 3. 继续下去直到只剩一个节点
     * 
     * 时间复杂度：O(n*m) - n个人，每次淘汰需要数m次
     * 空间复杂度：O(1) - 只使用常数额外空间
     */
    public static Node live1(Node head, int m) {
        // 边界情况处理
        if (head == null || head.next == head || m < 1) {
            return head;
        }
        
        // 找到循环链表的最后一个节点
        Node last = head;
        while (last.next != head) {
            last = last.next;
        }
        
        int cnt = 0; // 报数计数器
        
        // 继续淘汰直到只剩一个人
        while (head != last) {
            if (++cnt == m) {
                // 数到m，淘汰当前节点
                last.next = head.next; // 删除head节点
                cnt = 0; // 重置计数器
            } else {
                // 还没数到m，移动到下一个人
                last = last.next;
            }
            head = last.next; // 移动到下一个待数的人
        }
        
        return head;
    }

    /**
     * 约瑟夫环的数学递推解法
     * 
     * @param i 当前圆圈中的人数
     * @param m 间隔数
     * @return i个人时幸存者的位置（1-based）
     * 
     * 递推公式推导：
     * 设live(i)表示i个人时幸存者的位置（1-based）
     * 
     * 当有i个人时，第一个被杀的人的位置是：killed = (m-1) % i + 1
     * 杀掉一个人后，剩下i-1个人，编号需要重新映射
     * 
     * 新编号到旧编号的转换：
     * 旧号 = (新号 + killed - 1) % i + 1
     * 由于killed = (m-1) % i + 1，所以：
     * 旧号 = (新号 + (m-1) % i) % i + 1 = (新号 + m - 1) % i + 1
     * 
     * 因此递推公式为：live(i) = (live(i-1) + m - 1) % i + 1
     */
    private static int liveIdx(int i, int m) {
        // 递归边界：只有1个人时，幸存者就是第1个
        if (i == 1) {
            return 1;
        }
        
        // 递推公式：当前圆圈的幸存者位置
        return (liveIdx(i - 1, m) + m - 1) % i + 1;
    }

    /**
     * 方法2：数学公式解法
     * 利用递推公式直接计算幸存者位置，然后定位到对应节点
     * 
     * @param head 循环链表头节点
     * @param m 间隔数
     * @return 最后剩余的节点
     * 
     * 算法步骤：
     * 1. 计算链表长度
     * 2. 使用递推公式计算幸存者位置
     * 3. 从头节点开始移动到幸存者位置
     * 4. 将该节点设为自循环并返回
     * 
     * 时间复杂度：O(n) - 计算长度O(n) + 递推计算O(n) + 定位O(n)
     * 空间复杂度：O(n) - 递归栈深度
     */
    public static Node live2(Node head, int m) {
        // 边界情况处理
        if (head == null || head.next == head || m < 1) {
            return head;
        }
        
        // 计算链表长度
        Node cur = head.next;
        int size = 1;
        while (cur != head) {
            size++;
            cur = cur.next;
        }
        
        // 使用递推公式计算幸存者位置
        int li = liveIdx(size, m);
        
        // 从头节点移动到幸存者位置
        while (--li > 0) {
            head = head.next;
        }
        
        // 将幸存者节点设为自循环
        head.next = head;
        return head;
    }

    /**
     * 打印循环链表的辅助方法
     * 
     * @param head 循环链表头节点
     */
    private static void print(Node head) {
        if (head == null) {
            return;
        }
        
        System.out.print("list: " + head.val + " ");
        Node cur = head.next;
        while (cur != head) {
            System.out.print(cur.val + " ");
            cur = cur.next;
        }
        System.out.println("-> " + head.val);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例：5个人的约瑟夫环，间隔为3
        System.out.println("=== 方法1：链表模拟 ===");
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = head1; // 形成循环
        
        System.out.print("初始状态：");
        print(head1);
        
        head1 = live1(head1, 3);
        System.out.print("最终幸存者：");
        print(head1);

        System.out.println("\n=== 方法2：数学公式 ===");
        Node head2 = new Node(1);
        head2.next = new Node(2);
        head2.next.next = new Node(3);
        head2.next.next.next = new Node(4);
        head2.next.next.next.next = new Node(5);
        head2.next.next.next.next.next = head2; // 形成循环
        
        System.out.print("初始状态：");
        print(head2);
        
        head2 = live2(head2, 3);
        System.out.print("最终幸存者：");
        print(head2);
        
        // 验证不同参数的结果
        System.out.println("\n=== 不同参数测试 ===");
        for (int n = 5; n <= 10; n++) {
            for (int m = 2; m <= 4; m++) {
                System.out.println("n=" + n + ", m=" + m + ", 幸存者位置: " + liveIdx(n, m));
            }
        }
    }
}
