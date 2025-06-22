package base.tree;

/**
 * 二叉树可视化打印工具
 * 功能：将二叉树以直观的树形结构打印到控制台
 * 打印格式：右子树在上，左子树在下，根节点在中间
 * 使用不同的符号标识节点类型：H(头节点)、v(右子节点)、^(左子节点)
 */
public class Print {
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
     * 生成指定数量的空格字符串
     * @param num 空格数量
     * @return 空格字符串
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
     * 递归打印二叉树的核心方法
     * @param head 当前节点
     * @param height 当前节点的层数（用于控制缩进）
     * @param to 节点标识符（H表示头节点，v表示右子节点，^表示左子节点）
     * @param len 每个节点占用的字符宽度
     */
    private static void printIn(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        // 先打印右子树（显示在上方）
        printIn(head.right, height + 1, "v", len);
        
        // 构造当前节点的显示字符串
        String val = to + head.val + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;  // 左边填充空格数
        int lenR = len - lenM - lenL; // 右边填充空格数
        val = getSpace(lenL) + val + getSpace(lenR);
        
        // 打印当前节点（带层级缩进）
        System.out.println(getSpace(height * len) + val);
        
        // 后打印左子树（显示在下方）
        printIn(head.left, height + 1, "^", len);
    }

    /**
     * 打印二叉树的公共接口
     * @param head 二叉树根节点
     */
    public static void print(Node head) {
        // 从第0层开始，根节点标记为H，每个节点占17个字符宽度
        printIn(head, 0, "H", 17);
    }


    public static void main(String[] args) {
        // 测试用例1：包含负数和极值的复杂树结构
        Node head = new Node(1);
        head.left = new Node(-222222222);
        head.right = new Node(3);
        head.left.left = new Node(Integer.MIN_VALUE);
        head.right.left = new Node(55555555);
        head.right.right = new Node(66);
        head.left.left.right = new Node(777);
        print(head);

        // 测试用例2：标准的满二叉树结构
        head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.right.left = new Node(5);
        head.right.right = new Node(6);
        head.left.left.right = new Node(7);
        print(head);

        // 测试用例3：所有节点值相同的树结构
        head = new Node(1);
        head.left = new Node(1);
        head.right = new Node(1);
        head.left.left = new Node(1);
        head.right.left = new Node(1);
        head.right.right = new Node(1);
        head.left.left.right = new Node(1);
        print(head);
    }
}
