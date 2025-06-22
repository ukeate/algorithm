package base.tree;

import java.util.Stack;

/**
 * 二叉树的三种遍历方式（非递归实现）
 * 
 * 包含以下遍历方法：
 * 1. 先序遍历（前序遍历）：根 -> 左 -> 右
 * 2. 中序遍历：左 -> 根 -> 右  
 * 3. 后序遍历：左 -> 右 -> 根
 * 
 * 所有方法都使用栈来模拟递归过程，避免了递归调用的开销
 */
public class Traversal {

    /**
     * 二叉树节点定义
     */
    private static class Node {
        public int val;      // 节点值
        public Node left;    // 左子节点
        public Node right;   // 右子节点

        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * 递归遍历模板（用于理解遍历顺序）
     * @param head 当前节点
     */
    public static void traversal(Node head) {
        if (head == null) {
            return;
        }
        // 这里可以处理当前节点 - 先序位置
        traversal(head.left);   // 递归处理左子树
        // 这里可以处理当前节点 - 中序位置
        traversal(head.right);  // 递归处理右子树
        // 这里可以处理当前节点 - 后序位置
    }

    /**
     * 先序遍历（非递归实现）
     * 算法思路：使用栈，先压入右子节点，再压入左子节点
     * 这样弹出时就是先左后右的顺序
     * @param cur 树的根节点
     */
    public static void pre(Node cur) {
        if (cur == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(cur);
        while (!stack.isEmpty()) {
            cur = stack.pop();
            System.out.print(cur.val + ",");  // 处理当前节点
            // 先压入右子节点，再压入左子节点
            // 这样弹出时左子节点在右子节点之前被处理
            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left != null) {
                stack.push(cur.left);
            }
        }
    }

    /**
     * 中序遍历（非递归实现）
     * 算法思路：先一路向左压栈，到底后弹出并处理节点，然后转向右子树
     * @param cur 树的根节点
     */
    public static void in(Node cur) {
        if (cur == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        while (cur != null || !stack.isEmpty()) {
            // 一路向左，将所有左子节点压入栈中
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                // 左子树处理完毕，弹出节点并处理
                cur = stack.pop();
                System.out.print(cur.val + ",");  // 处理当前节点
                // 转向右子树
                cur = cur.right;
            }
        }
    }

    /**
     * 后序遍历（非递归实现 - 方法1：使用两个栈）
     * 算法思路：第一个栈按照"根右左"的顺序遍历，结果压入第二个栈
     * 第二个栈弹出的顺序就是"左右根"
     * @param cur 树的根节点
     */
    public static void pos1(Node cur) {
        if (cur == null) {
            return;
        }
        Stack<Node> s1 = new Stack<>();  // 辅助栈1
        Stack<Node> s2 = new Stack<>();  // 辅助栈2
        s1.push(cur);
        while (!s1.isEmpty()) {
            cur = s1.pop();
            s2.push(cur);  // 将节点压入第二个栈
            // 先压入左子节点，再压入右子节点
            // 这样弹出顺序是：根 -> 右 -> 左
            if (cur.left != null) {
                s1.push(cur.left);
            }
            if (cur.right != null) {
                s1.push(cur.right);
            }
        }
        // 第二个栈弹出的顺序就是后序遍历：左 -> 右 -> 根
        while (!s2.isEmpty()) {
            System.out.print(s2.pop().val + ",");
        }
    }

    /**
     * 后序遍历（非递归实现 - 方法2：使用一个栈）
     * 算法思路：记录上一次弹出的节点，用于判断何时可以弹出当前节点
     * @param h 树的根节点
     */
    public static void pos2(Node h) {
        if (h == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(h);
        Node c = null;    // 当前栈顶节点
        while (!stack.isEmpty()) {
            c = stack.peek();  // 查看栈顶节点但不弹出
            // 如果左子节点存在且还没有被处理过，压入左子节点
            if (c.left != null && h != c.left && h != c.right) {
                stack.push(c.left);
            } 
            // 如果右子节点存在且还没有被处理过，压入右子节点
            else if (c.right != null && h != c.right) {
                stack.push(c.right);
            } 
            // 左右子树都处理完毕，可以弹出并处理当前节点
            else {
                System.out.print(stack.pop().val + ",");
                h = c;  // 更新上一次处理的节点
            }
        }
    }

    public static void main(String[] args) {
        // 构建测试二叉树
        //       1
        //     /   \
        //    2     3
        //   / \   / \
        //  4   5 6   7
        
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);

        // 测试先序遍历：1,2,4,5,3,6,7
        pre(head);
        System.out.println("========");
        
        // 测试中序遍历：4,2,5,1,6,3,7
        in(head);
        System.out.println("========");
        
        // 测试后序遍历方法1：4,5,2,6,7,3,1
        pos1(head);
        System.out.println("========");
        
        // 测试后序遍历方法2：4,5,2,6,7,3,1
        pos2(head);
        System.out.println("========");
    }
}
