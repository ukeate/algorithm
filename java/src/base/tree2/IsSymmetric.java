package base.tree2;

/**
 * 判断二叉树是否对称（镜像对称）
 * 
 * 对称二叉树定义：一棵二叉树以根节点为中心轴，左子树和右子树互为镜像
 * 
 * 算法思路：
 * 将问题转化为判断两个子树是否互为镜像：
 * 1. 两个节点都为空，互为镜像
 * 2. 只有一个节点为空，不互为镜像
 * 3. 两个节点都不为空，需要满足：
 *    - 节点值相等
 *    - 第一个节点的左子树与第二个节点的右子树互为镜像
 *    - 第一个节点的右子树与第二个节点的左子树互为镜像
 * 
 * 时间复杂度：O(n)，需要访问所有节点
 * 空间复杂度：O(h)，其中h是树的高度（递归栈空间）
 */
public class IsSymmetric {

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
     * 判断两个子树是否互为镜像
     * @param h1 第一个子树的根节点
     * @param h2 第二个子树的根节点
     * @return 是否互为镜像
     */
    private static boolean isMirror(Node h1, Node h2) {
        // 情况1：如果只有一个节点为空，则不互为镜像
        if (h1 == null ^ h2 == null) {
            return false;
        }
        
        // 情况2：如果两个节点都为空，则互为镜像
        if (h1 == null && h2 == null) {
            return true;
        }
        
        // 情况3：两个节点都不为空，需要满足镜像条件：
        // - 节点值相等
        // - h1的左子树与h2的右子树互为镜像
        // - h1的右子树与h2的左子树互为镜像
        return h1.val == h2.val && isMirror(h1.left, h2.right) && isMirror(h1.right, h2.left);
    }

    /**
     * 判断二叉树是否对称
     * @param root 树的根节点
     * @return 是否对称
     */
    public static boolean isSymmetric(Node root) {
        // 将问题转化为判断根节点的左右子树是否互为镜像
        return isMirror(root, root);
    }
}
