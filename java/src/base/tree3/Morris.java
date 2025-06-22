package base.tree3;

/**
 * Morris遍历算法实现
 * Morris遍历是一种基于线索二叉树的遍历方法，可以在O(1)空间复杂度下完成树的遍历
 * 核心思想：利用叶子节点的空指针来存储回到上层的路径信息
 */
public class Morris {
    /**
     * 二叉树节点类
     */
    public static class Node {
        public int val;
        Node left;
        Node right;

        public Node(int v) {
            val = v;
        }
    }

    /**
     * Morris遍历的基本框架
     * 不打印任何内容，只是展示遍历过程
     * @param head 二叉树根节点
     */
    public static void morris(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node r = null;
        while (cur != null) {
            r = cur.left;  // r指向cur的左子树
            if (r != null) {
                // 找到cur左子树的最右节点
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                if (r.right == null) {
                    // 第一次到达cur，建立Morris连接
                    r.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 第二次到达cur，恢复树结构
                    r.right = null;
                }
            }
            // 继续遍历右子树
            cur = cur.right;
        }
    }

    /**
     * Morris先序遍历
     * 先序遍历顺序：根 -> 左 -> 右
     * @param head 二叉树根节点
     */
    public static void morrisPre(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node r = null;
        while (cur != null) {
            r = cur.left;
            if (r != null) {
                // 找到cur左子树的最右节点
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                if (r.right == null) {
                    // 第一次到达cur，打印当前节点（先序）
                    System.out.print(cur.val + ",");
                    r.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 第二次到达cur，恢复树结构
                    r.right = null;
                }
            } else {
                // 没有左子树，直接打印（先序）
                System.out.print(cur.val + ",");
            }
            cur = cur.right;
        }
        System.out.println();
    }

    /**
     * Morris中序遍历
     * 中序遍历顺序：左 -> 根 -> 右
     * @param head 二叉树根节点
     */
    public static void morrisIn(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node r = null;
        while (cur != null) {
            r = cur.left;
            if (r != null) {
                // 找到cur左子树的最右节点
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                if (r.right == null) {
                    // 第一次到达cur，建立Morris连接
                    r.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 第二次到达cur，恢复树结构
                    r.right = null;
                }
            }
            // 打印当前节点（中序：在处理完左子树后打印）
            System.out.print(cur.val + ",");
            cur = cur.right;
        }
        System.out.println();
    }

    /**
     * 反转右边界链表
     * @param from 链表起始节点
     * @return 反转后的头节点
     */
    private static Node reverseEdge(Node from) {
        Node pre = null;
        Node next = null;
        while (from != null) {
            next = from.right;
            from.right = pre;
            pre = from;
            from = next;
        }
        return pre;
    }

    /**
     * 打印右边界并恢复链表结构
     * @param head 右边界起始节点
     */
    private static void printEdge(Node head) {
        Node tail = reverseEdge(head);  // 反转右边界
        Node cur = tail;
        while (cur != null) {
            System.out.print(cur.val + ",");
            cur = cur.right;
        }
        reverseEdge(tail);  // 恢复原始结构
    }

    /**
     * Morris后序遍历
     * 后序遍历顺序：左 -> 右 -> 根
     * 后序遍历需要在第二次到达节点时打印其左子树的右边界
     * @param head 二叉树根节点
     */
    public static void morrisPos(Node head) {
        if (head == null) {
            return;
        }
        Node cur = head;
        Node r = null;
        while (cur != null) {
            r = cur.left;
            if (r != null) {
                // 找到cur左子树的最右节点
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                if (r.right == null) {
                    // 第一次到达cur，建立Morris连接
                    r.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 第二次到达cur，打印左子树的右边界（后序）
                    r.right = null;
                    printEdge(cur.left);
                }
            }
            cur = cur.right;
        }
        // 最后打印整棵树的右边界
        printEdge(head);
        System.out.println();
    }

    /**
     * 使用Morris遍历判断是否为二叉搜索树
     * 二叉搜索树的中序遍历序列是严格递增的
     * @param head 二叉树根节点
     * @return 是否为二叉搜索树
     */
    public static boolean isBST(Node head) {
        if (head == null) {
            return true;
        }
        Node cur = head;
        Node r = null;
        Integer preVal = null;  // 前一个访问节点的值
        boolean ans = true;
        
        while (cur != null) {
            r = cur.left;
            if (r != null) {
                // 找到cur左子树的最右节点
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                if (r.right == null) {
                    // 第一次到达cur，建立Morris连接
                    r.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 第二次到达cur，恢复树结构
                    r.right = null;
                }
            }
            // 检查中序遍历的递增性
            if (preVal != null && preVal >= cur.val) {
                ans = false;
            }
            preVal = cur.val;
            cur = cur.right;
        }
        return ans;
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
     * 递归打印二叉树结构
     * @param head 当前节点
     * @param height 当前高度
     * @param to 节点标识符
     * @param len 每个节点的显示长度
     */
    private static void printIn(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printIn(head.right, height + 1, "v", len);  // 先打印右子树
        String val = to + head.val + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printIn(head.left, height + 1, "^", len);   // 再打印左子树
    }

    /**
     * 打印二叉树结构
     * @param head 二叉树根节点
     */
    private static void print(Node head) {
        printIn(head, 0, "H", 17);
    }

    /**
     * 测试Morris遍历算法
     */
    public static void main(String[] args) {
        // 构建测试二叉树
        Node head = new Node(4);
        head.left = new Node(2);
        head.right = new Node(6);
        head.left.left = new Node(1);
        head.left.right = new Node(3);
        head.right.left = new Node(5);
        head.right.right = new Node(7);
        
        print(head);
        morrisIn(head);   // 中序遍历
        morrisPre(head);  // 先序遍历
        morrisPos(head);  // 后序遍历
        print(head);
        System.out.println(isBST(head));  // 判断是否为BST
    }
}
