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
public class SerializeBuild {
    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            val = v;
        }
    }

    private static void pres(Node head, Queue<String> ans) {
        if (head == null) {
            ans.add(null);
            return;
        }
        ans.add(String.valueOf(head.val));
        pres(head.left, ans);
        pres(head.right, ans);
    }

    public static Queue<String> preSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        pres(head, ans);
        return ans;
    }

    private static void ins(Node head, Queue<String> ans) {
        if (head == null) {
            ans.add(null);
            return;
        }
        ins(head.left, ans);
        ans.add(String.valueOf(head.val));
        ins(head.right, ans);
    }

    public static Queue<String> inSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        ins(head, ans);
        return ans;
    }

    private static void poss(Node head, Queue<String> ans) {
        if (head == null) {
            ans.add(null);
            return;
        }
        poss(head.left, ans);
        poss(head.right, ans);
        ans.add(String.valueOf(head.val));
    }

    public static Queue<String> posSerial(Node head) {
        Queue<String> ans = new LinkedList<>();
        poss(head, ans);
        return ans;
    }

    //

    private static Node preb(Queue<String> pre) {
        String val = pre.poll();
        if (val == null) {
            return null;
        }
        Node head = new Node(Integer.valueOf(val));
        head.left = preb(pre);
        head.right = preb(pre);
        return head;
    }

    public static Node buildPre(Queue<String> pre) {
        if (pre == null || pre.size() == 0) {
            return null;
        }
        return preb(pre);
    }

    private static Node posb(Stack<String> pos) {
        String val = pos.pop();
        if (val == null) {
            return null;
        }
        Node head = new Node(Integer.valueOf(val));
        head.right = posb(pos);
        head.left = posb(pos);
        return head;
    }

    public static Node buildPos(Queue<String> pos) {
        if (pos == null || pos.size() == 0) {
            return null;
        }
        Stack<String> stack = new Stack<>();
        while (!pos.isEmpty()) {
            stack.push(pos.poll());
        }
        return posb(stack);
    }

    //

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
            if (head.left != null) {
                ans.add(String.valueOf(head.left.val));
                queue.add(head.left);
            } else {
                ans.add(null);
            }
            if (head.right != null) {
                ans.add(String.valueOf(head.right.val));
                queue.add(head.right);
            } else {
                ans.add(null);
            }
        }
        return ans;
    }

    private static Node node(String val) {
        if (val == null) {
            return null;
        }
        return new Node(Integer.valueOf(val));
    }

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
            node.left = node(level.poll());
            node.right = node(level.poll());
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return head;
    }

    //

    private static Node randomLevel(int level, int maxLevel, int maxVal) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) ((maxVal + 1) * Math.random()));
        head.left = randomLevel(level + 1, maxLevel, maxVal);
        head.right = randomLevel(level + 1, maxLevel, maxVal);
        return head;
    }

    private static Node randomTree(int maxLevel, int maxVal) {
        return randomLevel(1, maxLevel, maxVal);
    }

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

    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

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

    private static void print(Node head) {
        printIn(head, 0, "H", 17);
    }

    public static void main(String[] args) {
        int maxLevel = 5;
        int maxValue = 100;
        int testTimes = 10;
        System.out.println("test begin");
        for (int i = 0; i < testTimes; i++) {
            Node head = randomTree(maxLevel, maxValue);
            print(head);
            Queue<String> pre = preSerial(head);
            Queue<String> pos = posSerial(head);
            Queue<String> level = levelSerial(head);
            Node preBuild = buildPre(pre);
            Node posBuild = buildPos(pos);
            Node levelBuild = buildLevel(level);
            if (!isSame(preBuild, posBuild) || !isSame(posBuild, levelBuild)) {
                System.out.println("Oops!");
            }
        }
        System.out.println("test finish!");

    }
}
