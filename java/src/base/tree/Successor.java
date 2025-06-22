package base.tree;

/**
 * 二叉搜索树中寻找后继节点
 * 
 * 问题描述：给定一个二叉搜索树和其中的一个节点，找到该节点在中序遍历序列中的下一个节点
 * 
 * 算法思路：
 * 1. 如果节点有右子树，那么后继节点就是右子树中的最左节点
 * 2. 如果节点没有右子树，那么需要向上寻找：
 *    - 如果当前节点是其父节点的左子节点，那么父节点就是后继节点
 *    - 如果当前节点是其父节点的右子节点，那么继续向上寻找，
 *      直到找到一个节点，它是其父节点的左子节点，这个父节点就是后继节点
 * 
 * 时间复杂度：O(h)，其中h是树的高度
 * 空间复杂度：O(1)
 */
public class Successor {
    /**
     * 二叉搜索树节点定义（包含父节点指针）
     */
    public static class Node {
        public int val;       // 节点值
        public Node left;     // 左子节点
        public Node right;    // 右子节点
        public Node parent;   // 父节点

        public Node(int v) {
            this.val = v;
        }
    }

    /**
     * 找到以node为根的子树中的最左节点
     * @param node 子树根节点
     * @return 最左节点
     */
    private static Node leftMost(Node node) {
        if (node == null) {
            return node;
        }
        // 一直向左走到底
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * 寻找给定节点的后继节点
     * @param node 给定节点
     * @return 后继节点，如果不存在则返回null
     */
    public static Node successor(Node node) {
        if (node == null) {
            return null;
        }
        
        // 情况1：如果节点有右子树，后继节点是右子树的最左节点
        if (node.right != null) {
            return leftMost(node.right);
        } else {
            // 情况2：节点没有右子树，需要向上寻找
            Node parent = node.parent;
            // 当当前节点是其父节点的右子节点时，继续向上寻找
            while (parent != null && parent.right == node) {
                node = parent;
                parent = node.parent;
            }
            // 找到的父节点就是后继节点（如果存在的话）
            return parent;
        }
    }

    public static void main(String[] args) {
        // 构建测试用的二叉搜索树
        //       6
        //     /   \
        //    3     9
        //   / \   / \
        //  1   4 8  10
        //   \   \  /
        //    2   5 7
        
        Node head = new Node(6);
        head.parent = null;
        head.left = new Node(3);
        head.left.parent = head;
        head.left.left = new Node(1);
        head.left.left.parent = head.left;
        head.left.left.right = new Node(2);
        head.left.left.right.parent = head.left.left;
        head.left.right = new Node(4);
        head.left.right.parent = head.left;
        head.left.right.right = new Node(5);
        head.left.right.right.parent = head.left.right;
        head.right = new Node(9);
        head.right.parent = head;
        head.right.left = new Node(8);
        head.right.left.parent = head.right;
        head.right.left.left = new Node(7);
        head.right.left.left.parent = head.right.left;
        head.right.right = new Node(10);
        head.right.right.parent = head.right;

        // 测试各节点的后继节点
        // 中序遍历序列：1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 -> 9 -> 10
        
        Node test = head.left.left;  // 节点1
        System.out.println(test.val + " next: " + successor(test).val);  // 1的后继是2
        
        test = head.left.left.right;  // 节点2
        System.out.println(test.val + " next: " + successor(test).val);  // 2的后继是3
        
        test = head.left;  // 节点3
        System.out.println(test.val + " next: " + successor(test).val);  // 3的后继是4
        
        test = head.left.right;  // 节点4
        System.out.println(test.val + " next: " + successor(test).val);  // 4的后继是5
        
        test = head.left.right.right;  // 节点5
        System.out.println(test.val + " next: " + successor(test).val);  // 5的后继是6
        
        test = head;  // 节点6
        System.out.println(test.val + " next: " + successor(test).val);  // 6的后继是7
        
        test = head.right.left.left;  // 节点7
        System.out.println(test.val + " next: " + successor(test).val);  // 7的后继是8
        
        test = head.right.left;  // 节点8
        System.out.println(test.val + " next: " + successor(test).val);  // 8的后继是9
        
        test = head.right;  // 节点9
        System.out.println(test.val + " next: " + successor(test).val);  // 9的后继是10
        
        test = head.right.right;  // 节点10（最大节点）
        System.out.println(test.val + " next: " + successor(test));  // 10的后继是null
    }
}
