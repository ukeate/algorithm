package base.tree4;

/**
 * AVL树（平衡二叉搜索树）实现
 * AVL树是一种自平衡的二叉搜索树，保证左右子树高度差不超过1
 * 所有操作的时间复杂度都是O(log n)
 */
public class AVL {
    /**
     * AVL树节点类
     * @param <K> 键类型，必须实现Comparable接口
     * @param <V> 值类型
     */
    public static class Node<K extends Comparable<K>, V> {
        public K k;                // 键
        public V v;                // 值
        public Node<K, V> l;       // 左子节点
        public Node<K, V> r;       // 右子节点
        public int h;              // 节点高度

        /**
         * 构造函数
         * @param key 键
         * @param val 值
         */
        public Node(K key, V val) {
            k = key;
            v = val;
            h = 1;  // 新节点的高度为1
        }
    }

    /**
     * AVL树Map实现类
     * @param <K> 键类型
     * @param <V> 值类型
     */
    public static class AVLTreeMap<K extends Comparable<K>, V> {
        private Node<K, V> root;  // 根节点
        private int size;         // 树的大小

        /**
         * 构造函数
         */
        public AVLTreeMap() {
        }

        /**
         * 右旋操作（处理LL型不平衡）
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node<K, V> rightRotate(Node<K, V> cur) {
            Node<K, V> left = cur.l;
            cur.l = left.r;
            left.r = cur;
            // 更新高度：先更新cur，再更新left
            cur.h = Math.max((cur.l != null ? cur.l.h : 0), (cur.r != null ? cur.r.h : 0)) + 1;
            left.h = Math.max((left.l != null ? left.l.h : 0), (left.r != null ? left.r.h : 0)) + 1;
            return left;
        }

        /**
         * 左旋操作（处理RR型不平衡）
         * @param cur 当前节点
         * @return 旋转后的新根节点
         */
        private Node<K, V> leftRotate(Node<K, V> cur) {
            Node<K, V> right = cur.r;
            cur.r = right.l;
            right.l = cur;
            // 更新高度：先更新cur，再更新right
            cur.h = Math.max((cur.l != null ? cur.l.h : 0), (cur.r != null ? cur.r.h : 0)) + 1;
            right.h = Math.max((right.l != null ? right.l.h : 0), (right.r != null ? right.r.h : 0)) + 1;
            return right;
        }

        /**
         * 维护AVL树平衡性
         * @param cur 当前节点
         * @return 平衡后的节点
         */
        private Node<K, V> maintain(Node<K, V> cur) {
            if (cur == null) {
                return null;
            }
            int lh = cur.l != null ? cur.l.h : 0;  // 左子树高度
            int rh = cur.r != null ? cur.r.h : 0;  // 右子树高度
            
            if (Math.abs(lh - rh) > 1) {  // 高度差超过1，需要旋转
                if (lh > rh) {  // 左子树更高
                    int llh = cur.l.l != null ? cur.l.l.h : 0;  // 左左子树高度
                    int lrh = cur.l.r != null ? cur.l.r.h : 0;  // 左右子树高度
                    if (llh >= lrh) {
                        // LL型不平衡，右旋
                        cur = rightRotate(cur);
                    } else {
                        // LR型不平衡，先左旋再右旋
                        cur.l = leftRotate(cur.l);
                        cur = rightRotate(cur);
                    }
                } else {  // 右子树更高
                    int rlh = cur.r.l != null ? cur.r.l.h : 0;  // 右左子树高度
                    int rrh = cur.r.r != null ? cur.r.r.h : 0;  // 右右子树高度
                    if (rrh >= rlh) {
                        // RR型不平衡，左旋
                        cur = leftRotate(cur);
                    } else {
                        // RL型不平衡，先右旋再左旋
                        cur.r = rightRotate(cur.r);
                        cur = leftRotate(cur);
                    }
                }
            }
            return cur;
        }

        /**
         * 查找指定键的节点（如果不存在则返回应该插入的位置）
         * @param key 查找的键
         * @return 找到的节点或插入位置
         */
        private Node<K, V> findLast(K key) {
            Node<K, V> pre = root, cur = root;
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
         * 查找小于等于key的最大节点
         * @param key 查找的键
         * @return 小于等于key的最大节点
         */
        private Node<K, V> findLastSmallEqual(K key) {
            Node<K, V> ans = null;
            Node<K, V> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.k) == 0) {
                    ans = cur;
                    break;
                } else if (key.compareTo(cur.k) < 0) {
                    cur = cur.l;
                } else {
                    ans = cur;  // 当前节点小于key，可能是答案
                    cur = cur.r;
                }
            }
            return ans;
        }

        /**
         * 查找大于等于key的最小节点
         * @param key 查找的键
         * @return 大于等于key的最小节点
         */
        private Node<K, V> findLastBigEqual(K key) {
            Node<K, V> ans = null;
            Node<K, V> cur = root;
            while (cur != null) {
                if (key.compareTo(cur.k) == 0) {
                    ans = cur;
                    break;
                } else if (key.compareTo(cur.k) < 0) {
                    ans = cur;  // 当前节点大于key，可能是答案
                    cur = cur.l;
                } else {
                    cur = cur.r;
                }
            }
            return ans;
        }

        /**
         * 判断是否包含指定键
         * @param key 查找的键
         * @return 是否包含
         */
        public boolean containsKey(K key) {
            if (key == null) {
                return false;
            }
            Node<K, V> n = findLast(key);
            return n != null && key.compareTo(n.k) == 0 ? true : false;
        }

        /**
         * 递归添加节点
         * @param cur 当前节点
         * @param key 插入的键
         * @param val 插入的值
         * @return 插入后的根节点
         */
        private Node<K, V> add(Node<K, V> cur, K key, V val) {
            if (cur == null) {
                return new Node<>(key, val);
            } else {
                if (key.compareTo(cur.k) < 0) {
                    cur.l = add(cur.l, key, val);
                } else {
                    cur.r = add(cur.r, key, val);
                }
                // 更新高度
                cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
                return maintain(cur);  // 维护平衡
            }
        }

        /**
         * 递归删除节点
         * @param cur 当前节点
         * @param key 要删除的键
         * @return 删除后的根节点
         */
        private Node<K, V> delete(Node<K, V> cur, K key) {
            if (key.compareTo(cur.k) > 0) {
                cur.r = delete(cur.r, key);
            } else if (key.compareTo(cur.k) < 0) {
                cur.l = delete(cur.l, key);
            } else {
                // 找到要删除的节点
                if (cur.l == null && cur.r == null) {
                    // 叶子节点，直接删除
                    cur = null;
                } else if (cur.l == null && cur.r != null) {
                    // 只有右子树
                    cur = cur.r;
                } else if (cur.l != null && cur.r == null) {
                    // 只有左子树
                    cur = cur.l;
                } else {
                    // 有两个子树，找右子树的最小节点替代
                    Node<K, V> des = cur.r;
                    while (des.l != null) {
                        des = des.l;
                    }
                    cur.r = delete(cur.r, des.k);  // 递归删除后继节点
                    des.l = cur.l;
                    des.r = cur.r;
                    cur = des;
                }
            }
            if (cur != null) {
                // 更新高度
                cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
            }
            return maintain(cur);  // 维护平衡
        }

        /**
         * 插入或更新键值对
         * @param key 键
         * @param val 值
         */
        public void put(K key, V val) {
            if (key == null) {
                return;
            }
            Node<K, V> lastNode = findLast(key);
            if (lastNode != null && key.compareTo(lastNode.k) == 0) {
                // 键已存在，更新值
                lastNode.v = val;
            } else {
                // 键不存在，插入新节点
                size++;
                root = add(root, key, val);
            }
        }

        /**
         * 删除指定键的节点
         * @param key 要删除的键
         */
        public void remove(K key) {
            if (key == null) {
                return;
            }
            if (containsKey(key)) {
                size--;
                root = delete(root, key);
            }
        }

        /**
         * 获取指定键的值
         * @param key 查找的键
         * @return 对应的值，不存在则返回null
         */
        public V get(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> n = findLast(key);
            if (n != null && key.compareTo(n.k) == 0) {
                return n.v;
            }
            return null;
        }

        /**
         * 获取最小键
         * @return 最小键
         */
        public K firstKey() {
            if (root == null) {
                return null;
            }
            Node<K, V> cur = root;
            while (cur.l != null) {
                cur = cur.l;
            }
            return cur.k;
        }

        /**
         * 获取最大键
         * @return 最大键
         */
        public K lastKey() {
            if (root == null) {
                return null;
            }
            Node<K, V> cur = root;
            while (cur.r != null) {
                cur = cur.r;
            }
            return cur.k;
        }

        /**
         * 获取小于等于key的最大键（floor函数）
         * @param key 查找的键
         * @return 小于等于key的最大键
         */
        public K floorKey(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> n = findLastSmallEqual(key);
            return n == null ? null : n.k;
        }

        /**
         * 获取大于等于key的最小键（ceiling函数）
         * @param key 查找的键
         * @return 大于等于key的最小键
         */
        public K ceilingKey(K key) {
            if (key == null) {
                return null;
            }
            Node<K, V> n = findLastBigEqual(key);
            return n == null ? null : n.k;
        }

        /**
         * 获取树的大小
         * @return 节点数量
         */
        public int size() {
            return size;
        }
    }
}
