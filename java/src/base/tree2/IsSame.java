package base.tree2;

/**
 * 判断两个二叉树是否相同
 * 
 * 相同二叉树定义：两个二叉树在结构上相同，且对应位置的节点值也相同
 * 
 * 算法思路：
 * 使用递归方法同时遍历两棵树：
 * 1. 如果两个节点都为空，返回true
 * 2. 如果只有一个节点为空，返回false  
 * 3. 如果两个节点都不为空，比较节点值并递归检查左右子树
 * 
 * 时间复杂度：O(min(m,n))，其中m和n分别是两棵树的节点数
 * 空间复杂度：O(min(m,n))（递归栈空间）
 */
public class IsSame {

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
     * 判断两个二叉树是否相同
     * @param p 第一棵树的根节点
     * @param q 第二棵树的根节点
     * @return 两棵树是否相同
     */
    public static boolean isSame(Node p, Node q) {
        // 情况1：如果只有一个节点为空，则两棵树不同
        if (p == null ^ q == null) {
            return false;
        }
        
        // 情况2：如果两个节点都为空，则相同
        if (p == null && q == null) {
            return true;
        }
        
        // 情况3：两个节点都不为空，比较节点值并递归检查左右子树
        return p.val == q.val && isSame(p.left, q.left) && isSame(p.right, q.right);
    }

    public static void main(String[] args) {
        // 测试用例可以在这里添加
        // 例如：构建两棵相同的树和两棵不同的树进行测试
    }
}
