package base.tree3;

// 树状数组, 下标从1开始
public class SimpleIndexTree {
    public static class IndexTree {
        private int[] tree;
        private int len;
        public IndexTree(int s) {
            len = s;
            tree = new int[len + 1];
        }

        // i之前的累加和
        public int sum(int i) {
            int ans = 0;
            while (i > 0) {
                ans += tree[i];
                i -= i & -i;
            }
            return ans;
        }

        public void add(int i, int d) {
            while (i <= len) {
                tree[i] += d;
                i += i & -i;
            }
        }
    }

    //

    public static class ArrSure {
        private int[] nums;
        private int len;
        public ArrSure(int s) {
            len = s + 1;
            nums = new int[len + 1];
        }
        public int sum(int idx) {
            int ans = 0;
            for (int i = 1; i <= idx; i++) {
                ans += nums[i];
            }
            return ans;
        }

        public void add(int i, int d) {
            nums[i] += d;
        }
    }

    //

    public static void main(String[] args) {
        int times = 1000000;
        int len = 100;
        int maxVal = 100;
        IndexTree tree = new IndexTree(len);
        ArrSure sure = new ArrSure(len);
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int idx = (int) (len * Math.random()) + 1;
            if (Math.random() <= 0.5) {
                int add = (int) ((maxVal + 1) * Math.random());
                tree.add(idx, add);
                sure.add(idx, add);
            } else {
                if (tree.sum(idx) != sure.sum(idx)) {
                    System.out.println("Wrong");
                }
            }
        }
        System.out.println("test end");
    }
}
