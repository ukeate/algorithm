package base.tree3;

/**
 * 简单树状数组（Binary Indexed Tree / Fenwick Tree）实现
 * 下标从1开始，支持前缀和查询和单点更新
 * 时间复杂度：查询和更新都是O(log n)
 */
public class SimpleIndexTree {
    /**
     * 树状数组实现类
     */
    public static class IndexTree {
        private int[] tree;  // 树状数组
        private int len;     // 数组长度

        /**
         * 构造函数
         * @param s 数组大小
         */
        public IndexTree(int s) {
            len = s;
            tree = new int[len + 1];  // 下标从1开始，所以需要len+1个空间
        }

        /**
         * 查询前缀和：计算从下标1到i的累加和
         * @param i 查询的结束位置（包含）
         * @return 前缀和
         */
        public int sum(int i) {
            int ans = 0;
            while (i > 0) {
                ans += tree[i];
                i -= i & -i;  // 移除最低位的1，向前跳跃
            }
            return ans;
        }

        /**
         * 单点更新：在位置i上增加值d
         * @param i 更新位置
         * @param d 增加的值
         */
        public void add(int i, int d) {
            while (i <= len) {
                tree[i] += d;
                i += i & -i;  // 添加最低位的1，向后跳跃
            }
        }
    }

    /**
     * 暴力实现类，用于对比验证树状数组的正确性
     */
    public static class ArrSure {
        private int[] nums;  // 存储数组
        private int len;     // 数组长度

        /**
         * 构造函数
         * @param s 数组大小
         */
        public ArrSure(int s) {
            len = s + 1;
            nums = new int[len + 1];
        }

        /**
         * 暴力计算前缀和
         * @param idx 查询的结束位置
         * @return 前缀和
         */
        public int sum(int idx) {
            int ans = 0;
            for (int i = 1; i <= idx; i++) {
                ans += nums[i];
            }
            return ans;
        }

        /**
         * 单点更新
         * @param i 更新位置
         * @param d 增加的值
         */
        public void add(int i, int d) {
            nums[i] += d;
        }
    }

    /**
     * 测试方法：对比树状数组和暴力实现的结果
     */
    public static void main(String[] args) {
        int times = 1000000;    // 测试次数
        int len = 100;          // 数组长度
        int maxVal = 100;       // 最大值
        
        IndexTree tree = new IndexTree(len);
        ArrSure sure = new ArrSure(len);
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int idx = (int) (len * Math.random()) + 1;  // 随机生成位置[1, len]
            if (Math.random() <= 0.5) {
                // 50%概率执行add操作
                int add = (int) ((maxVal + 1) * Math.random());
                tree.add(idx, add);
                sure.add(idx, add);
            } else {
                // 50%概率执行sum查询操作
                if (tree.sum(idx) != sure.sum(idx)) {
                    System.out.println("Wrong");
                }
            }
        }
        System.out.println("test end");
    }
}
