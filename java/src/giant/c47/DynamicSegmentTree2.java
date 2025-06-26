package giant.c47;

/**
 * 动态开点线段树（支持范围修改）
 * 
 * 问题描述：
 * 在动态开点线段树基础上，增加范围修改功能
 * 支持区间更新、区间增加、区间查询操作
 * 
 * 核心技术：
 * - 懒标记（Lazy Propagation）：延迟更新子节点
 * - 动态开点：按需创建节点
 * - 区间操作：支持范围更新和查询
 * 
 * @author Zhu Runqi
 */
public class DynamicSegmentTree2 {
    
    /**
     * 线段树节点类
     */
    private static class Node {
        /** 当前节点管理区间的和 */
        public int sum;
        /** 懒标记：区间增加值 */
        public int lazy;
        /** 懒标记：区间设置值 */
        public int change;
        /** 是否有区间设置操作 */
        public boolean update;
        /** 左子节点 */
        public Node left;
        /** 右子节点 */
        public Node right;
    }

    /**
     * 动态开点线段树类（支持范围修改）
     */
    public static class Tree {
        /** 根节点 */
        public Node root;
        /** 线段树管理的范围大小 */
        public int size;

        /**
         * 构造函数
         * 
         * @param max 线段树管理的范围 [1, max]
         */
        public Tree(int max) {
            root = new Node();
            size = max;
        }

        /**
         * 向上更新节点信息
         * 
         * @param c 当前节点
         */
        private void pushUp(Node c) {
            c.sum = c.left.sum + c.right.sum;
        }

        private void pushDown(Node p, int ln, int rn) {
            if (p.left == null) {
                p.left = new Node();
            }
            if (p.right == null) {
                p.right = new Node();
            }
            if (p.update) {
                p.left.update = true;
                p.right.update = true;
                p.left.change = p.change;
                p.right.change = p.change;
                p.left.lazy = 0;
                p.right.lazy = 0;
                p.left.sum = p.change * ln;
                p.right.sum = p.change * rn;
                p.update = false;
            }
            if (p.lazy != 0) {
                p.left.lazy += p.lazy;
                p.right.lazy += p.lazy;
                p.left.sum += p.lazy * ln;
                p.right.sum += p.lazy * rn;
                p.lazy = 0;
            }
        }

        private void update(Node c, int l, int r, int s, int e, int v) {
            if (s <= l && r <= e) {
                c.update = true;
                c.change = v;
                c.sum = v * (r - l + 1);
                c.lazy = 0;
            } else {
                int mid = (l + r) >> 1;
                pushDown(c, mid - l + 1, r - mid);
                if (s <= mid) {
                    update(c.left, l, mid, s, e, v);
                }
                if (e > mid) {
                    update(c.right, mid + 1, r, s, e, v);
                }
                pushUp(c);
            }
        }

        public void update(int s, int e, int v) {
            update(root, 1, size, s, e, v);
        }

        private void add(Node c, int l, int r, int s, int e, int v) {
            if (s <= l && r <= e) {
                c.sum += v * (r - l + 1);
                c.lazy += v;
            } else {
                int mid = (l + r) >> 1;
                pushDown(c, mid - l + 1, r - mid);
                if (s <= mid) {
                    add(c.left, l, mid, s, e, v);
                }
                if (e > mid) {
                    add(c.right, mid + 1, r, s, e, v);
                }
                pushUp(c);
            }
        }

        public void add(int s, int e, int v) {
            add(root, 1, size, s, e, v);
        }

        private int query(Node c, int l, int r, int s, int e) {
            if (s <= l && r <= e) {
                return c.sum;
            }
            int mid = (l + r) >> 1;
            pushDown(c, mid - l + 1, r - mid);
            int ans = 0;
            if (s <= mid) {
                ans += query(c.left, l, mid, s, e);
            }
            if (e > mid) {
                ans += query(c.right, mid + 1, r, s, e);
            }
            return ans;
        }

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

        public void add(int s, int e, int v) {
            for (int i = s; i <= e; i++) {
                arr[i] += v;
            }
        }

        public void update(int s, int e, int v) {
            for (int i = s; i <= e; i++) {
                arr[i] = v;
            }
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
        int n = 1000;
        int val = 50;
        int createTimes = 5000;
        int operateTimes = 5000;
        System.out.println("test begin");
        for (int i = 0; i < createTimes; i++) {
            int size = (int) (Math.random() * n) + 1;
            Tree tree = new Tree(size);
            Sure sure = new Sure(size);
            for (int k = 0; k < operateTimes; k++) {
                double choose = Math.random();
                if (choose < 0.333) {
                    int a = (int) (Math.random() * size) + 1;
                    int b = (int) (Math.random() * size) + 1;
                    int s = Math.min(a, b);
                    int e = Math.max(a, b);
                    int v = (int) (Math.random() * val);
                    tree.update(s, e, v);
                    sure.update(s, e, v);
                } else if (choose < 0.666) {
                    int a = (int) (Math.random() * size) + 1;
                    int b = (int)(Math.random() * size) + 1;
                    int s = Math.min(a, b);
                    int e = Math.max(a, b);
                    int v = (int) (Math.random() * val);
                    tree.add(s, e, v);
                    sure.add(s, e, v);
                } else {
                    int a = (int) (Math.random() * size) + 1;
                    int b = (int) (Math.random() * size) + 1;
                    int s = Math.min(a, b);
                    int e = Math.max(a, b);
                    int ans1 = tree.query(s, e);
                    int ans2 = sure.query(s, e);
                    if (ans1 != ans2) {
                        System.out.println("Wrong");
                    }
                }
            }
        }
        System.out.println("test end");
    }
}
