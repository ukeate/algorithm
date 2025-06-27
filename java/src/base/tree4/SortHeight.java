package base.tree4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * 根据身高重新排列队列问题
 * LeetCode 406: Queue Reconstruction by Height
 * 
 * 问题描述：
 * 给定一个数组people，每个元素[h, k]表示：
 * - h: 人的身高
 * - k: 在这个人前面身高大于等于h的人数
 * 
 * 要求重新排列队列，使得每个人前面身高大于等于自己身高的人数恰好等于k
 * 
 * 解法思路：
 * 1. 按身高降序排列，身高相同时按k升序排列
 * 2. 依次将每个人插入到索引为k的位置
 * 
 * 提供两种实现：
 * - sort1: 使用ArrayList，时间复杂度O(n²)
 * - sort2: 使用SB树，时间复杂度O(n log n)
 */
// https://leetcode.com/problems/queue-reconstruction-by-height/
public class SortHeight {
    /**
     * 表示一个人的信息
     */
    private static class Unit {
        public int h; // 身高
        public int k; // 前面身高大于等于h的人数

        public Unit(int height, int greater) {
            h = height;
            k = greater;
        }
    }

    /**
     * 比较器：按身高降序，身高相同时按k升序
     * 这样排序的原因：
     * 1. 身高高的人先处理，这样后面的人不会影响前面人的k值
     * 2. 身高相同时k小的先处理，保证插入位置的正确性
     */
    private static class Comp implements Comparator<Unit> {
        @Override
        public int compare(Unit o1, Unit o2) {
            return o1.h != o2.h ? (o2.h - o1.h) : (o1.k - o2.k);
        }
    }

    /**
     * 解法1：使用ArrayList
     * 时间复杂度：O(n²) - 每次插入可能需要移动O(n)个元素
     * 空间复杂度：O(n)
     */
    public static int[][] sort1(int[][] people) {
        int n = people.length;
        Unit[] units = new Unit[n];
        
        // 转换为Unit对象
        for (int i = 0; i < n; i++) {
            units[i] = new Unit(people[i][0], people[i][1]);
        }
        
        // 按规则排序
        Arrays.sort(units, new Comp());
        
        // 依次插入到指定位置
        ArrayList<Unit> arr = new ArrayList<>();
        for (Unit unit : units) {
            arr.add(unit.k, unit); // 插入到索引k的位置
        }
        
        // 转换回结果格式
        int[][] ans = new int[n][2];
        int idx = 0;
        for (Unit unit : arr) {
            ans[idx][0] = unit.h;
            ans[idx++][1] = unit.k;
        }
        return ans;
    }

    /**
     * SB树节点
     */
    private static class Node {
        public int v;    // 存储的值（数组索引）
        public Node l;   // 左子树
        public Node r;   // 右子树
        public int size; // 子树大小

        public Node(int arrIdx) {
            v = arrIdx;
            size = 1;
        }
    }

    /**
     * Size Balanced Tree实现
     * 用于支持高效的按位置插入和查询操作
     */
    private static class SBTree {
        private Node root;

        /**
         * 右旋操作
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
         * 左旋操作
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
         * 维护SB树的平衡性
         * 根据size信息调整树的结构
         */
        private Node maintain(Node cur) {
            if (cur == null) {
                return null;
            }
            
            // 获取各子树的大小
            int ls = cur.l != null ? cur.l.size : 0;
            int lls = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
            int lrs = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            int rs = cur.r != null ? cur.r.size : 0;
            int rls = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            int rrs = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            
            // LL型违规
            if (lls > rs) {
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } 
            // LR型违规
            else if (lrs > rs) {
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } 
            // RR型违规
            else if (rrs > ls) {
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } 
            // RL型违规
            else if (rls > ls) {
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            }
            return cur;
        }

        /**
         * 在指定位置插入节点
         * @param root 当前子树根节点
         * @param idx 插入位置
         * @param cur 要插入的节点
         */
        private Node insert(Node root, int idx, Node cur) {
            if (root == null) {
                return cur;
            }
            
            root.size++; // 子树大小增加1
            int les = (root.l != null ? root.l.size : 0) + 1; // 左子树大小+1
            
            if (idx < les) {
                root.l = insert(root.l, idx, cur);
            } else {
                root.r = insert(root.r, idx - les, cur);
            }
            
            root = maintain(root); // 维护平衡
            return root;
        }

        /**
         * 获取指定位置的节点
         * @param root 当前子树根节点
         * @param idx 位置索引
         */
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
         * 在指定位置插入值
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
         * 获取指定位置的值
         */
        public int get(int idx) {
            Node ans = get(root, idx);
            return ans.v;
        }

        /**
         * 中序遍历获取所有索引
         */
        private void process(Node head, LinkedList<Integer> indexes) {
            if (head == null) {
                return;
            }
            process(head.l, indexes);
            indexes.addLast(head.v);
            process(head.r, indexes);
        }

        /**
         * 获取所有索引的有序列表
         */
        public LinkedList<Integer> allIndexes() {
            LinkedList<Integer> indexes = new LinkedList<>();
            process(root, indexes);
            return indexes;
        }
    }

    /**
     * 解法2：使用SB树优化
     * 时间复杂度：O(n log n) - 每次插入O(log n)
     * 空间复杂度：O(n)
     * 
     * 优势：当n很大时，性能显著优于ArrayList方法
     */
    public static int[][] sort2(int[][] people) {
        int n = people.length;
        Unit[] units = new Unit[n];
        
        // 转换为Unit对象
        for (int i = 0; i < n; i++) {
            units[i] = new Unit(people[i][0], people[i][1]);
        }
        
        // 按规则排序
        Arrays.sort(units, new Comp());
        
        // 使用SB树进行插入操作
        SBTree tree = new SBTree();
        for (int i = 0; i < n; i++) {
            tree.insert(units[i].k, i); // 在位置k插入索引i
        }
        
        // 获取最终的排列顺序
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
     * 测试方法
     */
    public static void main(String[] args) {
        int times = 100000;
        int maxVal = 10000000;
        // 可以在这里添加性能测试代码
    }
}
