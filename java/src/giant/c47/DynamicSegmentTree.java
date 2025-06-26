package giant.c47;

/**
 * 动态开点线段树（单点更新，范围查询）
 * 
 * 问题描述：
 * 实现一个支持大范围（如10^9）的线段树，但只有部分位置会被访问
 * 传统线段树需要4*10^9的空间，动态开点线段树只在需要时才创建节点
 * 
 * 支持操作：
 * 1. add(i, v)：在位置i上增加值v（单点更新）
 * 2. query(s, e)：查询区间[s, e]的和（范围查询）
 * 
 * 核心思想：
 * - 懒加载：只在访问时才创建子节点
 * - 递归结构：每个节点管理一个区间
 * - 空间优化：避免创建不必要的节点
 * 
 * 优势：
 * - 空间复杂度：O(操作次数)而不是O(范围大小)
 * - 时间复杂度：O(log(范围大小))
 * - 适合稀疏数据的处理
 * 
 * 时间复杂度：
 * - 单点更新：O(log n)
 * - 范围查询：O(log n)
 * 
 * 空间复杂度：O(操作次数)
 * 
 * @author Zhu Runqi
 */
public class DynamicSegmentTree {
    
    /**
     * 线段树节点类
     */
    private static class Node {
        /** 当前节点管理区间的和 */
        public int sum;
        /** 左子节点（懒加载） */
        public Node left;
        /** 右子节点（懒加载） */
        public Node right;
    }

    /**
     * 动态开点线段树类
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
         * 递归实现单点增加操作
         * 
         * 算法思路：
         * 1. 如果到达叶子节点，直接更新值
         * 2. 否则根据位置i决定递归左子树还是右子树
         * 3. 在需要时懒加载创建子节点
         * 4. 递归返回后更新当前节点的和
         * 
         * @param c 当前节点
         * @param l 当前节点管理区间的左边界
         * @param r 当前节点管理区间的右边界
         * @param i 要更新的位置
         * @param v 要增加的值
         */
        private void add(Node c, int l, int r, int i, int v) {
            if (l == r) {
                // 叶子节点，直接更新
                c.sum += v;
            } else {
                int mid = (l + r) / 2;
                if (i <= mid) {
                    // 在左半区间，访问左子树
                    if (c.left == null) {
                        c.left = new Node();  // 懒加载创建左子节点
                    }
                    add(c.left, l, mid, i, v);
                } else {
                    // 在右半区间，访问右子树
                    if (c.right == null) {
                        c.right = new Node();  // 懒加载创建右子节点
                    }
                    add(c.right, mid + 1, r, i, v);
                }
                // 更新当前节点的和
                c.sum = (c.left != null ? c.left.sum : 0) + (c.right != null ? c.right.sum : 0);
            }
        }

        /**
         * 在位置i上增加值v
         * 
         * @param i 位置
         * @param v 增加的值
         */
        public void add(int i, int v) {
            add(root, 1, size, i, v);
        }

        /**
         * 递归实现范围查询操作
         * 
         * 算法思路：
         * 1. 如果节点为空，返回0
         * 2. 如果查询区间完全包含当前节点区间，返回当前节点的和
         * 3. 如果查询区间与当前节点区间没有交集，返回0
         * 4. 否则递归查询左右子树，返回结果之和
         * 
         * @param c 当前节点
         * @param l 当前节点管理区间的左边界
         * @param r 当前节点管理区间的右边界
         * @param s 查询区间的左边界
         * @param e 查询区间的右边界
         * @return 查询区间的和
         */
        private int query(Node c, int l, int r, int s, int e) {
            if (c == null) {
                // 节点不存在，返回0
                return 0;
            }
            if (s <= l && r <= e) {
                // 查询区间完全包含当前节点区间
                return c.sum;
            }
            int mid = (l + r) / 2;
            if (e <= mid) {
                // 查询区间完全在左半边
                return query(c.left, l, mid, s, e);
            } else if (s > mid) {
                // 查询区间完全在右半边
                return query(c.right, mid + 1, r, s, e);
            } else {
                // 查询区间跨越中点，需要查询两边
                return query(c.left, l, mid, s, e) + query(c.right, mid + 1, r, s, e);
            }
        }

        /**
         * 查询区间[s, e]的和
         * 
         * @param s 查询区间的左边界
         * @param e 查询区间的右边界
         * @return 区间和
         */
        public int query(int s, int e) {
            return query(root, 1, size, s, e);
        }
    }

    /**
     * 用于验证正确性的朴素实现
     */
    private static class Sure {
        /** 使用数组存储所有值 */
        public int[] arr;
        
        /**
         * 构造函数
         * 
         * @param size 数组大小
         */
        public Sure(int size) {
            arr = new int[size + 1];
        }

        /**
         * 在位置i上增加值v
         * 
         * @param i 位置
         * @param v 增加的值
         */
        public void add(int i, int v) {
            arr[i] += v;
        }
        
        /**
         * 查询区间[s, e]的和
         * 
         * @param s 查询区间的左边界
         * @param e 查询区间的右边界
         * @return 区间和
         */
        public int query(int s, int e) {
            int sum = 0;
            for (int i = s; i <= e; i++) {
                sum += arr[i];
            }
            return sum;
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 动态开点线段树测试 ===\n");
        
        // 基本功能测试
        System.out.println("基本功能测试：");
        Tree tree = new Tree(100);
        
        // 测试单点更新
        tree.add(5, 10);
        tree.add(15, 20);
        tree.add(25, 30);
        System.out.println("添加: 位置5加10, 位置15加20, 位置25加30");
        
        // 测试范围查询
        System.out.println("查询[1, 10]: " + tree.query(1, 10));
        System.out.println("查询[10, 20]: " + tree.query(10, 20));
        System.out.println("查询[1, 30]: " + tree.query(1, 30));
        System.out.println("查询[5, 5]: " + tree.query(5, 5));
        System.out.println();
        
        // 追加更新测试
        tree.add(5, 5);  // 位置5再加5，总共15
        tree.add(15, -10);  // 位置15减10，变成10
        System.out.println("追加更新后：");
        System.out.println("查询[1, 30]: " + tree.query(1, 30));
        System.out.println("查询[5, 5]: " + tree.query(5, 5));
        System.out.println("查询[15, 15]: " + tree.query(15, 15));
        System.out.println();
        
        // 正确性验证
        System.out.println("=== 正确性验证 ===");
        int size = 10000;
        int times = 50000;
        int value = 500;
        Tree testTree = new Tree(size);
        Sure sure = new Sure(size);
        boolean allPassed = true;
        
        System.out.println("开始随机测试（范围1-" + size + "，操作" + times + "次）...");
        
        for (int k = 0; k < times; k++) {
            if (Math.random() < 0.5) {
                // 50%概率进行add操作
                int i = (int) (Math.random() * size) + 1;
                int v = (int) (Math.random() * value);
                testTree.add(i, v);
                sure.add(i, v);
            } else {
                // 50%概率进行query操作
                int a = (int) (Math.random() * size) + 1;
                int b = (int) (Math.random() * size) + 1;
                int s = Math.min(a, b);
                int e = Math.max(a, b);
                int ans1 = testTree.query(s, e);
                int ans2 = sure.query(s, e);
                if (ans1 != ans2) {
                    System.out.println("测试失败!");
                    System.out.println("查询区间[" + s + ", " + e + "]");
                    System.out.println("线段树结果: " + ans1);
                    System.out.println("朴素算法结果: " + ans2);
                    allPassed = false;
                    break;
                }
            }
        }
        
        if (allPassed) {
            System.out.println("所有随机测试通过！");
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int largeSize = 1000000;
        int largeOps = 100000;
        Tree largeTree = new Tree(largeSize);
        
        long startTime = System.currentTimeMillis();
        
        // 执行大量操作
        for (int i = 0; i < largeOps; i++) {
            if (i % 2 == 0) {
                int pos = (int) (Math.random() * largeSize) + 1;
                int val = (int) (Math.random() * 1000);
                largeTree.add(pos, val);
            } else {
                int a = (int) (Math.random() * largeSize) + 1;
                int b = (int) (Math.random() * largeSize) + 1;
                int left = Math.min(a, b);
                int right = Math.max(a, b);
                largeTree.query(left, right);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("大规模测试完成:");
        System.out.println("范围: 1 到 " + largeSize);
        System.out.println("操作次数: " + largeOps);
        System.out.println("执行时间: " + (endTime - startTime) + " 毫秒");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 动态开点技术：");
        System.out.println("   - 懒加载：只在需要时创建节点");
        System.out.println("   - 内存优化：避免预分配大量空间");
        System.out.println("   - 适用场景：稀疏数据、大范围查询");
        System.out.println();
        System.out.println("2. 节点结构：");
        System.out.println("   - sum：当前节点管理区间的和");
        System.out.println("   - left/right：子节点指针（可为null）");
        System.out.println("   - 区间：通过递归参数传递");
        System.out.println();
        System.out.println("3. 关键操作：");
        System.out.println("   - 单点更新：O(log n)，路径上所有节点");
        System.out.println("   - 范围查询：O(log n)，分治策略");
        System.out.println("   - 空间复杂度：O(操作次数)");
        System.out.println();
        System.out.println("4. 优化技巧：");
        System.out.println("   - 延迟创建：减少内存开销");
        System.out.println("   - 递归实现：代码简洁易懂");
        System.out.println("   - 范围参数：避免存储区间信息");
        System.out.println();
        System.out.println("5. 应用场景：");
        System.out.println("   - 大范围稀疏数据处理");
        System.out.println("   - 在线算法中的动态数据结构");
        System.out.println("   - 坐标压缩的替代方案");
        System.out.println("   - 内存受限的区间查询问题");
        System.out.println();
        System.out.println("6. 与传统线段树对比：");
        System.out.println("   - 空间：O(使用量) vs O(范围大小)");
        System.out.println("   - 时间：相同的O(log n)复杂度");
        System.out.println("   - 适用性：更适合稀疏和大范围场景");
    }
}
