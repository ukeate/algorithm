package base.tree4;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * SB树实现的动态数组与传统List性能对比
 * 
 * 本类实现了基于Size Balanced Tree (SB树)的动态数组SBList，
 * 并与ArrayList和LinkedList进行性能对比测试。
 * 
 * SBList的特点：
 * 1. 支持O(log n)时间复杂度的随机位置插入、删除、查找
 * 2. 相比ArrayList，在频繁的中间插入删除操作时性能更好
 * 3. 相比LinkedList，在随机访问时性能更好
 * 
 * SB树是一种自平衡二叉搜索树，每个节点维护子树的大小信息，
 * 通过这个信息可以快速定位到数组的指定位置。
 */
public class ListGreat {
    
    /**
     * SB树的节点类
     * 
     * @param <V> 节点存储的值的类型
     */
    public static class Node<V> {
        public V v;           // 节点存储的值
        public Node<V> l;     // 左子节点
        public Node<V> r;     // 右子节点
        public int size;      // 以当前节点为根的子树的节点总数

        public Node(V val) {
            v = val;
            size = 1;
        }
    }

    /**
     * 基于SB树实现的动态数组
     * 
     * 核心思想：
     * 1. 每个节点维护子树大小信息
     * 2. 通过子树大小可以快速定位到数组的第k个位置
     * 3. 使用SB树的平衡策略保持树的平衡，确保操作的时间复杂度为O(log n)
     * 
     * @param <V> 数组元素的类型
     */
    public static class SBList<V> {
        private Node<V> root;  // 树根

        /**
         * 右旋操作
         * 
         * 将节点cur向右旋转，用于维护SB树的平衡性质
         * 
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node<V> rightRotate(Node<V> cur) {
            Node<V> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            // 更新size信息
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return l;
        }

        /**
         * 左旋操作
         * 
         * 将节点cur向左旋转，用于维护SB树的平衡性质
         * 
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node<V> leftRotate(Node<V> cur) {
            Node<V> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            // 更新size信息
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return r;
        }

        /**
         * SB树的调整操作
         * 
         * 维护SB树的平衡性质：对于任意节点x，有size[x.child] <= size[x.sibling]
         * 通过旋转操作来修复违规的情况
         * 
         * @param cur 当前节点
         * @return 调整后的节点
         */
        private Node<V> maintain(Node<V> cur) {
            if (cur == null) {
                return null;
            }
            // 获取各个子树的大小
            int ls = cur.l != null ? cur.l.size : 0;
            int lls = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
            int lrs = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
            int rs = cur.r != null ? cur.r.size : 0;
            int rls = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
            int rrs = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
            
            if (lls > rs) {
                // LL违规：左子树的左子树 > 右子树
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (lrs > rs) {
                // LR违规：左子树的右子树 > 右子树
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rrs > ls) {
                // RR违规：右子树的右子树 > 左子树
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur = maintain(cur);
            } else if (rls > ls) {
                // RL违规：右子树的左子树 > 左子树
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
         * 
         * @param root 当前子树的根节点
         * @param idx 插入的位置索引
         * @param cur 要插入的节点
         * @return 插入后的子树根节点
         */
        private Node<V> add(Node<V> root, int idx, Node<V> cur) {
            if (root == null) {
                return cur;
            }
            root.size++;  // 子树大小增加1
            
            // 计算根节点在子树中的位置
            int les = (root.l != null ? root.l.size : 0) + 1;
            if (idx < les) {
                // 插入到左子树
                root.l = add(root.l, idx, cur);
            } else {
                // 插入到右子树
                root.r = add(root.r, idx - les, cur);
            }
            root = maintain(root);  // 维护平衡
            return root;
        }

        /**
         * 删除指定位置的节点
         * 
         * @param root 当前子树的根节点
         * @param idx 要删除的位置索引
         * @return 删除后的子树根节点
         */
        private Node<V> remove(Node<V> root, int idx) {
            root.size--;  // 子树大小减少1
            int rootIdx = root.l != null ? root.l.size : 0;
            
            if (idx != rootIdx) {
                // 不是删除根节点，递归到子树
                if (idx < rootIdx) {
                    root.l = remove(root.l, idx);
                } else {
                    root.r = remove(root.r, idx - rootIdx - 1);
                }
                return root;
            }
            
            // 删除根节点的情况
            if (root.l == null && root.r == null) {
                return null;  // 叶子节点直接删除
            }
            if (root.l == null) {
                return root.r;  // 只有右子树
            }
            if (root.r == null) {
                return root.l;  // 只有左子树
            }
            
            // 有左右子树：找右子树的最小节点作为新根
            Node<V> pre = null;
            Node<V> des = root.r;
            des.size--;
            while (des.l != null) {
                pre = des;
                des = des.l;
                des.size--;
            }
            if (pre != null) {
                pre.l = des.r;
                des.r = root.r;
            }
            des.l = root.l;
            des.size = des.l.size + (des.r == null ? 0 : des.r.size) + 1;
            return des;
        }

        /**
         * 获取指定位置的节点
         * 
         * @param root 当前子树的根节点
         * @param idx 位置索引
         * @return 指定位置的节点
         */
        private Node<V> get(Node<V> root, int idx) {
            int ls = root.l != null ? root.l.size : 0;
            if (idx < ls) {
                return get(root.l, idx);      // 在左子树
            } else if (idx == ls) {
                return root;                  // 就是根节点
            } else {
                return get(root.r, idx - ls - 1);  // 在右子树
            }
        }

        /**
         * 在指定位置插入元素
         * 
         * @param idx 插入位置
         * @param v 要插入的值
         */
        public void add(int idx, V v) {
            Node<V> cur = new Node<>(v);
            if (root == null) {
                root = cur;
            } else {
                if (idx <= root.size) {
                    root = add(root, idx, cur);
                }
            }
        }

        /**
         * 获取指定位置的元素
         * 
         * @param idx 位置索引
         * @return 指定位置的元素值
         */
        public V get(int idx) {
            Node<V> ans = get(root, idx);
            return ans.v;
        }

        /**
         * 删除指定位置的元素
         * 
         * @param idx 要删除的位置索引
         */
        public void remove(int idx) {
            if (idx >= 0 && size() > idx) {
                root = remove(root, idx);
            }
        }

        /**
         * 获取数组的大小
         * 
         * @return 数组中元素的个数
         */
        public int size() {
            return root == null ? 0 : root.size;
        }
    }

    /**
     * 主方法：测试SBList与传统List的功能正确性和性能对比
     */
    public static void main(String[] args) {
        int times = 50000;
        int maxVal = 10000000;
        ArrayList<Integer> list = new ArrayList<>();
        SBList<Integer> sbList = new SBList<>();
        
        // 功能正确性测试
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            if (list.size() != sbList.size()) {
                System.out.println("Wrong");
                break;
            }
            if (list.size() > 1 && Math.random() < 0.5) {
                // 随机删除操作
                int removeIdx = (int)(list.size() * Math.random());
                list.remove(removeIdx);
                sbList.remove(removeIdx);
            } else {
                // 随机插入操作
                int idx = (int) ((list.size() + 1) * Math.random());
                int val = (int) ((maxVal + 1) * Math.random());
                list.add(idx, val);
                sbList.add(idx, val);
            }
        }
        // 验证最终结果是否一致
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(sbList.get(i))) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");

        // 性能测试
        times = 500000;
        list = new ArrayList<>();
        sbList = new SBList<>();
        long start = 0, end = 0;

        // ArrayList性能测试
        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (list.size() + 1));
            int randomValue = (int) (Math.random() * (maxVal + 1));
            list.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList插入总时长(毫秒) ： " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            list.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList读取总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * list.size());
            list.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList删除总时长(毫秒) : " + (end - start));

        // SBList性能测试
        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (sbList.size() + 1));
            int randomValue = (int) (Math.random() * (maxVal + 1));
            sbList.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("SBList插入总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            sbList.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SBList读取总时长(毫秒) :  " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * sbList.size());
            sbList.remove(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SBList删除总时长(毫秒) :  " + (end - start));

        // LinkedList性能测试
        times = 50000;  // LinkedList较慢，减少测试次数
        LinkedList list2 = new LinkedList<>();
        sbList = new SBList<>();

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            int randomValue = (int) (Math.random() * (maxVal + 1));
            list2.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList插入总时长(毫秒) ： " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            list2.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList读取总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            int randomValue = (int) (Math.random() * (maxVal + 1));
            sbList.add(randomIndex, randomValue);
        }
        end = System.currentTimeMillis();
        System.out.println("SBTree插入总时长(毫秒) : " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            int randomIndex = (int) (Math.random() * (i + 1));
            sbList.get(randomIndex);
        }
        end = System.currentTimeMillis();
        System.out.println("SBTree读取总时长(毫秒) :  " + (end - start));
    }
}
