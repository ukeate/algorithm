package basic.c47;

import java.util.Stack;

/**
 * 恢复二叉搜索树问题
 * 
 * 问题描述：
 * 在一个二叉搜索树中，有且仅有两个节点的值被错误地交换了
 * 请在不改变树结构的前提下，找出这两个错误的节点并将它们的值交换回来
 * 
 * 关键思想：
 * 1. 利用BST的中序遍历应该是严格递增序列的性质
 * 2. 如果有两个节点被交换，中序遍历中会出现逆序对
 * 3. 通过Morris遍历或栈模拟中序遍历找到错误的节点
 * 4. 找到父节点信息，进行复杂的指针重连操作
 * 
 * 错误模式分析：
 * 假设原本正确的序列是：1,2,3,4,5,6,7,8
 * - 如果交换相邻节点（如3,4）：1,2,4,3,5,6,7,8 -> 一个逆序对
 * - 如果交换远距离节点（如2,7）：1,7,3,4,5,6,2,8 -> 两个逆序对
 * 
 * 算法步骤：
 * 1. 中序遍历找到两个错误节点
 * 2. 找到这两个节点的父节点
 * 3. 根据节点间的位置关系进行复杂的指针重连
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(h) 其中h是树的高度
 * 
 * @author 算法学习
 */
public class RecoverBST {
    
    /**
     * 二叉树节点定义
     */
    public static class Node {
        public int val;      // 节点值
        public Node left;    // 左子节点
        public Node right;   // 右子节点

        public Node(int v) {
            val = v;
        }
    }

    /**
     * 通过中序遍历找到两个被错误交换的节点
     * 
     * @param head 二叉树根节点
     * @return 长度为2的数组，[第一个错误节点, 第二个错误节点]
     * 
     * 算法原理：
     * BST的中序遍历应该是严格递增的，如果出现pre.val > cur.val，
     * 说明找到了一个逆序对。根据逆序对的数量可以确定错误节点：
     * - 一个逆序对：相邻节点交换，errs[0]=pre, errs[1]=cur
     * - 两个逆序对：远距离节点交换，errs[0]=第一个逆序对的pre，errs[1]=第二个逆序对的cur
     */
    private static Node[] getTwoErrNodes(Node head) {
        Node[] errs = new Node[2];  // 存储两个错误节点
        if (head == null) {
            return errs;
        }
        
        Stack<Node> stack = new Stack<>();
        Node pre = null;  // 前一个访问的节点
        
        // 中序遍历
        while (!stack.isEmpty() || head != null) {
            if (head != null) {
                stack.push(head);
                head = head.left;
            } else {
                head = stack.pop();
                
                // 检查是否出现逆序对
                if (pre != null && pre.val > head.val) {
                    // 第一次发现逆序对时，pre是第一个错误节点的候选
                    errs[0] = errs[0] == null ? pre : errs[0];
                    // 每次发现逆序对时，cur都可能是第二个错误节点
                    errs[1] = head;
                }
                
                pre = head;
                head = head.right;
            }
        }
        return errs;
    }

    /**
     * 找到两个错误节点的父节点
     * 
     * @param head 二叉树根节点
     * @param e1 第一个错误节点
     * @param e2 第二个错误节点
     * @return 长度为2的数组，[e1的父节点, e2的父节点]
     * 
     * 算法思路：
     * 遍历整个树，对每个节点检查其左右子节点是否是目标错误节点
     */
    private static Node[] getTwoParents(Node head, Node e1, Node e2) {
        Node[] parents = new Node[2];
        if (head == null) {
            return parents;
        }
        
        Stack<Node> stack = new Stack<>();
        
        // 遍历整个树查找父节点
        while (!stack.isEmpty() || head != null) {
            if (head != null) {
                stack.push(head);
                head = head.left;
            } else {
                head = stack.pop();
                
                // 检查当前节点是否是e1或e2的父节点
                if (head.left == e1 || head.right == e1) {
                    parents[0] = head;
                }
                if (head.left == e2 || head.right == e2) {
                    parents[1] = head;
                }
                
                head = head.right;
            }
        }
        return parents;
    }

    /**
     * 恢复BST：交换两个错误节点的位置
     * 
     * @param head 二叉树根节点
     * @return 恢复后的二叉树根节点
     * 
     * 算法复杂性：
     * 需要考虑各种情况的节点位置关系：
     * 1. 其中一个错误节点是根节点
     * 2. 两个错误节点是父子关系
     * 3. 两个错误节点没有直接关系
     * 
     * 每种情况都需要仔细处理指针的重连，确保树结构的完整性
     */
    public static Node recover(Node head) {
        // 第一步：找到两个错误节点
        Node[] errs = getTwoErrNodes(head);
        // 第二步：找到错误节点的父节点
        Node[] parents = getTwoParents(head, errs[0], errs[1]);
        
        // 提取节点信息，简化后续代码
        Node e1 = errs[0], e1p = parents[0], e1l = e1.left, e1r = e1.right;
        Node e2 = errs[1], e2p = parents[1], e2l = e2.left, e2r = e2.right;
        
        // 情况1：e1是根节点
        if (e1 == head) {
            // e1移动到e2的位置，继承e2的子树
            e1.left = e2l;
            e1.right = e2r;
            
            if (e1 == e2p) {
                // e1是e2的父节点（根节点）
                e2.right = e1;
                e2.left = e1l;
            } else if (e2p.left == e2) {
                // e2是其父节点的左子节点
                e2p.left = e1;
                e2.left = e1l;
                e2.right = e1r;
            } else {
                // e2是其父节点的右子节点
                e2p.right = e1;
                e2.left = e1l;
                e2.right = e1r;
            }
            head = e2;  // e2成为新的根节点
            
        // 情况2：e2是根节点
        } else if (e2 == head) {
            // e2移动到e1的位置，继承e1的子树
            e2.left = e1l;
            e2.right = e1r;
            
            if (e2 == e1p) {
                // e2是e1的父节点（根节点）
                e1.left = e2;
                e1.right = e2r;
            } else if (e1p.left == e1) {
                // e1是其父节点的左子节点
                e1p.left = e2;
                e1.left = e2l;
                e1.right = e2r;
            } else {
                // e1是其父节点的右子节点
                e1p.right = e2;
                e1.left = e2l;
                e1.right = e2r;
            }
            head = e1;  // e1成为新的根节点
            
        // 情况3：都不是根节点
        } else {
            if (e1 == e2p) {
                // e1是e2的父节点
                e1.left = e2l;
                e1.right = e2r;
                e2.left = e1l;
                e2.right = e1;
                
                // 更新e1的父节点指向
                if (e1p.left == e1) {
                    e1p.left = e2;
                } else {
                    e1p.right = e2;
                }
                
            } else if (e2 == e1p) {
                // e2是e1的父节点
                e2.left = e1l;
                e2.right = e1r;
                e1.left = e2;
                e1.right = e2r;
                
                // 更新e2的父节点指向
                if (e2p.left == e2) {
                    e2p.left = e1;
                } else {
                    e2p.right = e1;
                }
                
            } else {
                // e1和e2没有直接父子关系，直接交换位置
                e2.left = e1l;
                e2.right = e1r;
                e1.left = e2l;
                e1.right = e2r;
                
                // 更新e1的父节点指向
                if (e1p.left == e1) {
                    e1p.left = e2;
                } else {
                    e1p.right = e2;
                }
                
                // 更新e2的父节点指向
                if (e2p.left == e2) {
                    e2p.left = e1;
                } else {
                    e2p.right = e1;
                }
            }
        }
        
        return head;
    }

    /**
     * 生成指定数量的空格字符串（用于打印树形结构）
     */
    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    /**
     * 以树形结构打印二叉树
     */
    private static void printIn(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printIn(head.right, height + 1, "v", len);
        String val = to + head.val + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printIn(head.left, height + 1, "^", len);
    }

    /**
     * 打印二叉树
     */
    private static void print(Node head) {
        printIn(head, 0, "H", 17);
    }

    /**
     * 验证是否为有效的BST
     * 
     * @param head 二叉树根节点
     * @return 如果是有效BST返回true，否则返回false
     */
    private static boolean isBST(Node head) {
        if (head == null) {
            return false;
        }
        
        Stack<Node> stack = new Stack<>();
        Node pre = null;
        
        // 中序遍历验证是否严格递增
        while (!stack.isEmpty() || head != null) {
            if (head != null) {
                stack.push(head);
                head = head.left;
            } else {
                head = stack.pop();
                
                // 如果出现逆序，则不是有效BST
                if (pre != null && pre.val > head.val) {
                    return false;
                }
                
                pre = head;
                head = head.right;
            }
        }
        return true;
    }

    /**
     * 测试方法：验证各种BST恢复场景
     */
    public static void main(String[] args) {
        // 构建标准BST
        Node head = new Node(5);
        head.left = new Node(3);
        head.right = new Node(7);
        head.left.left = new Node(2);
        head.left.right = new Node(4);
        head.right.left = new Node(6);
        head.right.right = new Node(8);
        head.left.left.left = new Node(1);
        
        System.out.println("=== 原始正确的BST ===");
        print(head);
        System.out.println("是否为BST: " + isBST(head));

        // 测试情况1：根节点与叶子节点交换（7,5）
        System.out.println("\n=== 情况1：根节点与右子节点交换（7,5） ===");
        Node head1 = new Node(7);
        head1.left = new Node(3);
        head1.right = new Node(5);
        head1.left.left = new Node(2);
        head1.left.right = new Node(4);
        head1.right.left = new Node(6);
        head1.right.right = new Node(8);
        head1.left.left.left = new Node(1);
        
        System.out.println("错误的BST:");
        print(head1);
        System.out.println("是否为BST: " + isBST(head1));
        
        Node res1 = recover(head1);
        System.out.println("恢复后的BST:");
        print(res1);
        System.out.println("是否为BST: " + isBST(res1));

        // 测试情况2：根节点与更深层节点交换（6,5）
        System.out.println("\n=== 情况2：根节点与深层节点交换（6,5） ===");
        Node head2 = new Node(6);
        head2.left = new Node(3);
        head2.right = new Node(7);
        head2.left.left = new Node(2);
        head2.left.right = new Node(4);
        head2.right.left = new Node(5);
        head2.right.right = new Node(8);
        head2.left.left.left = new Node(1);
        
        System.out.println("错误的BST:");
        print(head2);
        System.out.println("是否为BST: " + isBST(head2));
        
        Node res2 = recover(head2);
        System.out.println("恢复后的BST:");
        print(res2);
        System.out.println("是否为BST: " + isBST(res2));

        // 测试情况3：非根节点的交换（相邻层级）
        System.out.println("\n=== 情况3：非根节点交换（4,3） ===");
        Node head7 = new Node(5);
        head7.left = new Node(4);
        head7.right = new Node(7);
        head7.left.left = new Node(2);
        head7.left.right = new Node(3);
        head7.right.left = new Node(6);
        head7.right.right = new Node(8);
        head7.left.left.left = new Node(1);
        
        System.out.println("错误的BST:");
        print(head7);
        System.out.println("是否为BST: " + isBST(head7));
        
        Node res7 = recover(head7);
        System.out.println("恢复后的BST:");
        print(res7);
        System.out.println("是否为BST: " + isBST(res7));

        // 性能测试提示
        System.out.println("\n=== 算法性能分析 ===");
        System.out.println("时间复杂度: O(n) - 需要遍历树两次");
        System.out.println("空间复杂度: O(h) - 栈空间，h为树的高度");
        System.out.println("算法特点: 在不改变树结构的前提下，仅通过指针重连恢复BST性质");
        System.out.println("应用场景: 数据结构修复、错误检测与恢复");
    }
}
