package basic.c34;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉搜索树转双向链表问题
 * 
 * 问题描述：
 * 将一个二叉搜索树转换为排序的循环双向链表。
 * 要求在原地转换，不能创建新节点，只能调整指针。
 * 转换后的链表应该保持排序顺序（中序遍历顺序）。
 * 
 * 解决方案：
 * 1. 方法1：使用中序遍历+队列，空间复杂度O(N)
 * 2. 方法2：递归分治法，空间复杂度O(H)（只有递归栈）
 * 
 * 核心思想：
 * - left指针作为前驱指针（prev）
 * - right指针作为后继指针（next）
 * - 保持中序遍历的顺序
 * 
 * 时间复杂度：O(N)，其中N是树中节点的个数
 * 空间复杂度：方法1为O(N)，方法2为O(H)，H为树的高度
 */
public class BstToDoubleLinkedList {
    
    /**
     * 二叉树节点定义
     */
    private static class Node {
        public int val;      // 节点值
        public Node left;    // 左子节点（转换后作为前驱节点）
        public Node right;   // 右子节点（转换后作为后继节点）
        
        /**
         * 构造函数
         * @param v 节点值
         */
        public Node(int v) {
            this.val = v;
        }
    }

    /**
     * 中序遍历辅助方法：将节点按中序遍历顺序加入队列
     * @param head 当前节点
     * @param que 存储节点的队列
     */
    private static void in(Node head, Queue<Node> que) {
        if (head == null) {
            return;
        }
        
        // 中序遍历：左 -> 根 -> 右
        in(head.left, que);     // 遍历左子树
        que.offer(head);        // 访问当前节点
        in(head.right, que);    // 遍历右子树
    }

    /**
     * 方法1：使用中序遍历+队列的解法
     * 
     * 算法步骤：
     * 1. 通过中序遍历将所有节点按顺序存入队列
     * 2. 逐个从队列中取出节点，重新连接left和right指针
     * 3. 形成双向链表
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)，需要队列存储所有节点
     * 
     * @param head 二叉搜索树的根节点
     * @return 转换后的双向链表的头节点
     */
    public static Node convert1(Node head) {
        Queue<Node> que = new LinkedList<>();
        
        // 步骤1：中序遍历，将节点按顺序存入队列
        in(head, que);
        
        // 边界情况：空树
        if (que.isEmpty()) {
            return head;
        }
        
        // 步骤2：从队列中依次取出节点，重新连接指针
        head = que.poll();       // 新的头节点
        Node pre = head;         // 前一个节点
        pre.left = null;         // 头节点的left指针置空
        Node cur = null;         // 当前节点
        
        // 逐个连接节点形成双向链表
        while (!que.isEmpty()) {
            cur = que.poll();
            pre.right = cur;     // 前节点的right指向当前节点
            cur.left = pre;      // 当前节点的left指向前节点
            pre = cur;           // 更新前节点
        }
        
        // 尾节点的right指针置空
        pre.right = null;
        
        return head;
    }

    /**
     * 信息类：用于递归方法的返回值
     * 包含转换后链表的头节点和尾节点信息
     */
    private static class Info {
        public Node start;  // 链表的头节点
        public Node end;    // 链表的尾节点
        
        /**
         * 构造函数
         * @param start 头节点
         * @param end 尾节点
         */
        public Info(Node start, Node end) {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * 递归处理方法：将以x为根的子树转换为双向链表
     * 
     * 算法思想：
     * 1. 递归处理左子树，得到左侧链表的头尾节点
     * 2. 递归处理右子树，得到右侧链表的头尾节点
     * 3. 将当前节点作为中间节点，连接左右两部分
     * 4. 返回整个链表的头尾节点信息
     * 
     * @param x 当前子树的根节点
     * @return 包含转换后链表头尾节点的信息对象
     */
    private static Info process(Node x) {
        if (x == null) {
            return new Info(null, null);
        }
        
        // 递归处理左右子树
        Info li = process(x.left);   // 左子树的链表信息
        Info ri = process(x.right);  // 右子树的链表信息
        
        // 连接左子树的尾节点与当前节点
        if (li.end != null) {
            li.end.right = x;        // 左子树尾节点指向当前节点
        }
        x.left = li.end;             // 当前节点的left指向左子树尾节点
        
        // 连接当前节点与右子树的头节点
        x.right = ri.start;          // 当前节点的right指向右子树头节点
        if (ri.start != null) {
            ri.start.left = x;       // 右子树头节点的left指向当前节点
        }
        
        // 返回整个链表的头尾信息
        // 头节点：如果左子树存在则为左子树的头，否则为当前节点
        // 尾节点：如果右子树存在则为右子树的尾，否则为当前节点
        return new Info(
            li.start != null ? li.start : x,
            ri.end != null ? ri.end : x
        );
    }

    /**
     * 方法2：递归分治法的解法（最优解）
     * 
     * 算法思想：
     * 使用递归分治，每个子树独立转换为双向链表，然后合并
     * 
     * 优势：
     * - 空间复杂度更低，只有递归栈的开销
     * - 思路清晰，符合分治算法的思想
     * 
     * 时间复杂度：O(N)
     * 空间复杂度：O(H)，H为树的高度
     * 
     * @param head 二叉搜索树的根节点
     * @return 转换后的双向链表的头节点
     */
    public static Node convert2(Node head) {
        if (head == null) {
            return null;
        }
        return process(head).start;
    }

    /**
     * 辅助方法：中序打印二叉树
     * @param head 根节点
     */
    private static void printIn(Node head) {
        if (head == null) {
            return;
        }
        printIn(head.left);          // 打印左子树
        System.out.print(head.val + " ");  // 打印当前节点
        printIn(head.right);         // 打印右子树
    }

    /**
     * 辅助方法：打印双向链表（正向和反向）
     * @param head 链表头节点
     */
    private static void printLink(Node head) {
        System.out.print("正向遍历: ");
        Node end = null;
        
        // 正向遍历
        while (head != null) {
            System.out.print(head.val + " ");
            end = head;              // 记录尾节点
            head = head.right;       // 移动到下一个节点
        }
        
        System.out.print("| 反向遍历: ");
        
        // 反向遍历（验证双向链表的正确性）
        while (end != null) {
            System.out.print(end.val + " ");
            end = end.left;          // 通过left指针反向移动
        }
        System.out.println();
    }

    /**
     * 测试方法：构建测试用例验证两种算法
     */
    public static void main(String[] args) {
        // 构建测试二叉搜索树：
        //       5
        //      / \
        //     2   9
        //    / \ / \
        //   1  3 7  10
        //     \ /\
        //      4 6 8
        
        Node head = new Node(5);
        head.left = new Node(2);
        head.right = new Node(9);
        head.left.left = new Node(1);
        head.left.right = new Node(3);
        head.left.right.right = new Node(4);
        head.right.left = new Node(7);
        head.right.right = new Node(10);
        head.right.left.left = new Node(6);
        head.right.left.right = new Node(8);

        System.out.println("=== 测试方法1：中序遍历+队列 ===");
        System.out.print("原始BST中序遍历: ");
        printIn(head);
        System.out.println();
        
        head = convert1(head);
        System.out.println("转换后的双向链表:");
        printLink(head);

        // 重新构建相同的测试树进行方法2测试
        head = new Node(5);
        head.left = new Node(2);
        head.right = new Node(9);
        head.left.left = new Node(1);
        head.left.right = new Node(3);
        head.left.right.right = new Node(4);
        head.right.left = new Node(7);
        head.right.right = new Node(10);
        head.right.left.left = new Node(6);
        head.right.left.right = new Node(8);

        System.out.println("\n=== 测试方法2：递归分治法 ===");
        System.out.print("原始BST中序遍历: ");
        printIn(head);
        System.out.println();
        
        head = convert2(head);
        System.out.println("转换后的双向链表:");
        printLink(head);
    }
}
