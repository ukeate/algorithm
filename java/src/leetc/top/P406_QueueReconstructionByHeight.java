package leetc.top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * LeetCode 406. 根据身高重建队列 (Queue Reconstruction by Height)
 * 
 * 问题描述：
 * 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
 * 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi，前面正好有 ki 个身高大于或等于 hi 的人。
 * 请你重新构造并返回输入数组 people 所表示的队列。
 * 
 * 示例：
 * 输入：people = [[7,0],[4,2],[5,0],[6,1],[5,2],[7,1]]
 * 输出：[[5,0],[7,0],[5,2],[6,1],[4,2],[7,1]]
 * 
 * 解法思路：
 * 贪心算法 + SB树（Size Balanced Tree）：
 * 1. 按身高从高到低排序，身高相同时按k值从小到大排序
 * 2. 使用平衡二叉搜索树维护当前已排列的人的索引
 * 3. 对于每个人，根据其k值在树中找到正确的插入位置
 * 4. 利用SB树的rank操作快速定位第k个位置
 * 
 * 核心思想：
 * - 从身高最高的人开始处理，这样可以保证后续插入不会影响已处理人的k值
 * - 使用SB树支持O(log n)的插入和按秩查找操作
 * 
 * 时间复杂度：O(n log n) - 排序O(n log n) + n次树操作O(log n)
 * 空间复杂度：O(n) - SB树存储空间
 * 
 * LeetCode链接：https://leetcode.com/problems/queue-reconstruction-by-height/
 */
public class P406_QueueReconstructionByHeight {
    /**
     * 表示一个人的属性
     */
    private static class Unit {
        public int h;  // 身高
        public int k;  // 前面有k个身高大于等于h的人

        public Unit(int height, int greater) {
            h = height;
            k = greater;
        }
    }

    /**
     * 比较器：按身高降序，身高相同时按k值升序
     */
    private static class Comp implements Comparator<Unit> {
        @Override
        public int compare(Unit o1, Unit o2) {
            // 身高不同：按身高降序（高的在前）
            // 身高相同：按k值升序（k小的在前）
            return o1.h != o2.h ? (o2.h - o1.h) : (o1.k - o2.k);
        }
    }

    /**
     * 重建队列主方法
     * 
     * @param people 人员属性数组，people[i] = [身高, 前面>=身高的人数]
     * @return 重建后的队列
     */
    public static int[][] reconstructQueue(int[][] people) {
        int n = people.length;
        // 1. 转换为Unit对象并排序
        Unit[] units = new Unit[n];
        for (int i = 0; i < n; i++) {
            units[i] = new Unit(people[i][0], people[i][1]);
        }
        Arrays.sort(units, new Comp());  // 按身高降序，k值升序排序
        
        // 2. 使用SB树维护插入顺序
        Tree tree = new Tree();
        for (int i = 0; i < n; i++) {
            // 在第units[i].k个位置插入索引i
            tree.insert(units[i].k, i);
        }
        
        // 3. 按照树的中序遍历顺序重建队列
        LinkedList<Integer> all = tree.allIndexes();
        int[][] ans = new int[n][2];
        int idx = 0;
        for (Integer i : all) {
            ans[idx][0] = units[i].h;
            ans[idx++][1] = units[i].k;
        }
        return ans;
    }

    /**
     * SB树的节点类
     */
    public static class Node {
        public int val;   // 节点值
        public Node l;    // 左子节点
        public Node r;    // 右子节点  
        public int size;  // 以该节点为根的子树大小

        public Node(int i) {
            val = i;
            size = 1;
        }
    }

    /**
     * Size Balanced Tree (SB树) - 支持按秩插入和查询的平衡二叉搜索树
     */
    public static class Tree {
        private Node root;

        /**
         * 右旋操作 - SB树平衡调整
         */
        private Node rightRotate(Node cur) {
            Node l = cur.l;
            cur.l = l.r;
            l.r = cur;
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return l;
        }

        /**
         * 左旋操作 - SB树平衡调整
         */
        private Node leftRotate(Node cur) {
            Node r = cur.r;
            cur.r = r.l;
            r.l = cur;
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return r;
        }

        /**
         * SB树的maintain操作 - 保持平衡性质
         */
        private Node maintain(Node cur) {
            if (cur == null) {
                return null;
            }
            int ls = cur.l != null ? cur.l.size : 0;
            int lls = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
            int lrs = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            int rs = cur.r != null ? cur.r.size : 0;
            int rls = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            int rrs = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            if (lls > rs) {
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (lrs > rs) {
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rrs > ls) {
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } else if (rls > ls) {
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            }
            return cur;
        }

        private Node insert(Node root, int idx, Node cur) {
            if (root == null) {
                return cur;
            }
            root.size++;
            int leftHeadSize = (root.l != null ? root.l.size : 0) + 1;
            if (idx < leftHeadSize) {
                root.l = insert(root.l, idx, cur);
            } else {
                root.r = insert(root.r, idx - leftHeadSize, cur);
            }
            root = maintain(root);
            return root;
        }

        private Node get(Node root, int idx) {
            int ls = root.l != null ? root.l.size : 0;
            if (idx < ls) {
                return get(root.l, idx);
            } else if (idx == ls) {
                return root;
            } else {
                return get(root.r, idx - ls - 1);
            }
        }

        /**
         * 向SB树中插入值val到第idx个位置
         * 
         * @param idx 插入位置（0-based）
         * @param val 插入的值
         */
        public void insert(int idx, int val) {
            Node cur = new Node(val);
            if (root == null) {
                root = cur;
            } else {
                if (idx <= root.size) {
                    root = insert(root, idx, cur);
                }
            }
        }

        /**
         * 获取第idx位置的值
         * 
         * @param idx 位置索引
         * @return 该位置的值
         */
        public int get(int idx) {
            Node ans = get(root, idx);
            return ans.val;
        }

        /**
         * 中序遍历收集所有节点值
         */
        private void process(Node head, LinkedList<Integer> indexes) {
            if (head == null) {
                return;
            }
            process(head.l, indexes);
            indexes.addLast(head.val);
            process(head.r, indexes);
        }

        /**
         * 获取所有节点值的有序列表
         * 
         * @return 按插入顺序排列的所有值
         */
        public LinkedList<Integer> allIndexes() {
            LinkedList<Integer> indexes = new LinkedList<>();
            process(root, indexes);
            return indexes;
        }
    }
}
