package giant.c47;

// 动态开点线段树，只能单点增加、范围查询
public class DynamicSegmentTree {
    private static class Node {
        public int sum;
        public Node left;
        public Node right;
    }

    public static class Tree {
        public Node root;
        public int size;

        public Tree(int max) {
            root = new Node();
            size = max;
        }

        // 当前是c节点l到r范围, i上增加v
        private void add(Node c, int l, int r, int i, int v) {
            if (l == r) {
                c.sum += v;
            } else {
                int mid = (l + r) / 2;
                if (i <= mid) {
                    if (c.left == null) {
                        c.left = new Node();
                    }
                    add(c.left, l, mid, i, v);
                } else {
                    if (c.right == null) {
                        c.right = new Node();
                    }
                    add(c.right, mid + 1, r, i, v);
                }
                c.sum = (c.left != null ? c.left.sum : 0) + (c.right != null ? c.right.sum : 0);
            }
        }

        public void add(int i, int v) {
            add(root, 1, size, i, v);
        }

        private int query(Node c, int l, int r, int s, int e) {
            if (c == null) {
                return 0;
            }
            if (s <= l && r <= e) {
                return c.sum;
            }
            int mid = (l + r) / 2;
            if (e <= mid) {
                return query(c.left, l, mid, s, e);
            } else if (s > mid) {
                return query(c.right, mid + 1, r, s, e);
            } else {
                return query(c.left, l, mid, s, e) + query(c.right, mid + 1, r, s, e);
            }
        }

        // 查询s到e范围的值
        public int query(int s, int e) {
            return query(root, 1, size, s, e);
        }
    }

    //

    private static class Sure {
        public int[] arr;
        public Sure(int size) {
            arr = new int[size + 1];
        }

        public void add(int i, int v) {
            arr[i] += v;
        }
        public int query(int s, int e) {
            int sum = 0;
            for (int i = s; i <= e; i++) {
                sum += arr[i];
            }
            return sum;
        }
    }

    //

    public static void main(String[] args) {
        int size = 10000;
        int times = 50000;
        int value = 500;
        Tree tree = new Tree(size);
        Sure sure = new Sure(size);
        System.out.println("test begin");
        for (int k = 0; k < times; k++) {
            if (Math.random() < 0.5) {
                int i = (int) (Math.random() * size) + 1;
                int v = (int) (Math.random() * value);
                tree.add(i, v);
                sure.add(i, v);
            } else {
                int a = (int) (Math.random() * size) + 1;
                int b = (int) (Math.random() * size) + 1;
                int s = Math.min(a, b);
                int e = Math.max(a, b);
                int ans1 = tree.query(s, e);
                int ans2 = tree.query(s, e);
                if (ans1 != ans2) {
                    System.out.println("Wrong");
                }
            }
        }
        System.out.println("test end");
    }
}
