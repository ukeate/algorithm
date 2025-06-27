package base.tree4;

/**
 * 滑动窗口中位数问题
 * 
 * 问题描述：给定一个数组和窗口大小k，求每个大小为k的滑动窗口的中位数。
 * 
 * 解决方案：使用SB树（Size Balanced Tree）维护滑动窗口中的元素，
 * 支持快速插入、删除和按索引查找，从而能够O(log k)时间内找到中位数。
 * 
 * 算法核心思想：
 * 1. 维护一个包含窗口内所有元素的SB树
 * 2. 当窗口滑动时，删除左边元素，添加右边元素
 * 3. 利用SB树支持按排名查找的特性，直接获取中位数
 * 
 * 时间复杂度：O(n log k)，其中n是数组长度，k是窗口大小
 * 空间复杂度：O(k)
 */
public class SlidingWindowMedian {
    
    /**
     * SB树的节点类
     * 
     * @param <K> 键的类型，必须实现Comparable接口
     */
    private static class Node<K extends Comparable<K>> {
        public K k;           // 键值
        public Node<K> l;     // 左子节点
        public Node<K> r;     // 右子节点
        public int size;      // 子树大小

        public Node(K key) {
            k = key;
            size = 1;
        }
    }

    /**
     * SB树实现的集合（专为滑动窗口中位数优化）
     * 
     * 支持的操作：
     * 1. add: 添加元素
     * 2. remove: 删除元素
     * 3. getIndexKey: 按排名获取元素
     * 4. size: 获取元素个数
     * 
     * @param <K> 元素类型，必须实现Comparable接口
     */
    private static class SBTMap<K extends Comparable<K>> {
        private Node<K> root;  // 树根

        /**
         * 右旋操作
         * 
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node<K> rightRotate(Node<K> cur) {
            Node<K> l = cur.l;
            cur.l = l.r;
            l.r = cur;
            l.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return l;
        }

        /**
         * 左旋操作
         * 
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node<K> leftRotate(Node<K> cur) {
            Node<K> r = cur.r;
            cur.r = r.l;
            r.l = cur;
            r.size = cur.size;
            cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
            return r;
        }

        /**
         * SB树的平衡维护操作
         * 
         * @param cur 当前节点
         * @return 调整后的节点
         */
        private Node<K> maintain(Node<K> cur) {
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
                // LL违规
                cur = rightRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (lrs > rs) {
                // LR违规
                cur.l = leftRotate(cur.l);
                cur = rightRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rrs > ls) {
                // RR违规
                cur = leftRotate(cur);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            } else if (rls > ls) {
                // RL违规
                cur.r = rightRotate(cur.r);
                cur = leftRotate(cur);
                cur.l = maintain(cur.l);
                cur.r = maintain(cur.r);
                cur = maintain(cur);
            }
            return cur;
        }

        /**
         * 查找键值对应的节点
         * 
         * @param key 要查找的键
         * @return 对应的节点，如果不存在则返回最近的节点
         */
        private Node<K> findLast(K key) {
            Node<K> pre = null;
            Node<K> cur = root;
            while (cur != null) {
                pre = cur;
                if (key.compareTo(cur.k) == 0) {
                    break;
                } else if (key.compareTo(cur.k) < 0) {
                    cur = cur.l;
                } else {
                    cur = cur.r;
                }
            }
            return pre;
        }

        /**
         * 向子树中添加新节点
         * 
         * @param cur 当前子树根节点
         * @param key 要添加的键
         * @return 添加后的子树根节点
         */
        private Node<K> add(Node<K> cur, K key) {
            if (cur == null) {
                return new Node<>(key);
            } else {
                cur.size++;
                if (key.compareTo(cur.k) < 0) {
                    cur.l = add(cur.l, key);
                } else {
                    cur.r = add(cur.r, key);
                }
                return maintain(cur);
            }
        }

        /**
         * 从子树中删除指定键的节点
         * 
         * @param cur 当前子树根节点
         * @param key 要删除的键
         * @return 删除后的子树根节点
         */
        private Node<K> delete(Node<K> cur, K key) {
            cur.size--;
            if (key.compareTo(cur.k) > 0) {
                cur.r = delete(cur.r, key);
            } else if (key.compareTo(cur.k) < 0) {
                cur.l = delete(cur.l, key);
            } else {
                // 找到要删除的节点
                if (cur.l == null && cur.r == null) {
                    cur = null;
                } else if (cur.l == null && cur.r != null) {
                    cur = cur.r;
                } else if (cur.l != null && cur.r == null) {
                    cur = cur.l;
                } else {
                    // 用右子树的最小节点替换当前节点
                    Node<K> pre = null;
                    Node<K> des = cur.r;
                    des.size--;
                    while (des.l != null) {
                        pre = des;
                        des = des.l;
                        des.size--;
                    }
                    if (pre != null) {
                        pre.l = des.r;
                        des.r = cur.r;
                    }
                    des.l = cur.l;
                    des.size = des.l.size + (des.r == null ? 0 : des.r.size) + 1;
                    cur = des;
                }
            }
            return cur;
        }

        /**
         * 获取排名为kth的节点（1-indexed）
         * 
         * @param cur 当前子树根节点
         * @param kth 排名（1表示最小值）
         * @return 排名为kth的节点
         */
        private Node<K> getIndex(Node<K> cur, int kth) {
            int ls = cur.l != null ? cur.l.size : 0;
            if (kth == ls + 1) {
                return cur;
            } else if (kth <= ls) {
                return getIndex(cur.l, kth);
            } else {
                return getIndex(cur.r, kth - ls - 1);
            }
        }

        /**
         * 获取集合中元素的个数
         * 
         * @return 元素个数
         */
        public int size() {
            return root == null ? 0 : root.size;
        }

        /**
         * 判断是否包含指定的键
         * 
         * @param key 要查找的键
         * @return 如果包含返回true，否则返回false
         */
        public boolean containsKey(K key) {
            if (key == null) {
                throw new RuntimeException("Key cannot be null");
            }
            Node<K> n = findLast(key);
            return n != null && key.compareTo(n.k) == 0 ? true : false;
        }

        /**
         * 添加元素到集合中
         * 
         * @param key 要添加的键
         */
        public void add(K key) {
            if (key == null) {
                throw new RuntimeException("Key cannot be null");
            }
            Node<K> n = findLast(key);
            if (n == null || key.compareTo(n.k) != 0) {
                root = add(root, key);
            }
        }

        /**
         * 从集合中删除元素
         * 
         * @param key 要删除的键
         */
        public void remove(K key) {
            if (key == null) {
                throw new RuntimeException("Key cannot be null");
            }
            if (containsKey(key)) {
                root = delete(root, key);
            }
        }

        /**
         * 获取排名为idx的元素（0-indexed）
         * 
         * @param idx 排名索引（0表示最小值）
         * @return 排名为idx的键
         */
        public K getIndexKey(int idx) {
            if (idx < 0 || idx >= this.size()) {
                throw new RuntimeException("Index out of bounds");
            }
            return getIndex(root, idx + 1).k;
        }
    }

    /**
     * 值节点类
     * 
     * 用于处理滑动窗口中可能出现的重复值。
     * 通过同时比较值和索引来确保唯一性，
     * 当值相同时按索引排序。
     */
    public static class VNode implements Comparable<VNode> {
        public int i;  // 元素在原数组中的索引
        public int v;  // 元素的值

        public VNode(int idx, int val) {
            i = idx;
            v = val;
        }

        /**
         * 比较方法：先比较值，值相同时比较索引
         * 
         * @param o 要比较的对象
         * @return 比较结果
         */
        @Override
        public int compareTo(VNode o) {
            return v != o.v ? Integer.valueOf(v).compareTo(o.v)
                    : Integer.valueOf(i).compareTo(o.i);
        }
    }

    /**
     * 计算滑动窗口中位数
     * 
     * 算法步骤：
     * 1. 先将前k-1个元素加入SB树
     * 2. 从第k个元素开始，每次：
     *    a. 加入当前元素
     *    b. 根据窗口大小是奇数还是偶数，计算中位数
     *    c. 删除窗口左边界的元素
     * 
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 每个窗口的中位数数组
     */
    public static double[] median(int[] nums, int k) {
        SBTMap<VNode> map = new SBTMap<>();
        
        // 先将前k-1个元素加入SB树
        for (int i = 0; i < k - 1; i++) {
            map.add(new VNode(i, nums[i]));
        }
        
        double[] ans = new double[nums.length - k + 1];
        int idx = 0;
        
        // 处理每个窗口
        for (int i = k - 1; i < nums.length; i++) {
            // 加入当前元素，形成大小为k的窗口
            map.add(new VNode(i, nums[i]));
            
            // 计算中位数
            if (map.size() % 2 == 0) {
                // 偶数个元素：取中间两个数的平均值
                VNode upMid = map.getIndexKey(map.size() / 2 - 1);
                VNode downMid = map.getIndexKey(map.size() / 2);
                ans[idx++] = ((double)upMid.v + (double) downMid.v) / 2;
            } else {
                // 奇数个元素：取中间的数
                VNode mid = map.getIndexKey(map.size() / 2);
                ans[idx++] = mid.v;
            }
            
            // 删除窗口左边界的元素
            map.remove(new VNode(i - k + 1, nums[i - k + 1]));
        }
        return ans;
    }
}
