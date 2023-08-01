package giant.c5;


import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// 节点x左右树结构相同，说x为相等子树，求相等子树个数
public class LeftRightSameTreeNum {
    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            val = v;
        }
    }

    private static boolean same(Node h1, Node h2) {
        if (h1 == null ^ h2 == null) {
            return false;
        }
        if (h1 == null && h2 == null) {
            return true;
        }
        return h1.val == h2.val && same(h1.left, h2.left) && same(h1.right, h2.right);
    }

    // O(NlogN)
    public static int num1(Node head) {
        if (head == null) {
            return 0;
        }
        return num1(head.left) + num1(head.right) + (same(head.left, head.right) ? 1 : 0);
    }

    //

    private static class Hash {
        private MessageDigest digest;

        public Hash(String algorithm) {
            try {
                digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        public String hashCode(String s) {
            return DatatypeConverter.printHexBinary(digest.digest(s.getBytes())).toUpperCase();
        }
    }

    private static class Info {
        public int ans;
        public String str;

        public Info(int a, String s) {
            ans = a;
            str = s;
        }
    }

    private static Info process(Node head, Hash hash) {
        if (head == null) {
            return new Info(0, hash.hashCode("#,"));
        }
        Info l = process(head.left, hash);
        Info r = process(head.right, hash);
        int ans = (l.str.equals(r.str) ? 1 : 0) + l.ans + r.ans;
        String str = hash.hashCode(head.val + "," + l.str + r.str);
        return new Info(ans, str);
    }

    // O(N)
    public static int num2(Node head) {
        String algorithm = "SHA-256";
        Hash hash = new Hash(algorithm);
        return process(head, hash).ans;
    }

    //

    private static Node randomTree(int restLevel, int maxVal) {
        if (restLevel == 0) {
            return null;
        }
        Node head = Math.random() < 0.2 ? null : new Node((int) (Math.random() * maxVal));
        if (head != null) {
            head.left = randomTree(restLevel - 1, maxVal);
            head.right = randomTree(restLevel - 1, maxVal);
        }
        return head;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLevel = 8;
        int maxVal = 4;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxVal);
            int ans1 = num1(head);
            int ans2 = num2(head);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
