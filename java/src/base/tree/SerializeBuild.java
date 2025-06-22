package base.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/*
 * 不能中序序列化和反序列化，比如如下两棵树
 *         __2
 *        /
 *       1
 *       和
 *       1__
 *          \
 *           2
 * 补足空位置的中序遍历结果都是{ null, 1, null, 2, null}
 */

/**
 * 二叉树的序列化与反序列化
 * 
 * 支持的序列化方式：
 * 1. 先序遍历序列化/反序列化
 * 2. 后序遍历序列化/反序列化  
 * 3. 层序遍历序列化/反序列化
 * 
 * 注意：不支持中序遍历的序列化，因为无法唯一确定树的结构
 * 
 * 序列化原理：将树结构转换为线性的字符串序列，空节点用null表示
 * 反序列化原理：根据序列化的字符串序列重建原始的树结构
 */
public class SerializeBuild {
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

    // ==================== 先序遍历序列化 ====================
    
    /**
     * 先序遍历序列化的递归实现
     * @param head 当前节点
     * @param ans 存储序列化结果的队列
     */
    private static void pres(Node head, Queue<String> ans) {
        if (head == null) {
            ans.add(null);  // 空节点用null表示
            return;
        }
        // 先序：根 -> 左 -> 右
        ans.add(String.valueOf(head.val));  // 处理根节点
        pres(head.left, ans);               // 递归处理左子树
        pres(head.right, ans);              // 递归处理右子树
    }

    /**
     * 先序遍历序列化
     * @param head 树的根节点
     * @return 序列化后的队列
     */
    public static Queue<String> preSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        pres(head, ans);
        return ans;
    }

    // ==================== 中序遍历序列化（仅作演示，不建议使用）====================
    
    /**
     * 中序遍历序列化的递归实现
     * @param head 当前节点
     * @param ans 存储序列化结果的队列
     */
    private static void ins(Node head, Queue<String> ans) {
        if (head == null) {
            ans.add(null);
            return;
        }
        // 中序：左 -> 根 -> 右
        ins(head.left, ans);                // 递归处理左子树
        ans.add(String.valueOf(head.val));  // 处理根节点
        ins(head.right, ans);               // 递归处理右子树
    }

    /**
     * 中序遍历序列化（注意：无法用于反序列化）
     * @param head 树的根节点
     * @return 序列化后的队列
     */
    public static Queue<String> inSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        ins(head, ans);
        return ans;
    }

    // ==================== 后序遍历序列化 ====================
    
    /**
     * 后序遍历序列化的递归实现
     * @param head 当前节点
     * @param ans 存储序列化结果的队列
     */
    private static void poss(Node head, Queue<String> ans) {
        if (head == null) {
            ans.add(null);
            return;
        }
        // 后序：左 -> 右 -> 根
        poss(head.left, ans);               // 递归处理左子树
        poss(head.right, ans);              // 递归处理右子树
        ans.add(String.valueOf(head.val));  // 处理根节点
    }

    /**
     * 后序遍历序列化
     * @param head 树的根节点
     * @return 序列化后的队列
     */
    public static Queue<String> posSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        poss(head, ans);
        return ans;
    }

    // ==================== 先序遍历反序列化 ====================

    /**
     * 先序遍历反序列化的递归实现
     * @param pre 先序遍历序列化的队列
     * @return 重建的树节点
     */
    private static Node preb(Queue<String> pre) {
        String val = pre.poll();
        if (val == null) {
            return null;
        }
        // 先序重建：根 -> 左 -> 右
        Node head = new Node(Integer.valueOf(val));  // 重建根节点
        head.left = preb(pre);                       // 递归重建左子树
        head.right = preb(pre);                      // 递归重建右子树
        return head;
    }

    /**
     * 先序遍历反序列化
     * @param pre 先序遍历序列化的队列
     * @return 重建的树根节点
     */
    public static Node buildPre(Queue<String> pre) {
        if (pre == null || pre.size() == 0) {
            return null;
        }
        return preb(pre);
    }

    // ==================== 后序遍历反序列化 ====================
    
    /**
     * 后序遍历反序列化的递归实现
     * @param pos 后序遍历序列化的栈
     * @return 重建的树节点
     */
    private static Node posb(Stack<String> pos) {
        String val = pos.pop();
        if (val == null) {
            return null;
        }
        // 后序重建：根 -> 右 -> 左（与序列化相反的顺序）
        Node head = new Node(Integer.valueOf(val));  // 重建根节点
        head.right = posb(pos);                      // 递归重建右子树
        head.left = posb(pos);                       // 递归重建左子树
        return head;
    }

    /**
     * 后序遍历反序列化
     * @param pos 后序遍历序列化的队列
     * @return 重建的树根节点
     */
    public static Node buildPos(Queue<String> pos) {
        if (pos == null || pos.size() == 0) {
            return null;
        }
        // 将队列转换为栈，便于从后往前处理
        Stack<String> stack = new Stack<>();
        while (!pos.isEmpty()) {
            stack.push(pos.poll());
        }
        return posb(stack);
    }

    // ==================== 层序遍历序列化 ====================

    /**
     * 层序遍历序列化
     * @param head 树的根节点
     * @return 序列化后的队列
     */
    public static Queue<String> levelSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        if (head == null) {
            ans.add(null);
            return ans;
        }
        ans.add(String.valueOf(head.val));
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        while (!queue.isEmpty()) {
            head = queue.poll();
            // 处理左子节点
            if (head.left != null) {
                ans.add(String.valueOf(head.left.val));
                queue.add(head.left);
            } else {
                ans.add(null);
            }
            // 处理右子节点
            if (head.right != null) {
                ans.add(String.valueOf(head.right.val));
                queue.add(head.right);
            } else {
                ans.add(null);
            }
        }
        return ans;
    }

    /**
     * 创建节点的辅助方法
     * @param val 节点值字符串
     * @return 新建的节点或null
     */
    private static Node node(String val) {
        if (val == null) {
            return null;
        }
        return new Node(Integer.valueOf(val));
    }

    /**
     * 层序遍历反序列化
     * @param level 层序遍历序列化的队列
     * @return 重建的树根节点
     */
    public static Node buildLevel(Queue<String> level) {
        if (level == null || level.size() == 0) {
            return null;
        }
        Node head = node(level.poll());
        Queue<Node> queue = new LinkedList<>();
        if (head != null) {
            queue.add(head);
        }
        Node node = null;
        while (!queue.isEmpty()) {
            node = queue.poll();
            // 重建左右子节点
            node.left = node(level.poll());
            node.right = node(level.poll());
            // 将非空子节点加入队列
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return head;
    }

    // ==================== 测试工具方法 ====================

    /**
     * 随机生成树节点（递归）
     * @param level 当前层级
     * @param maxLevel 最大层级
     * @param maxVal 最大节点值
     * @return 随机生成的节点
     */
    private static Node randomLevel(int level, int maxLevel, int maxVal) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) ((maxVal + 1) * Math.random()));
        head.left = randomLevel(level + 1, maxLevel, maxVal);
        head.right = randomLevel(level + 1, maxLevel, maxVal);
        return head;
    }

    /**
     * 随机生成二叉树
     * @param maxLevel 最大层级
     * @param maxVal 最大节点值
     * @return 随机生成的树根节点
     */
    private static Node randomTree(int maxLevel, int maxVal) {
        return randomLevel(1, maxLevel, maxVal);
    }

    /**
     * 判断两棵树是否相同
     * @param head1 第一棵树的根节点
     * @param head2 第二棵树的根节点
     * @return 是否相同
     */
    private static boolean isSame(Node head1, Node head2) {
        if (head1 == null ^ head2 == null) {
            return false;
        }
        if (head1 == null && head2 == null) {
            return true;
        }
        if (head1.val != head2.val) {
            return false;
        }
        return isSame(head1.left, head2.left) && isSame(head1.right, head2.right);
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
     * 递归打印二叉树
     * @param head 当前节点
     * @param height 当前层级
     * @param to 节点标识
     * @param len 字符宽度
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
     * @param head 树的根节点
     */
    private static void print(Node head) {
        printIn(head, 0, "H", 17);
    }

    public static void main(String[] args) {
        int maxLevel = 5;    // 最大层级
        int maxValue = 100;  // 最大节点值
        int testTimes = 10;  // 测试次数
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            // 生成随机树
            Node head = randomTree(maxLevel, maxValue);
            print(head);
            
            // 测试三种序列化方式
            Queue<String> pre = preSerial(head);
            Queue<String> pos = posSerial(head);
            Queue<String> level = levelSerial(head);
            
            // 测试三种反序列化方式
            Node preBuild = buildPre(pre);
            Node posBuild = buildPos(pos);
            Node levelBuild = buildLevel(level);
            
            // 验证反序列化结果是否一致
            if (!isSame(preBuild, posBuild) || !isSame(posBuild, levelBuild)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish!");
    }
}
