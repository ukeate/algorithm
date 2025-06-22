package base.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * N叉树与二叉树相互转换编码算法
 * 
 * 问题描述：
 * 设计一种编码算法，将N叉树转换为二叉树，并能够从二叉树还原出原始的N叉树。
 * 
 * 核心思想：
 * 利用二叉树的左右指针来表示N叉树的结构关系：
 * - 左指针：指向当前节点的第一个子节点
 * - 右指针：指向当前节点的下一个兄弟节点
 * 
 * 编码策略（N叉树 -> 二叉树）：
 * 1. 对于N叉树的每个节点，其所有子节点通过右指针连成链表
 * 2. 父节点的左指针指向第一个子节点
 * 3. 兄弟节点通过右指针相连
 * 
 * 解码策略（二叉树 -> N叉树）：
 * 1. 二叉树的左指针对应N叉树的子节点关系
 * 2. 二叉树的右指针对应N叉树的兄弟节点关系
 * 3. 遍历右指针链表收集所有子节点
 * 
 * 应用场景：
 * - 文件系统目录结构转换
 * - 组织架构图数据转换
 * - XML/JSON树结构处理
 * - 数据库树形数据存储优化
 * 
 * LeetCode链接：https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree
 * 
 * @author algorithm-base
 * @version 1.0
 */
// https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree
public class EncodeNary {
    
    /**
     * N叉树节点定义
     */
    public static class Node {
        public int val;              // 节点值
        public List<Node> children;  // 子节点列表

        public Node() {}

        public Node(int v) {
            val = v;
        }

        public Node(int v, List<Node> c) {
            val = v;
            children = c;
        }

        @Override
        public String toString() {
            return "Node{" + val + ", children=" + (children != null ? children.size() : 0) + "}";
        }
    }

    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;           // 节点值
        TreeNode left;     // 左子节点（对应N叉树的第一个子节点）
        TreeNode right;    // 右子节点（对应N叉树的下一个兄弟节点）

        TreeNode(int x) {
            val = x;
        }

        @Override
        public String toString() {
            return "TreeNode{" + val + "}";
        }
    }

    /**
     * N叉树与二叉树转换编解码器
     */
    class Codec {
        
        /**
         * 将N叉树的子节点列表编码为二叉树的兄弟链表
         * 
         * 编码规则：
         * 1. 将所有子节点通过右指针连成一条链表
         * 2. 每个子节点的左指针递归处理其自己的子节点
         * 3. 链表的第一个节点作为父节点的左子节点
         * 
         * 示例：N叉树节点A有子节点[B, C, D]
         * 编码后的二叉树结构：
         *     A
         *    /
         *   B -> C -> D -> null
         *  /    /    /
         * ...  ... ...
         * 
         * @param children N叉树的子节点列表
         * @return 编码后的二叉树头节点（兄弟链表的第一个节点）
         */
        private TreeNode en(List<Node> children) {
            TreeNode head = null;     // 兄弟链表的头节点
            TreeNode cur = null;      // 当前处理的节点
            
            // 遍历所有子节点，构建兄弟链表
            for (Node child : children) {
                TreeNode tNode = new TreeNode(child.val);  // 创建对应的二叉树节点
                
                if (head == null) {
                    head = tNode;     // 第一个子节点作为链表头
                } else {
                    cur.right = tNode; // 通过右指针连接兄弟节点
                }
                cur = tNode;
                
                // 递归处理当前子节点的子节点列表
                cur.left = en(child.children);
            }
            return head;
        }

        /**
         * N叉树编码为二叉树的主方法
         * 
         * 编码过程：
         * 1. 如果N叉树根节点为空，返回空的二叉树
         * 2. 创建二叉树根节点，值与N叉树根节点相同
         * 3. 将N叉树根节点的子节点列表编码为二叉树的左子树
         * 
         * @param root N叉树的根节点
         * @return 编码后的二叉树根节点
         * 
         * 时间复杂度：O(N)，每个N叉树节点访问一次
         * 空间复杂度：O(N)，创建对应数量的二叉树节点
         */
        public TreeNode encode(Node root) {
            if (root == null) {
                return null;
            }
            
            // 创建二叉树根节点
            TreeNode head = new TreeNode(root.val);
            
            // 编码子节点列表为左子树
            head.left = en(root.children);
            
            return head;
        }

        /**
         * 将二叉树的兄弟链表解码为N叉树的子节点列表
         * 
         * 解码规则：
         * 1. 沿着右指针遍历整个兄弟链表
         * 2. 对每个兄弟节点，递归解码其左子树为子节点列表
         * 3. 构建N叉树节点并添加到子节点列表中
         * 
         * @param root 二叉树兄弟链表的头节点
         * @return 解码后的N叉树子节点列表
         */
        private List<Node> de(TreeNode root) {
            List<Node> children = new ArrayList<>();
            
            // 遍历兄弟链表（右指针链表）
            while (root != null) {
                // 创建N叉树节点，递归解码其子节点
                Node cur = new Node(root.val, de(root.left));
                children.add(cur);
                
                // 移动到下一个兄弟节点
                root = root.right;
            }
            
            return children;
        }

        /**
         * 二叉树解码为N叉树的主方法
         * 
         * 解码过程：
         * 1. 如果二叉树根节点为空，返回空的N叉树
         * 2. 创建N叉树根节点，值与二叉树根节点相同
         * 3. 将二叉树的左子树解码为N叉树根节点的子节点列表
         * 
         * @param root 二叉树的根节点
         * @return 解码后的N叉树根节点
         * 
         * 时间复杂度：O(N)，每个二叉树节点访问一次
         * 空间复杂度：O(N)，创建对应数量的N叉树节点
         */
        public Node decode(TreeNode root) {
            if (root == null) {
                return null;
            }
            
            // 创建N叉树根节点，解码子节点列表
            return new Node(root.val, de(root.left));
        }
    }

    /**
     * 构建测试用的N叉树
     * 树结构：      1
     *            / | \
     *           3  2  4
     *          /|
     *         5 6
     */
    private static Node buildTestNaryTree() {
        Node root = new Node(1);
        root.children = new ArrayList<>();
        
        Node node3 = new Node(3);
        node3.children = new ArrayList<>();
        node3.children.add(new Node(5));
        node3.children.add(new Node(6));
        
        Node node2 = new Node(2);
        Node node4 = new Node(4);
        
        root.children.add(node3);
        root.children.add(node2);
        root.children.add(node4);
        
        return root;
    }

    /**
     * 打印N叉树结构（递归前序遍历）
     */
    private static void printNaryTree(Node root, String prefix) {
        if (root == null) return;
        
        System.out.println(prefix + "节点值: " + root.val + 
                         ", 子节点数: " + (root.children != null ? root.children.size() : 0));
        
        if (root.children != null) {
            for (int i = 0; i < root.children.size(); i++) {
                printNaryTree(root.children.get(i), prefix + "  ");
            }
        }
    }

    /**
     * 打印二叉树结构（中序遍历）
     */
    private static void printBinaryTree(TreeNode root, String prefix) {
        if (root == null) return;
        
        printBinaryTree(root.left, prefix + "  ");
        System.out.println(prefix + "节点值: " + root.val);
        printBinaryTree(root.right, prefix + "  ");
    }

    /**
     * 验证编解码正确性：比较两个N叉树是否相同
     */
    private static boolean isSameNaryTree(Node tree1, Node tree2) {
        if (tree1 == null && tree2 == null) return true;
        if (tree1 == null || tree2 == null) return false;
        if (tree1.val != tree2.val) return false;
        
        List<Node> children1 = tree1.children != null ? tree1.children : new ArrayList<>();
        List<Node> children2 = tree2.children != null ? tree2.children : new ArrayList<>();
        
        if (children1.size() != children2.size()) return false;
        
        for (int i = 0; i < children1.size(); i++) {
            if (!isSameNaryTree(children1.get(i), children2.get(i))) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 测试N叉树与二叉树转换算法
     * 
     * 测试流程：
     * 1. 构建测试用的N叉树
     * 2. 编码N叉树为二叉树
     * 3. 解码二叉树为N叉树
     * 4. 验证解码结果与原始N叉树是否一致
     * 5. 展示编码前后的树结构对比
     */
    public static void main(String[] args) {
        System.out.println("N叉树与二叉树转换算法测试");
        System.out.println("===========================");
        
        EncodeNary encoder = new EncodeNary();
        Codec codec = encoder.new Codec();
        
        // 构建测试N叉树
        Node originalTree = buildTestNaryTree();
        
        System.out.println("原始N叉树结构：");
        printNaryTree(originalTree, "");
        System.out.println();
        
        // 编码：N叉树 -> 二叉树
        System.out.println("编码过程：N叉树 -> 二叉树");
        TreeNode encodedTree = codec.encode(originalTree);
        System.out.println("编码后的二叉树结构：");
        printBinaryTree(encodedTree, "");
        System.out.println();
        
        // 解码：二叉树 -> N叉树
        System.out.println("解码过程：二叉树 -> N叉树");
        Node decodedTree = codec.decode(encodedTree);
        System.out.println("解码后的N叉树结构：");
        printNaryTree(decodedTree, "");
        System.out.println();
        
        // 验证正确性
        boolean isCorrect = isSameNaryTree(originalTree, decodedTree);
        System.out.println("编解码正确性验证：" + (isCorrect ? "通过 ✓" : "失败 ✗"));
        System.out.println();
        
        // 算法特点总结
        System.out.println("算法特点总结：");
        System.out.println("================");
        System.out.println("编码策略：");
        System.out.println("• 左指针：表示父子关系（指向第一个子节点）");
        System.out.println("• 右指针：表示兄弟关系（指向下一个兄弟节点）");
        System.out.println("• 空间效率：无额外空间开销，一一对应转换");
        System.out.println();
        System.out.println("时间复杂度：O(N) - 每个节点访问一次");
        System.out.println("空间复杂度：O(N) - 创建对应数量的新节点");
        System.out.println();
        System.out.println("应用场景：");
        System.out.println("• 文件系统目录结构存储优化");
        System.out.println("• 组织架构数据的二叉树表示");
        System.out.println("• XML/JSON等层次数据的扁平化存储");
        System.out.println("• 数据库中树形结构的优化存储");
    }
}
